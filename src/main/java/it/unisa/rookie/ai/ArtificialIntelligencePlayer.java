package it.unisa.rookie.ai;

import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Transition;

public interface ArtificialIntelligencePlayer {
  Transition play(Board startingBoard);
}
