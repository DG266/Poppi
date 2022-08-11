package it.unisa.rookie.piece;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends Piece {

  public Pawn(Color color, Position position, boolean isFirstMove) {
    super(ChessPieceType.PAWN, color, position, isFirstMove);
  }

  public Pawn(Color color, Position position) {
    super(ChessPieceType.PAWN, color, position, true);
  }

  // TODO: Add promotion mechanics
  @Override
  public Collection<Move> getLegalMoves(Board board) {
    int[] possibleOffsets = {8, 16, 7, 9};

    ArrayList<Move> moves = new ArrayList<>();

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
        break;
      }
      // Go 1 tile up / Go 1 tile down
      if (offset == 8 && board.getPiece(candidateDestination) == null) {
        moves.add(new Move(board,
                this.getPosition(),
                Position.values()[candidateDestination],
                this)
        );
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
      } else if (offset == 7) {
        if (board.getPiece(candidateDestination) != null) {
          Piece pieceToCapture = board.getPiece(candidateDestination);
          if (pieceToCapture.getColor() != this.getColor()) {
            moves.add(new Move(board,
                    this.getPosition(),
                    Position.values()[candidateDestination],
                    this)
            );
          }
        }
      // Attack diagonally (Black - right | White - left)
      } else if (offset == 9) {
        if (board.getPiece(candidateDestination) != null) {
          Piece pieceToCapture = board.getPiece(candidateDestination);
          if (pieceToCapture.getColor() != this.getColor()) {
            moves.add(new Move(board,
                    this.getPosition(),
                    Position.values()[candidateDestination],
                    this)
            );
          }
        }
      }
    }
    return moves;
  }
}
