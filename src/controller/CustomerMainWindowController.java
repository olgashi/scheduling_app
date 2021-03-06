package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customer;
import model.Schedule;
import utilities.AlertMessage;
import utilities.NewWindow;
import utilities.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerMainWindowController implements Initializable {
    @FXML
    private Text customerMainWindowLabel;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> customerId;
    @FXML
    private TableColumn<Customer, String> customerName;
    @FXML
    private TableColumn<Customer, String> customerAddress;
    @FXML
    private TableColumn<Customer, String> customerCity;
    @FXML
    private TableColumn<Customer, String> customerZipCode;
    @FXML
    private TableColumn<Customer, String> customerCountry;
    @FXML
    private TableColumn<Customer, String> customerPhoneNumber;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Button returnToMainWindowButton;

    @FXML
    private void loadMainWindow() throws IOException {
        NewWindow.display((Stage) customerMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/MainWindow.fxml"));
    }

    public void openAddCustomerWindow() throws IOException {
        NewWindow.display((Stage) customerMainWindowLabel.getScene().getWindow(),
                getClass().getResource("/view/CustomerAddNew.fxml"));
    }

    public void openModifyCustomerWindow(ActionEvent event) throws IOException {
        //TODO refactor this if possible to still use NewWindow.display
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/CustomerModify.fxml"));
                Parent mainViewParent = loader.load();
                Scene modifyCustomerView = new Scene(mainViewParent);
                CustomerModifyController controller = loader.getController();
                controller.initModifyCustomerData(customer);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(modifyCustomerView);
                window.show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else AlertMessage.display("Please select customer to modify.", "warning");
    }

    public void deleteCustomer() throws SQLException {
            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if (customer != null) {
                if (AlertMessage.display("Please confirm that you want to delete customer " + customer.getCustomerName() +".", "confirmation")){
                    DBQuery.createQuery("DELETE FROM appointment WHERE customerId = " + "'" + customer.getCustomerId()  + "'");
                    DBQuery.createQuery("DELETE FROM customer WHERE customerId = " + "'" + customer.getCustomerId()  + "'");
                    Schedule.deleteCustomer(customer);
                    customerTable.setItems(Customer.getCustomerList());
                }
            }
            else AlertMessage.display("Please select customer you want to delete", "warning");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerCity.setCellValueFactory(new PropertyValueFactory<>("customerCity"));
        customerZipCode.setCellValueFactory(new PropertyValueFactory<>("customerZipCode"));
        customerCountry.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        customerTable.setItems(Customer.getCustomerList());
    }
}
