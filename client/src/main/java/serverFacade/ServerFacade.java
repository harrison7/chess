package serverFacade;

import chess.*;
import dataAccess.DataAccessException;
import model.*;

import java.util.UUID;

public class ServerFacade {
    private int port;
    private AuthData authToken;
    public ServerFacade(int port) {
        this.port = port;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (user.password() == null) {
            throw new DataAccessException("Null password");
        } else {
            return new AuthData(UUID.randomUUID().toString(), user.username());
        }
    }


}
