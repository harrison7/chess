import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import server.Server;
import ui.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static client.ui.State.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        int port = 8080;
        var server = new Server();
        port = server.run(port);
        String serverUrl = "http://localhost:" + port;
        if (args.length == 1) {
            serverUrl = args[0];
        }

        var preloginUI = new PreloginUI(port);
        var postloginUI = new PostloginUI(port);
        var gameplayUI = new GameplayUI(port);

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