package it.unisa.rookie.board;

import it.unisa.rookie.piece.Piece;
import java.util.Comparator;

public class MoveComparator implements Comparator<Move> {
  private static final int[][] MVV_LVA = {
        //  Aggressors
        //  P   N   B   R   Q   K    // Victims
          { 6,  5,  4,  3,  2,  1},  // P
          {12, 11, 10,  9,  8,  7},  // N
          {18, 17, 16, 15, 14, 13},  // B
          {24, 23, 22, 21, 20, 19},  // R
          {30, 29, 28, 27, 26, 25},  // Q
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

  @Override
  public int compare(Move m1, Move m2) {
    // Sort in descending order
    if (mvvlva(m1) > mvvlva(m2)) {
      return -1;
    } else if (mvvlva(m1) == mvvlva(m2)) {
      return 0;
    } else {
      return 1;
    }
  }
}
