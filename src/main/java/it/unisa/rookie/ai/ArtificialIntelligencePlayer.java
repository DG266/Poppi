package it.unisa.rookie.ai;

import it.unisa.rookie.Board;
import it.unisa.rookie.Transition;

public interface ArtificialIntelligencePlayer {
  Transition play(Board startingBoard);
}
