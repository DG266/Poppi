package it.unisa.rookie.gui;

import it.unisa.rookie.ai.AlphaBetaPlayer;
import it.unisa.rookie.ai.AlphaBetaPlayerWithMoveOrdering;
import it.unisa.rookie.ai.AlphaBetaPlayerWithMoveOrderingAndKillerMoves;
import it.unisa.rookie.ai.ArtificialIntelligencePlayer;
import it.unisa.rookie.ai.ArtificialIntelligenceTask;
import it.unisa.rookie.ai.MiniMaxPlayer;
import it.unisa.rookie.ai.RandomAlphaBetaPlayer;
import it.unisa.rookie.ai.RandomPlayer;
import it.unisa.rookie.board.Board;
import it.unisa.rookie.board.Move;
import it.unisa.rookie.board.Player;
import it.unisa.rookie.board.Transition;
import it.unisa.rookie.board.evaluation.Evaluator;
import it.unisa.rookie.board.evaluation.HighCostEvaluator;
import it.unisa.rookie.board.evaluation.LowCostEvaluator;
import it.unisa.rookie.board.evaluation.MediumCostEvaluator;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
  private GridPane buttonsAndFieldsPane;
  private VBox logPane;

  private CheckMenuItem logMenuItem;
  private TextArea log;

  private RadioMenuItem randomPlayerItem;
  private RadioMenuItem minimaxPlayerItem;
  private RadioMenuItem alphaBetaPlayerItem;
  private RadioMenuItem randomAlphaBetaPlayerItem;
  private RadioMenuItem moveOrderingAlphaBetaPlayerItem;
  private RadioMenuItem killerAlphaBetaPlayerItem;
  private RadioMenuItem lowCostEvItem;
  private RadioMenuItem medCostEvItem;
  private RadioMenuItem highCostEvItem;

  private ArrayList<Tile> tiles;

  private boolean isWhiteAi;
  private boolean isBlackAi;
  private CheckBox isWhiteAiCheckBox;
  private CheckBox isBlackAiCheckBox;

  private Piece selectedPiece;

  private Stack<Transition> gameHistory;

  private Transition aiTransition;

  private TextField depthTextField;

  private MenuBar createMenuBar() {
    // File Menu
    final Menu fileMenu = new Menu("File");

    MenuItem fileMenuItem1 = new MenuItem("Exit");
    fileMenuItem1.setOnAction((ActionEvent t) -> {
      System.exit(0);
    });

    fileMenu.getItems().addAll(fileMenuItem1);

    // Actions Menu
    final Menu actionsMenu = new Menu("Actions");

    logMenuItem = new CheckMenuItem("Log board description");
    logMenuItem.setOnAction((ActionEvent t) -> {
      if (logMenuItem.isSelected()) {
        log.appendText("Board logging enabled!\n");
      } else {
        log.appendText("Board logging disabled!\n");
      }
    });

    logMenuItem.setSelected(true);

    actionsMenu.getItems().addAll(logMenuItem);

    // Player Type Menu
    final Menu playerTypeMenu = new Menu("AI Player Type");

    randomPlayerItem = new RadioMenuItem("Random moves player");
    minimaxPlayerItem = new RadioMenuItem("Minimax player");
    alphaBetaPlayerItem = new RadioMenuItem("Alpha Beta Pruning player");
    randomAlphaBetaPlayerItem = new RadioMenuItem("Random Alpha Beta Pruning player");
    moveOrderingAlphaBetaPlayerItem = new RadioMenuItem("Alpha Beta Pruning with move ordering player");
    killerAlphaBetaPlayerItem = new RadioMenuItem("Alpha Beta Pruning with move ordering and killer moves player");

    ToggleGroup playerRadioGroup = new ToggleGroup();

    randomPlayerItem.setToggleGroup(playerRadioGroup);
    minimaxPlayerItem.setToggleGroup(playerRadioGroup);
    alphaBetaPlayerItem.setToggleGroup(playerRadioGroup);
    randomAlphaBetaPlayerItem.setToggleGroup(playerRadioGroup);
    moveOrderingAlphaBetaPlayerItem.setToggleGroup(playerRadioGroup);
    killerAlphaBetaPlayerItem.setToggleGroup(playerRadioGroup);

    // Default value
    killerAlphaBetaPlayerItem.setSelected(true);

    playerTypeMenu.getItems().addAll(
            randomPlayerItem,
            minimaxPlayerItem,
            alphaBetaPlayerItem,
            randomAlphaBetaPlayerItem,
            moveOrderingAlphaBetaPlayerItem,
            killerAlphaBetaPlayerItem
    );

    // Evaluator Type Menu
    final Menu evaluatorTypeMenu = new Menu("Evaluator Type");

    lowCostEvItem = new RadioMenuItem("Low cost evaluator");
    medCostEvItem = new RadioMenuItem("Medium cost evaluator");
    highCostEvItem = new RadioMenuItem("High cost evaluator");

    ToggleGroup evalRadioGroup = new ToggleGroup();

    lowCostEvItem.setToggleGroup(evalRadioGroup);
    medCostEvItem.setToggleGroup(evalRadioGroup);
    highCostEvItem.setToggleGroup(evalRadioGroup);

    // Default value
    highCostEvItem.setSelected(true);

    evaluatorTypeMenu.getItems().addAll(lowCostEvItem, medCostEvItem, highCostEvItem);

    // Other Menus...

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(fileMenu, actionsMenu, playerTypeMenu, evaluatorTypeMenu);

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

  private GridPane createButtonsAndFieldsPane() {
    GridPane pane = new GridPane();

    pane.setAlignment(Pos.CENTER);

    pane.setHgap(10);
    pane.setVgap(10);

    Button newMatchButton = new Button("New Match");
    newMatchButton.setOnAction(actionEvent -> {
      startNewMatch();
    });

    Button undoButton = new Button("Undo last move");
    undoButton.setOnAction(actionEvent ->  {
      if (!gameHistory.empty()) {
        selectedPiece = null;
        aiTransition = null;
        log.appendText("Undo move #" + gameHistory.size() + "\n");
        Transition t = gameHistory.pop();
        gameBoard = t.getStartBoard();
        drawBoard();
      }
    });

    Button clearLogButton = new Button("Clear log");
    clearLogButton.setOnAction(actionEvent -> {
      log.setText("");
    });

    pane.add(newMatchButton, 0, 0, 1, 1);
    pane.add(undoButton, 1, 0, 1, 1);
    pane.add(clearLogButton, 2, 0, 1, 1);

    Label depthLabel = new Label("Depth: ");
    this.depthTextField = new TextField();
    this.depthTextField.setText("6");  // Default value

    pane.add(depthLabel, 4, 0, 1, 1);
    pane.add(this.depthTextField, 5, 0, 1, 1);

    this.isWhiteAiCheckBox = new CheckBox("White AI Controlled");
    this.isBlackAiCheckBox = new CheckBox("Black AI Controlled");

    this.isWhiteAiCheckBox.setOnAction((ActionEvent t) -> {
      if (this.isWhiteAiCheckBox.isSelected()) {
        this.isWhiteAi = true;
        if (gameBoard.getCurrentPlayer().getPlayerColor() == it.unisa.rookie.piece.Color.WHITE) {
          letComputerPlayIfPossible();
        }
      } else {
        this.isWhiteAi = false;
      }
    });

    this.isBlackAiCheckBox.setOnAction((ActionEvent t) -> {
      if (this.isBlackAiCheckBox.isSelected()) {
        this.isBlackAi = true;
        if (gameBoard.getCurrentPlayer().getPlayerColor() == it.unisa.rookie.piece.Color.BLACK) {
          letComputerPlayIfPossible();
        }
      } else {
        this.isBlackAi = false;
      }
    });

    // Default values
    this.isWhiteAiCheckBox.setSelected(false);
    this.isBlackAiCheckBox.setSelected(false);

    pane.add(this.isWhiteAiCheckBox, 0, 1, 3, 2);
    pane.add(this.isBlackAiCheckBox, 3, 1, 3, 2);

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

        if (aiTransition != null) {
          t.drawArtificialIntelligenceMove(aiTransition.getMove());
        }

        boardPane.add(t, j, i);
        tileCounter++;
      }
    }
  }

  EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent e) {

      // IMPORTANT! Don't even try to remove this!
      // This will prevent the creation of multiple AI tasks
      if (isComputerTurn()) {
        return;
      }

      // The match is over, the board is "untouchable" now
      if (gameBoard.matchIsOver()) {
        return;
      }

      Tile selectedTile = null;

      if (e.getTarget() instanceof Tile) {
        // An empty tile has been clicked
        selectedTile = (Tile) e.getTarget();
      } else if (e.getTarget() instanceof Label) {
        // A non-empty tile has been clicked
        selectedTile = (Tile) ((Label) e.getTarget()).getParent();
      } else if (e.getTarget() instanceof Circle) {
        // A non-empty tile (legal move) has been clicked
        selectedTile = (Tile) ((Circle) e.getTarget()).getParent();
      }

      // != null if the selected tile contains a piece
      // == null if the selected tile is empty
      Piece clicked = gameBoard.getPiece(selectedTile.getTileId());

      Player currentPlayer = gameBoard.getCurrentPlayer();

      if (selectedPiece == null
              && clicked != null
              && currentPlayer.getPlayerColor() == clicked.getColor()
      ) {
          selectedPiece = gameBoard.getPiece(selectedTile.getTileId());
      } else if (selectedPiece != null) {
        Move move = new Move(
                gameBoard,
                selectedPiece.getPosition(),
                Position.values()[selectedTile.getTileId()],
                selectedPiece
        );

        // TODO: Optimize this piece of code
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
            logKingInCheckInfo(newGameBoard.getOpponentPlayer());
          } else {
            Transition t = new Transition(gameBoard, newGameBoard, move);
            gameHistory.push(t);
            gameBoard = newGameBoard;

            if (logMenuItem.isSelected()) {
              logBoardInfo();
            }
          }
        }
        selectedPiece = null;
      }
      Platform.runLater(() -> {
        letComputerPlayIfPossible();
        checkEndOfMatch();
        drawBoard();
      });
    }
  };

  public void createArtificialIntelligenceTask() {
    int depth = 0;
    Evaluator ev;
    ArtificialIntelligencePlayer ai;

    // Read user-chosen depth
    try {
      if (this.depthTextField != null) {
        depth = Integer.parseInt(this.depthTextField.getText());
      } else {
        depth = 6;
      }
    } catch (NumberFormatException e) {
      this.log.appendText("WARNING! The depth field MUST contain a number! "
              + "Proceeding with depth = 6.\n"
      );
      depth = 6;  // Default value
    }

    // Read user-chosen board evaluation
    if (highCostEvItem.isSelected()) {
      ev = new HighCostEvaluator();
    } else if (medCostEvItem.isSelected()) {
      ev = new MediumCostEvaluator();
    } else if (lowCostEvItem.isSelected()) {
      ev = new LowCostEvaluator();
    } else {
      ev = new LowCostEvaluator();
    }

    // Read user-chosen AI player type
    if (killerAlphaBetaPlayerItem.isSelected()) {
      ai = new AlphaBetaPlayerWithMoveOrderingAndKillerMoves(depth, ev);
    } else if (moveOrderingAlphaBetaPlayerItem.isSelected()) {
      ai = new AlphaBetaPlayerWithMoveOrdering(depth, ev);
    } else if (randomAlphaBetaPlayerItem.isSelected()) {
      ai = new RandomAlphaBetaPlayer(depth, ev);
    } else if (alphaBetaPlayerItem.isSelected()) {
      ai = new AlphaBetaPlayer(depth, ev);
    } else if (minimaxPlayerItem.isSelected()) {
      ai = new MiniMaxPlayer(depth, ev);
    } else if (randomPlayerItem.isSelected()) {
      ai = new RandomPlayer();
    } else {
      ai = new RandomPlayer();  // Default choice - for now
    }

    ArtificialIntelligenceTask task = new ArtificialIntelligenceTask(gameBoard, ai);
    task.setOnSucceeded(event -> {
      aiTransition = task.getValue();
      gameHistory.push(aiTransition);
      gameBoard = aiTransition.getEndBoard();
      if (logMenuItem.isSelected()) {
        logBoardInfo();
      }

      letComputerPlayIfPossible();
      checkEndOfMatch();
      drawBoard();
    });

    // This is ESSENTIAL
    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  public void letComputerPlayIfPossible() {
    if (!gameBoard.matchIsOver() && isComputerTurn()) {

      if (gameBoard.getCurrentPlayer().getPlayerColor() == it.unisa.rookie.piece.Color.WHITE) {
        this.isWhiteAiCheckBox.setDisable(true);
        this.isBlackAiCheckBox.setDisable(false);
      } else {
        this.isWhiteAiCheckBox.setDisable(false);
        this.isBlackAiCheckBox.setDisable(true);
      }
      createArtificialIntelligenceTask();
    }
  }

  public void checkEndOfMatch() {
    boolean checkMateAvoidable = gameBoard.isCheckMateAvoidable(gameBoard.getCurrentPlayer());
    boolean staleMate = gameBoard.isInStaleMate(gameBoard.getCurrentPlayer());

    // Stop the game (checkmate)
    if (!checkMateAvoidable) {
      String headerText = gameBoard.getOpponentPlayer().getPlayerColor() + " wins!";
      declareEndOfMatch("End of the match!", headerText, "You can start another match.");
    }

    // Stop the game (stalemate)
    if (staleMate) {
      declareEndOfMatch("End of the match!", "Stalemate!", "You can start another match.");
    }
  }

  public void declareEndOfMatch(String title, String headerText, String contentText) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);

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

  public void startNewMatch() {
    this.selectedPiece = null;
    this.aiTransition = null;
    this.gameHistory = new Stack<>();
    this.gameBoard = new Board(it.unisa.rookie.piece.Color.WHITE);

    this.isWhiteAiCheckBox.setDisable(false);
    this.isBlackAiCheckBox.setDisable(false);

    drawBoard();
  }

  public boolean isComputerTurn() {
    if (gameBoard.getCurrentPlayer().getPlayerColor() == it.unisa.rookie.piece.Color.WHITE) {
      return this.isWhiteAi;
    } else {
      return this.isBlackAi;
    }
  }

  public void logBoardInfo() {
    Player curPl = gameBoard.getCurrentPlayer();
    Player whPl = gameBoard.getWhitePlayer();
    Player blPl = gameBoard.getBlackPlayer();
    boolean whiteInCheck = whPl.isKingInCheck();
    boolean whiteInCheckMate = !gameBoard.isCheckMateAvoidable(whPl);
    boolean blackInCheck = blPl.isKingInCheck();
    boolean blackInCheckMate = !gameBoard.isCheckMateAvoidable(blPl);

    log.appendText("////////////////////////////////////////////////////"
            + "\nMove number: " + gameHistory.size()
            + " - Turn: " + curPl.getPlayerColor()
            + "\nWhiteInCheck: " + whiteInCheck
            + " - WhiteInCheckMate: " + whiteInCheckMate
            + "\nBlackInCheck: " + blackInCheck
            + " - BlackInCheckMate: " + blackInCheckMate
            + "\nWhite pcs: " + gameBoard.getWhitePieces()
            + "\nBlack pcs: " + gameBoard.getBlackPieces() + "\n\n");

    // TODO: Should print somewhere else
    //System.out.println(new HighCostEvaluator().getEvaluationDescription(gameBoard));
  }

  public void logKingInCheckInfo(Player player) {
    log.appendText("WARNING! " + player.getPlayerColor() + " watch out for your king!\n");
  }

  @Override
  public void start(Stage primaryStage) {
    this.gameHistory = new Stack<>();

    it.unisa.rookie.piece.Color startingPlayerColor = it.unisa.rookie.piece.Color.WHITE;

    // Two human players - default
    this.isWhiteAi = false;
    this.isBlackAi = false;

    this.gameBoard = new Board(startingPlayerColor);

    gameMenuBar = createMenuBar();
    boardPane = createBoardPane();
    buttonsAndFieldsPane = createButtonsAndFieldsPane();
    logPane = createLogPane();

    BorderPane console = new BorderPane();
    console.setBackground(
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
    );
    console.setTop(buttonsAndFieldsPane);
    console.setBottom(logPane);

    root = new BorderPane(boardPane);
    root.setTop(gameMenuBar);
    root.setBottom(console);
    root.setBackground(
      new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY))
    );

    boardPane.setAlignment(Pos.CENTER);

    // Load application icon
    InputStream input = this.getClass().getResourceAsStream("/pics/BlackRook.png");

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