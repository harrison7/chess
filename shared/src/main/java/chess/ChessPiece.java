package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public String toString() {
        StringBuilder returnChar = new StringBuilder(" ");
        char pieceChar = switch(type) {
            case PieceType.PAWN -> 'p';
            case PieceType.KNIGHT -> 'n';
            case PieceType.BISHOP -> 'b';
            case PieceType.ROOK -> 'r';
            case PieceType.QUEEN -> 'q';
            case PieceType.KING -> 'k';
        };
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            pieceChar = Character.toUpperCase(pieceChar);
        }

        returnChar.setCharAt(0, pieceChar);
        return returnChar.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor opponent;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            opponent = ChessGame.TeamColor.BLACK;
        } else {
            opponent = ChessGame.TeamColor.WHITE;
        }
        if (type == PieceType.BISHOP) {
            BishopMovesCalculator moveCalculator = new BishopMovesCalculator();
            return moveCalculator.pieceMoves(board, myPosition, opponent);
        } else if (type == PieceType.KING) {
            KingMovesCalculator moveCalculator = new KingMovesCalculator();
            return moveCalculator.pieceMoves(board, myPosition, opponent);
        } else if (type == PieceType.KNIGHT) {
            KnightMovesCalculator moveCalculator = new KnightMovesCalculator();
            return moveCalculator.pieceMoves(board, myPosition, opponent);
        } else if (type == PieceType.PAWN) {
            PawnMovesCalculator moveCalculator = new PawnMovesCalculator();
            return moveCalculator.pieceMoves(board, myPosition, opponent);
        } else if (type == PieceType.QUEEN) {
            QueenMovesCalculator moveCalculator = new QueenMovesCalculator();
            return moveCalculator.pieceMoves(board, myPosition, opponent);
        } else if (type == PieceType.ROOK) {
            RookMovesCalculator moveCalculator = new RookMovesCalculator();
            return moveCalculator.pieceMoves(board, myPosition, opponent);
        }
        return null;
    }
}
