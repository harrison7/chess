package ui;

import serverFacade.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;

import static ui.State.*;

public class PostloginUI {
    private State state;
    private ServerFacade facade;

    public PostloginUI(int port) throws URISyntaxException, IOException {
        state = POSTLOGIN;
        facade = new ServerFacade(port);
    }

    public State run() throws IOException, URISyntaxException {
        return POSTLOGIN;
    }
}
