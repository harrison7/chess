import chess.*;
import server.Server;
import ui.PreloginUI;
import ui.State;

import java.io.IOException;
import java.net.URISyntaxException;

import static ui.State.*;

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
                    break;
                case GAMEPLAY:
                    break;
            }
        }

        server.stop();
    }
}