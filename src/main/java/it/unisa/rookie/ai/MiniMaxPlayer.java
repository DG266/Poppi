package it.unisa.rookie.ai;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import it.unisa.rookie.Transition;
import it.unisa.rookie.evaluation.Evaluator;
import it.unisa.rookie.piece.Color;

public class MiniMaxPlayer implements ArtificialIntelligencePlayer {
  private int depth;
  private int examinedBoards;
  private Evaluator evaluator;

  public MiniMaxPlayer(int depth, Evaluator evaluator) {
    this.depth = depth;
    this.evaluator = evaluator;
    this.examinedBoards = 0;
  }

  @Override
  public Transition play(Board startingBoard) {
    long startTime = System.currentTimeMillis();
    ScoredMove result;

    if (startingBoard.getCurrentPlayer().getPlayerColor() == Color.WHITE) {
      // White starts as maximizing player
      System.out.println("White player AI starting... (depth = " + this.depth + ")");
      result = max(startingBoard, depth);
    } else {
      // Black starts as minimizing player
      System.out.println("Black player AI starting... (depth = " + this.depth + ")");
      result = min(startingBoard, depth);
    }

    Move bestMove = result.getMove();

    long endTime = System.currentTimeMillis();

    System.out.println("\tExecution time: " + (endTime - startTime) + "ms\n"
                     + "\tExamined boards: " + examinedBoards + "\n"
                     + "\tBest move chosen: " + bestMove + " (score: " + result.getScore() + ")");

    return new Transition(startingBoard, bestMove.makeMove(), bestMove);
  }

  private ScoredMove max(Board board, int depth) {
    if (depth == 0) {
      this.examinedBoards++;
      return new ScoredMove(evaluator.evaluate(board), null);
    }
    if (board.matchIsOver()) {
      return new ScoredMove(evaluator.evaluate(board), null);
    }

    int highestScore = Integer.MIN_VALUE;
    Move bestMove = new Move();

    for (Move move : board.getCurrentPlayer().getLegalMoves()) {
      Board transitionedBoard = move.makeMove();
      if (transitionedBoard.getOpponentPlayer().isKingInCheck()) {
        continue;
      }
      ScoredMove scoredMove = min(transitionedBoard, depth - 1);
      if (scoredMove.getScore() > highestScore) {
        highestScore = scoredMove.getScore();
        bestMove = move;
      }
    }
    return new ScoredMove(highestScore, bestMove);
  }

  private ScoredMove min(Board board, int depth) {
    if (depth == 0) {
      this.examinedBoards++;
      return new ScoredMove(evaluator.evaluate(board), null);
    }
    if (board.matchIsOver()) {
      return new ScoredMove(evaluator.evaluate(board), null);
    }

    int lowestScore = Integer.MAX_VALUE;
    Move bestMove = new Move();

    for (Move move : board.getCurrentPlayer().getLegalMoves()) {
      Board transitionedBoard = move.makeMove();
      if (transitionedBoard.getOpponentPlayer().isKingInCheck()) {
        continue;
      }
      ScoredMove scoredMove = max(transitionedBoard, depth - 1);
      if (scoredMove.getScore() < lowestScore) {
        lowestScore = scoredMove.getScore();
        bestMove = move;
      }
    }
    return new ScoredMove(lowestScore, bestMove);
  }
}
