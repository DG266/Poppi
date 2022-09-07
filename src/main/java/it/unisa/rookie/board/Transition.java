package it.unisa.rookie.board;

public class Transition {
  private Board startBoard;
  private Board endBoard;
  private Move move;

  public Transition(Board startBoard, Board endBoard, Move move) {
    this.startBoard = startBoard;
    this.endBoard = endBoard;
    this.move = move;
  }

  public Board getStartBoard() {
    return startBoard;
  }

  public void setStartBoard(Board startBoard) {
    this.startBoard = startBoard;
  }

  public Board getEndBoard() {
    return endBoard;
  }

  public void setEndBoard(Board endBoard) {
    this.endBoard = endBoard;
  }

  public Move getMove() {
    return move;
  }

  public void setMove(Move move) {
    this.move = move;
  }

  @Override
  public String toString() {
    return "Transition{"
            + "startBoard=" + startBoard
            + ", endBoard=" + endBoard
            + ", move=" + move
            + "}";
  }
}
