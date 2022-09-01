package it.unisa.rookie.board.evaluation;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
import it.unisa.rookie.piece.Piece;

public class MediumCostEvaluator implements Evaluator {
  @Override
  public int evaluate(Board board) {
    return getScoreByPlayer(board.getWhitePlayer()) - getScoreByPlayer(board.getBlackPlayer());
  }

  private int getScoreByPlayer(Player player) {
    // TODO: add some weights (maybe)
    return player.getMaterialCount()
            + availableGoodAttacks(player)
            + kingInCheckBonus(player);
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

  private int kingInCheckBonus(Player player) {
    Board board = player.getPlayingBoard();
    Player opponent = player.getOpponentPlayer();

    if (opponent.isKingInCheck()) {
      if (!(board.isCheckMateAvoidable(opponent))) {
        return 10000;
      }
      return 100;
    }
    return 0;
  }

  /*
  private int pieceValueCount(Player player) {
    int pieceValueScore = 0;

    for (Piece p : player.getPieces()) {
      pieceValueScore += p.getType().getValue();
    }
    return pieceValueScore;
  }
  */

  @Override
  public String toString() {
    return "MediumCostEvaluator{}";
  }
}
