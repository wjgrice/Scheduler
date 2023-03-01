package grice.c195.helper;

import grice.c195.model.Appointment;
import grice.c195.model.Customer;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Class used to update table views with data
 */
public class TableUpdater {
    /**
     * Updates and appointment table view with the given table, columns, and data.
     * <p>
     *  LAMBDA: Using lambda expressions in this code makes the code more concise and easier to read by encapsulating the logic
     *  of setting the start time column's cell value in a single line of code. The lambda expression acts as a "factory"
     *  to create the cell value property and set its value, which reduces the need for boilerplate code and makes the
     *  intent of the code clearer.
     *
     * @param table the table view to be updated
     * @param idColumn the column for appointment IDs
     * @param userColumn the column for user IDs@param customerIdColumn the column for customer IDs
     * @param titleColumn the column for appointment titles
     * @param descriptionColumn the column for appointment descriptions
     * @param locationColumn the column for appointment locations
     * @param contactColumn the column for appointment contacts
     * @param typeColumn the column for appointment types
     * @param startColumn the column for appointment start times
     * @param endColumn the column for appointment end times
     * @param appList the list of appointment data to populate the table view with
     *
     */
    public static void appsTableUpdate(TableView<Appointment> table,
                                       TableColumn<Appointment, Integer> idColumn,
                                       TableColumn<Appointment, Integer> userColumn,
                                       TableColumn<Appointment, Integer> customerIdColumn,
                                       TableColumn<Appointment, String> titleColumn,
                                       TableColumn<Appointment, String> descriptionColumn,
                                       TableColumn<Appointment, String> locationColumn,
                                       TableColumn<Appointment, String> contactColumn,
                                       TableColumn<Appointment, String> typeColumn,
                                       TableColumn<Appointment, String> startColumn,
                                       TableColumn<Appointment, String> endColumn,
                                       ObservableList<Appointment> appList) {

        // Set the appointment data as the items in the table
        table.setItems(appList);
        // Set the cell value factories for each column to the appropriate appointment property
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        // LAMBDA: Set the start time column to display the local start time of the appointment as a STRING with format "MM/dd/yyyy HH:mm"
        startColumn.setCellValueFactory(cellData -> {
            ZonedDateTime zonedDate = cellData.getValue().getStart().atZone(ZoneId.systemDefault());
            String formattedDateTime = zonedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
            return new SimpleObjectProperty<>(formattedDateTime);
        });

        // LAMBDA: Set the end time column to display the local end time of the appointment as a STRING with format "MM/dd/yyyy HH:mm"
        endColumn.setCellValueFactory(cellData -> {
            ZonedDateTime zonedDate = cellData.getValue().getEnd().atZone(ZoneId.systemDefault());
            String formattedDateTime = zonedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
            return new SimpleObjectProperty<>(formattedDateTime);
        });
    }
    /**
     * Updates and customer table view with the given table, columns, and data.
     * @param customerClientTable the table view to be updated
     * @param customerIDColumn the column for customer IDs
     * @param customerNameColumn the column for customer names
     * @param customerAddressColumn the column for customer addresses
     * @param customerPostalColumn the column for customer postal codes
     * @param customerPhoneColumn the column for customer phone numbers
     * @param customerDivColumn the column for customer divisions
     * @param customerCountryColumn the column for customer countries
     * @param customerList the list of customer data to populate the table view with
     *
     */
    public static void customerTableUpdate(TableView<Customer> customerClientTable, TableColumn<Customer, Integer> customerIDColumn,
                                           TableColumn<Customer, String> customerNameColumn, TableColumn<Customer, String> customerAddressColumn,
                                           TableColumn<Customer, String> customerPostalColumn, TableColumn<Customer, String> customerPhoneColumn,
                                           TableColumn<Customer, String> customerDivColumn, TableColumn<Customer, String> customerCountryColumn,
                                            ObservableList<Customer> customerList) {
            // Set the customer data as the items in the table
            customerClientTable.setItems(customerList);
            // Set the cell value factories for each column to the appropriate customer property
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            customerDivColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
    }
}
