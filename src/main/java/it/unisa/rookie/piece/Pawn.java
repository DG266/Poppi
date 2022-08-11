package it.unisa.rookie.piece;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import it.unisa.rookie.PawnPromotionMove;
import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends Piece {

  public Pawn(Color color, Position position, boolean isFirstMove) {
    super(ChessPieceType.PAWN, color, position, isFirstMove);
  }

  public Pawn(Color color, Position position) {
    super(ChessPieceType.PAWN, color, position, true);
  }

  @Override
  public Collection<Move> getLegalMoves(Board board) {
    int[] possibleOffsets = {8, 16, 7, 9};

    ArrayList<Move> moves = new ArrayList<>();

    int column = this.getPosition().getValue() % 8;

    // Black pawns "move forward" (+1 * offset) and White pawns "move backwards" (-1 * offset)
    int adjustment;
    if (this.getColor() == Color.BLACK) {
      adjustment = 1;
    } else {
      adjustment = -1;
    }

    for (int offset : possibleOffsets) {
      int candidateDestination = this.getPosition().getValue() + (adjustment * offset);

      if (!(candidateDestination >= 0 && candidateDestination < 64)) {
        continue;
      }

      // Go 1 tile up / Go 1 tile down
      if (offset == 8 && board.getPiece(candidateDestination) == null) {
        // If (promotion tile)
        if (isPromotionTile(candidateDestination)) {

          // TODO: Add more options (promote to queen/rook/knight/bishop)
          Piece pawnToQueen = new Queen(
                  this.getColor(),
                  Position.values()[candidateDestination],
                  false
          );

          moves.add(new PawnPromotionMove(board,
                  this.getPosition(),
                  Position.values()[candidateDestination],
                  this,
                  pawnToQueen)
          );
        } else {
          moves.add(new Move(board,
                  this.getPosition(),
                  Position.values()[candidateDestination],
                  this)
          );
        }

      // Go 2 tiles up / Go 2 tiles down
      } else if (offset == 16 && this.isFirstMove()) {
        int candidateDestinationPreviousTile = this.getPosition().getValue() + (adjustment * 8);
        if (board.getPiece(candidateDestinationPreviousTile) == null
            && board.getPiece(candidateDestination) == null) {
          moves.add(new Move(board,
                  this.getPosition(),
                  Position.values()[candidateDestination],
                  this)
          );
        }
      // Attack diagonally (Black - left | White - right)
      } else if (offset == 7
              && !((column == 7 && this.getColor() == Color.WHITE)
                || (column == 0 && this.getColor() == Color.BLACK))) {
        if (board.getPiece(candidateDestination) != null) {
          Piece pieceToCapture = board.getPiece(candidateDestination);
          if (pieceToCapture.getColor() != this.getColor()) {
            // If (promotion tile)
            if (isPromotionTile(candidateDestination)) {
              Piece pawnToQueen = new Queen(
                      this.getColor(),
                      Position.values()[candidateDestination],
                      false
              );
              moves.add(new PawnPromotionMove(board,
                      this.getPosition(),
                      Position.values()[candidateDestination],
                      this,
                      pawnToQueen)
              );
            } else {
              moves.add(new Move(board,
                      this.getPosition(),
                      Position.values()[candidateDestination],
                      this)
              );
            }
          }
        }
      // Attack diagonally (Black - right | White - left)
      } else if (offset == 9
              && !((column == 0 && this.getColor() == Color.WHITE)
                || (column == 7 && this.getColor() == Color.BLACK))) {
        if (board.getPiece(candidateDestination) != null) {
          Piece pieceToCapture = board.getPiece(candidateDestination);
          if (pieceToCapture.getColor() != this.getColor()) {
            // If (promotion tile)
            if (isPromotionTile(candidateDestination)) {
              Piece pawnToQueen = new Queen(
                      this.getColor(),
                      Position.values()[candidateDestination],
                      false
              );
              moves.add(new PawnPromotionMove(board,
                      this.getPosition(),
                      Position.values()[candidateDestination],
                      this,
                      pawnToQueen)
              );
            } else {
              moves.add(new Move(board,
                      this.getPosition(),
                      Position.values()[candidateDestination],
                      this)
              );
            }
          }
        }
      }
    }
    return moves;
  }

  private boolean isPromotionTile(int destination) {
    if (this.getColor() == Color.WHITE) {
      return (destination >= 0 && destination < 8);
    } else {
      return (destination >= 56 && destination < 64);
    }
  }
}
