package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares;
    public ChessBoard() {
        squares = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard copy) {
        squares = new ChessPiece[copy.squares.length][];
        for (int i = 0; i < copy.squares.length; i++) {
            squares[i] = Arrays.copyOf(copy.squares[i], copy.squares[i].length);
        }
    }

    @Override
    public String toString() {
        StringBuilder returnStr = new StringBuilder("""
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                """);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (squares[i][j] != null) {
                    int index = (i * 18) + (j * 2) + 1;
                    returnStr.replace(index, index + 1, squares[i][j].toString());
                }
            }
        }
        System.out.println(returnStr);

        return returnStr.toString();
    }

    public ChessPiece[][] getSquares() {
        return squares;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[8 - position.getRow()][position.getColumn() - 1] = piece;
    }

    public void addPieceHelper(int row, int col, ChessGame.TeamColor color,
                               ChessPiece.PieceType type) {
        addPiece(new ChessPosition(row, col), new ChessPiece(color, type));
    }

    public void removePiece(ChessPosition position) {
        squares[8 - position.getRow()][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return squares[8 - position.getRow()][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece.PieceType[] pieces =
                {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (i > 4) {
                    color = ChessGame.TeamColor.BLACK;
                }
                if (i == 1 || i == 8) {
                    addPieceHelper(i, j, color, pieces[j - 1]);
                } else if (i == 2 || i == 7) {
                    addPieceHelper(i, j, color, ChessPiece.PieceType.PAWN);
                }
            }
        }
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition checkKingPos = new ChessPosition(i, j);
                ChessPiece kingPiece = getPiece(checkKingPos);
                if (kingPiece != null &&
                        kingPiece.getTeamColor() == color &&
                        kingPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return checkKingPos;
                }
            }
        }
        return null;
    }
}
