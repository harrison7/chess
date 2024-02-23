package dataAccess;

import chess.ChessGame;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private HashMap<String, UserData> userList;

    public MemoryUserDAO() {
        userList = new HashMap<>();
    }

    public void clear() {
        userList.clear();
    }
    public void createUser(UserData user) throws DataAccessException {
        if (userList.containsKey(user.username())) {
            throw new DataAccessException("User is already registered");
        } else {
            userList.put(user.username(), user);
        }
    }
    public UserData getUser(UserData user) throws DataAccessException {
        if (userList.containsKey(user.username())) {
            return userList.get(user.username());
        } else {
            throw new DataAccessException("User does not exist");
        }
    }
}
