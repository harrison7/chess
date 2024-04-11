package client.ui;

import chess.ChessGame;
import facade.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static client.ui.State.*;

public class PostloginUI {
    private State state;
    private ServerFacade facade;
    private int gameID;
    private ChessGame.TeamColor color;

    public PostloginUI(int port) throws URISyntaxException, IOException {
        state = POSTLOGIN;
        facade = new ServerFacade(port);
    }

    public State run() throws IOException, URISyntaxException {
        state = POSTLOGIN;

        System.out.print("[LOGGED_IN] >>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var params = line.split(" ");
//        switch (params[0]) {
//            case "help":
//                help();
//                break;
//            case "logout":
//                state = logout();
//                break;
//            case "create":
//                create(params[1]);
//                break;
//            case "list":
//                list();
//                break;
//            case "join":
//                state = join(params[1], (params.length == 2) ? null : params[2]);
//                break;
//            case "observe":
//                state = observe(params[1]);
//                break;
//            case "quit":
//                state = quit();
//                break;
//            default:
//                System.out.println("Unknown command");
//                state = POSTLOGIN;
//                break;
//
//        }
        if (params[0].equals("help") && params.length == 1) {
            help();
        } else if (params[0].equals("logout") && params.length == 1) {
            state = logout();
        } else if (params[0].equals("create") && params.length == 2) {
            create(params[1]);
        } else if (params[0].equals("join") && (params.length == 2) || (params.length == 3)) {
            state = join(params[1], (params.length == 2) ? null : params[2]);
        }  else if (params[0].equals("observe") && params.length == 2) {
            state = observe(params[1]);
        } else if (params[0].equals("quit") && params.length == 4) {
            state = quit();
        } else {
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
        var id = facade.createGame("", name);
        System.out.printf("New game ID: %s\n", id.gameID());
    }

    public void list() throws IOException, URISyntaxException {
        var list = facade.listGames("");
        for (var game : list.games()) {
            System.out.printf("%s, %s, %s, %s", game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            System.out.println();
        }

    }

    public State join(String id, String color) throws IOException, URISyntaxException {
        String newColor = facade.joinGame("", color, Integer.parseInt(id)).message();
        if (Objects.equals(newColor, "WHITE")) {
            this.color = WHITE;
        } else if (Objects.equals(newColor, "BLACK")) {
            this.color = BLACK;
        }
        gameID = Integer.parseInt(id);
        return GAMEPLAY;
    }

    public State observe(String id) throws IOException, URISyntaxException {
        facade.joinGame("", null, Integer.parseInt(id)).message();
        color = WHITE;
        return GAMEPLAY;
    }
    public State quit() {
        return QUIT;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }
}
