package it.unisa.rookie;

import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class App extends Application {
  private static final double SCENE_WIDTH = 1280;
  private static final double SCENE_HEIGHT = 720;
  // private static final double BOARD_MIN_WIDTH = 200;
  // private static final double BOARD_MIN_HEIGHT = 200;
  // private static final double BOARD_MAX_WIDTH = 400;
  // private static final double BOARD_MAX_HEIGHT = 400;

  private Board gameBoard;

  private BorderPane root;
  private MenuBar gameMenuBar;
  private GridPane boardPane;
  private GridPane buttonsPane;
  private VBox logPane;
  private CheckMenuItem logMenuItem;
  private ArrayList<Tile> tiles;
  private TextArea log;

  private Piece clickedPiece;
  private Piece selectedPiece;

  private Stack<Transition> gameHistory;

  private MenuBar createMenuBar() {
    // File Menu
    Menu fileMenu = new Menu("File");

    MenuItem fileMenuItem1 = new MenuItem("Exit");
    fileMenuItem1.setOnAction((ActionEvent t) -> {
      System.exit(0);
    });

    fileMenu.getItems().addAll(fileMenuItem1);

    // Actions Menu
    Menu actionsMenu = new Menu("Actions");

    logMenuItem = new CheckMenuItem("Log current board description");
    logMenuItem.setOnAction((ActionEvent t) -> {
      if (logMenuItem.isSelected()) {
        log.appendText("Logging enabled!\n");
      } else {
        log.appendText("Logging disabled!\n");
      }
    });

    actionsMenu.getItems().addAll(logMenuItem);

    // Other Menus...

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu, actionsMenu);

    return menuBar;
  }

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

  private GridPane createButtonsPane() {
    GridPane pane = new GridPane();

    pane.setAlignment(Pos.CENTER);

    pane.setHgap(10);
    pane.setVgap(10);

    Button undoButton = new Button("Undo last move");

    undoButton.setOnAction(actionEvent ->  {
      if (!gameHistory.empty()) {
        selectedPiece = null;
        clickedPiece = null;
        log.appendText("Undo move #" + gameHistory.size() + "\n");
        Transition t = gameHistory.pop();
        gameBoard = t.getStartBoard();
        drawBoard();
      }
    });

    pane.add(undoButton, 0, 0, 1, 1);

    return pane;
  }

  private VBox createLogPane() {
    this.log = new TextArea();

    log.setEditable(false);

    log.textProperty().addListener((observable, oldValue, newValue) -> {
      log.setScrollTop(Double.MAX_VALUE);
    });

    return new VBox(log);
  }

  private void drawBoard() {
    boardPane.getChildren().clear();
    int tileCounter = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Tile t = tiles.get(tileCounter);
        t.setGameBoard(gameBoard);   // Very important!
        t.getChildren().clear();
        t.addColor();
        t.addPieceIcon();
        t.drawBorder(this.selectedPiece);
        // TODO: This gets called too many times, fix it
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

      it.unisa.rookie.piece.Color currentPlayerColor = gameBoard.getCurrentPlayer().getPlayerColor();

      //TODO: I need to come up with better variable names...
      if (clickedPiece == null && gameBoard.getPiece(selectedTile.getTileId()) != null) {
        if (currentPlayerColor == gameBoard.getPiece(selectedTile.getTileId()).getColor()) {
          clickedPiece = gameBoard.getPiece(selectedTile.getTileId());
          selectedPiece = clickedPiece;
        }
      } else if (clickedPiece != null) {   // Move a piece
        Move move = new Move(
                gameBoard,
                clickedPiece.getPosition(),
                Position.values()[selectedTile.getTileId()],
                selectedPiece
        );

        // TODO: Optimize this piece of code
        //ArrayList<Move> legalMoves = (ArrayList<Move>) clickedPiece.getLegalMoves(gameBoard);
        ArrayList<Move> legalMoves = gameBoard.getAllPossibleLegalMoves();

        boolean found = false;
        for (Move m : legalMoves) {
          if (move.equals(m)) {
            move = m;    // Do this to get the appropriate makeMove() (it's kinda bad)
            found = true;
          }
        }

        if (found) {
          Board newGameBoard = move.makeMove();
          if (newGameBoard.getOpponentPlayer().isKingInCheck()) {
            log.appendText("WARNING! "
                    + newGameBoard.getOpponentPlayer().getPlayerColor()
                    + " watch out for your king!\n"
            );
          } else {
            Transition t = new Transition(gameBoard, newGameBoard, move);
            gameHistory.push(t);
            gameBoard = newGameBoard;

            if (logMenuItem.isSelected()) {
              log.appendText("////////////////////////////////////////////////////"
                      + "\nNUMBER OF MOVES: " + gameHistory.size()
                      + "\nTURN: " + gameBoard.getCurrentPlayer().getPlayerColor()
                      + "\nIS CURRENT PLAYER KING IN CHECK: " + gameBoard.getCurrentPlayer().isKingInCheck()
                      + "\nIS OPPONENT PLAYER KING IN CHECK: " + gameBoard.getOpponentPlayer().isKingInCheck()
                      + "\nIS CURRENT PLAYER KING IN CHECKMATE: " + !(gameBoard.isCheckMateAvoidable(gameBoard.getCurrentPlayer().getPlayerColor()))
                      + "\nIS OPPONENT PLAYER KING IN CHECKMATE: " + !(gameBoard.isCheckMateAvoidable(gameBoard.getOpponentPlayer().getPlayerColor()))
                      + "\nWHITE PIECES: " + gameBoard.getWhitePieces()
                      + "\nBLACK PIECES: " + gameBoard.getBlackPieces() + "\n\n");
            }
          }
        }
        clickedPiece = null;
        selectedPiece = null;
      }
      drawBoard();
      checkWinner();
    }
  };

  public void checkWinner() {
    if (!(gameBoard.isCheckMateAvoidable(gameBoard.getCurrentPlayer().getPlayerColor()))) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("End of the match!");
      alert.setHeaderText(gameBoard.getOpponentPlayer().getPlayerColor() + " wins!");
      alert.setContentText("You can start another match.");

      ButtonType newMatchButton = new ButtonType("Start a new match");
      ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
      alert.getButtonTypes().setAll(newMatchButton, cancelButton);

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == newMatchButton) {
        startNewMatch();
      } else {
        // Do nothing
      }
    }
  }

  public void startNewMatch() {
    this.gameHistory = new Stack<>();
    this.gameBoard = new Board(it.unisa.rookie.piece.Color.WHITE);
    drawBoard();
  }

  @Override
  public void start(Stage primaryStage) {
    this.gameHistory = new Stack<>();
    gameBoard = new Board(it.unisa.rookie.piece.Color.WHITE);

    gameMenuBar = createMenuBar();
    boardPane = createBoardPane();
    buttonsPane = createButtonsPane();
    logPane = createLogPane();

    BorderPane console = new BorderPane();
    console.setBackground(
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
    );
    console.setTop(buttonsPane);
    console.setBottom(logPane);

    root = new BorderPane(boardPane);
    root.setTop(gameMenuBar);
    root.setBottom(console);
    root.setBackground(
      new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
    );

    boardPane.setAlignment(Pos.CENTER);

    // Load application icon
    FileInputStream input = null;
    try {
      input = new FileInputStream("pics/BlackRook.png");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Image icon = new Image(input);

    primaryStage.getIcons().add(icon);

    primaryStage.setTitle("Rookie");

    logMenuItem.setSelected(true);

    Scene game = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    primaryStage.setScene(game);
    primaryStage.show();
  }

  public static void main(String[] args) {
    Application.launch();
  }

}