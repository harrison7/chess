package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private int gameID;
    private ChessMove move;
    private String username;
    public MakeMoveCommand(String authToken, ChessMove move, int gameID, String username) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
        this.gameID = gameID;
        this.username = username;
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
