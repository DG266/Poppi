package it.unisa.rookie.board.evaluation;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
import it.unisa.rookie.piece.ChessPieceType;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Piece;

public class MediumCostEvaluator implements Evaluator {
  // Values taken from: https://www.chessprogramming.org/Simplified_Evaluation_Function
  private static final int[] WHITE_PAWN_STRUCTURE = {
      0,  0,  0,  0,  0,  0,  0,  0,
      50, 50, 50, 50, 50, 50, 50, 50,
      10, 10, 20, 30, 30, 20, 10, 10,
      5,  5, 10, 25, 25, 10,  5,  5,
      0,  0,  0, 20, 20,  0,  0,  0,
      5, -5, -10,  0,  0, -10, -5,  5,
      5, 10, 10, -20, -20, 10, 10,  5,
      0,  0,  0,  0,  0,  0,  0,  0
  };

  private static final int[] BLACK_PAWN_STRUCTURE = {
      0,  0,  0,  0,  0,  0,  0,  0,
      5, 10, 10, -20, -20, 10, 10,  5,
      5, -5, -10,  0,  0, -10, -5,  5,
      0,  0,  0, 20, 20,  0,  0,  0,
      5,  5, 10, 25, 25, 10,  5,  5,
      10, 10, 20, 30, 30, 20, 10, 10,
      50, 50, 50, 50, 50, 50, 50, 50,
      0,  0,  0,  0,  0,  0,  0,  0
  };

  @Override
  public int evaluate(Board board) {
    return getScoreByPlayer(board.getWhitePlayer()) - getScoreByPlayer(board.getBlackPlayer());
  }

  private int getScoreByPlayer(Player player) {
    // TODO: add some weights (maybe)
    return player.getMaterialCount()
            + availableGoodAttacks(player)
            + kingInCheckBonus(player)
            + pawnStructureScore(player);
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

  private int pawnStructureScore(Player player) {
    int structureScore = 0;
    if (player.getPlayerColor() == Color.WHITE) {
      for (Piece p : player.getPieces()) {
        if (p.getType() == ChessPieceType.PAWN) {
          structureScore += WHITE_PAWN_STRUCTURE[p.getPosition().getValue()];
        }
      }
    } else {
      for (Piece p : player.getPieces()) {
        if (p.getType() == ChessPieceType.PAWN) {
          structureScore += BLACK_PAWN_STRUCTURE[p.getPosition().getValue()];
        }
      }
    }

    return structureScore;
  }

  @Override
  public String toString() {
    return "MediumCostEvaluator{}";
  }
}
