package it.unisa.rookie.board;

import it.unisa.rookie.piece.Bishop;
import it.unisa.rookie.piece.ChessPieceType;
import it.unisa.rookie.piece.Color;
import it.unisa.rookie.piece.King;
import it.unisa.rookie.piece.Knight;
import it.unisa.rookie.piece.Pawn;
import it.unisa.rookie.piece.Piece;
import it.unisa.rookie.piece.Position;
import it.unisa.rookie.piece.Queen;
import it.unisa.rookie.piece.Rook;
import java.util.ArrayList;

public class Board {
  private final Piece[] boardPositions;
  private final Move generatorMove;
  private final Player currentPlayer;
  private final Player opponentPlayer;
  private final ArrayList<Piece> whitePieces;
  private final ArrayList<Piece> blackPieces;

  private final Piece enPassant;

  public Board(Piece[] boardPositions,
               Move generatorMove,
               Color currentPlayerColor,
               Piece enPassant,
               int whiteScore,
               int blackScore) {
    this.boardPositions = boardPositions;
    this.generatorMove = generatorMove;
    this.enPassant = enPassant;
    this.whitePieces = new ArrayList<>();
    this.blackPieces = new ArrayList<>();

    King currentPlayerKing = null;
    King opponentPlayerKing = null;

    for (Piece p : boardPositions) {
      if (p != null) {
        if (p.getColor() == Color.WHITE) {
          whitePieces.add(p);
        } else {
          blackPieces.add(p);
        }

        if (p.getType() == ChessPieceType.KING) {
          if (p.getColor() == currentPlayerColor) {
            currentPlayerKing = (King) p;
          } else {
            opponentPlayerKing = (King) p;
          }
        }
      }
    }

    // Current player / Opponent player creation
    Color opponentPlayerColor = (currentPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

    // Calculate legal moves for both players
    ArrayList<Move> currentPlayerLegalMoves = (currentPlayerColor == Color.WHITE)
            ? getLegalMoves(this.whitePieces)
            : this.getLegalMoves(this.blackPieces);
    ArrayList<Move> opponentPlayerLegalMoves = (currentPlayerColor == Color.WHITE)
            ? this.getLegalMoves(this.blackPieces)
            : getLegalMoves(this.whitePieces);

    ArrayList<Move> currentPlayerKingThreats = getThreats(
            currentPlayerKing.getPosition().getValue(),
            opponentPlayerLegalMoves
    );

    ArrayList<Move> opponentPlayerKingThreats = getThreats(
            opponentPlayerKing.getPosition().getValue(),
            currentPlayerLegalMoves
    );

    this.currentPlayer = new Player(
            this,
            currentPlayerColor,
            currentPlayerLegalMoves,
            currentPlayerKingThreats,
            currentPlayerColor == Color.WHITE ? whiteScore : blackScore
    );
    this.opponentPlayer = new Player(
            this,
            opponentPlayerColor,
            opponentPlayerLegalMoves,
            opponentPlayerKingThreats,
            opponentPlayerColor == Color.WHITE ? whiteScore : blackScore
    );

    // Search for castling moves
    currentPlayerLegalMoves.addAll(currentPlayerKing.getCastlingMoves(this));
    opponentPlayerLegalMoves.addAll(opponentPlayerKing.getCastlingMoves(this));

    currentPlayer.setLegalMoves(currentPlayerLegalMoves);
    opponentPlayer.setLegalMoves(opponentPlayerLegalMoves);
  }

  // Creates a "standard" starting board
  public Board(Color startingPlayerColor) {
    boardPositions = new Piece[64];
    this.generatorMove = null;
    this.enPassant = null;

    this.blackPieces = new ArrayList<>();
    this.whitePieces = new ArrayList<>();
    int blackScore = 0;
    int whiteScore = 0;

    blackPieces.add(new Rook(Color.BLACK, Position.A8));
    blackPieces.add(new Knight(Color.BLACK, Position.B8));
    blackPieces.add(new Bishop(Color.BLACK, Position.C8));
    blackPieces.add(new Queen(Color.BLACK, Position.D8));
    blackPieces.add(new King(Color.BLACK, Position.E8));
    blackPieces.add(new Bishop(Color.BLACK, Position.F8));
    blackPieces.add(new Knight(Color.BLACK, Position.G8));
    blackPieces.add(new Rook(Color.BLACK, Position.H8));
    blackPieces.add(new Pawn(Color.BLACK, Position.A7));
    blackPieces.add(new Pawn(Color.BLACK, Position.B7));
    blackPieces.add(new Pawn(Color.BLACK, Position.C7));
    blackPieces.add(new Pawn(Color.BLACK, Position.D7));
    blackPieces.add(new Pawn(Color.BLACK, Position.E7));
    blackPieces.add(new Pawn(Color.BLACK, Position.F7));
    blackPieces.add(new Pawn(Color.BLACK, Position.G7));
    blackPieces.add(new Pawn(Color.BLACK, Position.H7));

    whitePieces.add(new Pawn(Color.WHITE, Position.A2));
    whitePieces.add(new Pawn(Color.WHITE, Position.B2));
    whitePieces.add(new Pawn(Color.WHITE, Position.C2));
    whitePieces.add(new Pawn(Color.WHITE, Position.D2));
    whitePieces.add(new Pawn(Color.WHITE, Position.E2));
    whitePieces.add(new Pawn(Color.WHITE, Position.F2));
    whitePieces.add(new Pawn(Color.WHITE, Position.G2));
    whitePieces.add(new Pawn(Color.WHITE, Position.H2));
    whitePieces.add(new Rook(Color.WHITE, Position.A1));
    whitePieces.add(new Knight(Color.WHITE, Position.B1));
    whitePieces.add(new Bishop(Color.WHITE, Position.C1));
    whitePieces.add(new Queen(Color.WHITE, Position.D1));
    whitePieces.add(new King(Color.WHITE, Position.E1));
    whitePieces.add(new Bishop(Color.WHITE, Position.F1));
    whitePieces.add(new Knight(Color.WHITE, Position.G1));
    whitePieces.add(new Rook(Color.WHITE, Position.H1));

    for (Piece p : blackPieces) {
      putPiece(p);
      blackScore += p.getType().getValue();
    }

    for (Piece p : whitePieces) {
      putPiece(p);
      whiteScore += p.getType().getValue();
    }

    this.currentPlayer = new Player(
            this,
            startingPlayerColor,
            (startingPlayerColor == Color.WHITE)
                    ? getLegalMoves(this.whitePieces)
                    : getLegalMoves(this.blackPieces),
            new ArrayList<>(),
            (startingPlayerColor == Color.WHITE)
                    ? whiteScore
                    : blackScore
    );
    this.opponentPlayer = new Player(
            this,
            (startingPlayerColor == Color.WHITE) ? Color.BLACK : Color.WHITE,
            (startingPlayerColor == Color.WHITE)
                    ? getLegalMoves(this.blackPieces)
                    : getLegalMoves(this.whitePieces),
            new ArrayList<>(),
            (startingPlayerColor == Color.WHITE)
                    ? whiteScore
                    : blackScore
    );
  }

  public Piece[] getBoardPositions() {
    return boardPositions;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Player getOpponentPlayer() {
    return opponentPlayer;
  }

  public Move getGeneratorMove() {
    return generatorMove;
  }

  public ArrayList<Piece> getWhitePieces() {
    return whitePieces;
  }

  public ArrayList<Piece> getBlackPieces() {
    return blackPieces;
  }

  public Piece getEnPassant() {
    return enPassant;
  }

  public ArrayList<Move> getAllPossibleLegalMoves() {
    ArrayList<Move> moves = new ArrayList<>();
    moves.addAll(currentPlayer.getLegalMoves());
    moves.addAll(opponentPlayer.getLegalMoves());
    return moves;
  }

  public ArrayList<Move> getLegalMovesByPiece(Piece p) {
    ArrayList<Move> moves = new ArrayList<>();
    ArrayList<Move> legals = p.getColor() == Color.WHITE
            ? this.getWhitePlayer().getLegalMoves()
            : this.getBlackPlayer().getLegalMoves();
    for (Move m : legals) {
      if (m.getMovedPiece().equals(p)) {
        moves.add(m);
      }
    }
    return moves;
  }


  public void putPiece(Piece p) {
    this.boardPositions[p.getPosition().getValue()] = p;
  }

  public Piece getPiece(int pos) {
    return this.boardPositions[pos];
  }

  private ArrayList<Move> getLegalMoves(ArrayList<Piece> pieces) {
    ArrayList<Move> legalMoves = new ArrayList<>();
    for (Piece p : pieces) {
      ArrayList<Move> singlePieceLegalMoves = (ArrayList<Move>) p.getLegalMoves(this);
      legalMoves.addAll(singlePieceLegalMoves);
    }
    return legalMoves;
  }

  public ArrayList<Move> getThreats(int tile, ArrayList<Move> candidateThreats) {
    ArrayList<Move> result = new ArrayList<>();
    for (Move threat : candidateThreats) {
      if (threat.getDestination().getValue() == tile) {
        result.add(threat);
      }
    }
    return result;
  }

  public boolean isCheckMateAvoidable(Player player) {
    if (player.isKingInCheck()) {
      // Look for a move that can "free" the king
      for (Move m : player.getLegalMoves()) {
        Board nextBoard = m.makeMove();
        // ...found a move!
        if (!nextBoard.getOpponentPlayer().isKingInCheck()) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
  }

  public boolean isInStaleMate(Player player) {
    // If the king is not in check...
    if (!player.isKingInCheck()) {
      // ...and everything he does will put him in check...
      for (Move m : player.getLegalMoves()) {
        Board nextBoard = m.makeMove();
        if (!nextBoard.getOpponentPlayer().isKingInCheck()) {
          return false;
        }
      }
      // ...then -> stalemate
      return true;
    } else {
      return false;
    }
  }

  public boolean matchIsOver() {
    return !(this.isCheckMateAvoidable(this.getCurrentPlayer()))
            || this.isInStaleMate(this.getCurrentPlayer());
  }

  public Player getWhitePlayer() {
    return this.getCurrentPlayer().getPlayerColor() == Color.WHITE
            ? this.getCurrentPlayer()
            : this.getOpponentPlayer();
  }

  public Player getBlackPlayer() {
    return this.getCurrentPlayer().getPlayerColor() == Color.BLACK
            ? this.getCurrentPlayer()
            : this.getOpponentPlayer();
  }

  @Override
  public String toString() {
    return "Board{"
            + "boardPositions=" + boardPositions
            + ", currentPlayer=" + currentPlayer
            + ", whitePieces=" + whitePieces
            + ", blackPieces=" + blackPieces
            + "}";
  }
}
