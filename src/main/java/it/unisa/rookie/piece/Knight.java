package it.unisa.rookie.piece;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import java.util.ArrayList;
import java.util.Collection;

public class Knight extends Piece {

  public Knight(Color color, Position position, boolean isFirstMove) {
    super(ChessPieceType.KNIGHT, color, position, isFirstMove);
  }

  public Knight(Color color, Position position) {
    super(ChessPieceType.KNIGHT, color, position, true);
  }

  @Override
  public Collection<Move> getLegalMoves(Board board) {
    int[] possibleOffsets = {-17, -15, -10, -6, 6, 10, 15, 17};

    ArrayList<Move> moves = new ArrayList<>();

    int column = this.getPosition().getValue() % 8;

    for (int offset : possibleOffsets) {

      // Avoid weird "jumps" from one end of the board to the other
      if (column == 0 && (offset == -17 || offset == -10 || offset == 6 || offset == 15)) {
        continue;
      }

      if (column == 1 && (offset == -10 || offset == 6)) {
        continue;
      }

      if (column == 6 && (offset == -6 || offset == 10)) {
        continue;
      }

      if (column == 7 && (offset == -15 || offset == -6 || offset == 10 || offset == 17)) {
        continue;
      }

      int candidateDestination = this.getPosition().getValue() + offset;

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
        }
      }
    }
    return moves;
  }
}
