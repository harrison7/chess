import chess.*;
import ui.State;

import static ui.State.*;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        boolean quit = false;
        State state = PRELOGIN;

        while (!quit) {
            switch (state) {
                case PRELOGIN:
                    break;
                case POSTLOGIN:
                    break;
                case GAMEPLAY:
                    break;
            }
        }
    }
}