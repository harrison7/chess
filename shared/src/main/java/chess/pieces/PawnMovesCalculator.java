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
                if (moveRow > 7) {
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.ROOK));
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.KNIGHT));
                } else {
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, null));
                }
            }
            if (board.getPiece(captureLeft) != null &&
                    board.getPiece(captureLeft).getTeamColor() == opponent) {
                if (moveRow > 7) {
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.ROOK));
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.KNIGHT));
                } else {
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, null));
                }
            }
            if (board.getPiece(captureRight) != null &&
                    board.getPiece(captureRight).getTeamColor() == opponent) {
                if (moveRow > 7) {
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.ROOK));
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.KNIGHT));
                } else {
                    potentialMoves.add(new ChessMove(myPosition, captureRight, null));
                }
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
            ChessPosition captureLeft = new ChessPosition(moveRow, myPosition.getColumn() + 1);
            ChessPosition captureRight = new ChessPosition(moveRow, myPosition.getColumn() - 1);
            if (board.getPiece(moveIdea) == null) {
                if (moveRow < 2) {
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.ROOK));
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, ChessPiece.PieceType.KNIGHT));
                } else {
                    potentialMoves.add(new ChessMove(myPosition, moveIdea, null));
                }
            }
            if (board.getPiece(captureLeft) != null &&
                    board.getPiece(captureLeft).getTeamColor() == opponent) {
                if (moveRow < 2) {
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.ROOK));
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, ChessPiece.PieceType.KNIGHT));
                } else {
                    potentialMoves.add(new ChessMove(myPosition, captureLeft, null));
                }
            }
            if (board.getPiece(captureRight) != null &&
                    board.getPiece(captureRight).getTeamColor() == opponent) {
                if (moveRow < 2) {
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.QUEEN));
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.ROOK));
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.BISHOP));
                    potentialMoves.add(new ChessMove(myPosition, captureRight, ChessPiece.PieceType.KNIGHT));
                } else {
                    potentialMoves.add(new ChessMove(myPosition, captureRight, null));
                }
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
}
