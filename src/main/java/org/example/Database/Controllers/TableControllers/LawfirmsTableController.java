package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Database.Classes.ClassesForDatabase.Tables.LawfirmTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.WorkerTable;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.w3c.dom.css.CSSStyleRule;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableView<LawfirmTable> lawfirmsTable;

    @FXML
    private TableColumn<LawfirmTable, String> lawFirmsPhoneColumn;

    @FXML
    private TableColumn<LawfirmTable, LawfirmTable> deleteColumn;

    @FXML
    private TableColumn<LawfirmTable, String> lawFirmsAddressColumn;


    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;


    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet lawfirms = databaseHandler.selectLawfirms();
    private final ObservableList<LawfirmTable> data = FXCollections.observableArrayList();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private LawfirmTable rowDataLawfirmTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            while (lawfirms.next()) {
                data.add(new LawfirmTable(lawfirms.getInt(1),
                        lawfirms.getString(2),
                        lawfirms.getString(3),
                        lawfirms.getString(4)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idLawFirmsColumn.setCellValueFactory(new PropertyValueFactory("idLawFirms"));
        firmNameColumn.setCellValueFactory(new PropertyValueFactory("firmName"));
        lawFirmsPhoneColumn.setCellValueFactory(new PropertyValueFactory("phone"));
        lawFirmsAddressColumn.setCellValueFactory(new PropertyValueFactory("address"));
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
                });
            }
        });

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
