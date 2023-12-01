package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Arrays;
import java.util.ResourceBundle;

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
    private TextField fioField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField positionField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet workers = databaseHandler.selectWorkers();
    private final ObservableList<WorkerTable> data = FXCollections.observableArrayList();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private WorkerTable rowDataWorkerTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                });
            }
        });

        workersTable.setItems(data);

        /*FilteredList<WorkerTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> firmString=new AtomicReference<>("");
        AtomicReference<String> reputationString=new AtomicReference<>("");
        AtomicReference<String> yearsString=new AtomicReference<>("");

        firmField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            firmString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataMakerTable!=null) {
                changeFlags.set(0,firmString.get().equalsIgnoreCase(rowDataMakerTable.getMakerFirm()));
            }

            filteredList.setPredicate(maker-> isConfidence());
        }));*/
    }
}