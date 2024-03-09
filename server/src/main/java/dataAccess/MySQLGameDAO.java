package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLGameDAO extends HelperGameDAO {
    private static MySQLGameDAO instance;
    public MySQLGameDAO() throws DataAccessException {
        configureDatabase();
    }
    public static synchronized MySQLGameDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new MySQLGameDAO();
        }
        return instance;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }
    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO game (whiteUsername, blackUsername, name, " +
                " json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(game.game());
        var id = executeUpdate(statement, game.whiteUsername(), game.blackUsername(),
                game.gameName(), json);
        return new GameData(id, game.whiteUsername(), game.blackUsername(),
                game.gameName(), game.game());
    }
    @Override
    public GameData getGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, game.gameID());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }
    @Override
    public GameData updateGame(GameData game, AuthData auth, String clientColor)
            throws DataAccessException {
        var trueGame = getGame(game);
        if (clientColor == null) {
            return trueGame;
        }
        GameData updatedGame = updateGameHelper(trueGame, auth, clientColor);
        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, " +
                "name=?, json=? WHERE id=?";
        var json = new Gson().toJson(updatedGame.game());
        var id = executeUpdate(statement, updatedGame.whiteUsername(),
                updatedGame.blackUsername(), updatedGame.gameName(), json, trueGame.gameID());
        return new GameData(id, updatedGame.whiteUsername(),
                updatedGame.blackUsername(), updatedGame.gameName(),
                updatedGame.game());
    };
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var gameData = readGame(rs);
                        result.add(gameData);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("name");
        var json = rs.getString("json");
        var game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(id, whiteUsername, blackUsername, gameName, game);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `name` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
