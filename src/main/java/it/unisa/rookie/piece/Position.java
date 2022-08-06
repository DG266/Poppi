package it.unisa.rookie.piece;

public enum Position {
  A8(0, 0, 7),
  B8(1, 1, 7),
  C8(2, 2, 7),
  D8(3, 3, 7),
  E8(4, 4, 7),
  F8(5, 5, 7),
  G8(6, 6, 7),
  H8(7, 7, 7),

  A7(8, 0, 6),
  B7(9, 1, 6),
  C7(10, 2, 6),
  D7(11, 3, 6),
  E7(12, 4, 6),
  F7(13, 5, 6),
  G7(14, 6, 6),
  H7(15, 7, 6),

  A6(16, 0, 5),
  B6(17, 1, 5),
  C6(18, 2, 5),
  D6(19, 3, 5),
  E6(20, 4, 5),
  F6(21, 5, 5),
  G6(22, 6, 5),
  H6(23, 7, 5),

  A5(24, 0, 4),
  B5(25, 1, 4),
  C5(26, 2, 4),
  D5(27, 3, 4),
  E5(28, 4, 4),
  F5(29, 5, 4),
  G5(30, 6, 4),
  H5(31, 7, 4),

  A4(32, 0, 3),
  B4(33, 1, 3),
  C4(34, 2, 3),
  D4(35, 3, 3),
  E4(36, 4, 3),
  F4(37, 5, 3),
  G4(38, 6, 3),
  H4(39, 7, 3),

  A3(40, 0, 2),
  B3(41, 1, 2),
  C3(42, 2, 2),
  D3(43, 3, 2),
  E3(44, 4, 2),
  F3(45, 5, 2),
  G3(46, 6, 2),
  H3(47, 7, 2),

  A2(48, 0, 1),
  B2(49, 1, 1),
  C2(50, 2, 1),
  D2(51, 3, 1),
  E2(52, 4, 1),
  F2(53, 5, 1),
  G2(54, 6, 1),
  H2(55, 7, 1),

  A1(56, 0, 0),
  B1(57, 1, 0),
  C1(58, 2, 0),
  D1(59, 3, 0),
  E1(60, 4, 0),
  F1(61, 5, 0),
  G1(62, 6, 0),
  H1(63, 7, 0);

  private final int value;
  private final int x;
  private final int y;

  public int getValue() {
    return this.value;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  Position(int value, int x, int y) {
    this.value = value;
    this.x = x;
    this.y = y;
  }
}
