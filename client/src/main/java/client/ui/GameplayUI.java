package client.ui;

import com.google.gson.Gson;
import serverFacade.ServerFacade;

import javax.websocket.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static client.ui.State.*;

public class GameplayUI {
    private State state;
    private ServerFacade facade;
    private boolean lightSquare = true;

    Session session;
    NotificationHandler notificationHandler;

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 0;
    private static final String EMPTY = " ";
    private static final String X = "X";
    private static final String O = "O";

    private Character[][] whiteBoard =
            {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                    {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                    {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
    private Character[][] blackBoard =
            {{'R', 'N', 'B', 'K', 'Q', 'B', 'N', 'R'},
                    {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                    {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                    {'r', 'n', 'b', 'k', 'q', 'b', 'n', 'r'}};


    private final String[] whiteHeader = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private final String[] blackHeader = {"h", "g", "f", "e", "d", "c", "b", "a"};
    private final String[] whiteCols = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private final String[] blackCols = {"1", "2", "3", "4", "5", "6", "7", "8"};

    public GameplayUI(int port) throws URISyntaxException, IOException {
        state = GAMEPLAY;
        facade = new ServerFacade(port);
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public State run() throws IOException, URISyntaxException {
        state = GAMEPLAY;

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        drawHeaders(out, whiteHeader);
        drawTicTacToeBoard(out, whiteBoard, whiteCols);
        drawHeaders(out, whiteHeader);

        out.println();

        drawHeaders(out, blackHeader);
        drawTicTacToeBoard(out, blackBoard, blackCols);
        drawHeaders(out, blackHeader);

        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (Objects.equals(line, "Leave")) {
            return POSTLOGIN;
        } else {
            return GAMEPLAY;
        }
    }

    private void drawHeaders(PrintStream out, String[] headers) {

        setBlack(out);
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));

        out.println();
    }

    private void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private void printHeaderText(PrintStream out, String player) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private void drawTicTacToeBoard(PrintStream out, Character[][] board, String[] cols) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawHeader(out, cols[boardRow]);
            drawRowOfSquares(boardRow, out, board);
            drawHeader(out, cols[boardRow]);
            out.println();
            lightSquare = !lightSquare;

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                drawVerticalLine(out);
                setBlack(out);
            }
        }
    }

    private void drawRowOfSquares(int boardRow, PrintStream out, Character[][] board) {

        //for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if (lightSquare) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }


                //if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
                    String color;
                    if (Character.isUpperCase(board[boardRow][boardCol])) {
                        color = EscapeSequences.SET_TEXT_COLOR_RED;
                    } else {
                        color = EscapeSequences.SET_TEXT_COLOR_BLUE;
                    }

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, board[boardRow][boardCol], lightSquare, color);
                    out.print(EMPTY.repeat(suffixLength));
                    lightSquare = !lightSquare;
                //}
//                else {
//                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
//                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw right line
                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }

                setBlack(out);
            }

        //}
    }

    private void drawVerticalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private void setWhite(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private void setRed(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_RED);
        out.print(EscapeSequences.SET_TEXT_COLOR_RED);
    }

    private void setBlack(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }

    private void printPlayer(PrintStream out, Character player, boolean light, String team) {
        out.print(team);
        if (light) {
            out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        } else {
            out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        }


        out.print(player);

        //setWhite(out);
    }
}
