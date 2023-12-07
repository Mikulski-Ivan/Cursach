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
import org.example.Database.Classes.ClassesForDatabase.Tables.WorkerTable;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class WorkersTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<WorkerTable, Integer> idWorkersColumn;

    @FXML
    private TableColumn<WorkerTable, String> workersFIOColumn;

    @FXML
    private TableView<WorkerTable> workersTable;

    @FXML
    private TableColumn<WorkerTable, String> workersAddressColumn;

    @FXML
    private TableColumn<WorkerTable, WorkerTable> deleteColumn;

    @FXML
    private TableColumn<WorkerTable, String> workerPositionColumn;

    @FXML
    private TableColumn<WorkerTable, Double> salaryColumn;

    @FXML
    private TableColumn<WorkerTable, Double> ratingColumn;

    @FXML
    private TableColumn<WorkerTable, String> workersPhoneColumn;

    @FXML
    private TableColumn<WorkerTable, Integer> experienceColumn;

    @FXML
    private Button resetButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField fioField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField salaryField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField experienceField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet workers = databaseHandler.selectWorkers();
    private final ObservableList<WorkerTable> data = FXCollections.observableArrayList();
    private final List<String> fioList =new ArrayList<>();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private WorkerTable rowDataWorkerTable=null;

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
            while (workers.next()) {
                data.add(new WorkerTable(workers.getInt(1),
                        workers.getString(2),
                        workers.getString(3),
                        workers.getString(4),
                        workers.getDouble(5),
                        workers.getDouble(6),
                        workers.getString(7),
                        workers.getInt(8)));
                fioList.add(workers.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onAddEvent());

        idWorkersColumn.setCellValueFactory(new PropertyValueFactory<>("idWorkers"));
        workersFIOColumn.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        workersAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        workerPositionColumn.setCellValueFactory(new PropertyValueFactory<>("workerPosition"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        workersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(WorkerTable workerTable, boolean empty) {
                super.updateItem(workerTable, empty);

                if (workerTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(workerTable);
                    databaseHandler.deleteWorker(workerTable);
                    fioList.remove(workerTable.getFIO());
                });
            }
        });

        FilteredList<WorkerTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> fioString=new AtomicReference<>("");
        AtomicReference<String> addressString=new AtomicReference<>("");
        AtomicReference<String> positionString=new AtomicReference<>("");
        AtomicReference<String> salaryString=new AtomicReference<>("");
        AtomicReference<String> ratingString=new AtomicReference<>("");
        AtomicReference<String> phoneString=new AtomicReference<>("");
        AtomicReference<String> experienceString=new AtomicReference<>("");

        fioField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            fioString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(0,fioString.get().equalsIgnoreCase(rowDataWorkerTable.getFIO()) || fioList.contains(fioString.get()));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        addressField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            addressString.set(newValue);

            searchFlags.set(1,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(1,addressString.get().equalsIgnoreCase(String.valueOf(rowDataWorkerTable.getAddress())));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        positionField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            positionString.set(newValue);

            searchFlags.set(2,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(2,positionString.get().equalsIgnoreCase(String.valueOf(rowDataWorkerTable.getWorkerPosition())));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        salaryField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            salaryString.set(newValue);

            searchFlags.set(3,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(3,salaryString.get().equalsIgnoreCase(String.valueOf(rowDataWorkerTable.getSalary())));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        ratingField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            ratingString.set(newValue);

            searchFlags.set(4,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(4,ratingString.get().equalsIgnoreCase(String.valueOf(rowDataWorkerTable.getRating())));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        phoneField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            phoneString.set(newValue);

            searchFlags.set(5,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(5,phoneString.get().equalsIgnoreCase(String.valueOf(rowDataWorkerTable.getPhone())));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        experienceField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            experienceString.set(newValue);

            searchFlags.set(6,newValue.equals(""));

            if (rowDataWorkerTable!=null) {
                changeFlags.set(6,experienceString.get().equalsIgnoreCase(String.valueOf(rowDataWorkerTable.getExperience())));
            }

            filteredList.setPredicate(workerTable-> isConfidence(workerTable, fioString, addressString, positionString, salaryString, ratingString, phoneString, experienceString));
        }));

        SortedList<WorkerTable> sortedList=new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(workersTable.comparatorProperty());
        workersTable.setItems(sortedList);

        workersTable.setRowFactory(param -> {
            TableRow<WorkerTable> row=new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount()==2 && rowData.equals(workersTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataWorkerTable = rowData;
                    addWorkerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(WorkerTable workerTable, AtomicReference<String> fioString, AtomicReference<String> addressString, AtomicReference<String> positionString, AtomicReference<String> salaryString, AtomicReference<String> ratingString, AtomicReference<String> phoneString, AtomicReference<String> experienceString) {
        return workerTable.getFIO().contains(fioString.get()) &&
                workerTable.getAddress().toString().contains(addressString.get()) &&
                workerTable.getWorkerPosition().toString().contains(positionString.get()) &&
                workerTable.getSalary().toString().contains(salaryString.get()) &&
                workerTable.getRating().toString().contains(ratingString.get()) &&
                workerTable.getPhone().toString().contains(phoneString.get()) &&
                String.valueOf(workerTable.getExperience()).contains(experienceString.get());
    }

    private void addFlags() {
        for (int i = 0; i < 7; ++i) {
            searchFlags.add(true);
            changeFlags.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetWorkers(new WorkerTable(fioField.getText(), addressField.getText(), positionField.getText(), Double.parseDouble(salaryField.getText()), Double.parseDouble(ratingField.getText()), phoneField.getText(), Integer.parseInt(experienceField.getText()))));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataWorkerTable=null;
    }

    private void prepareTableForChanges() {
        fioList.remove(rowDataWorkerTable.getFIO());
    }

    private void resetChanges() {
        fioList.add(rowDataWorkerTable.getFIO());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteWorkerInf();
        updateRowDataWorker();
        hideRstButton();
        addWorkersInf();
        databaseHandler.updateWorker(rowDataWorkerTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataWorker() {
        rowDataWorkerTable.setFIO(fioField.getText());
        rowDataWorkerTable.setAddress(addressField.getText());
        rowDataWorkerTable.setWorkerPosition(positionField.getText());
        rowDataWorkerTable.setSalary(Double.valueOf(salaryField.getText()));
        rowDataWorkerTable.setRating(Double.valueOf(ratingField.getText()));
        rowDataWorkerTable.setPhone(phoneField.getText());
        rowDataWorkerTable.setExperience(Integer.parseInt(experienceField.getText()));
    }

    private void clearFields() {
        fioField.setText("");
        addressField.setText("");
        positionField.setText("");
        salaryField.setText("");
        ratingField.setText("");
        phoneField.setText("");
        experienceField.setText("");
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addWorkersInf() {
        data.add(rowDataWorkerTable);
        fioList.add(rowDataWorkerTable.getFIO());
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

    private void deleteWorkerInf() {
        data.remove(rowDataWorkerTable);
    }

    private void addWorkerToFields() {
        fioField.setText(rowDataWorkerTable.getFIO());
        addressField.setText(rowDataWorkerTable.getAddress());
        positionField.setText(rowDataWorkerTable.getWorkerPosition());
        salaryField.setText(String.valueOf(rowDataWorkerTable.getSalary()));
        ratingField.setText(String.valueOf(rowDataWorkerTable.getRating()));
        phoneField.setText(rowDataWorkerTable.getPhone());
        experienceField.setText(String.valueOf(rowDataWorkerTable.getExperience()));
    }

}