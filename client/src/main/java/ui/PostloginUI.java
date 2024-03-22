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
        System.out.print("[LOGGED_IN] >>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var params = line.split(" ");
        switch (params[0]) {
            case "help":
                help();
                break;
            case "logout":
                state = logout();
                break;
            case "create":
                create(params[1]);
                break;
            case "list":
                list();
                break;
            case "join":
                state = join(params[1], params[2]);
                break;
            case "observe":
                state = observe(params[1]);
                break;
            case "quit":
                state = quit();
                break;
            default:
                System.out.println("Unknown command");
        }
        return state;
    }

    public void help() {
        System.out.println("  create <NAME> - a game");
        System.out.println("  list - games");
        System.out.println("  join <ID> [WHITE|BLACK|<empty>] - a game");
        System.out.println("  observe <ID> - a game");
        System.out.println("  logout - when you are done");
        System.out.println("  quit - playing chess");
        System.out.println("  help - with possible commands");
    }

    public State logout() throws IOException, URISyntaxException {
        facade.logout("");
        return PRELOGIN;
    }

    public void create(String name) throws IOException, URISyntaxException {
        facade.createGame("", name);
    }

    public void list() throws IOException, URISyntaxException {
        facade.listGames("");
    }

    public State join(String id, String color) throws IOException, URISyntaxException {
        facade.joinGame("", color, Integer.parseInt(id));
        return GAMEPLAY;
    }

    public State observe(String id) throws IOException, URISyntaxException {
        facade.joinGame("", null, Integer.parseInt(id));
        return GAMEPLAY;
    }
    public State quit() {
        return QUIT;
    }
}
