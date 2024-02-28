package server.results;

public record RegisterResult(boolean success, String message, String username,
                             String authToken) {}
