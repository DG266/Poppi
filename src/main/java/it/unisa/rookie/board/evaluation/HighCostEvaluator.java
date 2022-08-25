package it.unisa.rookie.board.evaluation;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.CastlingMove;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
import it.unisa.rookie.piece.Piece;

public class HighCostEvaluator implements Evaluator {
  @Override
  public int evaluate(Board board) {
    return getScoreByPlayer(board.getWhitePlayer()) - getScoreByPlayer(board.getBlackPlayer());
  }

  private int getScoreByPlayer(Player player) {
    // TODO: add some weights (maybe)
    return availableGoodAttacks(player)
            + pieceValueCount(player)
            + mobility(player)
            + castlingEvaluation(player);
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

  private int mobility(Player player) {
    return player.getLegalMoves().size();
  }

  private int castlingEvaluation(Player player) {
    Move m = player.getPlayingBoard().getGeneratorMove();
    boolean isCastlingMove = (m instanceof CastlingMove);
    boolean playerMadeThisMove = m.getMovedPiece().getColor() == player.getPlayerColor();
    return (isCastlingMove && playerMadeThisMove) ? 500 : 0;
  }

  public String getEvaluationDescription(Board board) {
    Player w = board.getWhitePlayer();
    Player b = board.getBlackPlayer();
    String result1 =
            "* WHITE PLAYER" + "\n"
            + "Pieces value: " + pieceValueCount(w) + "\n"
            + "Attacks bonus: " + availableGoodAttacks(w) + "\n"
            + "Mobility bonus: " + mobility(w) + "\n"
            + "Castling bonus: " + castlingEvaluation(w) + "\n";
    String result2 =
            "* BLACK PLAYER" + "\n"
            + "Pieces value: " + pieceValueCount(b) + "\n"
            + "Attacks bonus: " + availableGoodAttacks(b) + "\n"
            + "Mobility bonus: " + mobility(b) + "\n"
            + "Castling bonus: " + castlingEvaluation(b) + "\n";
    return result1 + result2;
  }

  @Override
  public String toString() {
    return "HighCostEvaluator{}";
  }
}
