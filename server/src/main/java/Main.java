import chess.*;
import dataAccess.MemoryUserDAO;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var port = 8080;
        var server = new Server();
        port = server.run(port);
        System.out.printf("Server started on port %d%n", port);

//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
    }
}