package dataAccess.memoryDAOs;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    private static MemoryUserDAO instance;
    private HashMap<String, UserData> userList;

    public MemoryUserDAO() {
        userList = new HashMap<>();
    }

    public static synchronized MemoryUserDAO getInstance() {
        if (instance == null) {
            instance = new MemoryUserDAO();
        }
        return instance;
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
            throw new DataAccessException("unauthorized");
        } else {
            return returnUser;
        }
    }
}
