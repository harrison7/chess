package dataAccess.memoryDAOs;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static MemoryAuthDAO instance;
    private HashMap<String, AuthData> authList;


    public MemoryAuthDAO() {
        authList = new HashMap<>();
    }

    public static synchronized MemoryAuthDAO getInstance() {
        if (instance == null) {
            instance = new MemoryAuthDAO();
        }
        return instance;
    }

    public void clear() {
        authList = new HashMap<>();
    };
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newToken = new AuthData(authToken, auth.username());

        authList.put(newToken.authToken(), newToken);
        return newToken;
    };
    public AuthData getAuth(AuthData auth) throws DataAccessException {
        if (authList.containsKey(auth.authToken())) {
            return authList.get(auth.authToken());
        } else {
            throw new DataAccessException("unauthorized");
        } //unauthorized
    };
    public void deleteAuth(AuthData auth) throws DataAccessException {
        if (authList.containsKey(auth.authToken())) {
            authList.remove(auth.authToken());
        } else {
            throw new DataAccessException("unauthorized");
        }
    };
}
