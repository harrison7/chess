package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private HashMap<String, AuthData> authList;
    public void clear() {
        authList = new HashMap<>();
    };
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        if (authList.containsKey(auth.username())) {
            throw new DataAccessException("User is already authenticated");
        } else {
            authList.put(auth.username(), auth);
            String authToken = UUID.randomUUID().toString();
            return new AuthData(authToken, auth.username());
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
