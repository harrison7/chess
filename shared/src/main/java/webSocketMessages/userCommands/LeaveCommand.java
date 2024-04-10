package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private int gameID;
    private String username;
    public LeaveCommand(String authToken) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
    }

    public String getUsername() {
        return username;
    }
}
