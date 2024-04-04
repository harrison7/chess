package client.ui;

import facade.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import static client.ui.State.*;

public class PreloginUI {

    private State state;
    private ServerFacade facade;

    public PreloginUI(int port) throws URISyntaxException, IOException {
        state = PRELOGIN;
        facade = new ServerFacade(port);
    }

    public State run() throws IOException, URISyntaxException {
        state = PRELOGIN;

        System.out.printf("\uD83D\uDC51Welcome to 240 chess. Type help to get started.\uD83D\uDC51%n[LOGGED_OUT] >>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var params = line.split(" ");
        switch (params[0]) {
            case "help":
                help();
                break;
            case "quit":
                state = quit();
                break;
            case "login":
                state = login(params[1], params[2]);
                break;
            case "register":
                state = register(params[1], params[2], params[3]);
                break;
            default:
                System.out.println("Unknown command");
        }
        return state;
    }

    public void help() {
        System.out.println("  register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
        System.out.println("  login <USERNAME> <PASSWORD> - to play chess");
        System.out.println("  quit - playing chess");
        System.out.println("  help - with possible commands");
    }

    public State quit() {
        return QUIT;
    }

    public State login(String username, String password) throws IOException, URISyntaxException {
        try {
            facade.login(username, password);
            System.out.printf("Logged in as %s\n", username);
            return POSTLOGIN;
        } catch (IOException e) {
            System.out.printf("Log in fail: %s\n", e);
            return PRELOGIN;
        }
    }

    public State register(String username, String password, String email) throws IOException, URISyntaxException {
        try {
            facade.register(username, password, email);
            System.out.printf("Logged in as %s\n", username);
            return POSTLOGIN;
        } catch (IOException e) {
            System.out.printf("Register fail: %s\n", e);
            return PRELOGIN;
        }
    }
}
