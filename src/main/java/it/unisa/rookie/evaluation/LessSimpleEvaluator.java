package it.unisa.rookie.evaluation;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import it.unisa.rookie.Player;
import it.unisa.rookie.piece.Piece;

public class LessSimpleEvaluator implements Evaluator {
  @Override
  public int evaluate(Board board) {
    return getScoreByPlayer(board.getWhitePlayer()) - getScoreByPlayer(board.getBlackPlayer());
  }

  private int getScoreByPlayer(Player player) {
    // TODO: add some weights (maybe)
    return availableGoodAttacks(player) + pieceValueCount(player);
  }

  private int availableGoodAttacks(Player player) {
    int attackBonus = 0;
    // Check all legal moves...
    for (Move m : player.getLegalMoves()) {
      Piece attacker = m.getMovedPiece();
      Piece toAttack = player.getPlayingBoard().getPiece(m.getDestination().getValue());
      // ...if you can capture a piece...
      if (toAttack != null) {
        // ...check its value: if it's lower than the attacker's value, do nothing
        // Example:
        // Knight captures Queen? -> Bonus!
        // Queen captures Pawn? -> No bonus...
        // Pawn captures Pawn? -> Bonus!
        if (attacker.getType().getValue() <= toAttack.getType().getValue()) {
          attackBonus++;
        }
      }
    }
    return attackBonus;
  }

  private int pieceValueCount(Player player) {
    int pieceValueScore = 0;

    for (Piece p : player.getPieces()) {
      pieceValueScore += p.getType().getValue();
    }
    return pieceValueScore;
  }
}
