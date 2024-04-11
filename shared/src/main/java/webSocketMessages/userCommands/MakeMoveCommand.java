package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private int gameID;
    private ChessMove move;
    private String username;
    public MakeMoveCommand(String authToken, ChessMove move, int gameID) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getChessMove() {
        return move;
    }
}
