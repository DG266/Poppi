package it.unisa.rookie.ai;

import it.unisa.rookie.Move;

public class ScoredMove {
  private int score;
  private Move move;

  public ScoredMove(int score, Move move) {
    this.score = score;
    this.move = move;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
  }
}
