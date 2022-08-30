package it.unisa.rookie.board.evaluation;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.piece.Piece;
import java.util.ArrayList;

public class LowCostEvaluator implements Evaluator {

  public LowCostEvaluator() {
  }

  @Override
  public int evaluate(Board board) {
    return board.getWhitePlayer().getMaterialCount() - board.getBlackPlayer().getMaterialCount();
  }

  /*
  @Override
  public int evaluate(Board board) {
    int whiteScore = 0;
    int blackScore = 0;
    ArrayList<Piece> whitePieces = board.getWhitePieces();
    ArrayList<Piece> blackPieces = board.getBlackPieces();

    // Piece-value evaluation (not that good)
    for (Piece p : whitePieces) {
      whiteScore += p.getType().getValue();
    }
    for (Piece p : blackPieces) {
      blackScore += p.getType().getValue();
    }

    return whiteScore - blackScore;
  }
  */

  @Override
  public String toString() {
    return "LowCostEvaluator{}";
  }
}
