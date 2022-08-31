package it.unisa.rookie.board;

import it.unisa.rookie.piece.Piece;

import java.util.Comparator;

public class KillerMovesComparator implements Comparator<Move> {
  private Move firstKillerMove;
  private Move secondKillerMove;

  public KillerMovesComparator(Move firstKillerMove, Move secondKillerMove) {
    super();
    this.firstKillerMove = firstKillerMove;
    this.secondKillerMove = secondKillerMove;
  }

  private static final int[][] MVV_LVA = {
        //  Aggressors
        //  P   N   B   R   Q   K    // Victims
          {16, 15, 14, 13, 12, 11},  // P
          {22, 21, 20, 19, 18, 17},  // N
          {28, 27, 26, 25, 24, 23},  // B
          {34, 33, 32, 31, 30, 29},  // R
          {40, 39, 38, 37, 36, 35},  // Q
          { 0,  0,  0,  0,  0,  0},  // K
  };

  // Most Valuable Victim - Least Valuable Aggressor heuristic
  public int mvvlva(Move m) {
    Piece victim = m.getBoard().getPiece(m.getDestination().getValue());
    Piece aggressor = m.getMovedPiece();
    if (victim != null) {
      return MVV_LVA[victim.getType().getId()][aggressor.getType().getId()];
    }
    return 0;
  }

  public int score(Move m) {
    if (m.getBoard().getPiece(m.getDestination().getValue()) != null) {
      return mvvlva(m);
    } else if (m.equals(firstKillerMove)) {
      return 6;
    } else if (m.equals(secondKillerMove)) {
      return 4;
    } else {
      return 0;
    }
  }

  @Override
  public int compare(Move m1, Move m2) {
    // Sort in descending order
    if (score(m1) > score(m2)) {
      return -1;
    } else if (score(m1) == score(m2)) {
      return 0;
    } else {
      return 1;
    }
  }
}
