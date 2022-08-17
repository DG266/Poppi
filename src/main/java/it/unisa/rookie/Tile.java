package it.unisa.rookie;

import it.unisa.rookie.piece.Piece;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Tile extends StackPane {
  private Integer tileId;
  private Color color;
  private Board gameBoard;


  public Tile(Integer tileId, Color color, Board gameBoard) {
    this.tileId = tileId;
    this.color = color;
    this.gameBoard = gameBoard;

    GridPane.setFillHeight(this, true);
    GridPane.setFillWidth(this, true);

    this.addColor();
    this.addPieceIcon();
  }

  public Integer getTileId() {
    return tileId;
  }

  public void setTileId(Integer tileId) {
    this.tileId = tileId;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Board getGameBoard() {
    return gameBoard;
  }

  public void setGameBoard(Board gameBoard) {
    this.gameBoard = gameBoard;
  }

  public void addColor() {
    this.setBackground(
            new Background(new BackgroundFill(this.color, CornerRadii.EMPTY, Insets.EMPTY))
    );
  }

  public void addPieceIcon() {
    Piece p = this.gameBoard.getPiece(this.tileId);

    if (p != null) {
      String imagePath = "./pics/" + p.getColor().toString() + p.getType().getName() + ".png";
      FileInputStream input = null;
      try {
        input = new FileInputStream(imagePath);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      ImageView image = new ImageView(new Image(input));

      image.setFitHeight(46);
      image.setFitWidth(46);

      Label imageLabel = new Label();
      imageLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
      imageLabel.setGraphic(image);
      imageLabel.setTooltip(new Tooltip(p.getColor().toString() + " " + p.getType().getName()));

      this.getChildren().add(imageLabel);
    }
  }

  public void drawBorder(Piece selectedPiece) {
    // TODO: Check if player color is correct (Black player should be able to select only his pieces etc.)
    if (selectedPiece != null && selectedPiece.getPosition().getValue() == this.tileId) {
      this.setBorder(
              new Border(
                      new BorderStroke(
                              Color.RED,
                              BorderStrokeStyle.SOLID,
                              CornerRadii.EMPTY,
                              BorderWidths.DEFAULT)
              )
      );
    } else {
      this.setBorder(null);
    }
  }

  public void drawLegalMove(Piece selectedPiece) {
    if (selectedPiece != null) {
      ArrayList<Move> legalMoves = (ArrayList<Move>) selectedPiece.getLegalMoves(this.gameBoard);
      for (Move m : legalMoves) {
        if (m.getDestination().getValue() == this.tileId) {
          Circle c = new Circle();
          c.setRadius(10);
          c.setStroke(Color.GREEN);
          c.setFill(Color.GREEN);
          this.getChildren().add(c);
        }
      }
    }
  }

  // TODO: choose better colors...?
  public void drawArtificialIntelligenceMove(Move m) {
    if (m != null) {
      int source = m.getSource().getValue();
      int destination = m.getDestination().getValue();
      if (source == this.tileId) {
        this.setBackground(
                new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY))
        );
      } else if (destination == this.tileId) {
        this.setBackground(
                new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY))
        );
      }
    }
  }
}
