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
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class MakersTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<MakerTable, Integer> idMakersColumn;

    @FXML
    private TableColumn<MakerTable, String> makerFirmColumn;

    @FXML
    private TableView<MakerTable> makersTable;

    @FXML
    private TableColumn<MakerTable, Double> makersYearsOfCooperationColumn;

    @FXML
    private TableColumn<MakerTable, MakerTable> deleteColumn;

    @FXML
    private TableColumn<MakerTable, Double> reputationColumn;


    @FXML
    private TextField yearsField;
    @FXML
    private TextField reputationField;
    @FXML
    private TextField firmField;

    @FXML
    private Button resetButton;

    @FXML
    private Button addButton;


    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet makers = databaseHandler.selectMakers();
    private final ObservableList<MakerTable> data = FXCollections.observableArrayList();
    private final List<String> firmList =new ArrayList<>();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private MakerTable rowDataMakerTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        hideRstButton();

        addFlags();

        changeFlags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(!changeFlags.contains(false)));
        searchFlags.addListener((ListChangeListener<Boolean>) change -> addButton.setDisable(searchFlags.contains(true)));

        backButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) backButton.getScene().getWindow()));

        try {
            while (makers.next()) {
                data.add(new MakerTable(makers.getInt(1),
                        makers.getString(2),
                        makers.getDouble(3),
                        makers.getInt(4)));
                firmList.add(makers.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onAddEvent());

        idMakersColumn.setCellValueFactory(new PropertyValueFactory<>("idMakers"));
        makerFirmColumn.setCellValueFactory(new PropertyValueFactory<>("makerFirm"));
        reputationColumn.setCellValueFactory(new PropertyValueFactory<>("reputation"));
        makersYearsOfCooperationColumn.setCellValueFactory(new PropertyValueFactory<>("yearsOfCooperation"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(MakerTable makerTable, boolean empty) {
                super.updateItem(makerTable, empty);

                if (makerTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(makerTable);
                    databaseHandler.deleteMaker(makerTable);
                    firmList.remove(makerTable.getMakerFirm());
                });
            }
        });

        FilteredList<MakerTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> firmString=new AtomicReference<>("");
        AtomicReference<String> reputationString=new AtomicReference<>("");
        AtomicReference<String> yearsString=new AtomicReference<>("");

        firmField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            firmString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataMakerTable!=null) {
                changeFlags.set(0,firmString.get().equalsIgnoreCase(rowDataMakerTable.getMakerFirm()) || firmList.contains(firmString.get()));
            }

            filteredList.setPredicate(makerTable-> isConfidence(makerTable, firmString, yearsString, reputationString));
        }));

        reputationField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            reputationString.set(newValue);

            searchFlags.set(1,newValue.equals(""));

            if (rowDataMakerTable!=null) {
                changeFlags.set(1,reputationString.get().equalsIgnoreCase(String.valueOf(rowDataMakerTable.getReputation())));
            }

            filteredList.setPredicate(makerTable-> isConfidence(makerTable, firmString, yearsString, reputationString));
        }));

        yearsField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            yearsString.set(newValue);

            searchFlags.set(2,newValue.equals(""));

            if (rowDataMakerTable!=null) {
                changeFlags.set(2,yearsString.get().equalsIgnoreCase(String.valueOf(rowDataMakerTable.getYearsOfCooperation())));
            }

            filteredList.setPredicate(makerTable-> isConfidence(makerTable,firmString,yearsString,reputationString));
        }));

        SortedList<MakerTable> sortedList=new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(makersTable.comparatorProperty());
        makersTable.setItems(sortedList);

        makersTable.setRowFactory(param -> {
            TableRow<MakerTable> row=new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount()==2 && rowData.equals(makersTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataMakerTable = rowData;
                    addMakerToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(MakerTable makerTable, AtomicReference<String> firmString, AtomicReference<String> reputationString, AtomicReference<String> yearsString) {
        return makerTable.getMakerFirm().contains(firmString.get()) &&
                makerTable.getReputation().toString().contains(reputationString.get()) &&
                String.valueOf(makerTable.getYearsOfCooperation()).contains(yearsString.get());
    }

    private void addFlags() {
        for (int i = 0; i < 3; ++i) {
            searchFlags.add(true);
            changeFlags.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetMakers(new MakerTable(firmField.getText(), Double.parseDouble(yearsField.getText()), Integer.parseInt(reputationField.getText()))));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataMakerTable=null;
    }

    private void prepareTableForChanges() {
        firmList.remove(rowDataMakerTable.getMakerFirm());
    }

    private void resetChanges() {
        firmList.add(rowDataMakerTable.getMakerFirm());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteMakerInf();
        updateRowDataMaker();
        hideRstButton();
        addMakersInf();
        databaseHandler.updateMaker(rowDataMakerTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }


    private void updateRowDataMaker() {
        rowDataMakerTable.setMakerFirm(firmField.getText());
        rowDataMakerTable.setYearsOfCooperation(Integer.parseInt(yearsField.getText()));
        rowDataMakerTable.setReputation(Double.valueOf(reputationField.getText()));
    }

    private void clearFields() {
        firmField.setText("");
        yearsField.setText("");
        reputationField.setText("");
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addMakersInf() {
        data.add(rowDataMakerTable);
        firmList.add(rowDataMakerTable.getMakerFirm());
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

    private void deleteMakerInf() {
        data.remove(rowDataMakerTable);
    }

    private void addMakerToFields() {
        firmField.setText(rowDataMakerTable.getMakerFirm());
        yearsField.setText(String.valueOf(rowDataMakerTable.getYearsOfCooperation()));
        reputationField.setText(String.valueOf(rowDataMakerTable.getReputation()));
    }


}