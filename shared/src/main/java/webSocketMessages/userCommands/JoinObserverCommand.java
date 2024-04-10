package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    private int gameID;
    private String username;
    public JoinObserverCommand(String authToken) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.JOIN_OBSERVER;
    }

    public String getUsername() {
        return username;
    }
}
