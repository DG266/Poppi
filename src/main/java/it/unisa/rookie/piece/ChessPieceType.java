package it.unisa.rookie.piece;

public enum ChessPieceType {

  // TODO: Add (better?) values

  PAWN(1, "Pawn", ""),
  KNIGHT(3, "Knight", "N"),
  BISHOP(3, "Bishop", "B"),
  ROOK(5, "Rook", "R"),
  QUEEN(9, "Queen", "Q"),
  KING(100, "King", "K");

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
