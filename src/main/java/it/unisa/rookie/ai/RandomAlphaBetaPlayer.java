package it.unisa.rookie.ai;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.ScoredMove;
import it.unisa.rookie.board.Transition;
import it.unisa.rookie.board.evaluation.Evaluator;
import it.unisa.rookie.piece.Color;
import java.util.ArrayList;
import java.util.Collections;

public class RandomAlphaBetaPlayer implements ArtificialIntelligencePlayer {
  private int depth;
  private int examinedBoards;
  private Evaluator evaluator;

  public RandomAlphaBetaPlayer(int depth, Evaluator evaluator) {
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
      System.out.println("White player AI starting... "
              + "(algorithm = RandomAlphaBeta) "
              + "(depth = " + this.depth + ") "
              + "(evaluator = " + this.evaluator + ")"
      );
      result = max(startingBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    } else {
      // Black starts as minimizing player
      System.out.println("Black player AI starting... "
              + "(algorithm = RandomAlphaBeta) "
              + "(depth = " + this.depth + ") "
              + "(evaluator = " + this.evaluator + ")"
      );
      result = min(startingBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    Move bestMove = result.getMove();

    long endTime = System.currentTimeMillis();

    System.out.println("\tExecution time: " + (endTime - startTime) + "ms\n"
            + "\tExamined boards: " + examinedBoards + "\n"
            + "\tBest move chosen: " + bestMove + " (score: " + result.getScore() + ")");

    return new Transition(startingBoard, bestMove.makeMove(), bestMove);
  }

  private ScoredMove max(Board board, int depth, int alpha, int beta) {
    if (depth == 0 || board.matchIsOver()) {
      this.examinedBoards++;
      return new ScoredMove(evaluator.evaluate(board), null);
    }

    int highestScore = Integer.MIN_VALUE;
    Move bestMove = new Move();

    ArrayList<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();

    Collections.shuffle(legalMoves);

    for (Move move : legalMoves) {
      Board transitionedBoard = move.makeMove();
      if (transitionedBoard.getOpponentPlayer().isKingInCheck()) {
        continue;
      }
      ScoredMove scoredMove = min(transitionedBoard, depth - 1, alpha, beta);
      if (scoredMove.getScore() > highestScore) {
        highestScore = scoredMove.getScore();
        bestMove = move;
        alpha = Math.max(alpha, highestScore);
      }
      if (highestScore >= beta) {
        return new ScoredMove(highestScore, bestMove);
      }
    }
    return new ScoredMove(highestScore, bestMove);
  }

  private ScoredMove min(Board board, int depth, int alpha, int beta) {
    if (depth == 0 || board.matchIsOver()) {
      this.examinedBoards++;
      return new ScoredMove(evaluator.evaluate(board), null);
    }

    int lowestScore = Integer.MAX_VALUE;
    Move bestMove = new Move();

    ArrayList<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();

    Collections.shuffle(legalMoves);

    for (Move move : legalMoves) {
      Board transitionedBoard = move.makeMove();
      if (transitionedBoard.getOpponentPlayer().isKingInCheck()) {
        continue;
      }
      ScoredMove scoredMove = max(transitionedBoard, depth - 1, alpha, beta);
      if (scoredMove.getScore() < lowestScore) {
        lowestScore = scoredMove.getScore();
        bestMove = move;
        beta = Math.min(beta, lowestScore);
      }
      if (lowestScore <= alpha) {
        return new ScoredMove(lowestScore, bestMove);
      }
    }
    return new ScoredMove(lowestScore, bestMove);
  }
}
