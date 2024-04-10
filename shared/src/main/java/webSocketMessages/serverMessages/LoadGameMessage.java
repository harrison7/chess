package webSocketMessages.serverMessages;

public class LoadGameMessage extends ServerMessage {
    public LoadGameMessage(ServerMessageType type) {
        super(ServerMessageType.LOAD_GAME);
    }
}
