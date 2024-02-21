package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{
    public void clear() {

    }
    public void createUser(UserData user) throws DataAccessException {

    }
    public UserData getUser(UserData user) throws DataAccessException {
        return user;
    }
}
