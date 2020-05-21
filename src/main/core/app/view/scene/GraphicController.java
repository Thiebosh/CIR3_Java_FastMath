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


/**
 * Contrôleur de la page de visualisation graphic.fxml
 */
public class GraphicController implements Initializable {
    /**
     * Element du fxml : affiche les courbes
     */
    @FXML
    private LineChart graphDisplay;

    /**
     * axe x : valeur minimale
     */
    private static Double xAxisLowerBound = -10.0;
    /**
     * axe x : valeur maximale
     */
    private static Double xAxisUpperBound = 10.0;
    /**
     * axe y : valeur minimale
     */
    private static Double yAxisLowerBound = -10.0;
    /**
     * axe y : valeur maximale
     */
    private static Double yAxisUpperBound = 10.0;

    /**
     * axe x : valeur de graduation
     */
    private static Double xAxisTickUnit = 5.0;
    /**
     * axe y : valeur de graduation
     */
    private static Double yAxisTickUnit = 5.0;

    /**
     * verrou thread-safe : synchronisation sur cet element uniquement
     */
    private Object lock = new Object();

    /**
     * Element du fxml : liste les expressions enregistrées
     */
    @FXML
    private ChoiceBox functionChoiceBox;
    /**
     * Elément d'affichage pour le functionChoiceBox
     */
    private static final String SEPARATOR = " : ";

    /**
     * Element du fxml : affiche les expressions utilisées pour le graphe
     * @see Express
     */
    @FXML
    private TableView<Express> functionTableViewGraphic;
    /**
     * Element du fxml (colonne de functionTableViewGraphic) : affiche le nom, le séparateur et l'expression
     */
    @FXML
    private TableColumn<Express, String> expressCol;
    /**
     * Element du fxml (colonne de functionTableViewGraphic) : affiche l'état (actif ou non) sous forme de checkbox
     */
    @FXML
    private TableColumn<Express, Boolean> stateCol;
    /**
     * Element du fxml (colonne de functionTableViewGraphic) : affiche le sampling (nb de points) sous forme d'un slider
     */
    @FXML
    private TableColumn<Express, Integer> samplingCol;
    /**
     * Nombre de points maximum par courbe
     */
    private static final double SAMPLING_MAX = 1000;
    /**
     * Element du fxml (colonne de functionTableViewGraphic) : affiche la couleur sous forme d'un colorPicker
     */
    @FXML
    private TableColumn<Express, Color> colorCol;

    /**
     * Instance courante pour garder un accès
     */
    private static GraphicController instance;

    /**
     * Constructeur de l'instance : enregistre un accès à l'instance courante
     */
    public GraphicController() { GraphicController.instance = this; }

    /**
     * Setter statique de la valeur minimale de l'axe x
     * @param xAxisLowerBound valeur à appliquer
     */
    public static void setXAxisLowerBound(final double xAxisLowerBound) {
        GraphicController.xAxisLowerBound = xAxisLowerBound;
    }

    /**
     * Setter statique de la valeur maximale de l'axe x
     * @param xAxisUpperBound valeur à appliquer
     */
    public static void setXAxisUpperBound(final double xAxisUpperBound) {
        GraphicController.xAxisUpperBound = xAxisUpperBound;
    }

    /**
     * Setter statique de la valeur minimale de l'axe y
     * @param yAxisLowerBound valeur à appliquer
     */
    public static void setYAxisLowerBound(final double yAxisLowerBound) {
        GraphicController.yAxisLowerBound = yAxisLowerBound;
    }

    /**
     * Setter statique de la valeur maximale de l'axe y
     * @param yAxisUpperBound valeur à appliquer
     */
    public static void setYAxisUpperBound(final double yAxisUpperBound) {
        GraphicController.yAxisUpperBound = yAxisUpperBound;
    }

    /**
     * Setter statique de la graduation de l'axe x
     * @param xAxisTickUnit valeur à appliquer
     */
    public static void setXAxisTickUnit(final double xAxisTickUnit) {
        GraphicController.xAxisTickUnit = xAxisTickUnit;
    }

    /**
     * Setter statique de la graduation de l'axe y
     * @param yAxisTickUnit valeur à appliquer
     */
    public static void setYAxisTickUnit(final double yAxisTickUnit) {
        GraphicController.yAxisTickUnit = yAxisTickUnit;
    }

    /**
     * Chargement initial (après le constructeur) du fxml lié au contrôleur  : prépration des éléments du fxml
     * @param location paramètre par défaut
     * @param resources paramètre par défaut
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Remplissage du ChoiceBox
        {
            ArrayList<String> list = new ArrayList<>();
            for (Express current : ExpressManager.getExpressList())
                list.add(current.getName() + SEPARATOR + current.getFunction());
            functionChoiceBox.getItems().addAll(list);
        }

        //Préparation des axes du graphe
        {
            graphDisplay.getXAxis().setAutoRanging(false);
            graphDisplay.getYAxis().setAutoRanging(false);
            GraphicController.updateGraphAxis();
        }

        initializeGraphTableView();
    }

    /**
     * Lie les instance de Express aux colonnes de TableViews + injection de données
     * @see Express
     */
    private void initializeGraphTableView() {
        //part 1 : link to data
        {
            expressCol.setCellValueFactory(cellData -> Bindings.createStringBinding(
                    () -> cellData.getValue().getName() + " : " + cellData.getValue().getFunction(),
                    cellData.getValue().nameProperty(),
                    cellData.getValue().functionProperty()
            ));
            stateCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
            samplingCol.setCellValueFactory(new PropertyValueFactory<>("sampling"));
            colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        }

        //part 2 : update data
        {
            //cas 1 : intialisation mais update ponctuelle (met pas à jour toutes les cases de la même fonction)
            stateCol.setCellFactory(CheckBoxTableCell.forTableColumn(stateCol));
            stateCol.setCellValueFactory((TableColumn.CellDataFeatures<Express, Boolean> p) -> {
                final Express express = p.getValue();
                final BooleanProperty result = new SimpleBooleanProperty(express.isActive());
                result.addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
                    express.setIsActive(newValue);
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
        }

        //Part 3 : injection des données
        refreshTableViewGraphic();
    }

    /**
     * onAction du fxml : ajoute fonction sélectionnée au TableView et l'affiche dans le graphe
     */
    @FXML
    private void addFunctionToTable() {
        String express = (String) functionChoiceBox.getValue();
        ExpressManager.addToGraphList(express.substring(0, express.indexOf(SEPARATOR)));//creation
        refreshTableViewGraphic();//visibilité
        updateGraphDisplay();
    }

    /**
     * Applique la liste de fonctions au TableView + mise en place derniers liens instance - colonne
     * @see Express
     */
    private void refreshTableViewGraphic() {
        samplingCol.setCellFactory(SliderTableCell.forTableColumn(2,(int) SAMPLING_MAX));

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
                        updateGraphDisplay();//consequence
                    }
                }
            }
        });

        //fill list
        list.addAll(ExpressManager.getExpressGraphList());

        functionTableViewGraphic.setItems(list);
    }

    /**
     * Génération parallèle des points des fonctions à afficher puis application de la couleur associée
     */
    public void updateGraphDisplay() {
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

    /**
     * onAction du fxml : ouvre une fenêtre contextuelle pour modifier les valeurs des axes du graphe
     */
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

    /**
     * Modifie les valeurs des axes et actualise l'afichage du graphe
     */
    public static void updateGraphAxis() {
        ((NumberAxis) instance.graphDisplay.getXAxis()).setLowerBound(xAxisLowerBound);
        ((NumberAxis) instance.graphDisplay.getXAxis()).setUpperBound(xAxisUpperBound);
        ((NumberAxis) instance.graphDisplay.getXAxis()).setTickUnit(xAxisTickUnit);//distance between two graduation

        ((NumberAxis) instance.graphDisplay.getYAxis()).setLowerBound(yAxisLowerBound);
        ((NumberAxis) instance.graphDisplay.getYAxis()).setUpperBound(yAxisUpperBound);
        ((NumberAxis) instance.graphDisplay.getYAxis()).setTickUnit(yAxisTickUnit);//distance between two graduation

        instance.updateGraphDisplay();//recalculate all function points
    }
}
