package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends MovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor opponent) {
        potentialMoves = new ArrayList<ChessMove>();
        // for each possible move on board starting from myPosition, add to PotentialMoves
        paths = new boolean[]{true, true, true, true};
        rowDistance = new int[]{1, 1, -1, -1};
        colDistance = new int[]{-1, 1, 1, -1};
        distanceMulti = 1;

        checkPaths(paths, myPosition);

        checkFourDirections(myPosition, board, opponent);
        return potentialMoves;
    }

    @Override
    protected void checkPaths(boolean[] paths, ChessPosition myPosition) {
        if (myPosition.getRow() > 8) {
            paths[0] = false;
            paths[1] = false;
        } if (myPosition.getRow() < 1) {
            paths[2] = false;
            paths[3] = false;
        } if (myPosition.getColumn() < 1) {
            paths[0] = false;
            paths[3] = false;
        } if (myPosition.getColumn() > 8) {
            paths[1] = false;
            paths[2] = false;
        }
    }
}
