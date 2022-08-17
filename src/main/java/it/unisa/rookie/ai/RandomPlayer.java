package it.unisa.rookie.ai;

import it.unisa.rookie.Board;
import it.unisa.rookie.Move;
import it.unisa.rookie.Transition;
import it.unisa.rookie.piece.Color;
import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer implements ArtificialIntelligencePlayer {

  public RandomPlayer() {
  }

  public Transition play(Board startingBoard) {
    ArrayList<Move> actualMoves = new ArrayList<>();

    for (Move m : startingBoard.getCurrentPlayer().getLegalMoves()) {
      if (!m.makeMove().getOpponentPlayer().isKingInCheck()) {
        actualMoves.add(m);
      }
    }

    // Choose a random move
    if (!actualMoves.isEmpty()) {
      int rand = (new Random()).nextInt(actualMoves.size());
      Move move = actualMoves.get(rand);
      Board newGameBoard = move.makeMove();
      Transition result = new Transition(startingBoard, newGameBoard, move);
      return result;
    } else {
      // Should never get here
      //System.out.println("AI: I can't do anything!");
      return null;
    }
  }
}
