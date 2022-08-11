package it.unisa.rookie.piece;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import java.util.ArrayList;
import java.util.Collection;

public class King extends Piece {

  public King(Color color, Position position, boolean isFirstMove) {
    super(ChessPieceType.KING, color, position, isFirstMove);
  }

  public King(Color color, Position position) {
    super(ChessPieceType.KING, color, position, true);
  }

  @Override
  public Collection<Move> getLegalMoves(Board board) {
    int[] possibleOffsets = {-9, -8, -7, -1, 1, 7, 8, 9};

    ArrayList<Move> moves = new ArrayList<>();

    int column = this.getPosition().getValue() % 8;

    for (int offset : possibleOffsets) {
      if (column == 0 && (offset == -9 || offset == -1 || offset == 7)) {
        break;
      }
      if (column == 7 && (offset == -7 || offset == 1 || offset == 9)) {
        break;
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
