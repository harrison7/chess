package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    private String username;
    private ChessGame.TeamColor playerColor;
    private int gameID;
    public JoinPlayerCommand(String authToken, int gameID, String username, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.username = username;
        this.playerColor = playerColor;
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
