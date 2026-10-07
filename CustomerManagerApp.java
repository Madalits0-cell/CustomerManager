import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private TableView<Customer> tableView;
    private TextField nameField;
    private ComboBox<String> provinceComboBox;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Manager");

        // ===== Form =====
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameField.setPromptText("Enter customer name");
        nameField.setPrefWidth(220);

        Label provinceLabel = new Label("Province:");
        provinceComboBox = new ComboBox<>();
        provinceComboBox.getItems().addAll(
                "Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal",
                "Limpopo", "Mpumalanga", "Northern Cape", "North West", "Western Cape"
        );
        provinceComboBox.setPromptText("Select province");
        provinceComboBox.setPrefWidth(220);

        Button addButton = new Button("Add Customer");
        addButton.setOnAction(e -> addCustomer());
        addButton.setDefaultButton(true); // Enter key works

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> deleteCustomer());

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.setPadding(new Insets(15));
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        form.add(provinceLabel, 0, 1);
        form.add(provinceComboBox, 1, 1);

        HBox buttonBox = new HBox(15, addButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        form.add(buttonBox, 1, 2);

        // ===== TableView =====
        tableView = new TableView<>();
        tableView.setItems(customers);
        tableView.setPrefHeight(300);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(250);

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        provinceCol.setPrefWidth(200);

        tableView.getColumns().addAll(nameCol, provinceCol);

        // ===== Layout =====
        BorderPane root = new BorderPane();
        root.setTop(form);
        root.setCenter(tableView);
        BorderPane.setMargin(tableView, new Insets(0, 15, 15, 15));

        Scene scene = new Scene(root, 520, 480);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Keyboard focus
        nameField.requestFocus();
    }

    private void addCustomer() {
        String name = nameField.getText().trim();
        String province = provinceComboBox.getValue();

        // Validation
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name cannot be empty.");
            nameField.requestFocus();
            return;
        }
        if (province == null || province.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a province.");
            provinceComboBox.requestFocus();
            return;
        }

        // Add customer
        customers.add(new Customer(name, province));

        // Clear form
        nameField.clear();
        provinceComboBox.getSelectionModel().clearSelection();
        nameField.requestFocus();
    }

    private void deleteCustomer() {
        Customer selected = tableView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Customer");
        confirm.setContentText("Are you sure you want to delete:\n" + selected.getName() + " (" + selected.getProvince() + ")?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            customers.remove(selected);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}