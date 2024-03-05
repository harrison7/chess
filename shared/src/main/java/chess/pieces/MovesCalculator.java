package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class MovesCalculator {

    protected ArrayList<ChessMove> potentialMoves = new ArrayList<ChessMove>();
    protected boolean[] paths;
    protected int[] rowDistance;
    protected int[] colDistance;
    protected int distanceMulti = 1;

    protected void initializeEightPaths() {
        paths = new boolean[]{true, true, true, true, true, true, true, true};;
        rowDistance = new int[]{1, 1, 1, 0, -1, -1, -1, 0};
        colDistance = new int[]{-1, 0, 1, 1, 1, 0, -1, -1};
    }
    protected void checkFourDirections(ChessPosition myPosition, ChessBoard board,
                                       ChessGame.TeamColor opponent) {
        while (paths[0] || paths[1] || paths[2] || paths[3]) {
            checkMove(4, myPosition, board, opponent, potentialMoves);
            distanceMulti++;
        }
    }

    protected boolean checkPath(boolean shouldRun, ChessBoard board,
                                ChessPosition moveIdea, ChessGame.TeamColor opponent,
                                ArrayList<ChessMove> potentialMoves,
                                ChessPosition myPosition) {
        if (shouldRun) {
            if (board.getPiece(moveIdea) != null &&
                    board.getPiece(moveIdea).getTeamColor() == opponent) {
                potentialMoves.add(new ChessMove(myPosition, moveIdea, null));
                return false;
            }
            if (board.getPiece(moveIdea) == null) {
                potentialMoves.add(new ChessMove(myPosition, moveIdea, null));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected void checkMove(int numPaths, ChessPosition myPosition,
                             ChessBoard board, ChessGame.TeamColor opponent,
                             ArrayList<ChessMove> potentialMoves) {
        for (int i = 0; i < numPaths; i++) {
            int moveRow = myPosition.getRow() +
                    (rowDistance[i] * distanceMulti);
            int moveCol = myPosition.getColumn() +
                    (colDistance[i] * distanceMulti);
            ChessPosition moveIdea = new ChessPosition(moveRow, moveCol);
            checkPaths(paths, moveIdea);
            paths[i] = checkPath(paths[i], board, moveIdea, opponent, potentialMoves,
                    myPosition);
        }
    }

    protected void checkPaths(boolean[] paths, ChessPosition myPosition) {
        if (myPosition.getRow() > 8) {
            paths[0] = false;
            paths[1] = false;
            paths[2] = false;
        } if (myPosition.getRow() < 1) {
            paths[4] = false;
            paths[5] = false;
            paths[6] = false;
        } if (myPosition.getColumn() < 1) {
            paths[0] = false;
            paths[7] = false;
            paths[6] = false;
        } if (myPosition.getColumn() > 8) {
            paths[2] = false;
            paths[3] = false;
            paths[4] = false;
        }
    }
}
