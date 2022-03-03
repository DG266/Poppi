package it.unisa.rookie;

import javafx.scene.layout.StackPane;

public class Tile {
  private Integer id;
  private StackPane pane;

  public Tile(Integer id, StackPane pane) {
    this.id = id;
    this.pane = pane;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public StackPane getPane() {
    return pane;
  }

  public void setPane(StackPane pane) {
    this.pane = pane;
  }
}
