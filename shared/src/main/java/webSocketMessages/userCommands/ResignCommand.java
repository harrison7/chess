package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private int gameID;
    private String username;
    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public int getGameID() {
        return gameID;
    }
}
