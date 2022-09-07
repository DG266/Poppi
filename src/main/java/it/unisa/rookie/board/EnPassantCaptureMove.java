package it.unisa.rookie.board;

import it.unisa.rookie.piece.ChessPieceType;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Pawn;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import java.util.ArrayList;

public class EnPassantCaptureMove extends Move {
  private final Piece enPassantToCapture;

  public EnPassantCaptureMove(Board board, Position source, Position destination, Piece movedPiece,
                              Piece enPassantToCapture) {
    super(board, source, destination, movedPiece);
    this.enPassantToCapture = enPassantToCapture;
  }

  public Piece getEnPassantToCapture() {
    return enPassantToCapture;
  }

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

    // Opponent loses a pawn -> - score
    if (currentPlayerColor == Color.WHITE) {
      blackScore -= ChessPieceType.PAWN.getValue();
    } else {
      whiteScore -= ChessPieceType.PAWN.getValue();
    }

    // Remove En Passant Captured Pawn
    newBoardPositions[enPassantToCapture.getPosition().getValue()] = null;

    // Add the moved piece
    Piece clonePawn = new Pawn(getMovedPiece().getColor(), getMovedPiece().getPosition(), getMovedPiece().isFirstMove());
    if (clonePawn.isFirstMove()) {
      clonePawn.setFirstMove(false);
    }
    clonePawn.setPosition(getDestination());
    newBoardPositions[getDestination().getValue()] = clonePawn;

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
            + ", enPassantToCapture=" + this.enPassantToCapture
            + "}";
  }
  */

  @Override
  public String toString() {
    return super.toString() + " - En Passant Capture";
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof EnPassantCaptureMove) {
      return super.equals(o) && enPassantToCapture.equals(((EnPassantCaptureMove) o).getEnPassantToCapture());
    }
    return false;
  }

}
