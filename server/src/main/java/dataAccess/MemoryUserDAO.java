package dataAccess;

import chess.ChessGame;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

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
        UserData returnUser = userList.getOrDefault(user.username(), null);
        if (returnUser != null &&
                !Objects.equals(returnUser.password(), user.password())) {
            throw new DataAccessException("Wrong password");
        } else {
            return returnUser;
        }
    }
}
