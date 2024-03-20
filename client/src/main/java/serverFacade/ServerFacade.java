package serverFacade;

import chess.*;
import dataAccess.DataAccessException;
import model.*;
import ui.State;

import java.util.UUID;

public class ServerFacade {
    private int port;
    private AuthData authToken;
    private State state;
    public ServerFacade(int port) {
        this.port = port;
        state = State.PRELOGIN;
    }

    public String register(UserData user) throws DataAccessException {

    }


}
