package serverFacade;

import com.google.gson.Gson;
import model.*;
import org.junit.jupiter.api.function.Executable;
import server.results.*;
import ui.State;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

public class ServerFacade {
    private static int port;
    private static AuthData authToken;
    public ServerFacade(int port) throws URISyntaxException, IOException {
        this.port = port;
        URI uri = new URI("http://localhost:" + port + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        JsonObject body = new JsonObject();
        writeRequestBody(body.toString(), http);
        http.connect();
        RegisterResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, RegisterResult.class);
        }
    }

//    public String register(UserData user) throws DataAccessException {
//        return "";
//    }

    public RegisterResult register(String username, String password, String email) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        body.addProperty("email", email);
        writeRequestBody(body.toString(), http);

        http.connect();
        System.out.printf("= Request =========\n[POST] http://localhost:%s/user\n\n%s\n\n", port, body);
        RegisterResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, RegisterResult.class);
        }
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", http.getResponseCode(), http.getResponseMessage(), res);

        return res;
    }

    public LoginResult login(String username, String password) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);
        writeRequestBody(body.toString(), http);

        http.connect();
        System.out.printf("= Request =========\n[POST] http://localhost:%s/session\n\n%s\n\n", port, body);
        LoginResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, LoginResult.class);
        }
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", http.getResponseCode(), http.getResponseMessage(), res);

        return res;
    }

    public LogoutResult logout(String authToken) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        http.setRequestProperty("Authorization", authToken);

        http.connect();
        System.out.printf("= Request =========\n[DELETE] http://localhost:%s/session\n\n%s\n\n", port, authToken);
        LogoutResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, LogoutResult.class);
        }
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", http.getResponseCode(), http.getResponseMessage(), res);

        return res;
    }

    public ListGamesResult listGames(String authToken) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        http.setRequestProperty("Authorization", authToken);

        http.connect();
        System.out.printf("= Request =========\n[GET] http://localhost:%s/game\n\n%s\n\n", port, authToken);
        ListGamesResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, ListGamesResult.class);
        }
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", http.getResponseCode(), http.getResponseMessage(), res);

        return res;
    }

    public CreateGameResult createGame(String authToken, String gameName) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        http.setRequestProperty("Authorization", authToken);
        JsonObject body = new JsonObject();
        body.addProperty("gameName", gameName);
        writeRequestBody(body.toString(), http);

        http.connect();
        System.out.printf("= Request =========\n[POST] http://localhost:%s/game\n\n%s\n\n", port, authToken);
        CreateGameResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, CreateGameResult.class);
        }
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", http.getResponseCode(), http.getResponseMessage(), res);

        return res;
    }

    public JoinGameResult joinGame(String authToken, String playerColor, int gameID) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:" + port + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        http.setRequestProperty("Authorization", authToken);
        JsonObject body = new JsonObject();
        body.addProperty("playerColor", playerColor);
        body.addProperty("gameID", gameID);
        writeRequestBody(body.toString(), http);

        http.connect();
        System.out.printf("= Request =========\n[PUT] http://localhost:%s/game\n\n%s\n\n", port, authToken);
        JoinGameResult res;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            res = new Gson().fromJson(inputStreamReader, JoinGameResult.class);
        }
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", http.getResponseCode(), http.getResponseMessage(), res);

        return res;
    }

    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static Object receiveResponse(HttpURLConnection http) throws IOException {
        var statusCode = http.getResponseCode();
        var statusMessage = http.getResponseMessage();

        Object responseBody = readResponseBody(http);
        System.out.printf("= Response =========\n[%d] %s\n\n%s\n\n", statusCode, statusMessage, responseBody);
        return responseBody;
    }

    private static Object readResponseBody(HttpURLConnection http) throws IOException {
        Object responseBody = "";
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
        }
        return responseBody;
    }

//    private String convertToBody(ArrayList<String> params) {
//        StringBuilder body = new StringBuilder();
//        body.append("{");
//        for (int i = 0; i < params.size(); i += 2) {
//            body.append("\"").append(params.get(i)).append("\"").append(":");
//
//            body.append("\"" + )
//
//        }
//    }


}
