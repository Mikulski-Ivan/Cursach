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
import org.example.Database.Classes.ClassesForDatabase.Tables.LawfirmTable;
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

public class LawfirmsTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<LawfirmTable, Integer> idLawFirmsColumn;

    @FXML
    private TableColumn<LawfirmTable, String> firmNameColumn;

    @FXML
    private TableView<LawfirmTable> lawFirmsTable;

    @FXML
    private TableColumn<LawfirmTable, String> lawFirmsPhoneColumn;

    @FXML
    private TableColumn<LawfirmTable, LawfirmTable> deleteColumn;

    @FXML
    private TableColumn<LawfirmTable, String> lawFirmsAddressColumn;


    @FXML
    private Button resetButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet lawfirms = databaseHandler.selectLawfirms();
    private final ObservableList<LawfirmTable> data = FXCollections.observableArrayList();
    private final List<String> nameList =new ArrayList<>();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private LawfirmTable rowDataLawfirmTable=null;

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
            while (lawfirms.next()) {
                data.add(new LawfirmTable(lawfirms.getInt(1),
                        lawfirms.getString(2),
                        lawfirms.getString(3),
                        lawfirms.getString(4)));
                nameList.add(lawfirms.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onAddEvent());

        idLawFirmsColumn.setCellValueFactory(new PropertyValueFactory<>("idLawFirms"));
        firmNameColumn.setCellValueFactory(new PropertyValueFactory<>("firmName"));
        lawFirmsPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        lawFirmsAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(LawfirmTable lawfirmTable, boolean empty) {
                super.updateItem(lawfirmTable, empty);

                if (lawfirmTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(lawfirmTable);
                    databaseHandler.deleteLawfirm(lawfirmTable);
                    nameList.remove(lawfirmTable.getFirmName());
                });
            }
        });

        FilteredList<LawfirmTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> nameString=new AtomicReference<>("");
        AtomicReference<String> phoneString=new AtomicReference<>("");
        AtomicReference<String> addressString=new AtomicReference<>("");

        nameField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            nameString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataLawfirmTable!=null) {
                changeFlags.set(0,nameString.get().equalsIgnoreCase(rowDataLawfirmTable.getFirmName()) || nameList.contains(nameString.get()));
            }

            filteredList.setPredicate(lawfirmTable-> isConfidence(lawfirmTable, nameString, phoneString, addressString));
        }));

        phoneField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            phoneString.set(newValue);

            searchFlags.set(1,newValue.equals(""));

            if (rowDataLawfirmTable!=null) {
                changeFlags.set(1,phoneString.get().equalsIgnoreCase(String.valueOf(rowDataLawfirmTable.getPhone())));
            }

            filteredList.setPredicate(lawfirmTable-> isConfidence(lawfirmTable, nameString, phoneString, addressString));
        }));

        addressField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            addressString.set(newValue);

            searchFlags.set(2,newValue.equals(""));

            if (rowDataLawfirmTable!=null) {
                changeFlags.set(2,addressString.get().equalsIgnoreCase(String.valueOf(rowDataLawfirmTable.getAddress())));
            }

            filteredList.setPredicate(lawfirmTable-> isConfidence(lawfirmTable, nameString, phoneString, addressString));
        }));

        SortedList<LawfirmTable> sortedList=new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(lawFirmsTable.comparatorProperty());
        lawFirmsTable.setItems(sortedList);

        lawFirmsTable.setRowFactory(param -> {
            TableRow<LawfirmTable> row=new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount()==2 && rowData.equals(lawFirmsTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataLawfirmTable = rowData;
                    addLawfirmToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(LawfirmTable lawfirmTable, AtomicReference<String> nameString, AtomicReference<String> phoneString, AtomicReference<String> addressString) {
        return lawfirmTable.getFirmName().contains(nameString.get()) &&
                lawfirmTable.getPhone().contains(phoneString.get()) &&
                lawfirmTable.getAddress().contains(addressString.get());
    }

    private void addFlags() {
        for (int i = 0; i < 3; ++i) {
            searchFlags.add(true);
            changeFlags.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetLawfirms(new LawfirmTable(nameField.getText(), phoneField.getText(), addressField.getText())));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataLawfirmTable=null;
    }

    private void prepareTableForChanges() {
        nameList.remove(rowDataLawfirmTable.getFirmName());
    }

    private void resetChanges() {
        nameList.add(rowDataLawfirmTable.getFirmName());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteLawfirmInf();
        updateRowDataLawfirm();
        hideRstButton();
        addLawfirmsInf();
        databaseHandler.updateLawfirm(rowDataLawfirmTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataLawfirm() {
        rowDataLawfirmTable.setFirmName(nameField.getText());
        rowDataLawfirmTable.setPhone(phoneField.getText());
        rowDataLawfirmTable.setAddress(addressField.getText());
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addLawfirmsInf() {
        data.add(rowDataLawfirmTable);
        nameList.add(rowDataLawfirmTable.getFirmName());
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

    private void deleteLawfirmInf() {
        data.remove(rowDataLawfirmTable);
    }

    private void addLawfirmToFields() {
        nameField.setText(rowDataLawfirmTable.getFirmName());
        phoneField.setText(String.valueOf(rowDataLawfirmTable.getPhone()));
        addressField.setText(String.valueOf(rowDataLawfirmTable.getAddress()));
    }

}
