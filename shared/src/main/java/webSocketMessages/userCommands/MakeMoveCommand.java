package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private int gameID;
    private ChessMove move;
    private String username;
    public MakeMoveCommand(String authToken, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
    }

    public String getUsername() {
        return username;
    }
}
