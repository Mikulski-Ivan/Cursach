package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
        import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
        import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Database.Classes.ClassesForDatabase.Tables.ContractTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
        import org.example.Database.Classes.ClassesForDatabase.Tables.WorkerTable;
        import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.w3c.dom.css.CSSStyleRule;

        import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
        import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
        import java.util.concurrent.atomic.AtomicReference;

public class ContractsTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<ContractTable, Integer> idContractsColumn;

    @FXML
    private TableColumn<ContractTable, String> customerColumn;

    @FXML
    private TableView<ContractTable> contractsTable;

    @FXML
    private TableColumn<ContractTable, Integer> quantityColumn;

    @FXML
    private TableColumn<ContractTable, ContractTable> deleteColumn;

    @FXML
    private TableColumn<ContractTable, Double> totalCostColumn;

    @FXML
    private TableColumn<ContractTable, Integer> idGoodColumn;

    @FXML
    private TableColumn<ContractTable, Integer> idWorkerColumn;

    @FXML
    private TableColumn<ContractTable, Integer> idLawyerColumn;

    @FXML
    private TableColumn<WorkerTable, Date> dateOfSaleColumn;

    @FXML
    private Button resetButton;

    @FXML
    private Button addButton;


    @FXML
    private TextField customerField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costField;
    @FXML
    private TextField goodField;
    @FXML
    private TextField workerField;
    @FXML
    private TextField lawyerField;
    @FXML
    private TextField dateField;


    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet contracts = databaseHandler.selectContracts();
    private final ObservableList<ContractTable> data = FXCollections.observableArrayList();
    private final List<String> customerList =new ArrayList<>();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private ContractTable rowDataContractTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        addFlags();

        changeFlags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!changeFlags.contains(false)));
        searchFlags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(searchFlags.contains(true)));

        backButton.setOnAction(actionEvent -> {
            Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow());
        });

        try {
            while (contracts.next()) {
                data.add(new ContractTable(contracts.getInt(1),
                        contracts.getString(2),
                        contracts.getInt(3),
                        contracts.getDouble(4),
                        contracts.getInt(5),
                        contracts.getInt(6),
                        contracts.getInt(7),
                        contracts.getDate(8)));
                customerList.add(contracts.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onAddEvent());

        idContractsColumn.setCellValueFactory(new PropertyValueFactory<>("idContracts"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        idGoodColumn.setCellValueFactory(new PropertyValueFactory<>("idGood"));
        idWorkerColumn.setCellValueFactory(new PropertyValueFactory<>("idWorker"));
        idLawyerColumn.setCellValueFactory(new PropertyValueFactory<>("idLawyer"));
        dateOfSaleColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfSale"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(ContractTable contractTable, boolean empty) {
                super.updateItem(contractTable, empty);

                if (contractTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(contractTable);
                    databaseHandler.deleteContract(contractTable);
                    customerList.remove(contractTable.getCustomer());
                });
            }
        });

        FilteredList<ContractTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> customerString=new AtomicReference<>("");
        AtomicReference<String> quantityString=new AtomicReference<>("");
        AtomicReference<String> costString=new AtomicReference<>("");
        AtomicReference<String> goodString=new AtomicReference<>("");
        AtomicReference<String> workerString=new AtomicReference<>("");
        AtomicReference<String> lawyerString=new AtomicReference<>("");
        AtomicReference<String> dateString=new AtomicReference<>("");

        customerField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            customerString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(0,customerString.get().equalsIgnoreCase(rowDataContractTable.getCustomer()) || customerList.contains(customerString.get()));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        quantityField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            quantityString.set(newValue);

            searchFlags.set(1,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(1,quantityString.get().equalsIgnoreCase(String.valueOf(rowDataContractTable.getQuantity())));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        costField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            costString.set(newValue);

            searchFlags.set(2,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(2,costString.get().equalsIgnoreCase(String.valueOf(rowDataContractTable.getTotalCost())));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        goodField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            goodString.set(newValue);

            searchFlags.set(3,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(3,goodString.get().equalsIgnoreCase(String.valueOf(rowDataContractTable.getIdGood())));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        workerField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            workerString.set(newValue);

            searchFlags.set(4,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(4,workerString.get().equalsIgnoreCase(String.valueOf(rowDataContractTable.getIdWorker())));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        lawyerField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            lawyerString.set(newValue);

            searchFlags.set(5,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(5,lawyerString.get().equalsIgnoreCase(String.valueOf(rowDataContractTable.getIdLawyer())));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        dateField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            dateString.set(newValue);

            searchFlags.set(6,newValue.equals(""));

            if (rowDataContractTable!=null) {
                changeFlags.set(6,dateString.get().equalsIgnoreCase(String.valueOf(rowDataContractTable.getDateOfSale())));
            }

            filteredList.setPredicate(contractTable-> isConfidence(contractTable, customerString, quantityString, costString, goodString, workerString, lawyerString, dateString));
        }));

        SortedList<ContractTable> sortedList=new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(contractsTable.comparatorProperty());
        contractsTable.setItems(sortedList);

        contractsTable.setRowFactory(param -> {
            TableRow<ContractTable> row=new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount()==2 && rowData.equals(contractsTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataContractTable = rowData;
                    addContractToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(ContractTable contractTable, AtomicReference<String> customerString, AtomicReference<String> quantityString, AtomicReference<String> costString, AtomicReference<String> goodString, AtomicReference<String> workerString, AtomicReference<String> lawyerString, AtomicReference<String> dateString) {
        return contractTable.getCustomer().contains(customerString.get()) &&
                String.valueOf(contractTable.getQuantity()).toString().contains(quantityString.get()) &&
                contractTable.getTotalCost().toString().contains(costString.get()) &&
                String.valueOf(contractTable.getIdGood()).toString().contains(goodString.get()) &&
                String.valueOf(contractTable.getIdWorker()).toString().contains(workerString.get()) &&
                String.valueOf(contractTable.getIdLawyer()).toString().contains(lawyerString.get()) &&
                String.valueOf(contractTable.getDateOfSale()).contains(dateString.get());
    }

    private void addFlags() {
        for (int i = 0; i < 7; ++i) {
            searchFlags.add(true);
            changeFlags.add(false);
        }
    }

    void onAddEvent() {
        clearFields();
        data.add(databaseHandler.insertAndGetContracts(new ContractTable(customerField.getText(), Integer.parseInt(quantityField.getText()), Double.parseDouble(costField.getText()), Integer.parseInt(goodField.getText()), Integer.parseInt(workerField.getText()), Integer.parseInt(lawyerField.getText()), Date.valueOf(dateField.getText()))));
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataContractTable=null;
    }

    private void prepareTableForChanges() {
        customerList.remove(rowDataContractTable.getCustomer());
    }

    private void resetChanges() {
        customerList.add(rowDataContractTable.getCustomer());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteContractInf();
        updateRowDataContract();
        hideRstButton();
        addContractsInf();
        databaseHandler.updateContract(rowDataContractTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataContract() {
        rowDataContractTable.setCustomer(customerField.getText());
        rowDataContractTable.setQuantity(Integer.parseInt(quantityField.getText()));
        rowDataContractTable.setTotalCost(Double.valueOf(costField.getText()));
        rowDataContractTable.setIdGood(Integer.parseInt(goodField.getText()));
        rowDataContractTable.setIdWorker(Integer.parseInt(workerField.getText()));
        rowDataContractTable.setIdLawyer(Integer.parseInt(lawyerField.getText()));
        rowDataContractTable.setDateOfSale(Date.valueOf(String.valueOf(dateField.getText())));

    }

    private void clearFields() {
        customerField.setText("");
        quantityField.setText("");
        costField.setText("");
        goodField.setText("");
        workerField.setText("");
        lawyerField.setText("");
        dateField.setText("");
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addContractsInf() {
        data.add(rowDataContractTable);
        customerList.add(rowDataContractTable.getCustomer());
    }

    private void hideRstButton() {
        resetButton.setDisable(true);
        resetButton.setVisible(false);
    }

    private void showRstButton() {
        resetButton.setVisible(true);
        resetButton.setDisable(false);
    }

    private void convertAddToChg() {
        addButton.setText("Chg");
        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onChangeEvent());
    }

    private void deleteContractInf() {
        data.remove(rowDataContractTable);
    }

    private void addContractToFields() {
        customerField.setText(rowDataContractTable.getCustomer());
        quantityField.setText(String.valueOf(rowDataContractTable.getQuantity()));
        costField.setText(String.valueOf(rowDataContractTable.getTotalCost()));
        goodField.setText(String.valueOf(rowDataContractTable.getIdGood()));
        workerField.setText(String.valueOf(rowDataContractTable.getIdWorker()));
        lawyerField.setText(String.valueOf(rowDataContractTable.getIdLawyer()));
        dateField.setText(String.valueOf(rowDataContractTable.getDateOfSale()));
    }

}