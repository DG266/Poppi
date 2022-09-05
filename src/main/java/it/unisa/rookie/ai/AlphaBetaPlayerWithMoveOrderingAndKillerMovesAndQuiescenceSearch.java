package it.unisa.rookie.ai;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.KillerMovesComparator;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.ScoredMove;
import it.unisa.rookie.board.Transition;
import it.unisa.rookie.board.evaluation.Evaluator;
import it.unisa.rookie.piece.Color;
import java.util.ArrayList;

public class AlphaBetaPlayerWithMoveOrderingAndKillerMovesAndQuiescenceSearch implements ArtificialIntelligencePlayer {
  private int depth;
  private int examinedBoards;
  private Evaluator evaluator;

  private Move[][] killerMoves;

  private int numberOfQuiescenceSearches;

  private static final int MAX_DISTANCE_FROM_ROOT = 64;  // Just use a large value
  private static final int KILLER_MOVES_SLOTS = 2;

  private static final int MAX_QUIESCENCE_SEARCHES = 10000;

  public AlphaBetaPlayerWithMoveOrderingAndKillerMovesAndQuiescenceSearch(int depth, Evaluator evaluator) {
    this.depth = depth;
    this.evaluator = evaluator;
    this.examinedBoards = 0;

    this.killerMoves = new Move[MAX_DISTANCE_FROM_ROOT][KILLER_MOVES_SLOTS];

    this.numberOfQuiescenceSearches = 0;
  }

  @Override
  public Transition play(Board startingBoard) {
    long startTime = System.currentTimeMillis();
    ScoredMove result;

    if (startingBoard.getCurrentPlayer().getPlayerColor() == Color.WHITE) {
      // White starts as maximizing player
      System.out.println("White player AI starting... "
              + "(algorithm = AlphaBetaMoveOrderingKillerMovesQuiescence) "
              + "(depth = " + this.depth + ") "
              + "(evaluator = " + this.evaluator + ")"
      );
      result = max(startingBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    } else {
      // Black starts as minimizing player
      System.out.println("Black player AI starting... "
              + "(algorithm = AlphaBetaMoveOrderingKillerMovesQuiescence) "
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

    int currentPly = this.depth - depth;

    int highestScore = Integer.MIN_VALUE;
    Move bestMove = new Move();

    ArrayList<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();
    legalMoves.sort(new KillerMovesComparator(killerMoves[currentPly][0], killerMoves[currentPly][1]));

    for (Move move : legalMoves) {
      if (this.depth == depth) {
        System.out.println("\tAnalyzing: " + move);
      }

      Board transitionedBoard = move.makeMove();

      if (transitionedBoard.getOpponentPlayer().isKingInCheck()) {
        continue;
      }

      ScoredMove scoredMove = min(transitionedBoard, getQuiescenceDepth(transitionedBoard, depth), alpha, beta);

      if (scoredMove.getScore() > highestScore) {
        highestScore = scoredMove.getScore();
        bestMove = move;
        alpha = Math.max(alpha, highestScore);
      }

      if (highestScore >= beta) {
        // Save killer move if it isn't a capture move
        if (!bestMove.isCaptureMove()) {
          saveKillerMove(currentPly, bestMove);
        }

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

    int currentPly = this.depth - depth;

    int lowestScore = Integer.MAX_VALUE;
    Move bestMove = new Move();

    ArrayList<Move> legalMoves = board.getCurrentPlayer().getLegalMoves();
    legalMoves.sort(new KillerMovesComparator(killerMoves[currentPly][0], killerMoves[currentPly][1]));

    for (Move move : legalMoves) {
      if (this.depth == depth) {
        System.out.println("\tAnalyzing: " + move);
      }

      Board transitionedBoard = move.makeMove();

      if (transitionedBoard.getOpponentPlayer().isKingInCheck()) {
        continue;
      }

      ScoredMove scoredMove = max(transitionedBoard, getQuiescenceDepth(transitionedBoard, depth), alpha, beta);
      if (scoredMove.getScore() < lowestScore) {
        lowestScore = scoredMove.getScore();
        bestMove = move;
        beta = Math.min(beta, lowestScore);
      }

      if (lowestScore <= alpha) {
        // Save killer move if it isn't a capture move
        if (!bestMove.isCaptureMove()) {
          saveKillerMove(currentPly, bestMove);
        }
        return new ScoredMove(lowestScore, bestMove);
      }
    }
    return new ScoredMove(lowestScore, bestMove);
  }

  private void saveKillerMove(int ply, Move candidateKiller) {
    Move firstKiller = killerMoves[ply][0];

    // If candidateKiller is not already present, save it
    if (!(candidateKiller.equals(firstKiller))) {
      for (int i = KILLER_MOVES_SLOTS - 2; i >= 0; i--) {
        killerMoves[ply][i + 1] = killerMoves[ply][i];
      }
      killerMoves[ply][0] = candidateKiller;
    }
  }

  private int getQuiescenceDepth(Board transitionedBoard, int currentDepth) {
    if (currentDepth == 1 && this.numberOfQuiescenceSearches < MAX_QUIESCENCE_SEARCHES) {
      int notQuiescentCounter = 0;
      // TODO: are 2 moves enough?
      // Check if the last 2 moves are capture moves.
      //
      // Consider transitionedBoard as the n-th board:
      // - lastMove is the move that generated transitionedBoard, so
      //   its board is the (n-1)-th board
      // - lastLastMove is the move that generated the (n-1)-th board, so
      //   its board is the (n-2)-th board
      //
      //  (n-2)-th board -----> (n-1)-th board -----> n-th board (transitionedBoard)
      //                   |                     |
      //              lastLastMove            lastMove
      //
      Move lastMove = transitionedBoard.getGeneratorMove();
      if (lastMove.isCaptureMove()) {
        notQuiescentCounter += 1;
      }

      Move lastLastMove = lastMove.getBoard().getGeneratorMove();
      if (lastLastMove.isCaptureMove()) {
        notQuiescentCounter += 1;
      }

      if (transitionedBoard.getCurrentPlayer().isKingInCheck()) {
        notQuiescentCounter += 1;
      }

      if (notQuiescentCounter >= 2) {
        this.numberOfQuiescenceSearches++;
        return currentDepth + 1;   // Basically it's always 1 + 1 = 2 (go 1 ply deeper)
      }
    }
    return currentDepth - 1;  // Proceed normally (no quiescence search)
  }
}