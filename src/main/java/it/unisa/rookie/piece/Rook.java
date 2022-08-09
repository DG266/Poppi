package it.unisa.rookie.piece;

import it.unisa.rookie.Board;
import java.util.ArrayList;
import java.util.Collection;

public class Rook extends Piece {

  public Rook(Color color, Position position, boolean isFirstMove) {
    super(ChessPieceType.ROOK, color, position, isFirstMove);
  }

  public Rook(Color color, Position position) {
    super(ChessPieceType.ROOK, color, position, true);
  }

  @Override
  public Collection<Move> getLegalMoves(Board board) {
    int[] possibleOffsets = {-8, -1, 1, 8};

    ArrayList<Move> moves = new ArrayList<>();

    int column;

    for (int offset : possibleOffsets) {

      int startingPosition = this.getPosition().getValue();
      int candidateDestination = startingPosition;

      while (candidateDestination >= 0 && candidateDestination < 64) {

        column = candidateDestination % 8;

        // Avoid weird "jumps" from one end of the board to the other
        if (column == 0 && offset == -1) {
          break;
        }
        if (column == 7 && offset == 1) {
          break;
        }

        candidateDestination = candidateDestination + offset;

        if (candidateDestination >= 0 && candidateDestination < 64) {
          Piece pieceToCapture = board.getPiece(candidateDestination);
          if (pieceToCapture == null) {
            moves.add(new Move(board,
                    this.getPosition(),
                    Position.values()[candidateDestination],
                    this)
            );
          } else {
            if (pieceToCapture.getColor() != this.getColor()) {
              moves.add(new Move(board,
                      this.getPosition(),
                      Position.values()[candidateDestination],
                      this)
              );
            }
            break;
          }
        }

      }

    }
    return moves;
  }
}
