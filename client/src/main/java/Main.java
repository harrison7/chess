import client.Repl;
import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import server.Server;

import java.io.IOException;
import java.net.URISyntaxException;

import static client.ui.State.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        int port = 8080;
        String serverUrl = "http://localhost:" + port;
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(port, serverUrl).run();
    }
}