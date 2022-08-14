package it.unisa.rookie.ai;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import it.unisa.rookie.Transition;
import it.unisa.rookie.piece.Color;
import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer implements ArtificialIntelligencePlayer {
  private Board gameBoard;

  public RandomPlayer(Board gameBoard) {
    this.gameBoard = gameBoard;
  }

  public Transition play() {
    ArrayList<Move> actualMoves = new ArrayList<>();
    ArrayList<Move> legalMoves = gameBoard.getCurrentPlayer().getPlayerColor() == Color.BLACK
            ? new ArrayList<>(gameBoard.getBlackPlayerLegalMoves())
            : new ArrayList<>(gameBoard.getWhitePlayerLegalMoves());

    for (Move m : legalMoves) {
      if (!m.makeMove().getOpponentPlayer().isKingInCheck()) {
        actualMoves.add(m);
      }
    }

    // Choose a random move
    if (!actualMoves.isEmpty()) {
      int rand = (new Random()).nextInt(actualMoves.size());
      Move move = actualMoves.get(rand);
      Board newGameBoard = move.makeMove();
      Transition result = new Transition(gameBoard, newGameBoard, move);
      return result;
    } else {
      // Should never get here
      //System.out.println("AI: I can't do anything!");
      return null;
    }
  }
}
