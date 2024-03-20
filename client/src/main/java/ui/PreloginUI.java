package ui;

import dataAccess.DataAccessException;
import model.UserData;
import serverFacade.ServerFacade;

public class PreloginUI {

    private State state;

    public String register(UserData user) throws DataAccessException {
        if (!user.password().isEmpty()) {
            //state = State.POSTLOGIN;
            visitorName = String.join("-", params);
            ws = new ServerFacade(serverUrl, notificationHandler);
            ws.enterPetShop(visitorName);
            return String.format("You signed in as %s.", visitorName);
        }
        throw new DataAccessException("Null password");
    }
}
