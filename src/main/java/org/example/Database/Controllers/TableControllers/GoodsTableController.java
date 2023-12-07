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

public class GoodsTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<GoodTable, Integer> idGoodsColumn;

    @FXML
    private TableColumn<GoodTable, String> goodsNameColumn;

    @FXML
    private TableView<GoodTable> goodsTable;

    @FXML
    private TableColumn<GoodTable, Double> costPerPieceColumn;

    @FXML
    private TableColumn<GoodTable, GoodTable> deleteColumn;

    @FXML
    private TableColumn<GoodTable, Integer> idMakerColumn;

    @FXML
    private TableColumn<GoodTable, Integer> leftInStockColumn;

    @FXML
    private Button resetButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField nameField;
    @FXML
    private TextField costField;
    @FXML
    private TextField makerField;
    @FXML
    private TextField stockField;


    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet goods = databaseHandler.selectGoods();
    private final ObservableList<GoodTable> data = FXCollections.observableArrayList();
    private final List<String> nameList =new ArrayList<>();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private GoodTable rowDataGoodTable=null;

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
            while (goods.next()) {
                data.add(new GoodTable(goods.getInt(1),
                        goods.getString(2),
                        goods.getDouble(3),
                        goods.getInt(4),
                        goods.getInt(5)));
                nameList.add(goods.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addButton.setDisable(true);
        addButton.setOnAction(actionEvent -> onAddEvent());

        idGoodsColumn.setCellValueFactory(new PropertyValueFactory<>("idGoods"));
        goodsNameColumn.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
        costPerPieceColumn.setCellValueFactory(new PropertyValueFactory<>("costPerPiece"));
        idMakerColumn.setCellValueFactory(new PropertyValueFactory<>("idMaker"));
        leftInStockColumn.setCellValueFactory(new PropertyValueFactory<>("leftInStock"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(GoodTable goodTable, boolean empty) {
                super.updateItem(goodTable, empty);

                if (goodTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(goodTable);
                    databaseHandler.deleteGood(goodTable);
                    nameList.remove(goodTable.getGoodsName());
                });
            }
        });

        FilteredList<GoodTable> filteredList=new FilteredList<>(data, b->true);
        AtomicReference<String> nameString=new AtomicReference<>("");
        AtomicReference<String> costString=new AtomicReference<>("");
        AtomicReference<String> makerString=new AtomicReference<>("");
        AtomicReference<String> stockString=new AtomicReference<>("");

        nameField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            nameString.set(newValue);

            searchFlags.set(0,newValue.equals(""));

            if (rowDataGoodTable!=null) {
                changeFlags.set(0,nameString.get().equalsIgnoreCase(rowDataGoodTable.getGoodsName()) || nameList.contains(nameString.get()));
            }

            filteredList.setPredicate(goodTable-> isConfidence(goodTable, nameString, costString, makerString, stockString));
        }));

        costField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            costString.set(newValue);

            searchFlags.set(1,newValue.equals(""));

            if (rowDataGoodTable!=null) {
                changeFlags.set(1,costString.get().equalsIgnoreCase(String.valueOf(rowDataGoodTable.getCostPerPiece())));
            }

            filteredList.setPredicate(goodTable-> isConfidence(goodTable, nameString, costString, makerString, stockString));
        }));

        makerField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            makerString.set(newValue);

            searchFlags.set(2,newValue.equals(""));

            if (rowDataGoodTable!=null) {
                changeFlags.set(2,makerString.get().equalsIgnoreCase(String.valueOf(rowDataGoodTable.getIdMaker())));
            }

            filteredList.setPredicate(goodTable-> isConfidence(goodTable, nameString, costString, makerString, stockString));
        }));

        stockField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            stockString.set(newValue);

            searchFlags.set(3,newValue.equals(""));

            if (rowDataGoodTable!=null) {
                changeFlags.set(3,stockString.get().equalsIgnoreCase(String.valueOf(rowDataGoodTable.getLeftInStock())));
            }

            filteredList.setPredicate(goodTable-> isConfidence(goodTable, nameString, costString, makerString, stockString));
        }));

        SortedList<GoodTable> sortedList=new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(goodsTable.comparatorProperty());
        goodsTable.setItems(sortedList);

        goodsTable.setRowFactory(param -> {
            TableRow<GoodTable> row=new TableRow<>();

            resetButton.setOnAction(actionEvent -> {
                convertChgToAdd();
                hideRstButton();
                resetChanges();
                clearFields();
            });

            row.setOnMouseClicked(mouseEvent -> Optional.ofNullable(row.getItem()).ifPresent(rowData -> {
                if (mouseEvent.getClickCount()==2 && rowData.equals(goodsTable.getSelectionModel().getSelectedItem())) {
                    showRstButton();
                    rowDataGoodTable = rowData;
                    addGoodToFields();
                    prepareTableForChanges();
                    convertAddToChg();
                }
            }));

            return row;
        });
    }

    private boolean isConfidence(GoodTable goodTable, AtomicReference<String> nameString, AtomicReference<String> costString, AtomicReference<String> makerString, AtomicReference<String> stockString) {
        return goodTable.getGoodsName().contains(nameString.get()) &&
                goodTable.getCostPerPiece().toString().contains(costString.get()) &&
                String.valueOf(goodTable.getIdMaker()).contains(makerString.get()) &&
                String.valueOf(goodTable.getLeftInStock()).contains(stockString.get());
    }

    private void addFlags() {
        for (int i = 0; i < 4; ++i) {
            searchFlags.add(true);
            changeFlags.add(false);
        }
    }

    void onAddEvent() {
        data.add(databaseHandler.insertAndGetGoods(new GoodTable(nameField.getText(), Double.parseDouble(costField.getText()), Integer.parseInt(makerField.getText()), Integer.parseInt(stockField.getText()))));
        clearFields();
        setRowDataNull();
    }

    private void setRowDataNull() {
        rowDataGoodTable=null;
    }

    private void prepareTableForChanges() {
        nameList.remove(rowDataGoodTable.getGoodsName());
    }

    private void resetChanges() {
        nameList.add(rowDataGoodTable.getGoodsName());
        clearFields();
        setRowDataNull();
    }

    private void onChangeEvent() {
        deleteGoodInf();
        updateRowDataGood();
        hideRstButton();
        addGoodsInf();
        databaseHandler.updateGood(rowDataGoodTable);
        convertChgToAdd();
        clearFields();
        setRowDataNull();
    }

    private void updateRowDataGood() {
        rowDataGoodTable.setGoodsName(nameField.getText());
        rowDataGoodTable.setCostPerPiece(Double.valueOf(costField.getText()));
        rowDataGoodTable.setIdMaker(Integer.parseInt(makerField.getText()));
        rowDataGoodTable.setLeftInStock(Integer.parseInt(stockField.getText()));;
    }

    private void clearFields() {
        nameField.setText("");
        costField.setText("");
        makerField.setText("");
        stockField.setText("");
    }

    private void convertChgToAdd() {
        addButton.setDisable(false);
        addButton.setText("Add");
        addButton.setOnAction(actionEvent -> onAddEvent());
    }

    private void addGoodsInf() {
        data.add(rowDataGoodTable);
        nameList.add(rowDataGoodTable.getGoodsName());
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

    private void deleteGoodInf() {
        data.remove(rowDataGoodTable);
    }

    private void addGoodToFields() {
        nameField.setText(rowDataGoodTable.getGoodsName());
        costField.setText(String.valueOf(rowDataGoodTable.getCostPerPiece()));
        makerField.setText(String.valueOf(rowDataGoodTable.getIdMaker()));
        stockField.setText(String.valueOf(rowDataGoodTable.getLeftInStock()));
    }

}