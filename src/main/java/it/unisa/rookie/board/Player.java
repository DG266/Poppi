package it.unisa.rookie.board;

import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.Piece;
import java.util.ArrayList;

public class Player {
  private Board playingBoard;
  private Color playerColor;
  private ArrayList<Move> legalMoves;
  private ArrayList<Move> kingThreats;
  private int materialCount;

  private boolean isKingInCheck;

  public Player(Board playingBoard,
                Color playerColor,
                ArrayList<Move> legalMoves,
                ArrayList<Move> kingThreats,
                int materialCount) {
    this.playingBoard = playingBoard;
    this.playerColor = playerColor;
    this.legalMoves = legalMoves;
    this.kingThreats = kingThreats;
    this.materialCount = materialCount;

    this.isKingInCheck = !(this.kingThreats.isEmpty());
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

  public ArrayList<Move> getKingThreats() {
    return kingThreats;
  }

  public void setKingThreats(ArrayList<Move> kingThreats) {
    this.kingThreats = kingThreats;
  }

  public int getMaterialCount() {
    return materialCount;
  }

  public void setMaterialCount(int materialCount) {
    this.materialCount = materialCount;
  }

  public boolean isKingInCheck() {
    return this.isKingInCheck;
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
