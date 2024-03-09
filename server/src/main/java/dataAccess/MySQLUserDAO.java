package dataAccess;

import com.google.gson.Gson;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLUserDAO implements UserDAO {
    private static MySQLUserDAO instance;
    public MySQLUserDAO() throws DataAccessException {
        configureDatabase();
    }
    public static synchronized MySQLUserDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new MySQLUserDAO();
        }
        return instance;
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        executeUpdate(statement);
    }
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(user.password());
        var id = executeUpdate(statement, user.username(), hashedPassword, user.email());
        //return new UserData(id, user.password(), user.email());
    }
    public UserData getUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");

                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                        boolean match = encoder.matches(user.password(), storedPassword);

                        if (match) {
                            return readUser(rs);
                        } else {
                            throw new DataAccessException("unauthorized");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        return new UserData(username, password, email);
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
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256),
              PRIMARY KEY (`username`),
              INDEX(username),
              INDEX(password),
              INDEX(email)
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
};