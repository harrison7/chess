package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear();
    public void createUser(UserData user) throws DataAccessException;
    public UserData getUser(UserData user) throws DataAccessException;

}
