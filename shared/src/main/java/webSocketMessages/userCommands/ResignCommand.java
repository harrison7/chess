package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private int gameID;
    private String username;
    public ResignCommand(String authToken, int gameID, String username) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
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
