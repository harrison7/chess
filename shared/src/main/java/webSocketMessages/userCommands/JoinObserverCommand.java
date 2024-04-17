package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    private int gameID;
    private String username;
    public JoinObserverCommand(String authToken, int gameID, String username) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }
}
