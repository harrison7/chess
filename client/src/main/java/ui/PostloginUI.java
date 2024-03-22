package ui;

import serverFacade.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import static ui.State.*;

public class PostloginUI {
    private State state;
    private ServerFacade facade;

    public PostloginUI(int port) throws URISyntaxException, IOException {
        state = POSTLOGIN;
        facade = new ServerFacade(port);
    }

    public State run() throws IOException, URISyntaxException {
        System.out.printf("\uD83D\uDC51Welcome to 240 chess. Type help to get started.\uD83D\uDC51%n[LOGGED_OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var params = line.split(" ");
        switch (params[0]) {
            case "help":
                //help();
                break;
            case "quit":
                //state = quit();
                break;
            case "login":
                //state = login(params[1], params[2]);
                break;
            case "register":
                //state = register(params[1], params[2], params[3]);
                break;
            default:
                System.out.println("Unknown command");
        }
        return state;
    }
}
