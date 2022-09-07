package it.unisa.rookie.piece;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Move;
import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends Piece {

  public Bishop(Color color, Position position, boolean isFirstMove) {
    super(ChessPieceType.BISHOP, color, position, isFirstMove);
  }

  public Bishop(Color color, Position position) {
    super(ChessPieceType.BISHOP, color, position, true);
  }

  @Override
  public Collection<Move> getLegalMoves(Board board) {
    int[] possibleOffsets = {-9, -7, 7, 9};

    ArrayList<Move> moves = new ArrayList<>();

    int column;

    for (int offset : possibleOffsets) {

      int startingPosition = this.getPosition().getValue();
      int candidateDestination = startingPosition;

      while (candidateDestination >= 0 && candidateDestination < 64) {

        column = candidateDestination % 8;

        // Avoid weird "jumps" from one end of the board to the other
        if (column == 0 && (offset == -9 || offset == 7)) {
          break;
        }
        if (column == 7 && (offset == -7 || offset == 9)) {
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
