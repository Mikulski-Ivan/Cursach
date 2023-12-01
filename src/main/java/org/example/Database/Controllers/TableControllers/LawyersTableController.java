package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Database.Classes.ClassesForDatabase.Tables.LawyerTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.WorkerTable;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.w3c.dom.css.CSSStyleRule;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class LawyersTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<LawyerTable, Integer> idLawyersColumn;

    @FXML
    private TableColumn<LawyerTable, String> lawyersFIOColumn;

    @FXML
    private TableView<LawyerTable> lawyersTable;

    @FXML
    private TableColumn<LawyerTable, String> lawyersPhoneColumn;

    @FXML
    private TableColumn<LawyerTable, LawyerTable> deleteColumn;

    @FXML
    private TableColumn<LawyerTable, Integer> idLawFirmColumn;

    @FXML
    private TableColumn<LawyerTable, Integer> lawyersYearsOfCooperationColumn;


    @FXML
    private TextField fioField;
    @FXML
    private TextField phoneField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet lawyers = databaseHandler.selectLawyers();
    private final ObservableList<LawyerTable> data = FXCollections.observableArrayList();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private LawyerTable rowDataLawyerTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            while (lawyers.next()) {
                data.add(new LawyerTable(lawyers.getInt(1),
                        lawyers.getString(2),
                        lawyers.getString(3),
                        lawyers.getInt(4),
                        lawyers.getInt(5)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idLawyersColumn.setCellValueFactory(new PropertyValueFactory("idLawyers"));
        lawyersFIOColumn.setCellValueFactory(new PropertyValueFactory("FIO"));
        lawyersPhoneColumn.setCellValueFactory(new PropertyValueFactory("phone"));
        idLawFirmColumn.setCellValueFactory(new PropertyValueFactory("idLawFirm"));
        lawyersYearsOfCooperationColumn.setCellValueFactory(new PropertyValueFactory("yearsOfCooperation"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(LawyerTable lawyerTable, boolean empty) {
                super.updateItem(lawyerTable, empty);

                if (lawyerTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(lawyerTable);
                    databaseHandler.deleteLawyer(lawyerTable);
                });
            }
        });

        lawyersTable.setItems(data);

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
