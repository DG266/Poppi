package it.unisa.rookie.evaluation;

import it.unisa.rookie.Board;
import it.unisa.rookie.piece.Piece;
import java.util.ArrayList;

public class SimpleEvaluator implements Evaluator {

  public SimpleEvaluator() {
  }

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

}
