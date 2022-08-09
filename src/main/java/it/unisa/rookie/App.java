package it.unisa.rookie;

import it.unisa.rookie.piece.Move;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

  private Piece clickedPiece;
  private Piece selectedPiece;

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

    // Create tiles
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Color color = ((i + j) % 2 == 0) ? Color.WHITESMOKE : Color.LIGHTGRAY;
        Tile tile = new Tile(tileIdCounter, color, gameBoard);
        board.add(tile, j, i);  // j -> column i -> row
        tile.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        tiles.add(tile);
        tileIdCounter++;
      }
    }
    return board;
  }

  private void drawBoard() {
    boardPane.getChildren().clear();
    int tileCounter = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Tile t = tiles.get(tileCounter);
        t.getChildren().clear();
        t.addColor();
        t.addPieceIcon();
        t.drawBorder(this.selectedPiece);
        t.drawLegalMove(this.selectedPiece);
        boardPane.add(t, j, i);
        tileCounter++;
      }
    }
  }

  EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent e) {
      Tile selectedTile = null;

      if (e.getTarget() instanceof Tile) {          // An empty tile has been clicked
        selectedTile = (Tile) e.getTarget();
      } else if (e.getTarget() instanceof Label) {  // A non-empty tile has been clicked
        selectedTile = (Tile) ((Label) e.getTarget()).getParent();
      } else if (e.getTarget() instanceof Circle) { // A non-empty tile (legal move) has been clicked
        selectedTile = (Tile) ((Circle) e.getTarget()).getParent();
      }

      //TODO: I need to come up with better variable names...
      if (clickedPiece == null) {
        clickedPiece = gameBoard.getPiece(selectedTile.getTileId());
        selectedPiece = clickedPiece;
      } else {   // Move a piece
        Move move = new Move(
                gameBoard,
                clickedPiece.getPosition(),
                Position.values()[selectedTile.getTileId()],
                selectedPiece
        );
        //System.out.println(move);
        System.out.println(
                "SOURCE:" + clickedPiece.getPosition().getValue()
                        + " | DESTINATION: " + Position.values()[selectedTile.getTileId()].getValue()
                        + " | MOVED_PIECE: " + selectedPiece.getType()
        );
        ArrayList<Move> legalMoves = (ArrayList<Move>) clickedPiece.getLegalMoves(gameBoard);

        System.out.println(legalMoves);

        if (legalMoves.contains(move)) {
          gameBoard.makeMove(move);
          if (move.getMovedPiece().isFirstMove() == true) {
            move.getMovedPiece().setFirstMove(false);
          }
        }
        clickedPiece = null;
        selectedPiece = null;
      }
      //System.out.println(gameBoard);
      drawBoard();
    }
  };

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