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
import org.example.Database.Classes.ClassesForDatabase.Tables.GoodTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.LawyerTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.WorkerTable;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;
import org.w3c.dom.css.CSSStyleRule;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private Button resetButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField fioField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField firmField;
    @FXML
    private TextField yearsField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet lawyers = databaseHandler.selectLawyers();
    private final ObservableList<LawyerTable> data = FXCollections.observableArrayList();
    private final List<String> fioList =new ArrayList<>();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private LawyerTable rowDataLawyerTable=null;

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
            while (lawyers.next()) {
                data.add(new LawyerTable(lawyers.getInt(1),
                        lawyers.getString(2),
                        lawyers.getString(3),
                        lawyers.getInt(4),
                        lawyers.getInt(5)));
                fioList.add(lawyers.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onAddEvent());

        idLawyersColumn.setCellValueFactory(new PropertyValueFactory<>("idLawyers"));
        lawyersFIOColumn.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        lawyersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        idLawFirmColumn.setCellValueFactory(new PropertyValueFactory<>("idLawFirm"));
        lawyersYearsOfCooperationColumn.setCellValueFactory(new PropertyValueFactory<>("yearsOfCooperation"));
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
                    fioList.remove(lawyerTable.getFIO());
                });
            }
        });

        FilteredList<LawyerTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> fioString=new AtomicReference<>("");
        AtomicReference<String> phoneString=new AtomicReference<>("");
        AtomicReference<String> firmString=new AtomicReference<>("");
        AtomicReference<String> yearsString=new AtomicReference<>("");

        fioField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            fioString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataLawyerTable!=null) {
                changeFlags.set(0,fioString.get().equalsIgnoreCase(rowDataLawyerTable.getFIO()) || fioList.contains(fioString.get()));
            }

            filteredList.setPredicate(lawyerTable-> isConfidence(lawyerTable, fioString, phoneString, firmString, yearsString));
        }));

        phoneField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            phoneString.set(newValue);

            searchFlags.set(1,newValue.equals(""));

            if (rowDataLawyerTable!=null) {
                changeFlags.set(1,phoneString.get().equalsIgnoreCase(String.valueOf(rowDataLawyerTable.getPhone())));
            }

            filteredList.setPredicate(lawyerTable-> isConfidence(lawyerTable, fioString, phoneString, firmString, yearsString));
        }));

        firmField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            firmString.set(newValue);

            searchFlags.set(2,newValue.equals(""));

            if (rowDataLawyerTable!=null) {
                changeFlags.set(2,firmString.get().equalsIgnoreCase(String.valueOf(rowDataLawyerTable.getIdLawFirm())));
            }

            filteredList.setPredicate(lawyerTable-> isConfidence(lawyerTable, fioString, phoneString, firmString, yearsString));
        }));

        yearsField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            yearsString.set(newValue);

            searchFlags.set(3,newValue.equals(""));

            if (rowDataLawyerTable!=null) {
                changeFlags.set(3,yearsString.get().equalsIgnoreCase(String.valueOf(rowDataLawyerTable.getYearsOfCooperation())));
            }

            filteredList.setPredicate(lawyerTable-> isConfidence(lawyerTable, fioString, phoneString, firmString, yearsString));
        }));

        SortedList<LawyerTable> sortedList=new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(lawyersTable.comparatorProperty());
        lawyersTable.setItems(sortedList);

        lawyersTable.setRowFactory(param -> {
            TableRow<LawyerTable> row=new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount()==2 && rowData.equals(lawyersTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataLawyerTable = rowData;
                    addLawyerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(LawyerTable lawyerTable, AtomicReference<String> fioString, AtomicReference<String> phoneString, AtomicReference<String> firmString, AtomicReference<String> yearsString) {
        return lawyerTable.getFIO().contains(fioString.get()) &&
                lawyerTable.getPhone().toString().contains(phoneString.get()) &&
                String.valueOf(lawyerTable.getIdLawFirm()).contains(firmString.get()) &&
                String.valueOf(lawyerTable.getYearsOfCooperation()).contains(yearsString.get());
    }

    private void addFlags() {
        for (int i = 0; i < 4; ++i) {
            searchFlags.add(true);
            changeFlags.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetLawyers(new LawyerTable(fioField.getText(), phoneField.getText(), Integer.parseInt(firmField.getText()), Integer.parseInt(yearsField.getText()))));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataLawyerTable=null;
    }

    private void prepareTableForChanges() {
        fioList.remove(rowDataLawyerTable.getFIO());
    }

    private void resetChanges() {
        fioList.add(rowDataLawyerTable.getFIO());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteLawyerInf();
        updateRowDataLawyer();
        hideRstButton();
        addLawyersInf();
        databaseHandler.updateLawyer(rowDataLawyerTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataLawyer() {
        rowDataLawyerTable.setFIO(fioField.getText());
        rowDataLawyerTable.setPhone(phoneField.getText());
        rowDataLawyerTable.setIdLawFirm(Integer.parseInt(firmField.getText()));
        rowDataLawyerTable.setYearsOfCooperation(Integer.parseInt(yearsField.getText()));;
    }

    private void clearFields() {
        fioField.setText("");
        phoneField.setText("");
        firmField.setText("");
        yearsField.setText("");
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addLawyersInf() {
        data.add(rowDataLawyerTable);
        fioList.add(rowDataLawyerTable.getFIO());
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

    private void deleteLawyerInf() {
        data.remove(rowDataLawyerTable);
    }

    private void addLawyerToFields() {
        fioField.setText(rowDataLawyerTable.getFIO());
        phoneField.setText(String.valueOf(rowDataLawyerTable.getPhone()));
        firmField.setText(String.valueOf(rowDataLawyerTable.getIdLawFirm()));
        yearsField.setText(String.valueOf(rowDataLawyerTable.getYearsOfCooperation()));
    }
}
