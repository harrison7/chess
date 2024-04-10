package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    private String message;
    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}
