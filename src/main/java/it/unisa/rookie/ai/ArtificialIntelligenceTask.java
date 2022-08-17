package it.unisa.rookie.ai;

import it.unisa.rookie.Board;
import it.unisa.rookie.Transition;
import javafx.concurrent.Task;

public class ArtificialIntelligenceTask extends Task<Transition> {

  private Board gameBoard;
  private ArtificialIntelligencePlayer ai;

  public ArtificialIntelligenceTask(Board gameBoard, ArtificialIntelligencePlayer ai) {
    this.gameBoard = gameBoard;
    this.ai = ai;
  }

  @Override
  protected Transition call() throws Exception {
    return ai.play(gameBoard);
  }
}
