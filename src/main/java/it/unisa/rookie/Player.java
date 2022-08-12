package it.unisa.rookie;

import it.unisa.rookie.piece.Color;

public class Player {
  private Color playerColor;
  private boolean isKingInCheck;

  public Player(Color playerColor, boolean isKingInCheck) {
    this.playerColor = playerColor;
    this.isKingInCheck = isKingInCheck;
  }

  public Color getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(Color playerColor) {
    this.playerColor = playerColor;
  }

  public boolean isKingInCheck() {
    return isKingInCheck;
  }

  public void setKingInCheck(boolean kingInCheck) {
    isKingInCheck = kingInCheck;
  }
}
