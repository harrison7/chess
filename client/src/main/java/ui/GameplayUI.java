package ui;

import serverFacade.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;

import static ui.State.*;

public class GameplayUI {
    private State state;
    private ServerFacade facade;

    public GameplayUI(int port) throws URISyntaxException, IOException {
        state = GAMEPLAY;
        facade = new ServerFacade(port);
    }

    public State run() throws IOException, URISyntaxException {
        return GAMEPLAY;
    }
}
