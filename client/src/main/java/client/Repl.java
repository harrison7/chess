package client;

import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import server.Server;

import java.io.IOException;
import java.net.URISyntaxException;

import static client.ui.State.PRELOGIN;

public class Repl {
    private PreloginUI preloginUI;
    private PostloginUI postloginUI;
    private GameplayUI gameplayUI;

    public Repl(int port, String serverUrl) throws URISyntaxException, IOException {
        preloginUI = new PreloginUI(port);
        postloginUI = new PostloginUI(port);
        gameplayUI = new GameplayUI(port);
    }

    public void run() {
        var server = new Server();
        port = server.run(port);

        boolean quit = false;
        State state = PRELOGIN;

        while (!quit) {
            switch (state) {
                case QUIT:
                    quit = true;
                    break;
                case PRELOGIN:
                    state = preloginUI.run();
                    break;
                case POSTLOGIN:
                    state = postloginUI.run();
                    break;
                case GAMEPLAY:
                    state = gameplayUI.run();
                    break;
            }
        }

        server.stop();
    }
}
