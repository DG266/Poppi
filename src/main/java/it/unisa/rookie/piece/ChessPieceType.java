package it.unisa.rookie.piece;

public enum ChessPieceType {

  // TODO: Add values

  PAWN(0, "Pawn", ""),
  KNIGHT(0, "Knight", "N"),
  BISHOP(0, "Bishop", "B"),
  ROOK(0, "Rook", "R"),
  QUEEN(0, "Queen", "Q"),
  KING(0, "King", "K");

  private final int value;
  private final String name;
  private final String shortName;

  public int getValue() {
    return this.value;
  }

  public String getName() {
    return this.name;
  }

  public String getShortName() {
    return this.shortName;
  }

  ChessPieceType(int val, String name, String shortName) {
    this.value = val;
    this.name = name;
    this.shortName = shortName;
  }

}
