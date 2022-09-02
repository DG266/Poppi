package it.unisa.rookie.board;

import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Pawn;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import java.util.ArrayList;

public class PawnLongMove extends Move {
  private final Piece enPassant;

  public PawnLongMove(Board board, Position source, Position destination, Piece movedPiece,
                      Piece enPassant) {
    super(board, source, destination, movedPiece);
    this.enPassant = enPassant;
  }

  public Piece getEnPassant() {
    return enPassant;
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

    Piece cloneEnPassant = new Pawn(getMovedPiece().getColor(), getMovedPiece().getPosition(), getMovedPiece().isFirstMove());
    if (cloneEnPassant.isFirstMove()) {
      cloneEnPassant.setFirstMove(false);
    }
    cloneEnPassant.setPosition(getDestination());
    newBoardPositions[getDestination().getValue()] = cloneEnPassant;

    // Choose the next player
    Color next = (currentPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

    return new Board(newBoardPositions, this, next, cloneEnPassant, whiteScore, blackScore);
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
            + ", enPassant=" + this.enPassant
            + "}";
  }
  */

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof PawnLongMove) {
      return super.equals(o) && enPassant.equals(((PawnLongMove) o).getEnPassant());
    }
    return false;
  }

}
