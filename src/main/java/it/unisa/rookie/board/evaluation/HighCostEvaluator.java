package it.unisa.rookie.board.evaluation;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.CastlingMove;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Piece;

public class HighCostEvaluator implements Evaluator {
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

  private static final int[] WHITE_KNIGHT_STRUCTURE = {
      -50, -40, -30, -30, -30, -30, -40, -50,
      -40, -20,  0,  0,  0,  0, -20, -40,
      -30,  0, 10, 15, 15, 10,  0, -30,
      -30,  5, 15, 20, 20, 15,  5, -30,
      -30,  0, 15, 20, 20, 15,  0, -30,
      -30,  5, 10, 15, 15, 10,  5, -30,
      -40, -20,  0,  5,  5,  0, -20, -40,
      -50, -40, -30, -30, -30, -30, -40, -50
  };

  private static final int[] BLACK_KNIGHT_STRUCTURE = {
      -50, -40, -30, -30, -30, -30, -40, -50,
      -40, -20,  0,  5,  5,  0, -20, -40,
      -30,  5, 10, 15, 15, 10,  5, -30,
      -30,  0, 15, 20, 20, 15,  0, -30,
      -30,  5, 15, 20, 20, 15,  5, -30,
      -30,  0, 10, 15, 15, 10,  0, -30,
      -40, -20,  0,  0,  0,  0, -20, -40,
      -50, -40, -30, -30, -30, -30, -40, -50
  };

  private static final int[] WHITE_BISHOP_STRUCTURE = {
      -20, -10, -10, -10, -10, -10, -10, -20,
      -10,  0,  0,  0,  0,  0,  0, -10,
      -10,  0,  5, 10, 10,  5,  0, -10,
      -10,  5,  5, 10, 10,  5,  5, -10,
      -10,  0, 10, 10, 10, 10,  0, -10,
      -10, 10, 10, 10, 10, 10, 10, -10,
      -10,  5,  0,  0,  0,  0,  5, -10,
      -20, -10, -10, -10, -10, -10, -10, -20
  };

  private static final int[] BLACK_BISHOP_STRUCTURE = {
      -20, -10, -10, -10, -10, -10, -10, -20,
      -10,  5,  0,  0,  0,  0,  5, -10,
      -10, 10, 10, 10, 10, 10, 10, -10,
      -10,  0, 10, 10, 10, 10,  0, -10,
      -10,  5,  5, 10, 10,  5,  5, -10,
      -10,  0,  5, 10, 10,  5,  0, -10,
      -10,  0,  0,  0,  0,  0,  0, -10,
      -20, -10, -10, -10, -10, -10, -10, -20
  };

  private static final int[] WHITE_ROOK_STRUCTURE = {
      0,  0,  0,  0,  0,  0,  0,  0,
      5, 10, 10, 10, 10, 10, 10,  5,
      -5,  0,  0,  0,  0,  0,  0, -5,
      -5,  0,  0,  0,  0,  0,  0, -5,
      -5,  0,  0,  0,  0,  0,  0, -5,
      -5,  0,  0,  0,  0,  0,  0, -5,
      -5,  0,  0,  0,  0,  0,  0, -5,
      0,  0,  0,  5,  5,  0,  0,  0
  };

  private static final int[] BLACK_ROOK_STRUCTURE = {
      0,  0,  0,  5,  5,  0,  0,  0,
    -5,  0,  0,  0,  0,  0,  0, -5,
    -5,  0,  0,  0,  0,  0,  0, -5,
    -5,  0,  0,  0,  0,  0,  0, -5,
    -5,  0,  0,  0,  0,  0,  0, -5,
    -5,  0,  0,  0,  0,  0,  0, -5,
      5, 10, 10, 10, 10, 10, 10,  5,
      0,  0,  0,  0,  0,  0,  0,  0
  };

  private static final int[] WHITE_QUEEN_STRUCTURE = {
      -20, -10, -10, -5, -5, -10, -10, -20,
      -10,  0,  0,  0,  0,  0,  0, -10,
      -10,  0,  5,  5,  5,  5,  0, -10,
      -5,  0,  5,  5,  5,  5,  0, -5,
      0,  0,  5,  5,  5,  5,  0, -5,
      -10,  5,  5,  5,  5,  5,  0, -10,
      -10,  0,  5,  0,  0,  0,  0, -10,
      -20, -10, -10, -5, -5, -10, -10, -20
  };

  private static final int[] BLACK_QUEEN_STRUCTURE = {
      -20, -10, -10, -5, -5, -10, -10, -20,
      -10,  0,  5,  0,  0,  0,  0, -10,
      -10,  5,  5,  5,  5,  5,  0, -10,
      0,  0,  5,  5,  5,  5,  0, -5,
      -5,  0,  5,  5,  5,  5,  0, -5,
      -10,  0,  5,  5,  5,  5,  0, -10,
      -10,  0,  0,  0,  0,  0,  0, -10,
      -20, -10, -10, -5, -5, -10, -10, -20
  };

  private static final int[] WHITE_KING_MIDDLE_GAME_STRUCTURE = {
      -30, -40, -40, -50, -50, -40, -40, -30,
      -30, -40, -40, -50, -50, -40, -40, -30,
      -30, -40, -40, -50, -50, -40, -40, -30,
      -30, -40, -40, -50, -50, -40, -40, -30,
      -20, -30, -30, -40, -40, -30, -30, -20,
      -10, -20, -20, -20, -20, -20, -20, -10,
      20,   20,   0,   0,   0,   0,  20,  20,
      20,   30,  10,   0,   0,  10,  30,  20
  };

  private static final int[] BLACK_KING_MIDDLE_GAME_STRUCTURE = {
      20, 30, 10,  0,  0, 10, 30, 20,
      20, 20,  0,  0,  0,  0, 20, 20,
      -10, -20, -20, -20, -20, -20, -20, -10,
      -20, -30, -30, -40, -40, -30, -30, -20,
      -30, -40, -40, -50, -50, -40, -40, -30,
      -30, -40, -40, -50, -50, -40, -40, -30,
      -30, -40, -40, -50, -50, -40, -40, -30,
      -30, -40, -40, -50, -50, -40, -40, -30
  };

  private static final int[] WHITE_KING_END_GAME_STRUCTURE = {
      -50, -40, -30, -20, -20, -30, -40, -50,
      -30, -20, -10,  0,  0, -10, -20, -30,
      -30, -10, 20, 30, 30, 20, -10, -30,
      -30, -10, 30, 40, 40, 30, -10, -30,
      -30, -10, 30, 40, 40, 30, -10, -30,
      -30, -10, 20, 30, 30, 20, -10, -30,
      -30, -30,  0,  0,  0,  0, -30, -30,
      -50, -30, -30, -30, -30, -30, -30, -50
  };

  private static final int[] BLACK_KING_END_GAME_STRUCTURE = {
      -50, -30, -30, -30, -30, -30, -30, -50,
      -30, -30,  0,  0,  0,  0, -30, -30,
      -30, -10, 20, 30, 30, 20, -10, -30,
      -30, -10, 30, 40, 40, 30, -10, -30,
      -30, -10, 30, 40, 40, 30, -10, -30,
      -30, -10, 20, 30, 30, 20, -10, -30,
      -30, -20, -10,  0,  0, -10, -20, -30,
      -50, -40, -30, -20, -20, -30, -40, -50
  };

  @Override
  public int evaluate(Board board) {
    return getScoreByPlayer(board.getWhitePlayer()) - getScoreByPlayer(board.getBlackPlayer());
  }

  private int getScoreByPlayer(Player player) {
    // TODO: add some weights (maybe)
    return player.getMaterialCount()
            + mobility(player)
            + availableGoodAttacks(player)
            + castlingEvaluation(player)
            + kingInCheckBonus(player)
            + generalStructure(player);
  }

  private int mobility(Player player) {
    return player.getLegalMoves().size();
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

  private int castlingEvaluation(Player player) {
    Move m = player.getPlayingBoard().getGeneratorMove();
    if (m != null) {
      boolean isCastlingMove = (m instanceof CastlingMove);
      boolean playerMadeThisMove = m.getMovedPiece().getColor() == player.getPlayerColor();

      return (isCastlingMove && playerMadeThisMove) ? 500 : 0;
    }
    return 0;
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

  private int generalStructure(Player player) {
    int structureScore = 0;
    int totalPieces = player.getPieces().size() + player.getOpponentPlayer().getPieces().size();

    if (player.getPlayerColor() == Color.WHITE) {
      for (Piece p : player.getPieces()) {
        int pos = p.getPosition().getValue();
        switch (p.getType()) {
          case PAWN: structureScore += WHITE_PAWN_STRUCTURE[pos];
          break;
          case KNIGHT: structureScore += WHITE_KNIGHT_STRUCTURE[pos];
          break;
          case BISHOP: structureScore += WHITE_BISHOP_STRUCTURE[pos];
          break;
          case ROOK: structureScore += WHITE_ROOK_STRUCTURE[pos];
          break;
          case QUEEN: structureScore += WHITE_QUEEN_STRUCTURE[pos];
          break;
          case KING: {
            // This is an EXTREMELY BASIC example of "tapered eval"
            int midGameValue = totalPieces * WHITE_KING_MIDDLE_GAME_STRUCTURE[pos];
            int endGameValue = (32 - totalPieces) * WHITE_KING_END_GAME_STRUCTURE[pos];
            structureScore += ((midGameValue + endGameValue) / 32);
          }
          break;
          default:
        }
      }
    } else {
      for (Piece p : player.getPieces()) {
        int pos = p.getPosition().getValue();
        switch (p.getType()) {
          case PAWN: structureScore += BLACK_PAWN_STRUCTURE[pos];
                     break;
          case KNIGHT: structureScore += BLACK_KNIGHT_STRUCTURE[pos];
          break;
          case BISHOP: structureScore += BLACK_BISHOP_STRUCTURE[pos];
          break;
          case ROOK: structureScore += BLACK_ROOK_STRUCTURE[pos];
          break;
          case QUEEN: structureScore += BLACK_QUEEN_STRUCTURE[pos];
          break;
          case KING: {
            // This is an EXTREMELY BASIC example of "tapered eval"
            int midGameValue = totalPieces * BLACK_KING_MIDDLE_GAME_STRUCTURE[pos];
            int endGameValue = (32 - totalPieces) * BLACK_KING_END_GAME_STRUCTURE[pos];
            structureScore += ((midGameValue + endGameValue) / 32);
          }
          break;
          default:
        }
      }
    }
    return structureScore;
  }

  public String getEvaluationDescription(Board board) {
    Player w = board.getWhitePlayer();
    Player b = board.getBlackPlayer();
    String result1 =
            "WHITE SCORE (" + getScoreByPlayer(w) + ") - "
            + "Pieces value: " + w.getMaterialCount() + " | "
            + "Mobility bonus: " + mobility(w) + " | "
            + "Attacks bonus: " + availableGoodAttacks(w) + " | "
            + "Castling bonus: " + castlingEvaluation(w) + " | "
            + "Opponent King in check bonus: " + kingInCheckBonus(w) + " | "
            + "General Structure: " + generalStructure(w) + "\n";
    String result2 =
            "BLACK SCORE (" + getScoreByPlayer(b) + ") - "
            + "Pieces value: " + b.getMaterialCount() + " | "
            + "Mobility bonus: " + mobility(b) + " | "
            + "Attacks bonus: " + availableGoodAttacks(b) + " | "
            + "Castling bonus: " + castlingEvaluation(b) + " | "
            + "Opponent King in check bonus: " + kingInCheckBonus(b) + " | "
            + "General Structure: " + generalStructure(b) + "\n";
    return result1 + result2;
  }

  @Override
  public String toString() {
    return "HighCostEvaluator{}";
  }
}
