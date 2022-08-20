package it.unisa.rookie;

import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Piece;
import java.util.ArrayList;

public class Player {
  private Board playingBoard;
  private Color playerColor;
  private ArrayList<Move> legalMoves;
  private boolean isKingInCheck;

  public Player(Board playingBoard,
                Color playerColor,
                ArrayList<Move> legalMoves,
                boolean isKingInCheck) {
    this.playingBoard = playingBoard;
    this.playerColor = playerColor;
    this.legalMoves = legalMoves;
    this.isKingInCheck = isKingInCheck;
  }

  public Board getPlayingBoard() {
    return playingBoard;
  }

  public void setPlayingBoard(Board playingBoard) {
    this.playingBoard = playingBoard;
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

  public Player getOpponentPlayer() {
    return this.playerColor == Color.WHITE
            ? playingBoard.getBlackPlayer()
            : playingBoard.getWhitePlayer();
  }

  public ArrayList<Piece> getPieces() {
    return this.playerColor == Color.WHITE
            ? playingBoard.getWhitePieces()
            : playingBoard.getBlackPieces();
  }
}
