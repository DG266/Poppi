package it.unisa.rookie.board;

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

  @Override
  public String toString() {
    return "ScoredMove{"
            + "score=" + score
            + ", move=" + move
            + "}";
  }
}
