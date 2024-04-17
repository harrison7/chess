package client.ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import facade.ServerFacade;
import facade.WebSocketFacade;
import model.GameData;
import webSocket.NotificationHandler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static client.ui.State.*;

public class GameplayUI {
    private State state;
    private ServerFacade facade;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private String authToken;
    private ChessGame game;
    private int gameID;
    private ChessGame.TeamColor teamColor;
    private String username;

    private boolean lightSquare = true;
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
                    {' ', ' ', ' ', ' ', 'P', ' ', ' ', ' '},
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

    public GameplayUI(int port, String serverURL, NotificationHandler notificationHandler, String authToken, ServerFacade sf) throws URISyntaxException, IOException {
        state = GAMEPLAY;
        facade = sf;
        this.serverUrl = serverURL;
        this.notificationHandler = notificationHandler;
        this.authToken = authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public State run(boolean spectating) throws Exception {
        state = GAMEPLAY;
        ws = new WebSocketFacade(serverUrl, notificationHandler, authToken, username);
        if (spectating) {
            ws.joinObserver(gameID);
        } else {
            ws.joinPlayer(gameID, teamColor);
        }

        //printBoard();

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        var params = line.split(" ");
        if (params[0].equals("help") && params.length == 1) {
            help();
        } else if (params[0].equals("redraw") && params.length == 1) {
            redraw();
        } else if (params[0].equals("leave") && params.length == 1) {
            state = leave();
        } else if (params[0].equals("move") && (params.length == 5 || params.length == 6)) {
            move(params[1], params[2], params[3], params[4], (params.length == 5) ? null : params[5]);
        } else if (params[0].equals("resign") && params.length == 1) {
            resign();
        } else if (params[0].equals("highlight") && params.length == 1) {
            //highlight();
        } else {
            System.out.println("Unknown command");
        }
        return state;
    }

    public void help() {
        System.out.println("  redraw - the board");
        System.out.println("  leave - the game");
        System.out.println("  move <PIECE_ROW>[1-8] <PIECE_COL>[a-h] <DEST_ROW>[1-8] <DEST_COL>[a-h] - move a piece");
        System.out.println("  resign - a game");
        System.out.println("  highlight - legal moves");
    }

    public void redraw() {
        printBoard();
    }

    public State leave() throws IOException {
        ws.leave(gameID);
        return POSTLOGIN;
    }

    public void move(String pieceRow, String pieceCol, String destRow, String destCol, String promotion) throws IOException {
        int row;
        int col;
        int newRow;
        int newCol;
        //if (teamColor == WHITE) {
            row = Integer.parseInt(pieceRow);
            col = pieceCol.charAt(0) - 'a' + 1;
            newRow = Integer.parseInt(destRow);
            newCol = destCol.charAt(0) - 'a' + 1;
        //} else {
//            row = 9 - Integer.parseInt(pieceRow);
//            col = 9 - (pieceCol.charAt(0) - 'a' + 1);
//            newRow = 9 - Integer.parseInt(destRow);
//            newCol = 9 - (destCol.charAt(0) - 'a' + 1);
        //}
        ChessPiece.PieceType piece;

        if (promotion != null) {
            piece = switch(promotion) {
                case "QUEEN" -> ChessPiece.PieceType.QUEEN;
                case "ROOK" -> ChessPiece.PieceType.ROOK;
                case "BISHOP" -> ChessPiece.PieceType.BISHOP;
                case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
                default -> null;
            };
        } else {
            piece = null;
        }

        ChessPosition startPos = new ChessPosition(row, col);
        ChessPosition endPos = new ChessPosition(newRow, newCol);
        ChessMove move = new ChessMove(startPos, endPos, piece);
        ws.makeMove(move, gameID);
    }

    public void resign() throws IOException {
        ws.resign(gameID);
    }

    public void setGame(ChessGame game) {
        this.game = game;
        whiteBoard = this.game.displayBoard(true);
        blackBoard = this.game.displayBoard(false);

        printBoard();
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
        //this.teamColor = teamColor;
    }

    private void printBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        if (teamColor == WHITE) {
            drawHeaders(out, whiteHeader);
            drawChessBoard(out, whiteBoard, whiteCols);
            drawHeaders(out, whiteHeader);
        } else if (teamColor == BLACK) {
            drawHeaders(out, blackHeader);
            drawChessBoard(out, blackBoard, blackCols);
            drawHeaders(out, blackHeader);
        }
        //out.println();
        out.print(EscapeSequences.RESET_BG_COLOR);
        out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
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

    private void drawChessBoard(PrintStream out, Character[][] board, String[] cols) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawHeader(out, cols[boardRow]);
            drawRowOfSquares(boardRow, out, board);
            drawHeader(out, cols[boardRow]);
            out.println();
            lightSquare = !lightSquare;
        }
    }

    private void drawRowOfSquares(int boardRow, PrintStream out, Character[][] board) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (lightSquare) {
                setWhite(out);
            } else {
                setBlack(out);
            }

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

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw right line
                setRed(out);
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }

            setBlack(out);
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
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setColor(ChessGame.TeamColor color) {
        teamColor = color;
    }
}
