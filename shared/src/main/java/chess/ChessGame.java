package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor team;
    private ChessBoard board = new ChessBoard();
    private boolean resigned;

    public ChessGame() {
        team = TeamColor.WHITE;
        board.resetBoard();
        resigned = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    public void setResigned(boolean resigned) {
        this.resigned = resigned;
    }

    public boolean isResigned() {
        return resigned;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        var potentialMoves = (ArrayList<ChessMove>) piece.pieceMoves(getBoard(), startPosition);
        var removeMoves = new ArrayList<ChessMove>();

        TeamColor color = piece.getTeamColor();
        TeamColor opponentColor = (color == TeamColor.BLACK) ? TeamColor.WHITE : TeamColor.BLACK;

        ChessPosition kingPos = new ChessPosition(1, 1);
        if (piece.getPieceType() != ChessPiece.PieceType.KING) {
            kingPos = getBoard().getKingPosition(color);
        }

        // Check each move to see if it would put king in check
        for (ChessMove move : potentialMoves) {
            boolean shouldRemove = false;

            ChessBoard boardCopy = new ChessBoard(getBoard());
            boardCopy.addPiece(move.getEndPosition(), piece);
            boardCopy.removePiece(move.getStartPosition());

            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                kingPos = move.getEndPosition();
            }

            // Check every piece in this simulation to see if any can capture king
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition checkPos = new ChessPosition(i, j);
                    ChessPiece checkPiece = boardCopy.getPiece(checkPos);
                    if (checkPiece != null &&
                            checkPiece.getTeamColor() == opponentColor) {
                        var checkMoves =
                                (ArrayList<ChessMove>) checkPiece.pieceMoves(boardCopy, checkPos);

                        for (ChessMove checkMove : checkMoves) {
                            if (checkMove.getEndPosition().equals(kingPos)) {
                                shouldRemove = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (shouldRemove) {
                //System.out.println(move);
                removeMoves.add(move);
            }
        }

        potentialMoves.removeAll(removeMoves);
        return potentialMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //Throws exception if move is invalid, if leaves king in danger,
        //or if it's not your turn

        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if (getTeamTurn() != piece.getTeamColor()) {
            throw new InvalidMoveException("Invalid move: not your turn");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move: illegal move");
        } else {
            //System.out.println(board);
            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(move.getStartPosition());
            if (move.getPromotionPiece() != null) {
                piece.setPieceType(move.getPromotionPiece());
            }
            setTeamTurn((team == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean check = false;

        ChessPosition kingPos = getBoard().getKingPosition(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition checkPos = new ChessPosition(i, j);
                ChessPiece checkPiece = getBoard().getPiece(checkPos);
                if (checkPiece != null &&
                        checkPiece.getTeamColor() != teamColor) {
                    var checkMoves =
                            (ArrayList<ChessMove>) checkPiece.pieceMoves(getBoard(), checkPos);

                    for (ChessMove checkMove : checkMoves) {
                        if (checkMove.getEndPosition().equals(kingPos)) {
                            check = true;
                            break;
                        }
                    }
                }
            }
        }
        return check;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = false;

        ChessPosition kingPos = getBoard().getKingPosition(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition checkPos = new ChessPosition(i, j);
                ChessPiece checkPiece = getBoard().getPiece(checkPos);
                if (checkPiece != null &&
                        checkPiece.getTeamColor() == teamColor &&
                        validMoves(checkPos) != null &&
                        !validMoves(checkPos).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public Character[][] displayBoard(boolean isWhite) {
        Character[][] whiteBoard = new Character[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquares()[i][j] != null) {
                    whiteBoard[i][j] = board.getSquares()[i][j].toString().charAt(0);
                } else {
                    whiteBoard[i][j] = ' ';
                }

                //System.out.println(whiteBoard[i][j]);
            }
            if (!isWhite) {
                Collections.reverse(Arrays.asList(whiteBoard[i]));
            }
        }
        if (!isWhite) {
            Collections.reverse(Arrays.asList(whiteBoard));
        }

        return whiteBoard;
    }
}
