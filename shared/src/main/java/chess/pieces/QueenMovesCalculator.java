package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends MovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor opponent) {
        potentialMoves = new ArrayList<>();
        // for each possible move on board starting from myPosition, add to PotentialMoves
        initializeEightPaths();
        distanceMulti = 1;

        checkPaths(paths, myPosition);

        while (paths[0] || paths[1] || paths[2] || paths[3] || paths[4] ||
                paths[5] || paths[6] || paths[7]) {
            checkMove(8, myPosition, board, opponent, potentialMoves);
            distanceMulti++;
        }
        return potentialMoves;
    }
}
