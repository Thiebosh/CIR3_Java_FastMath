package core.view;

import core.model.db.Express;
import core.model.db.ExpressManager;
import core.model.mathlibrary.parser.Parser;
import core.model.mathlibrary.parser.util.Point;
import core.model.services.StageService;
import core.view.contextual.GraphicContextControllerFactory;
import core.view.javafxCustom.ColorTableCell;
import core.view.javafxCustom.SliderTableCell;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static java.lang.Math.abs;

/**
 * Contrôleur du fichier graphic.fxml
 */
public class GraphicController implements Initializable {
    @FXML
    private LineChart grapheDisplay;

    private static BooleanProperty graphUpdate = new SimpleBooleanProperty(false);

    private static double xAxisLowerBound = -10;
    private static double xAxisUpperBound = 10;
    private static double yAxisLowerBound = -10;
    private static double yAxisUpperBound = 10;

    private static double xAxisTickUnit = 5;
    private static double yAxisTickUnit = 5;

    @FXML
    private ChoiceBox functionChoiceBox;
    private static final String SEPARATOR = " : ";

    @FXML
    private TableView<Express> functionTableViewGraphic;
    @FXML
    private TableColumn<Express, Boolean> stateCol;
    @FXML
    private TableColumn<Express, String> expressCol;
    @FXML
    private TableColumn<Express, Integer> samplingCol;
    @FXML
    private TableColumn<Express, Color> colorCol;

    public static void requireGraphUpdate() {
        graphUpdate.setValue(true);
    }

    public static void setxAxisLowerBound(double xAxisLowerBound) {
        GraphicController.xAxisLowerBound = xAxisLowerBound;
    }

    public static void setxAxisUpperBound(double xAxisUpperBound) {
        GraphicController.xAxisUpperBound = xAxisUpperBound;
    }

    public static void setyAxisLowerBound(double yAxisLowerBound) {
        GraphicController.yAxisLowerBound = yAxisLowerBound;
    }

    public static void setyAxisUpperBound(double yAxisUpperBound) {
        GraphicController.yAxisUpperBound = yAxisUpperBound;
    }

    public static void setxAxisTickUnit(double xAxisTickUnit) {
        GraphicController.xAxisTickUnit = xAxisTickUnit;
    }

    public static void setyAxisTickUnit(double yAxisTickUnit) {
        GraphicController.yAxisTickUnit = yAxisTickUnit;
    }

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

    /**
     * Remplissage du ChoiceBox
     */
    private void initializeFunctionChoiceBox() {
        ArrayList<String> list = new ArrayList<String>();
        for (Express current : ExpressManager.getExpressList()) list.add(current.getName() + SEPARATOR + current.getFunction());
        functionChoiceBox.getItems().addAll(list);
    }

    /**
     * Préparation des axes du graphe
     */
    private void initializeGraphDisplay() {
        //axis settings
        grapheDisplay.getXAxis().setAutoRanging(false);
        grapheDisplay.getYAxis().setAutoRanging(false);

        //update settings : add change listener
        graphUpdate.addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {
                graphUpdate.setValue(false);

                ((NumberAxis) grapheDisplay.getXAxis()).setLowerBound(xAxisLowerBound);
                ((NumberAxis) grapheDisplay.getXAxis()).setUpperBound(xAxisUpperBound);
                ((NumberAxis) grapheDisplay.getXAxis()).setTickUnit(xAxisTickUnit);//distance between two graduation

                ((NumberAxis) grapheDisplay.getYAxis()).setLowerBound(yAxisLowerBound);
                ((NumberAxis) grapheDisplay.getYAxis()).setUpperBound(yAxisUpperBound);
                ((NumberAxis) grapheDisplay.getYAxis()).setTickUnit(yAxisTickUnit);//distance between two graduation
            }
        });

        graphUpdate.setValue(true);//set intial values
    }

    /**
     * Mise en place des liens entre instance de Express et colonnes de TableViews + injection de données
     */
    private void initializeGraphTableView() {
        //link to data
        stateCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        expressCol.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> cellData.getValue().getName() + " : " + cellData.getValue().getFunction(),
                cellData.getValue().nameProperty(),
                cellData.getValue().functionProperty()
            ));
        samplingCol.setCellValueFactory(new PropertyValueFactory<>("sampling"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));

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

        colorCol.setCellFactory(ColorTableCell::new);
        colorCol.setOnEditCommit((TableColumn.CellEditEvent<Express, Color> event) -> {
            TablePosition<Express, Color> pos = event.getTablePosition();
            Express express = event.getTableView().getItems().get(pos.getRow());
            express.setColor(event.getNewValue());
            updateGraphDisplay();//conséquence
        });


        //injection des données
        refreshTableViewGraphic();
    }

    /**
     * Ajoute fonction choisie au TableView et l'affiche dans le graphe
     */
    @FXML
    private void addFunctionToTable() {
        String express = (String) functionChoiceBox.getValue();
        ExpressManager.addGraph(express.substring(0, express.indexOf(SEPARATOR)));//creation
        refreshTableViewGraphic();//visibilité
        updateGraphDisplay();
    }

    /**
     * Applique la liste de fonctions au TableView + mise en place derniers liens instance - colonne
     */
    private void refreshTableViewGraphic() {
        double samplingMax = (abs(xAxisLowerBound)+abs(xAxisUpperBound))*2;//cas ou cadre > 1 ?
        samplingCol.setCellFactory(SliderTableCell.forTableColumn(2,(int)samplingMax));

        //cas 3 : update globale avec 1 seul appel mais pas initialisation (alourdit ajout)
        //create link to the instance (1 time)
        ObservableList<Express> list = FXCollections.observableArrayList(param -> new Observable[] {param.samplingProperty()});

        //create listener triggered by checkbox update (x times)
        list.addListener((ListChangeListener<Express>) expression -> {
            while (expression.next()) {
                if (expression.wasUpdated()) {
                    Express current = ExpressManager.getExpressGraph().get(expression.getFrom());
                    if (current.getSampling() % 2 != 0) { current.setSampling(current.getSampling()-1); }

                    if (current.getSampling() != current.getSamplingBefore()) {
                        current.setSamplingBefore(current.getSampling());//limiter les rafraichissements
                        updateGraphDisplay();//consequence
                    }
                }
            }
        });

        //fill list
        list.addAll(ExpressManager.getExpressGraph());

        functionTableViewGraphic.setItems(list);
    }

    /**
     * Génère les points des fonctions à afficher
     */
    public void updateGraphDisplay() {
        grapheDisplay.getData().clear();
        for (Express element : ExpressManager.getExpressGraph()) {
            if (element.isActive()) plotFunction(element);
        }
    }

    /**
     * Calcule les points de la fonction à afficher et lui applique sa couleur
     * @param expression La fonction dont il faut calculer les points
     */
    private void plotFunction(Express expression) {
        XYChart.Series<Number, Number> coords = new XYChart.Series<Number, Number>();
        coords.setName(expression.getName());

        double range = (xAxisUpperBound - xAxisLowerBound)/expression.getSampling();
        for (double i = xAxisLowerBound; i < xAxisUpperBound+range; i+=range) {
            coords.getData().add(new XYChart.Data<>(i, Parser.eval(expression.getFunction(), new Point("x", i)).getValue()));
        }

        grapheDisplay.getData().add(coords);

        // convert line color to CSS format and set line color on Series node
        String lineStyle = "-fx-stroke: rgba("
                +(int) (expression.getColor().getRed()*255)+", "
                +(int) (expression.getColor().getGreen()*255)+", "
                +(int) (expression.getColor().getBlue()*255)+", 1.0);";
        coords.getNode().lookup(".chart-series-line").setStyle(lineStyle);
    }

    @FXML
    private void editGraphParam() {
        HashMap<String,Double> arguments = new HashMap<>() {{
            put("xMin", xAxisLowerBound);
            put("xMax", xAxisUpperBound);
            put("yMin", yAxisLowerBound);
            put("yMax", yAxisUpperBound);
            put("scaleX", xAxisTickUnit);
            put("scaleY", yAxisTickUnit);
        }};
        StageService.Holder.openContextWindows("Propriétés","graphicContext", new GraphicContextControllerFactory(), arguments);


        //listener sur booleen contextopened -> quand passe a false, update des champs et du graphe
    }
}
