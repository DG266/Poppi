package it.unisa.rookie.piece;

public enum ChessPieceType {

  // TODO: Add (better?) values

  // Values in "centipawns"
  PAWN(100, "Pawn", "", 0),
  KNIGHT(320, "Knight", "N", 1),
  BISHOP(330, "Bishop", "B", 2),
  ROOK(500, "Rook", "R", 3),
  QUEEN(900, "Queen", "Q", 4),
  KING(20000, "King", "K", 5);

  private final int value;
  private final String name;
  private final String shortName;
  private final int id;

  public int getValue() {
    return this.value;
  }

  public String getName() {
    return this.name;
  }

  public String getShortName() {
    return this.shortName;
  }

  public int getId() {
    return this.id;
  }

  ChessPieceType(int val, String name, String shortName, int id) {
    this.value = val;
    this.name = name;
    this.shortName = shortName;
    this.id = id;
  }

}
