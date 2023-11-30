package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.w3c.dom.css.CSSStyleRule;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet makers = databaseHandler.selectMakers();
    private final ObservableList<MakerTable> data = FXCollections.observableArrayList();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private MakerTable rowDataMakerTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            while (makers.next()) {
                data.add(new MakerTable(makers.getInt(1),
                        makers.getString(2),
                        makers.getDouble(3),
                        makers.getInt(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                changeFlags.set(0,firmString.get().equalsIgnoreCase(rowDataMakerTable.getMakerFirm()));
            }

            //filteredList.setPredicate(maker-> isConfidence());
        }));
    }
}