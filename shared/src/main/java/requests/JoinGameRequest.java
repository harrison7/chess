package requests;

public record JoinGameRequest(String authorization, String playerColor, int gameID) {
}
