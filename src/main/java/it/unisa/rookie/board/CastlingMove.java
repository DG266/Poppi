package it.unisa.rookie.board;

import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.King;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import it.unisa.rookie.piece.Rook;
import java.util.ArrayList;

public class CastlingMove extends Move {
  private Position castleSource;
  private Position castleDestination;
  private Piece castle;

  public CastlingMove(Board board,
                      Position source, Position destination, Piece movedPiece,
                      Position castleSource, Position castleDestination, Piece castle) {
    super(board, source, destination, movedPiece);
    this.castleSource = castleSource;
    this.castleDestination = castleDestination;
    this.castle = castle;
  }

  public Position getCastleSource() {
    return castleSource;
  }

  public void setCastleSource(Position castleSource) {
    this.castleSource = castleSource;
  }

  public Position getCastleDestination() {
    return castleDestination;
  }

  public void setCastleDestination(Position castleDestination) {
    this.castleDestination = castleDestination;
  }

  public Piece getCastle() {
    return castle;
  }

  public void setCastle(Piece castle) {
    this.castle = castle;
  }

  @Override
  public Board makeMove() {
    ArrayList<Piece> whitePieces = new ArrayList<>(this.getBoard().getWhitePieces());
    ArrayList<Piece> blackPieces = new ArrayList<>(this.getBoard().getBlackPieces());

    int whiteScore = this.getBoard().getWhitePlayer().getMaterialCount();
    int blackScore = this.getBoard().getBlackPlayer().getMaterialCount();

    Color currentPlayerColor = this.getBoard().getCurrentPlayer().getPlayerColor();

    if (currentPlayerColor == Color.WHITE) {
      for (Piece p : whitePieces) {
        if (p.equals(this.getMovedPiece())) {
          whitePieces.remove(p);
          break;
        }
      }
    } else {
      for (Piece p : blackPieces) {
        if (p.equals(this.getMovedPiece())) {
          blackPieces.remove(p);
          break;
        }
      }
    }

    // Fill the new board
    Piece[] newBoardPositions = new Piece[64];

    for (Piece p : whitePieces) {
      newBoardPositions[p.getPosition().getValue()] = p;
    }
    for (Piece p : blackPieces) {
      newBoardPositions[p.getPosition().getValue()] = p;
    }

    Rook rookClone = new Rook(castle.getColor(), castleSource, castle.isFirstMove());
    King kingClone = new King(
            getMovedPiece().getColor(),
            getMovedPiece().getPosition(),
            getMovedPiece().isFirstMove()
    );

    rookClone.setFirstMove(false);
    kingClone.setFirstMove(false);

    rookClone.setPosition(castleDestination);
    kingClone.setPosition(this.getDestination());

    // Add the moved piece
    newBoardPositions[this.getDestination().getValue()] = kingClone;
    newBoardPositions[castleSource.getValue()] = null;  // Remove old rook
    newBoardPositions[castleDestination.getValue()] = rookClone;

    // Choose the next player
    Color next = (currentPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

    return new Board(newBoardPositions, this, next, null, whiteScore, blackScore);
  }

  /*
  @Override
  public String toString() {
    return "Move{"
            //+ "board=" + board
            //+ ", source=" + source
            + "source=" + this.getSource()
            + ", destination=" + this.getDestination()
            + ", movedPiece=" + this.getMovedPiece()
            + ", castleSource=" + this.getCastleSource()
            + ", castleDestination=" + this.getCastleDestination()
            + ", castle=" + this.getCastle()
            + "}";
  }
  */

  @Override
  public String toString() {
    return super.toString() + " - Castling";
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof CastlingMove) {
      return super.equals(o)
              && castleSource == ((CastlingMove) o).getCastleSource()
              && castleDestination == ((CastlingMove) o).getCastleDestination()
              && castle.equals(((CastlingMove) o).getCastle());
    }
    return false;
  }
}
