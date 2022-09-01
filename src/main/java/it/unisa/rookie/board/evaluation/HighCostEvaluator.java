package it.unisa.rookie.board.evaluation;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.CastlingMove;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
import it.unisa.rookie.piece.ChessPieceType;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Piece;

public class HighCostEvaluator implements Evaluator {

  private static final int[] WHITE_PAWN_STRUCTURE = {
      0,  0,  0,  0,  0,  0,  0,  0,
      50, 50, 50, 50, 50, 50, 50, 50,
      10, 10, 20, 30, 30, 20, 10, 10,
      5,  5, 10, 25, 25, 10,  5,  5,
      0,  0,  0, 20, 20,  0,  0,  0,
      5, -5,-10,  0,  0,-10, -5,  5,
      5, 10, 10,-20,-20, 10, 10,  5,
      0,  0,  0,  0,  0,  0,  0,  0
  };

  private static final int[] BLACK_PAWN_STRUCTURE = {
      0,  0,  0,  0,  0,  0,  0,  0,
      5, 10, 10,-20,-20, 10, 10,  5,
      5, -5,-10,  0,  0,-10, -5,  5,
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
            + mobility(player)
            + availableGoodAttacks(player)
            + castlingEvaluation(player)
            + kingInCheckBonus(player)
            + pawnStructureScore(player);
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
    boolean isCastlingMove = (m instanceof CastlingMove);
    boolean playerMadeThisMove = m.getMovedPiece().getColor() == player.getPlayerColor();

    return (isCastlingMove && playerMadeThisMove) ? 500 : 0;
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
            + "Pawn Structure: " + pawnStructureScore(w) + "\n";
    String result2 =
            "BLACK SCORE (" + getScoreByPlayer(b) + ") - "
            + "Pieces value: " + b.getMaterialCount() + " | "
            + "Mobility bonus: " + mobility(b) + " | "
            + "Attacks bonus: " + availableGoodAttacks(b) + " | "
            + "Castling bonus: " + castlingEvaluation(b) + " | "
            + "Opponent King in check bonus: " + kingInCheckBonus(b) + " | "
            + "Pawn Structure: " + pawnStructureScore(b) + "\n";
    return result1 + result2;
  }

  /*
  public String getEvaluationDescription(Board board) {
    Player w = board.getWhitePlayer();
    Player b = board.getBlackPlayer();
    String result1 =
            "* WHITE PLAYER" + "\n"
            + "Pieces value: " + w.getMaterialCount() + "\n"
            + "Mobility bonus: " + mobility(w) + "\n"
            + "Attacks bonus: " + availableGoodAttacks(w) + "\n"
            + "Castling bonus: " + castlingEvaluation(w) + "\n"
            + "Opponent King in check bonus: " + kingInCheckBonus(w) + "\n"
            + "Pawn Structure: " + pawnStructureScore(w) + "\n";
    String result2 =
            "* BLACK PLAYER" + "\n"
            + "Pieces value: " + b.getMaterialCount() + "\n"
            + "Mobility bonus: " + mobility(b) + "\n"
            + "Attacks bonus: " + availableGoodAttacks(b) + "\n"
            + "Castling bonus: " + castlingEvaluation(b) + "\n"
            + "Opponent King in check bonus: " + kingInCheckBonus(b) + "\n"
            + "Pawn Structure: " + pawnStructureScore(b) + "\n";
    return result1 + result2;
  }
  */

  @Override
  public String toString() {
    return "HighCostEvaluator{}";
  }
}
