package it.unisa.rookie.piece;

public enum ChessPieceType {

  // TODO: Add (better?) values

  // Values in "centipawns"
  PAWN(100, "Pawn", ""),
  KNIGHT(320, "Knight", "N"),
  BISHOP(330, "Bishop", "B"),
  ROOK(500, "Rook", "R"),
  QUEEN(900, "Queen", "Q"),
  KING(20000, "King", "K");

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
