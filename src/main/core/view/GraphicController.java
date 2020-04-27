package core.view;

import core.model.db.Express;
import core.model.db.ExpressManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur du fichier graphic.fxml
 */
public class GraphicController implements Initializable {
    @FXML
    private ChoiceBox functionChoiceBox;

    @FXML
    private TableView<Express> functionTableViewGraphic;

    @FXML
    private TableColumn<Express, Boolean> stateCol;

    @FXML
    private TableColumn<Express, String> nameCol;

    @FXML
    private TableColumn<Express, String> expressCol;

    @FXML
    private TableColumn<Express, Integer> samplingCol;

    @FXML
    private TableColumn styleCol;


    /**
     * Chargement initial des fonctions
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        functionChoiceBox.getItems().addAll(ExpressManager.getExpressNames());
        //tester ça pour un rafraichissement automatique : https://stackoverflow.com/questions/21854146/javafx-2-0-choice-box-issue-how-to-update-a-choicebox-which-represents-a-list


        stateCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        stateCol.setCellFactory(tc -> new CheckBoxTableCell<>());

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        expressCol.setCellValueFactory(new PropertyValueFactory<>("function"));

        samplingCol.setCellValueFactory(new PropertyValueFactory<>("sampling"));


        final var items = FXCollections.observableArrayList(
                new Express("f", "2*x", 15, true),
                new Express("g", "2.25*x", 30, false));
        functionTableViewGraphic.setItems(items);

/*
        stateCol.setCellValueFactory( f -> f.getValue().getCompleted());
        stateCol.setCellFactory( tc -> new CheckBoxTableCell< RSSReader, Boolean >());



        stateCol.setCellValueFactory(new Callback<CellDataFeatures<RSSReader,Boolean>, ObservableValue<Boolean>>() {
                    @Override public
                    ObservableValue<Boolean> call( CellDataFeatures<RSSReader,Boolean> p ){
                        return p.getValue().getCompleted();
                    }
        });
        stateCol.setCellFactory(
                new Callback<TableColumn<RSSReader,Boolean>,TableCell<RSSReader,Boolean>>(){
                    @Override public
                    TableCell<RSSReader,Boolean> call( TableColumn<RSSReader,Boolean> p ){
                        return new CheckBoxTableCell<>(); }});


        stateCol.setCellValueFactory(new PersonUnemployedValueFactory());

 */
    }

    @FXML
    private void addFunctionToTable() {
        //ExpressManager.addGraph((String) functionChoiceBox.getValue());
    }
}
