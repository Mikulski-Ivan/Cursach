package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    private TextField nameField;


    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet goods = databaseHandler.selectGoods();
    private final ObservableList<GoodTable> data = FXCollections.observableArrayList();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private GoodTable rowDataGoodTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idGoodsColumn.setCellValueFactory(new PropertyValueFactory("idGoods"));
        goodsNameColumn.setCellValueFactory(new PropertyValueFactory("goodsName"));
        costPerPieceColumn.setCellValueFactory(new PropertyValueFactory("costPerPiece"));
        idMakerColumn.setCellValueFactory(new PropertyValueFactory("idMaker"));
        leftInStockColumn.setCellValueFactory(new PropertyValueFactory("leftInStock"));
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
                });
            }
        });

        goodsTable.setItems(data);

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