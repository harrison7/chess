package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends MovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor opponent) {
        potentialMoves = new ArrayList<>();
        // for each possible move on board starting from myPosition, add to PotentialMoves
        initializeEightPaths();
        distanceMulti = 1;

        checkPaths(paths, myPosition);
        checkMove(8, myPosition, board, opponent, potentialMoves);
        return potentialMoves;
    }
}
