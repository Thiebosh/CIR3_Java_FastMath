package core.view;

import core.model.data.Express;
import core.model.data.ExpressManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ResourceBundle;

public class ComputeController implements Initializable {
    @FXML
    private TableView<Express> functionTableView;

    @FXML
    private TableColumn<Express, String> nameCol;

    @FXML
    private TableColumn<Express, String> functionCol;

    /**
     * Chargement initial des fonctions
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set link to data (needed to get displayed)
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        functionCol.setCellValueFactory(new PropertyValueFactory<>("function"));

        //set data injection
        refreshTableView();

        //set data edition
        nameCol.setCellFactory(TextFieldTableCell.<Express> forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<Express, String> event) -> tableViewEditCallback(event, "name"));

        functionCol.setCellFactory(TextFieldTableCell.<Express>forTableColumn());
        functionCol.setOnEditCommit((TableColumn.CellEditEvent<Express, String> event) -> tableViewEditCallback(event, "function"));
    }

    /**
     * Applique la liste de fonctions au TableView
     */
    private void refreshTableView() {
        ObservableList<Express> list = FXCollections.observableArrayList(ExpressManager.getExpressList());
        functionTableView.setItems(list);
    }

    private void tableViewEditCallback(TableColumn.CellEditEvent<Express, String> event, String attribute) {
        TablePosition<Express, String> pos = event.getTablePosition();
        Express expression = event.getTableView().getItems().get(pos.getRow());

        switch (attribute) {
            case "name":
                ExpressManager.updateName(expression.getName(), event.getNewValue());
                break;
            case "function":
                expression.setFunction(event.getNewValue());
                break;
            default:
                System.out.println("créer une exception \"bad attribute TableView calback\"");
                break;
        }

        refreshTableView();//hide invalid value injection
    }

    @FXML
    private void addFunctionLine() {
        ExpressManager.addToExpressList("fonction","0");//creation
        refreshTableView();//visibilité
    }
}
