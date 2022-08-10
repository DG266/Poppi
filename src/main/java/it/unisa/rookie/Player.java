package it.unisa.rookie;

import it.unisa.rookie.piece.Color;

public class Player {
  private Color playerColor;

  public Player(Color playerColor) {
    this.playerColor = playerColor;
  }

  public Color getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(Color playerColor) {
    this.playerColor = playerColor;
  }
}
