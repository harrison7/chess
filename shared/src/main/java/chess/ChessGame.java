package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor team;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        team = TeamColor.WHITE;
        board.resetBoard();
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
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition checkKingPos = new ChessPosition(i, j);
                    ChessPiece kingPiece = getBoard().getPiece(checkKingPos);
                    if (kingPiece != null &&
                            kingPiece.getTeamColor() == color &&
                            kingPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPos = checkKingPos;
                    }
                }
            }
        }

        for (ChessMove move : potentialMoves) {
            //check if any move would put king in check, if so then eliminate move:
            //check all valid moves of opposing team on board after move
            //start by checking color of piece at startPosition, get opposite color
            //scan the whole board, run pieceMoves on each opponent piece
            //if any move ends on king position, eliminate original move
            boolean shouldRemove = false;

            ChessBoard boardCopy = new ChessBoard(getBoard());
            boardCopy.addPiece(move.getEndPosition(), piece);
            boardCopy.removePiece(move.getStartPosition());
            //System.out.println(boardCopy.getPiece(move.getEndPosition()));
            //System.out.println(boardCopy.getPiece(move.getStartPosition()));

            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                kingPos = move.getEndPosition();
            }

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

        System.out.println(move.getStartPosition());
        System.out.println(validMoves(move.getStartPosition()));
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());
        if (getTeamTurn() != piece.getTeamColor()) {
            throw new InvalidMoveException("Invalid move: not your turn");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move: illegal move");
        } else {
            //System.out.println(board);
            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(move.getStartPosition());
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

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
}
