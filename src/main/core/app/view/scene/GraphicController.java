package core.app.view.scene;

import core.app.data.Express;
import core.app.data.ExpressManager;
import core.app.view.scene_components.ToggleSwitch;
import core.services.javafxCustom.SpinnerTableCell;
import core.services.mathLibrary.derivative.DerivativeX;
import core.services.mathLibrary.integral.Integral;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.ParserResult;
import core.services.mathLibrary.parser.util.Point;
import core.services.mathLibrary.util.Round;
import core.services.windowHolder.StageService;
import core.app.view.scene_contextual.GraphicContextController;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Contrôleur de la page de visualisation graphic.fxml
 */
public class GraphicController implements Initializable {
    /**
     * Instance courante pour garder un accès
     */
    private static GraphicController instance;

    /**
     * Element du fxml : affiche les courbes
     */
    @FXML
    private LineChart graphDisplay;

    /**
     * axe x : valeur minimale
     */
    private static Double xAxisLowerBound = -20.0;
    /**
     * axe x : valeur maximale
     */
    private static Double xAxisUpperBound = 20.0;
    /**
     * Nombre de points maximum par courbe
     */
    private static int samplingMax;
    /**
     * axe y : valeur minimale
     */
    private static Double yAxisLowerBound = -20.0;
    /**
     * axe y : valeur maximale
     */
    private static Double yAxisUpperBound = 20.0;

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

    @FXML
    private TableColumn<Express, Integer> derivateCol;
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
     * Element du fxml (colonne de functionTableViewGraphic) : affiche la couleur sous forme d'un colorPicker
     */
    @FXML
    private TableColumn<Express, Color> colorCol;

    /**
     * Element du fxml (bouton toggle) : affiche le mode (degrés ou radians)
     */
    @FXML
    private HBox toggleSwitchLocation;
    private ToggleSwitch toggleSwitch = new ToggleSwitch();

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
        //place toggleSwitch and configure it
        {
            toggleSwitchLocation.getChildren().add(toggleSwitch);
            toggleSwitch.getButton().setOnMouseClicked(mouseEvent -> {
                toggleSwitch.switchedOnProperty().set(!toggleSwitch.switchedOnProperty().get());
                ExpressManager.setDegree(toggleSwitch.switchedOnProperty().getValue());
                updateGraphDisplay();
            });
            toggleSwitch.getLabel().setOnMouseClicked(toggleSwitch.getButton().getOnMouseClicked());
        }

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
            derivateCol.setCellValueFactory(new PropertyValueFactory<>("degree"));
            stateCol.setCellValueFactory(new PropertyValueFactory<>("isActive"));
            samplingCol.setCellValueFactory(new PropertyValueFactory<>("sampling"));
            colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        }

        //part 2 : update data
        {
            //derivateCol.setCellFactory(SpinnerValueFactory);//TextFieldTableCell.<Integer>forTableColumn());

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

            derivateCol.setCellFactory(SpinnerTableCell.forTableColumn(-1,4,1));
            derivateCol.setOnEditCommit((TableColumn.CellEditEvent<Express, Integer> event) -> {
                TablePosition<Express, Integer> pos = event.getTablePosition();
                Express express = event.getTableView().getItems().get(pos.getRow());
                express.setDegree(event.getNewValue());
                updateGraphDisplay();//conséquence
            });

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
        samplingCol.setCellFactory(SliderTableCell.forTableColumn(2, samplingMax));
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

        SliderTableCell.setMaxValue(samplingMax);
        functionTableViewGraphic.setItems(list);
        functionTableViewGraphic.refresh();
    }

    /**
     * Génération parallèle des points des fonctions à afficher puis application de la couleur associée
     */
    public void updateGraphDisplay() {
        graphDisplay.getData().clear();
        Parser.setDegree(ExpressManager.getDegree());

        for (Express element : ExpressManager.getExpressGraphList()) {
            if (element.isActive()) {//pas de création de thread juste pour vérif
                new Thread(() -> {
                    double xMin = 0, xMax = 0;
                    synchronized (lock) {//un seul accès aux éléments externe du thread
                        xMin = xAxisLowerBound;
                        xMax = xAxisUpperBound;
                    }

                    double range = (xMax - xMin) / element.getSampling();
                    XYChart.Series<Number, Number> coords = new XYChart.Series<>();
                    coords.setName(element.getName());

                    for (double i = xMin; i < xMax + range; i += range) {
                        final String function = ExpressManager.replaceExpressNameByFunctionRecursively(element.getFunction());
                        Double result = 0.0;
                        Double y = i;
                        ParserResult val;
                        try {
                            DerivativeX deriv = new DerivativeX(function);
                            switch(element.getDegree()) {
                                case 0:
                                    val = Parser.eval(function, new Point("x", i));
                                    if(val.isComplex()){
                                        y = val.getComplexValue().getI();
                                        result = val.getComplexValue().getR();
                                    }
                                    else
                                        result = val.getValue();
                                    break;
                                case 1:
                                    result = Round.rint(deriv.getDerivative_xo_accurate(i), 8);
                                    break;
                                case 2:
                                    result = Round.rint(deriv.getDerivativeOrderTwo_xo_accurate(i), 8);
                                    break;
                                case 3:
                                    result = Round.rint(deriv.getDerivativeOrderThree_xo_accurate(i), 8);
                                    break;
                                case 4:
                                    result = Round.rint(deriv.getDerivativeOrderFour_xo_accurate(i), 8);
                                    break;
                                default://-1
                                    Integral integral = new Integral(function);
                                    if(i>0)
                                        result = Round.rint(integral.simpson(0, i), 8);
                                    else
                                        result = -Round.rint(integral.simpson(i, 0), 8);
                                    break;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        coords.getData().add(new XYChart.Data<>(y, result));

                        //creating anonymous threads here will cause java.util.ConcurrentModificationException but it's really funny to break
                        /*double finalI = i;
                        new Thread(() -> {
                            XYChart.Data<Number, Number> result = new XYChart.Data<>(finalI, Parser.eval(ExpressManager.replaceExpressNameByFunctionRecursively(element.getFunction()), new Point("x", finalI)).getValue());
                            synchronized (underlock) {
                                coords.getData().add(result);
                            }
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
        StageService.Holder.openContextWindows("Propriétés","graphicContext", Modality.APPLICATION_MODAL, new GraphicContextController(), arguments);//pas d'accès aux autres fenêtres
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

        computeSamplingMax();

        instance.refreshTableViewGraphic();//update sliders
        instance.updateGraphDisplay();//recalculate all function points
    }

    /**
     * Calcule le nombre de points max selon les valeurs extrêmes de l'axe x
     */
    private static void computeSamplingMax() {
        samplingMax = (xAxisUpperBound-xAxisLowerBound > 2) ? (int)(10*Math.log(50*Math.pow(xAxisUpperBound-xAxisLowerBound,5))) : 50;
    }
}
