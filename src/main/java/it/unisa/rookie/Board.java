package it.unisa.rookie;

import it.unisa.rookie.piece.Bishop;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.King;
import it.unisa.rookie.piece.Knight;
import it.unisa.rookie.piece.Move;
import it.unisa.rookie.piece.Pawn;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import it.unisa.rookie.piece.Queen;
import it.unisa.rookie.piece.Rook;
import java.util.HashMap;
import java.util.Map;

public class Board {
  private Map<Integer, Piece> boardPositions;
  private Player currentPlayer;

  public Board(Player startingPlayer) {
    boardPositions = new HashMap<>(32, 1.0f);

    putPiece(new Rook(Color.BLACK, Position.A8));
    putPiece(new Knight(Color.BLACK, Position.B8));
    putPiece(new Bishop(Color.BLACK, Position.C8));
    putPiece(new Queen(Color.BLACK, Position.D8));
    putPiece(new King(Color.BLACK, Position.E8));
    putPiece(new Bishop(Color.BLACK, Position.F8));
    putPiece(new Knight(Color.BLACK, Position.G8));
    putPiece(new Rook(Color.BLACK, Position.H8));
    putPiece(new Pawn(Color.BLACK, Position.A7));
    putPiece(new Pawn(Color.BLACK, Position.B7));
    putPiece(new Pawn(Color.BLACK, Position.C7));
    putPiece(new Pawn(Color.BLACK, Position.D7));
    putPiece(new Pawn(Color.BLACK, Position.E7));
    putPiece(new Pawn(Color.BLACK, Position.F7));
    putPiece(new Pawn(Color.BLACK, Position.G7));
    putPiece(new Pawn(Color.BLACK, Position.H7));

    putPiece(new Pawn(Color.WHITE, Position.A2));
    putPiece(new Pawn(Color.WHITE, Position.B2));
    putPiece(new Pawn(Color.WHITE, Position.C2));
    putPiece(new Pawn(Color.WHITE, Position.D2));
    putPiece(new Pawn(Color.WHITE, Position.E2));
    putPiece(new Pawn(Color.WHITE, Position.F2));
    putPiece(new Pawn(Color.WHITE, Position.G2));
    putPiece(new Pawn(Color.WHITE, Position.H2));
    putPiece(new Rook(Color.WHITE, Position.A1));
    putPiece(new Knight(Color.WHITE, Position.B1));
    putPiece(new Bishop(Color.WHITE, Position.C1));
    putPiece(new Queen(Color.WHITE, Position.D1));
    putPiece(new King(Color.WHITE, Position.E1));
    putPiece(new Bishop(Color.WHITE, Position.F1));
    putPiece(new Knight(Color.WHITE, Position.G1));
    putPiece(new Rook(Color.WHITE, Position.H1));

    this.currentPlayer = startingPlayer;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  private void putPiece(Piece p) {
    this.boardPositions.put(p.getPosition().getValue(), p);
  }

  public Piece getPiece(Integer pos) {
    return this.boardPositions.get(pos);
  }

  public void makeMove(Move move) {
    Piece toMove = move.getMovedPiece();
    Position source = move.getSource();
    Position destination = move.getDestination();

    if (source.getValue() != destination.getValue()) {
      boardPositions.remove(source.getValue());
      toMove.setPosition(destination);
      boardPositions.put(destination.getValue(), toMove);    // Maybe I should use "putPiece()"
      //this.putPiece(toMove);
    }
  }

  @Override
  public String toString() {
    return "Board{"
            + "boardPositions=" + boardPositions
            + "}";
  }
}
