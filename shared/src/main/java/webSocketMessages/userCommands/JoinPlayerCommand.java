package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    private String username;
    private ChessGame.TeamColor playerColor;
    private int gameID;
    public JoinPlayerCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
