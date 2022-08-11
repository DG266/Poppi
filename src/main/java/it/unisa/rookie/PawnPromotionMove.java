package it.unisa.rookie;

import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PawnPromotionMove extends Move {
  private Piece promotionPiece;

  public PawnPromotionMove(Board board, Position source, Position destination, Piece movedPiece,
                           Piece promotionPiece) {
    super(board, source, destination, movedPiece);
    this.promotionPiece = promotionPiece;
  }

  public Piece getPromotionPiece() {
    return promotionPiece;
  }

  public void setPromotionPiece(Piece promotionPiece) {
    this.promotionPiece = promotionPiece;
  }

  @Override
  public Board makeMove() {
    ArrayList<Piece> whitePieces = new ArrayList<>(this.getBoard().getWhitePieces());
    ArrayList<Piece> blackPieces = new ArrayList<>(this.getBoard().getBlackPieces());

    Color currentPlayerColor = this.getBoard().getCurrentPlayer().getPlayerColor();

    if (currentPlayerColor == Color.WHITE) {
      for (Piece p : whitePieces) {
        if (p.equals(this.getMovedPiece())) {
          whitePieces.remove(p);
          break;
        }
      }
    } else {
      for (Piece p : blackPieces) {
        if (p.equals(this.getMovedPiece())) {
          blackPieces.remove(p);
          break;
        }
      }
    }

    // Choose the next player
    Player next;
    if (currentPlayerColor == Color.WHITE) {
      next = new Player(Color.BLACK);
    } else {
      next = new Player(Color.WHITE);
    }

    // Fill the new board
    Map<Integer, Piece> newBoardPositions = new HashMap<>(32, 1.0f);

    for (Piece p : whitePieces) {
      newBoardPositions.put(p.getPosition().getValue(), p);
    }
    for (Piece p : blackPieces) {
      newBoardPositions.put(p.getPosition().getValue(), p);
    }

    // Add the moved piece
    newBoardPositions.put(this.getDestination().getValue(), this.promotionPiece);

    return new Board(newBoardPositions, next, this);
  }

  @Override
  public String toString() {
    return "Move{"
            //+ "board=" + board
            //+ ", source=" + source
            + "source=" + this.getSource()
            + ", destination=" + this.getDestination()
            + ", movedPiece=" + this.getMovedPiece()
            + ", promotionPiece=" + this.getPromotionPiece()
            + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof PawnPromotionMove) {
      return super.equals(o) && promotionPiece.equals(((PawnPromotionMove) o).getPromotionPiece());
    }
    return false;
  }
}
