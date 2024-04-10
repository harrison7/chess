package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private int gameID;
    private String username;
    public ResignCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
    }

    public String getUsername() {
        return username;
    }
}
