package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class KnightMovesCalculator extends MovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor opponent) {
        ArrayList<ChessMove> potentialMoves = new ArrayList<>();
        // for each possible move on board starting from myPosition, add to PotentialMoves
        boolean[] paths = {true, true, true, true, true, true, true, true};
        int[] rowDistance = {2, 2, 1, -1, -2, -2, -1, 1};
        int[] colDistance = {-1, 1, 2, 2, 1, -1, -2, -2};

        checkPaths(paths, myPosition);

        //while (paths[0] || paths[1] || paths[2] || paths[3] || paths[4] ||
        //        paths[5] || paths[6] || paths[7]) {
        for (int i = 0; i < 8; i++) {
            int moveRow = myPosition.getRow() + rowDistance[i];
            int moveCol = myPosition.getColumn() + colDistance[i];
            ChessPosition moveIdea = new ChessPosition(moveRow, moveCol);
            //checkPaths(paths, moveIdea);
            paths[i] = checkPath(paths[i], board, moveIdea, opponent, potentialMoves,
                    myPosition);
        }
        //distanceMulti++;
        //}
        return potentialMoves;
    }

    @Override
    protected void checkPaths(boolean[] paths, ChessPosition myPosition) {
        if (myPosition.getRow() > 6) {
            paths[0] = false;
            paths[1] = false;
            if (myPosition.getRow() > 7) {
                paths[2] = false;
                paths[7] = false;
            }
        }
        if (myPosition.getRow() < 3) {
            paths[4] = false;
            paths[5] = false;
            if (myPosition.getRow() < 2) {
                paths[3] = false;
                paths[6] = false;
            }
        }
        if (myPosition.getColumn() < 3) {
            paths[6] = false;
            paths[7] = false;
            if (myPosition.getColumn() < 2) {
                paths[0] = false;
                paths[5] = false;
            }
        }
        if (myPosition.getColumn() > 6) {
            paths[2] = false;
            paths[3] = false;
            if (myPosition.getColumn() > 7) {
                paths[1] = false;
                paths[4] = false;
            }
        }
    }
}
