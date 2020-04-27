package core.view.components;

import core.app.FastMath;
import core.model.db.Express;
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

public class FunctionTableViewController implements Initializable {
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
        //functionList.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.<Express> forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<Express, String> event) -> {
            tableViewEditcallback(event, "name");
        });

        functionCol.setCellFactory(TextFieldTableCell.<Express>forTableColumn());
        functionCol.setOnEditCommit((TableColumn.CellEditEvent<Express, String> event) -> {
            tableViewEditcallback(event, "function");
        });
    }

    /**
     * Applique la liste d'étudiants au TableView
     */
    private void refreshTableView() {
        ObservableList<Express> list = FXCollections.observableArrayList(FastMath.getExpressList());
        functionTableView.setItems(list);
    }

    private void tableViewEditcallback(TableColumn.CellEditEvent<Express, String> event, String attribute) {
        TablePosition<Express, String> pos = event.getTablePosition();
        Express expression = event.getTableView().getItems().get(pos.getRow());

        switch (attribute) {
            case "name":
                FastMath.update(expression, expression.getName(), event.getNewValue());
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

}
