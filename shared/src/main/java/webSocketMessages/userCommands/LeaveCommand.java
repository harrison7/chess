package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private int gameID;
    private String username;
    public LeaveCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }
}
