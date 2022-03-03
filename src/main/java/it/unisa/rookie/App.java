package it.unisa.rookie;

import it.unisa.rookie.piece.Piece;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
  private static final double SCENE_WIDTH = 600;
  private static final double SCENE_HEIGHT = 600;
  // private static final double BOARD_MIN_WIDTH = 200;
  // private static final double BOARD_MIN_HEIGHT = 200;
  // private static final double BOARD_MAX_WIDTH = 400;
  // private static final double BOARD_MAX_HEIGHT = 400;

  private Board gameBoard;

  private BorderPane root;
  private MenuBar gameMenuBar;
  private GridPane boardPane;
  private ArrayList<Tile> tiles;

  private GridPane createBoardPane() {
    GridPane board = new GridPane();

    board.setVgap(0);
    board.setHgap(0);

    // 8x8 chessboard - each of the 64 tiles will be 50x50 px wide
    for (int i = 0; i < 8; i++) {
      board.getColumnConstraints().add(new ColumnConstraints(50));
      board.getRowConstraints().add(new RowConstraints(50));
    }

    tiles = new ArrayList<>();
    int tileIdCounter = 0;

    // Assign colors and icons
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Color color = ((i + j) % 2 == 0) ? Color.WHITESMOKE : Color.LIGHTGRAY;
        StackPane pane = new StackPane();
        pane.setBackground(
          new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY))
        );
        board.add(pane, j, i);  // j -> column i -> row
        GridPane.setFillHeight(pane, true);
        GridPane.setFillWidth(pane, true);

        tiles.add(new Tile(tileIdCounter, pane));

        Piece p = gameBoard.getPiece(tileIdCounter);

        if (p != null) {
          String imagePath = "./pics/" + p.getColor().toString() + p.getType().getName() + ".png";
          FileInputStream input = null;
          try {
            input = new FileInputStream(imagePath);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          }
          ImageView image = new ImageView(new Image(input));

          image.setFitHeight(50);
          image.setFitWidth(50);

          Label imageLabel = new Label();
          imageLabel.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
          imageLabel.setGraphic(image);
          imageLabel.setTooltip(new Tooltip(p.getColor().toString() + " " + p.getType().getName()));

          tiles.get(tileIdCounter).getPane().getChildren().add(imageLabel);
        }
        tileIdCounter++;
      }
    }

    return board;
  }

  private MenuBar createMenuBar() {

    // File Menu
    Menu fileMenu = new Menu("File");

    MenuItem fileMenuItem1 = new MenuItem("Exit");
    fileMenuItem1.setOnAction((ActionEvent t) -> {
      System.exit(0);
    });

    fileMenu.getItems().addAll(fileMenuItem1);

    // Other Menus...

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu);

    return menuBar;
  }

  @Override
  public void start(Stage primaryStage) {
    gameBoard = new Board();

    gameMenuBar = createMenuBar();
    boardPane = createBoardPane();

    root = new BorderPane(boardPane);
    root.setTop(gameMenuBar);
    root.setBackground(
      new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))
    );

    boardPane.setAlignment(Pos.CENTER);

    // Load icon
    FileInputStream input = null;
    try {
      input = new FileInputStream("pics/BlackRook.png");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Image icon = new Image(input);

    primaryStage.getIcons().add(icon);

    primaryStage.setTitle("Rookie");

    Scene game = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    primaryStage.setScene(game);
    primaryStage.show();
  }

  public static void main(String[] args) {
    Application.launch();
  }

}