package core.app.view.scene;

import core.app.data.Express;
import core.app.data.ExpressManager;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.Point;
import core.services.windowHolder.StageService;
import core.app.view.scene_contextual.GraphicContextControllerFactory;
import core.services.javafxCustom.ColorTableCell;
import core.services.javafxCustom.SliderTableCell;
import javafx.application.Platform;
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
    private LineChart graphDisplay;

    private static BooleanProperty graphUpdate = new SimpleBooleanProperty(false);

    private static Double xAxisLowerBound = -10.0;
    private static Double xAxisUpperBound = 10.0;
    private static Double yAxisLowerBound = -10.0;
    private static Double yAxisUpperBound = 10.0;

    private static Double xAxisTickUnit = 5.0;
    private static Double yAxisTickUnit = 5.0;

    private Object lock = new Object();//synchronisation sur cet element uniquement

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
    private static double samplingMax = 100;

    @FXML
    private TableColumn<Express, Color> colorCol;

    public static void requireGraphUpdate() {
        graphUpdate.setValue(true);
    }

    public static void setXAxisLowerBound(double xAxisLowerBound) { GraphicController.xAxisLowerBound = xAxisLowerBound; }

    public static void setXAxisUpperBound(double xAxisUpperBound) { GraphicController.xAxisUpperBound = xAxisUpperBound; }

    public static void setYAxisLowerBound(double yAxisLowerBound) { GraphicController.yAxisLowerBound = yAxisLowerBound; }

    public static void setYAxisUpperBound(double yAxisUpperBound) { GraphicController.yAxisUpperBound = yAxisUpperBound; }

    public static void setXAxisTickUnit(double xAxisTickUnit) {
        GraphicController.xAxisTickUnit = xAxisTickUnit;
    }

    public static void setYAxisTickUnit(double yAxisTickUnit) {
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
        ArrayList<String> list = new ArrayList<>();
        for (Express current : ExpressManager.getExpressList()) list.add(current.getName() + SEPARATOR + current.getFunction());
        functionChoiceBox.getItems().addAll(list);
    }

    /**
     * Préparation des axes du graphe
     */
    private void initializeGraphDisplay() {
        //axis settings
        graphDisplay.getXAxis().setAutoRanging(false);
        graphDisplay.getYAxis().setAutoRanging(false);

        //update settings : add change listener
        graphUpdate.addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {
                graphUpdate.setValue(false);

                ((NumberAxis) graphDisplay.getXAxis()).setLowerBound(xAxisLowerBound);
                ((NumberAxis) graphDisplay.getXAxis()).setUpperBound(xAxisUpperBound);
                ((NumberAxis) graphDisplay.getXAxis()).setTickUnit(xAxisTickUnit);//distance between two graduation

                ((NumberAxis) graphDisplay.getYAxis()).setLowerBound(yAxisLowerBound);
                ((NumberAxis) graphDisplay.getYAxis()).setUpperBound(yAxisUpperBound);
                ((NumberAxis) graphDisplay.getYAxis()).setTickUnit(yAxisTickUnit);//distance between two graduation

                updateGraphDisplay(null, false);//recalculate all function points
            }
        });

        graphUpdate.setValue(true);//call listener : set intial values
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
            final Express express = p.getValue();
            final BooleanProperty result = new SimpleBooleanProperty(express.isActive());
            result.addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
                express.setIsActive(newValue);
                updateGraphDisplay(express.getName(), newValue);//conséquence
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
            updateGraphDisplay(express.getName(), false);//conséquence
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
        ExpressManager.addToGraphList(express.substring(0, express.indexOf(SEPARATOR)));//creation
        refreshTableViewGraphic();//visibilité
        updateGraphDisplay(express.substring(0, express.indexOf(SEPARATOR)), false);
    }

    /**
     * Applique la liste de fonctions au TableView + mise en place derniers liens instance - colonne
     */
    private void refreshTableViewGraphic() {
        samplingCol.setCellFactory(SliderTableCell.forTableColumn(2,(int)samplingMax));

        //cas 3 : update globale avec 1 seul appel mais pas initialisation (alourdit ajout)
        //create link to the instance (1 time)
        ObservableList<Express> list = FXCollections.observableArrayList(param -> new Observable[] {param.samplingProperty()});

        //create listener triggered by checkbox update (x times)
        list.addListener((ListChangeListener<Express>) expression -> {
            while (expression.next()) {
                if (expression.wasUpdated()) {
                    Express current = ExpressManager.getExpressGraphList().get(expression.getFrom());
                    if (current.getSampling() % 2 != 0) current.setSampling(current.getSampling()-1);

                    if (current.getSampling() != current.getSamplingBefore()) {
                        current.setSamplingBefore(current.getSampling());//limiter les rafraichissements
                        updateGraphDisplay(current.getName(), false);//consequence
                    }
                }
            }
        });

        //fill list
        list.addAll(ExpressManager.getExpressGraphList());

        functionTableViewGraphic.setItems(list);
    }

    /**
     * Génère en parallèle les points des fonctions à afficher et leur applique une couleur
     */
    public void updateGraphDisplay(String functionUpdate, boolean delete) {
        graphDisplay.getData().clear();

        for (Express element : ExpressManager.getExpressGraphList()) {
            if (element.isActive()) {//pas de création de thread juste pour vérif
                new Thread(() -> {
                    double xMin = 0, xMax = 0;
                    synchronized (lock) {//un seul accès
                        xMin = xAxisLowerBound;
                        xMax = xAxisUpperBound;
                    }

                    double range = (xMax - xMin) / element.getSampling();
                    XYChart.Series<Number, Number> coords = new XYChart.Series<>();
                    coords.setName(element.getName());
                    for (double i = xMin; i < xMax + range; i += range) {
                        coords.getData().add(new XYChart.Data<>(i, Parser.eval(element.getFunction(), new Point("x", i)).getValue()));
                        //creating anonymous threads here will cause java.util.ConcurrentModificationException but it's really funny to break
                        /*double finalI = i;
                        new Thread(() -> {
                            XYChart.Data<Number, Number> result = new XYChart.Data<>(finalI, Parser.eval(expression.getFunction(), new Point("x", finalI)).getValue());
                            synchronized (coords) { coords.getData().add(result); }
                        }).start();*/
                    }

                    Platform.runLater(() -> {
                        graphDisplay.getData().add(coords);

                        // convert line color to CSS format and set line color on Series node
                        String lineStyle = "-fx-stroke: rgba("
                                + (int) (element.getColor().getRed() * 255) + ", "
                                + (int) (element.getColor().getGreen() * 255) + ", "
                                + (int) (element.getColor().getBlue() * 255) + ", 1.0);";
                        coords.getNode().lookup(".chart-series-line").setStyle(lineStyle);
                    });
                }).start();
            }
        }
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
    }
}
