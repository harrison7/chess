package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor opponent) {
        ArrayList<ChessMove> potentialMoves = new ArrayList<>();
        if (opponent == ChessGame.TeamColor.BLACK) {
            int moveRow = myPosition.getRow() + 1;
            ChessPosition moveIdea = new ChessPosition(moveRow, myPosition.getColumn());
            ChessPosition captureLeft = new ChessPosition(moveRow, myPosition.getColumn() - 1);
            ChessPosition captureRight = new ChessPosition(moveRow, myPosition.getColumn() + 1);
            if (board.getPiece(moveIdea) == null) {
                addMoves(moveRow, myPosition, moveIdea, potentialMoves, true);
            }
            if (captureLeft.getColumn() > 0 && board.getPiece(captureLeft) != null &&
                    board.getPiece(captureLeft).getTeamColor() == opponent) {
                addMoves(moveRow, myPosition, captureLeft, potentialMoves, true);
            }
            if (captureRight.getColumn() < 8 && board.getPiece(captureRight) != null &&
                    board.getPiece(captureRight).getTeamColor() == opponent) {
                addMoves(moveRow, myPosition, captureRight, potentialMoves, true);
            }
            if (myPosition.getRow() < 3) {
                int moveBig = myPosition.getRow() + 2;
                ChessPosition initialMove = new ChessPosition(moveBig, myPosition.getColumn());
                if (board.getPiece(initialMove) == null && board.getPiece(moveIdea) == null) {
                    potentialMoves.add(new ChessMove(myPosition, initialMove, null));
                }
            }
        } else {
            int moveRow = myPosition.getRow() - 1;
            ChessPosition moveIdea = new ChessPosition(moveRow, myPosition.getColumn());
            ChessPosition captureLeft = new ChessPosition(moveRow, myPosition.getColumn() - 1);
            ChessPosition captureRight = new ChessPosition(moveRow, myPosition.getColumn() + 1);
            if (board.getPiece(moveIdea) == null) {
                addMoves(moveRow, myPosition, moveIdea, potentialMoves, false);
            }
            if (captureLeft.getColumn() > 0 && board.getPiece(captureLeft) != null &&
                    board.getPiece(captureLeft).getTeamColor() == opponent) {
                addMoves(moveRow, myPosition, captureLeft, potentialMoves, false);
            }
            if (captureRight.getColumn() < 8 && board.getPiece(captureRight) != null &&
                    board.getPiece(captureRight).getTeamColor() == opponent) {
                addMoves(moveRow, myPosition, captureRight, potentialMoves, false);
            }
            if (myPosition.getRow() > 6) {
                int moveBig = myPosition.getRow() - 2;
                ChessPosition initialMove = new ChessPosition(moveBig, myPosition.getColumn());
                if (board.getPiece(initialMove) == null && board.getPiece(moveIdea) == null) {
                    potentialMoves.add(new ChessMove(myPosition, initialMove, null));
                }
            }
        }
        return potentialMoves;
    }

    private void addMoves(int moveRow, ChessPosition myPosition, ChessPosition moveIdea, Collection<ChessMove> potentialMoves, boolean up) {
        boolean truth = up ? (moveRow > 7) : (moveRow < 2);
        ChessPiece.PieceType[] piece = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        if (truth) {
            for (int i = 0; i < 4; i++) {
                potentialMoves.add(new ChessMove(myPosition, moveIdea, piece[i]));
            }
        } else {
            potentialMoves.add(new ChessMove(myPosition, moveIdea, null));
        }
    }
}
