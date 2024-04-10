package server.webSocket;

import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import server.webSocket.Connection;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private static ConnectionManager instance;
    private static ConcurrentHashMap<String, Connection> connections;

    ConnectionManager() {

    }

    public static synchronized ConnectionManager getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new ConnectionManager();
            connections = new ConcurrentHashMap<>();
        }
        return instance;
    }

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void reply(String username, ServerMessage message) throws IOException {
        var userConnection = connections.get(username);
        if (userConnection != null) {
            userConnection.send(message.toString());
        }
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
