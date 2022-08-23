package it.unisa.rookie.board;

import it.unisa.rookie.piece.Bishop;
import it.unisa.rookie.piece.ChessPieceType;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.King;
import it.unisa.rookie.piece.Knight;
import it.unisa.rookie.piece.Pawn;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import it.unisa.rookie.piece.Queen;
import it.unisa.rookie.piece.Rook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Move {
  private final Board board;
  private final Position source;
  private final Position destination;
  private final Piece movedPiece;

  public Move() {
    this.board = null;
    this.source = null;
    this.destination = null;
    this.movedPiece = null;
  }

  public Move(final Board board, final Position source, final Position destination,
              final Piece movedPiece) {
    this.board = board;
    this.source = source;
    this.destination = destination;
    this.movedPiece = movedPiece;
  }

  public Board getBoard() {
    return board;
  }

  public Position getSource() {
    return source;
  }

  public Position getDestination() {
    return destination;
  }

  public Piece getMovedPiece() {
    return movedPiece;
  }

  // Generates a completely new board
  public Board makeMove() {
    ArrayList<Piece> whitePieces = new ArrayList<>(this.board.getWhitePieces());
    ArrayList<Piece> blackPieces = new ArrayList<>(this.board.getBlackPieces());

    Color currentPlayerColor = this.board.getCurrentPlayer().getPlayerColor();

    if (currentPlayerColor == Color.WHITE) {
      for (Piece p : whitePieces) {
        if (p.equals(this.movedPiece)) {
          whitePieces.remove(p);
          break;
        }
      }
    } else {
      for (Piece p : blackPieces) {
        if (p.equals(this.movedPiece)) {
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

    // Add the moved piece
    Piece clone;

    // TODO: This is pretty bad...
    if (movedPiece.getType() == ChessPieceType.PAWN) {
      clone = new Pawn(movedPiece.getColor(), movedPiece.getPosition(), movedPiece.isFirstMove());
    } else if (movedPiece.getType() == ChessPieceType.KNIGHT) {
      clone = new Knight(movedPiece.getColor(), movedPiece.getPosition(), movedPiece.isFirstMove());
    } else if (movedPiece.getType() == ChessPieceType.BISHOP) {
      clone = new Bishop(movedPiece.getColor(), movedPiece.getPosition(), movedPiece.isFirstMove());
    } else if (movedPiece.getType() == ChessPieceType.ROOK) {
      clone = new Rook(movedPiece.getColor(), movedPiece.getPosition(), movedPiece.isFirstMove());
    } else if (movedPiece.getType() == ChessPieceType.QUEEN) {
      clone = new Queen(movedPiece.getColor(), movedPiece.getPosition(), movedPiece.isFirstMove());
    } else {
      clone = new King(movedPiece.getColor(), movedPiece.getPosition(), movedPiece.isFirstMove());
    }

    if (clone.isFirstMove()) {
      clone.setFirstMove(false);
    }
    clone.setPosition(destination);
    newBoardPositions[destination.getValue()] = clone;

    // Choose the next player
    Color next = (currentPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

    return new Board(newBoardPositions, next, this);
  }


  /*
  @Override
  public String toString() {
    return "Move{"
            //+ "board=" + board
            //+ ", source=" + source
            + "source=" + source
            + ", destination=" + destination
            + ", movedPiece=" + movedPiece
            + "}";
  }
  */

  @Override
  public String toString() {
    return "Move{"
            + source
            + " to " + destination
            + ", " + movedPiece.getType()
            + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    /*
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    */

    if (!(o instanceof Move) || o == null) {
      return false;
    }

    Move move = (Move) o;
    return this.getSource() == move.getSource()
            && this.getDestination() == move.getDestination()
            && this.getMovedPiece().equals(move.getMovedPiece());
  }

}
