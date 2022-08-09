package it.unisa.rookie.piece;

import it.unisa.rookie.Board;

public class Move {
  private final Board board;
  private final Position source;
  private final Position destination;
  private final Piece movedPiece;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Move move = (Move) o;
    return this.getSource() == move.getSource()
            && this.getDestination() == move.getDestination()
            && this.getMovedPiece().equals(move.getMovedPiece());
  }

}
