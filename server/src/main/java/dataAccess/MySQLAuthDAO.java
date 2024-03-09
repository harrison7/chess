package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {
    private static MySQLAuthDAO instance;
    public MySQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }
    public static synchronized MySQLAuthDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new MySQLAuthDAO();
        }
        return instance;
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        String authToken = UUID.randomUUID().toString();
        AuthData newToken = new AuthData(authToken, auth.username());
        var id = executeUpdate(statement, newToken.authToken(), newToken.username());
        return new AuthData(newToken.authToken(), newToken.username());
    }
    public AuthData getAuth(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth.authToken());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    } else {
                        throw new DataAccessException("unauthorized");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        //return null;
    }

    public void deleteAuth(AuthData auth) throws DataAccessException {
        var statement = "SELECT authToken FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, auth.authToken());
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new DataAccessException("unauthorized");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error checking auth token existence: %s", e.getMessage()));
        }

        var deleteStatement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(deleteStatement, auth.authToken());
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    private String executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getString(1);
                }

                return "";
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken),
              INDEX(username)
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
