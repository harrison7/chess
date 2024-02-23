package dataAccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authList;
    public void clear() {
        authList = new HashMap<>();
    };
    public void createAuth(AuthData auth) throws DataAccessException {
        if (authList.containsKey(auth.username())) {
            throw new DataAccessException("User is already authenticated");
        } else {
            authList.put(auth.username(), auth);
        }
    };
    public AuthData getAuth(AuthData auth) throws DataAccessException {
        if (authList.containsKey(auth.username())) {
            return authList.get(auth.username());
        } else {
            throw new DataAccessException("Authentication does not exist");
        }
    };
    public void deleteAuth(AuthData auth) throws DataAccessException {
        if (authList.containsKey(auth.username())) {
            authList.remove(auth.username());
        } else {
            throw new DataAccessException("Authentication is not in database");
        }
    };
}
