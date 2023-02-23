package grice.c195.DAO;


import grice.c195.helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for the contacts table.
 */
public class ContactDAO {
    /**
     * Returns a list of all Contact names in the contacts database table.
     */
    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String sql = "SELECT Contact_Name FROM contacts";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                contactNames.add(rs.getString("Contact_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection();
        }
        return contactNames;
    }
}
