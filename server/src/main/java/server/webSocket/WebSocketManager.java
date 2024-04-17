package server.webSocket;
import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.*;

public class WebSocketManager {
    private static WebSocketManager instance;
    private ConnectionManager connections;
    protected MySQLGameDAO gameDAO;
    protected MySQLUserDAO userDAO;
    protected MySQLAuthDAO authDAO;
    public WebSocketManager() throws DataAccessException {
        connections = ConnectionManager.getInstance();
        gameDAO = MySQLGameDAO.getInstance();
        userDAO = MySQLUserDAO.getInstance();
        authDAO = MySQLAuthDAO.getInstance();
    }

    public static synchronized WebSocketManager getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    public void evaluateMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);

        switch (action.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
        }
    }

    public void joinPlayer(Session session, String message) throws IOException, DataAccessException {
        JoinPlayerCommand action = new Gson().fromJson(message, JoinPlayerCommand.class);
        var user = action.getAuthString();
        connections.add(action.getGameID(), user, session);

        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));

        try {
            var joiningUser = authDAO.getAuth(new AuthData(user, ""));
            if (game == null) {
                var res = new ErrorMessage("Error: bad game ID");
                connections.reply(action.getGameID(), user, res);
                connections.remove(action.getGameID(), user);
            } else if ((!Objects.equals(game.whiteUsername(), joiningUser.username()) && action.getPlayerColor() == WHITE) ||
                    (!Objects.equals(game.blackUsername(), joiningUser.username()) && action.getPlayerColor() == BLACK)) {
                var res = new ErrorMessage("Error: wrong team or empty team");
                connections.reply(action.getGameID(), user, res);
                connections.remove(action.getGameID(), user);
            } else {
                var res = new LoadGameMessage(game.game());
                connections.reply(action.getGameID(), user, res);

                var reply = String.format("%s joined the game as %s", action.getUsername(), action.getPlayerColor());
                var notification = new NotificationMessage(reply);
                connections.broadcast(action.getGameID(), user, notification);
            }
        } catch (DataAccessException e) {
            var res = new ErrorMessage("Error: bad authToken");
            connections.reply(action.getGameID(), user, res);
            connections.remove(action.getGameID(), user);
        }
    }

    public void joinObserver(Session session, String message) throws IOException, DataAccessException {
        JoinObserverCommand action = new Gson().fromJson(message, JoinObserverCommand.class);
        var user = action.getAuthString();
        connections.add(action.getGameID(), user, session);

        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));

        try {
            authDAO.getAuth(new AuthData(user, ""));
            if (game == null) {
                var res = new ErrorMessage("Error: bad game ID");
                connections.reply(action.getGameID(), user, res);
                connections.remove(action.getGameID(), user);
            } else {
                var res = new LoadGameMessage(game.game());
                connections.reply(action.getGameID(), user, res);

                var reply = String.format("%s joined as an observer", action.getUsername());
                var notification = new NotificationMessage(reply);
                connections.broadcast(action.getGameID(), user, notification);
            }
        } catch (DataAccessException e) {
            var res = new ErrorMessage("Error: bad authToken");
            connections.reply(action.getGameID(), user, res);
            connections.remove(action.getGameID(), user);
        }
    }

    public void makeMove(Session session, String message) throws IOException, DataAccessException {
        MakeMoveCommand action = new Gson().fromJson(message, MakeMoveCommand.class);
        var user = action.getAuthString();
        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));
        try {
            var fullUser = authDAO.getAuth(new AuthData(user, ""));
            String clientColor;
            if (Objects.equals(fullUser.username(), game.whiteUsername())) {
                clientColor = "WHITE";
            } else {
                clientColor = "BLACK";
            }
            if (game.game().isInCheckmate(WHITE) || game.game().isInCheckmate(BLACK) ||
                    game.game().isInStalemate(WHITE) || game.game().isInStalemate(BLACK)) {
                var res = new ErrorMessage("Error: game over");
                connections.reply(action.getGameID(), user, res);
            } else if ((game.game().getTeamTurn() == WHITE && clientColor.equals("BLACK")) ||
                    (game.game().getTeamTurn() == BLACK && clientColor.equals("WHITE"))) {
                var res = new ErrorMessage("Error: wrong turn");
                connections.reply(action.getGameID(), user, res);
            } else if (game.game().isResigned()) {
                var res = new ErrorMessage("Error: resigned");
                connections.reply(action.getGameID(), user, res);
            } else {
                game.game().makeMove(action.getChessMove());
                game.game().setTeamTurn((clientColor.equals("WHITE")) ? BLACK : WHITE);
                gameDAO.updateGame(game, fullUser, clientColor);
                var res = new LoadGameMessage(game.game());
                connections.broadcast(action.getGameID(), "", res);

                var pieceType = game.game().getBoard().getPiece(action.getChessMove().getStartPosition());

                var reply = String.format("%s moved %s %s", action.getUsername(), pieceType, action.getChessMove());
                var notification = new NotificationMessage(reply);
                connections.broadcast(action.getGameID(), user, notification);
            }
        } catch (InvalidMoveException e) {
            var res = new ErrorMessage("Error: invalid move");
            connections.reply(action.getGameID(), user, res);
        }
    }

    public void leave(Session session, String message) throws IOException {
        LeaveCommand action = new Gson().fromJson(message, LeaveCommand.class);
        var user = action.getAuthString();
        connections.remove(action.getGameID(), user);
        var res = new LoadGameMessage(new ChessGame());

        var reply = String.format("%s left the game", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast(action.getGameID(), user, notification);
    }

    public void resign(Session session, String message) throws IOException, DataAccessException {
        ResignCommand action = new Gson().fromJson(message, ResignCommand.class);
        var user = action.getAuthString();
        var res = new LoadGameMessage(new ChessGame());
        var game = gameDAO.getGame(new GameData(action.getGameID(), "", "", "", null));

        var fullUser = authDAO.getAuth(new AuthData(user, ""));
        String clientColor;
        if (Objects.equals(fullUser.username(), game.whiteUsername())) {
            clientColor = "WHITE";
        } else if (Objects.equals(fullUser.username(), game.blackUsername())){
            clientColor = "BLACK";
        } else {
            var errorRes = new ErrorMessage("Error: observer cannot resign");
            connections.reply(action.getGameID(), user, errorRes);
            return;
        }
        if (game.game().isResigned()) {
            var errorRes = new ErrorMessage("Error: game already resigned");
            connections.reply(action.getGameID(), user, errorRes);
            return;
        }
        game.game().setResigned(true);
        gameDAO.updateGame(game, fullUser, clientColor);

        var reply = String.format("%s resigned", action.getUsername());
        var notification = new NotificationMessage(reply);
        connections.broadcast(action.getGameID(), "", notification);
    }
}
