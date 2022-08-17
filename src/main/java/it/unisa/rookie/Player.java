package it.unisa.rookie;

import it.unisa.rookie.piece.Color;
import java.util.ArrayList;

public class Player {
  private Color playerColor;
  private ArrayList<Move> legalMoves;
  private boolean isKingInCheck;

  public Player(Color playerColor, ArrayList<Move> legalMoves, boolean isKingInCheck) {
    this.playerColor = playerColor;
    this.legalMoves = legalMoves;
    this.isKingInCheck = isKingInCheck;
  }

  public Color getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(Color playerColor) {
    this.playerColor = playerColor;
  }

  public ArrayList<Move> getLegalMoves() {
    return legalMoves;
  }

  public void setLegalMoves(ArrayList<Move> legalMoves) {
    this.legalMoves = legalMoves;
  }

  public boolean isKingInCheck() {
    return isKingInCheck;
  }

  public void setKingInCheck(boolean kingInCheck) {
    isKingInCheck = kingInCheck;
  }
}
