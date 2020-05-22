package core.app.view.scene;

import core.app.data.Express;
import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionComboBoxController;
import core.app.view.scene_components.ToggleSwitch;
import core.app.view.scene_contextual.ComputeContextController;
import core.services.windowHolder.StageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur de la page de calculs compute.fxml
 */
public class ComputeController implements Initializable {
    /**
     * Chargement initial (après le constructeur) du fxml lié au contrôleur  : prépration des éléments du fxml
     * @param location paramètre par défaut
     * @param resources paramètre par défaut
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        //place toggleSwitch and configure it
        toggleSwitchLocation.getChildren().add(toggleSwitch);
        toggleSwitch.getButton().setOnMouseClicked(mouseEvent -> {
            toggleSwitch.switchedOnProperty().set(!toggleSwitch.switchedOnProperty().get());
            ExpressManager.setDegree(toggleSwitch.switchedOnProperty().getValue());
        });
        toggleSwitch.getLabel().setOnMouseClicked(toggleSwitch.getButton().getOnMouseClicked());


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
     * Regroupement des callbacks de modification des colonnes du tableView
     * @param event la modification d'une colonne
     * @param attribute le nom de la colonne modifiée
     */
    private void tableViewEditCallback(TableColumn.CellEditEvent<Express, String> event, String attribute) {
        TablePosition<Express, String> pos = event.getTablePosition();
        Express expression = event.getTableView().getItems().get(pos.getRow());

        switch (attribute) {
            case "name":
                if (event.getNewValue().length() == 0) ExpressManager.removeFromExpressList(event.getOldValue());
                else ExpressManager.renameExpress(expression.getName(), event.getNewValue());
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


    /**
     * Element du fxml : affiche les expressions
     * @see Express
     */
    @FXML
    private TableView<Express> functionTableView;
    /**
     * Element du fxml (colonne de functionTableView) : affiche le nom de l'expression
     */
    @FXML
    private TableColumn<Express, String> nameCol;
    /**
     * Element du fxml (colonne de functionTableView) : affiche la fonction de l'expression
     */
    @FXML
    private TableColumn<Express, String> functionCol;
    /**
     * Applique la liste de fonctions au TableView
     */
    private void refreshTableView() {
        ObservableList<Express> list = FXCollections.observableArrayList(ExpressManager.getExpressList());
        functionTableView.setItems(list);
        functionTableView.refresh();

        FunctionComboBoxController.updateList();
    }
    /**
     * onAction du fxml : créer une expression par défaut et l'ajouter au tableView
     */
    @FXML
    private void addFunctionLine() {
        ExpressManager.addToExpressList("fonction","0");//creation
        refreshTableView();//visibilité
    }


    /**
     * Element du fxml (bouton toggle) : affiche le mode (degrés ou radians)
     */
    @FXML
    private HBox toggleSwitchLocation;
    private ToggleSwitch toggleSwitch = new ToggleSwitch();


    @FXML
    private void showWritingConvention() {
        StageService.Holder.openContextWindows("Tokens","computeContext", Modality.WINDOW_MODAL, new ComputeContextController(), null);
    }
}
