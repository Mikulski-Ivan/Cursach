package org.example.Database.Controllers.TableControllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.collections.transformation.FilteredList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Database.Classes.ClassesForDatabase.Tables.ContractTable;
import org.example.Database.Classes.ClassesForDatabase.Tables.MakerTable;
        import org.example.Database.Classes.ClassesForDatabase.Tables.WorkerTable;
        import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
        import org.w3c.dom.css.CSSStyleRule;

        import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.ResourceBundle;
        import java.util.concurrent.atomic.AtomicReference;

public class ContractsTableController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<ContractTable, Integer> idContractsColumn;

    @FXML
    private TableColumn<ContractTable, String> customerColumn;

    @FXML
    private TableView<ContractTable> contractsTable;

    @FXML
    private TableColumn<ContractTable, Integer> quantityColumn;

    @FXML
    private TableColumn<ContractTable, ContractTable> deleteColumn;

    @FXML
    private TableColumn<ContractTable, Double> totalCostColumn;

    @FXML
    private TableColumn<ContractTable, Integer> idGoodColumn;

    @FXML
    private TableColumn<ContractTable, Integer> idWorkerColumn;

    @FXML
    private TableColumn<ContractTable, Integer> idLawyerColumn;

    @FXML
    private TableColumn<WorkerTable, Date> dateOfSaleColumn;


    @FXML
    private TextField customerField;
    @FXML
    private TextField datePicker;

    private final DatabaseHandler databaseHandler = new DatabaseHandler();
    private final ResultSet contracts = databaseHandler.selectContracts();
    private final ObservableList<ContractTable> data = FXCollections.observableArrayList();

    private final ObservableList<Boolean> searchFlags = FXCollections.observableArrayList();
    private final ObservableList<Boolean> changeFlags = FXCollections.observableArrayList();

    private ContractTable rowDataContractTable=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            while (contracts.next()) {
                data.add(new ContractTable(contracts.getInt(1),
                        contracts.getString(2),
                        contracts.getInt(3),
                        contracts.getDouble(4),
                        contracts.getInt(5),
                        contracts.getInt(6),
                        contracts.getInt(7),
                        contracts.getDate(8)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idContractsColumn.setCellValueFactory(new PropertyValueFactory("idContracts"));
        customerColumn.setCellValueFactory(new PropertyValueFactory("customer"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory("quantity"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory("totalCost"));
        idGoodColumn.setCellValueFactory(new PropertyValueFactory("idGood"));
        idWorkerColumn.setCellValueFactory(new PropertyValueFactory("idWorker"));
        idLawyerColumn.setCellValueFactory(new PropertyValueFactory("idLawyer"));
        dateOfSaleColumn.setCellValueFactory(new PropertyValueFactory("dateOfSale"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>(){
            private final Button deleteButton=new Button("Delete");

            @Override
            protected void updateItem(ContractTable contractTable, boolean empty) {
                super.updateItem(contractTable, empty);

                if (contractTable==null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(actionEvent -> {
                    data.remove(contractTable);
                    databaseHandler.deleteContract(contractTable);
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