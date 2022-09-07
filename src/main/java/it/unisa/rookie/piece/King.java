package it.unisa.rookie.piece;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.CastlingMove;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
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
        continue;
      }
      if (column == 7 && (offset == -7 || offset == 1 || offset == 9)) {
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

  public ArrayList<Move> getCastlingMoves(Board board) {
    ArrayList<Move> castlingMoves = new ArrayList<>();

    if (this.getColor() == Color.WHITE) {
      Player p = board.getWhitePlayer();

      Piece pawnAttacker = board.getPiece(Position.E2.getValue());
      boolean isOpponentPawnInFrontOfTheKing =
              pawnAttacker != null
                      && pawnAttacker.getType() == ChessPieceType.PAWN
                      && pawnAttacker.getColor() != this.getColor();

      // WHITE KING SIDE CASTLING (SHORT CASTLING)
      if (board.getPiece(Position.F1.getValue()) == null && board.getPiece(Position.G1.getValue()) == null) {
        if (this.isFirstMove() && this.getPosition() == Position.E1 && !p.isKingInCheck()) {
          Piece kingSideRook = board.getPiece(Position.H1.getValue());
          if (kingSideRook != null && kingSideRook.isFirstMove() && kingSideRook.getType() == ChessPieceType.ROOK) {
            ArrayList<Move> threats = p.getOpponentPlayer().getLegalMoves();
            if ((board.getThreats(Position.F1.getValue(), threats).isEmpty())
                    && (board.getThreats(Position.G1.getValue(), threats).isEmpty())
                    && !isOpponentPawnInFrontOfTheKing) {
              //System.out.println("WHITE KING SIDE CASTLING POSSIBLE!");
              castlingMoves.add(new CastlingMove(
                      board,
                      this.getPosition(),
                      Position.G1,
                      this,
                      kingSideRook.getPosition(),
                      Position.F1,
                      kingSideRook
              ));
            }
          }
        }
      }

      // WHITE QUEEN SIDE CASTLING (LONG CASTLING)
      if (board.getPiece(Position.B1.getValue()) == null
              && board.getPiece(Position.C1.getValue()) == null
              && board.getPiece(Position.D1.getValue()) == null) {
        if (this.isFirstMove() && this.getPosition() == Position.E1 && !p.isKingInCheck()) {
          Piece queenSideRook = board.getPiece(Position.A1.getValue());
          if (queenSideRook != null && queenSideRook.isFirstMove() && queenSideRook.getType() == ChessPieceType.ROOK) {
            ArrayList<Move> threats = p.getOpponentPlayer().getLegalMoves();
            if ((board.getThreats(Position.B1.getValue(), threats).isEmpty())
                    && (board.getThreats(Position.C1.getValue(), threats).isEmpty())
                    && (board.getThreats(Position.D1.getValue(), threats).isEmpty())
                    && !isOpponentPawnInFrontOfTheKing) {
              //System.out.println("WHITE QUEEN SIDE CASTLING POSSIBLE!");
              castlingMoves.add(new CastlingMove(
                      board,
                      this.getPosition(),
                      Position.C1,
                      this,
                      queenSideRook.getPosition(),
                      Position.D1,
                      queenSideRook
              ));
            }
          }
        }
      }
    } else {
      Player p = board.getBlackPlayer();

      Piece pawnAttacker = board.getPiece(Position.E7.getValue());
      boolean isOpponentPawnInFrontOfTheKing =
              pawnAttacker != null
                      && pawnAttacker.getType() == ChessPieceType.PAWN
                      && pawnAttacker.getColor() != this.getColor();

      // BLACK KING SIDE CASTLING (SHORT CASTLING)
      if (board.getPiece(Position.F8.getValue()) == null && board.getPiece(Position.G8.getValue()) == null) {
        if (this.isFirstMove() && this.getPosition() == Position.E8 && !p.isKingInCheck()) {
          Piece kingSideRook = board.getPiece(Position.H8.getValue());
          if (kingSideRook != null && kingSideRook.isFirstMove() && kingSideRook.getType() == ChessPieceType.ROOK) {
            ArrayList<Move> threats = p.getOpponentPlayer().getLegalMoves();
            if ((board.getThreats(Position.F8.getValue(), threats).isEmpty())
                    && (board.getThreats(Position.G8.getValue(), threats).isEmpty())
                    && !isOpponentPawnInFrontOfTheKing) {
              //System.out.println("BLACK KING SIDE CASTLING POSSIBLE!");
              castlingMoves.add(new CastlingMove(
                      board,
                      this.getPosition(),
                      Position.G8,
                      this,
                      kingSideRook.getPosition(),
                      Position.F8,
                      kingSideRook
              ));
            }
          }
        }
      }

      // BLACK QUEEN SIDE CASTLING (LONG CASTLING)
      if (board.getPiece(Position.B8.getValue()) == null
              && board.getPiece(Position.C8.getValue()) == null
              && board.getPiece(Position.D8.getValue()) == null) {
        if (this.isFirstMove() && this.getPosition() == Position.E8 && !p.isKingInCheck()) {
          Piece queenSideRook = board.getPiece(Position.A8.getValue());
          if (queenSideRook != null && queenSideRook.isFirstMove() && queenSideRook.getType() == ChessPieceType.ROOK) {
            ArrayList<Move> threats = p.getOpponentPlayer().getLegalMoves();
            if ((board.getThreats(Position.B8.getValue(), threats).isEmpty())
                    && (board.getThreats(Position.C8.getValue(), threats).isEmpty())
                    && (board.getThreats(Position.D8.getValue(), threats).isEmpty())
                    && !isOpponentPawnInFrontOfTheKing) {
              //System.out.println("BLACK QUEEN SIDE CASTLING POSSIBLE!");
              castlingMoves.add(new CastlingMove(
                      board,
                      this.getPosition(),
                      Position.C8,
                      this,
                      queenSideRook.getPosition(),
                      Position.D8,
                      queenSideRook
              ));
            }
          }
        }
      }
    }
    return castlingMoves;
  }
}
