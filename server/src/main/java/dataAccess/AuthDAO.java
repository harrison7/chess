package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void clear();
    public AuthData createAuth(AuthData auth) throws DataAccessException;
    public AuthData getAuth(AuthData auth) throws DataAccessException;
    public void deleteAuth(AuthData auth) throws DataAccessException;
}
