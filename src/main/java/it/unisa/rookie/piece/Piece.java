package it.unisa.rookie.piece;

public abstract class Piece {
  private ChessPieceType type;
  private Color color;
  private Position position;

  public Piece(ChessPieceType type, Color color, Position position) {
    this.type = type;
    this.color = color;
    this.position = position;
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
}
