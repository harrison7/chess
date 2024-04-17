import client.Repl;
import client.ui.GameplayUI;
import client.ui.PostloginUI;
import client.ui.PreloginUI;
import client.ui.State;
import facade.ServerFacade;
import facade.WebSocketFacade;

import java.io.IOException;
import java.net.URISyntaxException;

import static client.ui.State.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        String serverUrl = "http://localhost:" + port;
        if (args.length == 1) {
            serverUrl = args[0];
        }
        var sf = new ServerFacade(port);

        new Repl(port, serverUrl, sf).run();
    }
}