package grice.c195.DAO;

import grice.c195.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to validate a user's login credentials.
 */
public class UserDAO {
    /**
     * This method validates a user's login credentials.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The user ID of the user if the credentials are valid, otherwise 0.
     */
    public static int validateUser(String username, String password) throws SQLException {
        JDBC.openConnection();
        int userId = 0;
        String query = "SELECT * FROM USERS WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(query);
        ps.setString(1, username);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()) {
            if (resultSet.getString("Password").equals(password)) {
                userId = resultSet.getInt("User_ID");
                JDBC.closeConnection();
                return userId;
            }
        }
        JDBC.closeConnection();
        return userId;
    }
}
