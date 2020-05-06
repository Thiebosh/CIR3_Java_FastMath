package core.view;

import core.model.db.Express;
import core.model.db.ExpressManager;
import core.model.mathlibrary.parser.Parser;
import core.model.mathlibrary.parser.util.ParserResult;
import core.model.mathlibrary.parser.util.Point;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur du fichier graphic.fxml
 */
public class GraphicController implements Initializable {
    @FXML
    private LineChart grapheDisplay;

    private double xAxisLowerBound = -10;
    private double xAxisUpperBound = 10;
    private double xAxisTickUnit = 10;
    private double yAxisLowerBound = -10;
    private double yAxisUpperBound = 10;
    private double yAxisTickUnit = 10;


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
        initializeFunctionChoiceBox();
        initializeGraphTableView();
        initializeGraphDisplay();
    }

    private void initializeFunctionChoiceBox() {
        functionChoiceBox.getItems().addAll(ExpressManager.getExpressNames());
        //tester ça pour un rafraichissement automatique : https://stackoverflow.com/questions/21854146/javafx-2-0-choice-box-issue-how-to-update-a-choicebox-which-represents-a-list
    }

    private void initializeGraphTableView() {
        //link to data
        stateCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        expressCol.setCellValueFactory(new PropertyValueFactory<>("function"));
        samplingCol.setCellValueFactory(new PropertyValueFactory<>("sampling"));

        //edition
        //cas 1 : intialisation mais update ponctuelle (met pas à jour toutes les cases de la même fonction)
        stateCol.setCellFactory(CheckBoxTableCell.forTableColumn(stateCol));
        stateCol.setCellValueFactory((TableColumn.CellDataFeatures<Express, Boolean> p) -> {
            final Express t = p.getValue();
            final BooleanProperty result = new SimpleBooleanProperty(t.isActive());
            result.addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
                t.setIsActive(newValue);
                updateGraphDisplay();//conséquence
            });
            return result;
        });

        /*
        //cas 2 : intialisation et update globale mais bcp d'appels (donc bcp de rafraichissement)
        stateCol.setCellFactory(CheckBoxTableCell.forTableColumn((Callback<Integer, ObservableValue<Boolean>>) param -> {
            System.out.println("appel");
            final Express input = functionTableViewGraphic.getItems().get(param);
            input.isActiveProperty().addListener(l -> {
                updateGraphDisplay();//conséquence
                System.out.println(input.isActive());
            });
            return input.isActiveProperty();
        }));
        */


        samplingCol.setCellFactory(TextFieldTableCell.<Express, Integer>forTableColumn(new IntegerStringConverter()));
        samplingCol.setOnEditCommit((TableColumn.CellEditEvent<Express, Integer> event) -> {
            TablePosition<Express, Integer> pos = event.getTablePosition();
            Express express = event.getTableView().getItems().get(pos.getRow());
            express.setSampling(event.getNewValue());

            refreshTableViewGraphic();//reset si valeur invalide
            updateGraphDisplay();//conséquence
        });

        //injection des données
        refreshTableViewGraphic();
    }

    /**
     * Applique la liste de fonctions au TableView
     */
    private void refreshTableViewGraphic() {
        /*
        //cas 3 : update globale avec 1 seul appel mais pas initialisation (alourdit ajout)
        //create link to the instance (1 time)
        ObservableList<Express> list = FXCollections.observableArrayList(new Callback<Express, Observable[]>() {
            @Override
            public Observable[] call(Express param) { return new Observable[] {param.isActiveProperty()}; }
        });

        //create listener triggered by checkbox update (x times)
        list.addListener((ListChangeListener<Express>) expression -> {
            while (expression.next()) {
                if (expression.wasUpdated()) {
                    updateGraphDisplay();//consequence
                    //ExpressManager.getExpressGraph().get(expression.getFrom()).isActive();
                }
            }
        });

        //fill list
        list.addAll(ExpressManager.getExpressGraph());
*/

        ObservableList<Express> list = FXCollections.observableArrayList(ExpressManager.getExpressGraph());
        functionTableViewGraphic.setItems(list);
    }

    private void initializeGraphDisplay() {
        //axis settings
        grapheDisplay.getXAxis().setAutoRanging(false);
        ((NumberAxis) grapheDisplay.getXAxis()).setLowerBound(xAxisLowerBound);
        ((NumberAxis) grapheDisplay.getXAxis()).setUpperBound(xAxisUpperBound);
        //double range = ((NumberAxis) grapheDisplay.getXAxis()).getUpperBound() - ((NumberAxis) grapheDisplay.getXAxis()).getLowerBound();
        ((NumberAxis) grapheDisplay.getXAxis()).setTickUnit(xAxisTickUnit);//distance between two graduation

        grapheDisplay.getYAxis().setAutoRanging(false);
        ((NumberAxis) grapheDisplay.getYAxis()).setLowerBound(yAxisLowerBound);
        ((NumberAxis) grapheDisplay.getYAxis()).setUpperBound(yAxisUpperBound);
        ((NumberAxis) grapheDisplay.getYAxis()).setTickUnit(yAxisTickUnit);//distance between two graduation

    }

    private void updateGraphDisplay() {
        ObservableList<XYChart.Series<Number,Number>> graphMatrix = FXCollections.observableArrayList();

        for (Express element : ExpressManager.getExpressGraph()) {
            if (element.isActive()) graphMatrix.add(functionPlotCoords(element));
        }

        grapheDisplay.setData(graphMatrix);
    }

    private XYChart.Series<Number, Number> functionPlotCoords(Express expression) {
        XYChart.Series<Number, Number> coords = new XYChart.Series<Number, Number>();
        coords.setName(expression.getName());

        double range = (xAxisUpperBound - xAxisLowerBound)/expression.getSampling();
        for (double i = xAxisLowerBound; i <= xAxisUpperBound; i+=range) {
            coords.getData().add(new XYChart.Data<>(i, Parser.eval(expression.getFunction(), new Point("x", i)).getValue()));
        }

        return coords;
    }

    @FXML
    private void addFunctionToTable() {
        ExpressManager.addGraph((String) functionChoiceBox.getValue());//creation
        refreshTableViewGraphic();//visibilité
        updateGraphDisplay();
    }
}
