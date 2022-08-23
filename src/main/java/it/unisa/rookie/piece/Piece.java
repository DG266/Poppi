package it.unisa.rookie.piece;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Move;
import java.util.Collection;

public abstract class Piece {
  private ChessPieceType type;
  private Color color;
  private Position position;
  private boolean isFirstMove;

  public Piece(ChessPieceType type, Color color, Position position, boolean isFirstMove) {
    this.type = type;
    this.color = color;
    this.position = position;
    this.isFirstMove = isFirstMove;
  }

  public ChessPieceType getType() {
    return type;
  }

  public void setType(ChessPieceType type) {
    this.type = type;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public boolean isFirstMove() {
    return isFirstMove;
  }

  public void setFirstMove(boolean firstMove) {
    isFirstMove = firstMove;
  }

  public abstract Collection<Move> getLegalMoves(Board board);

  /*
  @Override
  public String toString() {
    return "Piece{"
            + "type=" + type.getShortName()
            + ", color=" + color.toString().substring(0, 1)
            + ", pos=" + position
            + ", isFirstMove=" + isFirstMove
            + "}";
  }
  */

  @Override
  public String toString() {
    String pieceName;
    if (this.getType() == ChessPieceType.PAWN) {
      pieceName = "P";
    } else {
      pieceName = this.getType().getShortName();
    }

    String result = pieceName + "_" + position;

    if (this.isFirstMove) {
      result += "f";
    }

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Piece piece = (Piece) o;

    return this.getPosition() == piece.getPosition()
            && this.getType() == piece.getType()
            && this.getColor() == piece.getColor()
            && this.isFirstMove() == piece.isFirstMove();
  }
}
