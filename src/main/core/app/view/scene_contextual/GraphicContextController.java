package core.app.view.scene_contextual;

import core.services.windowHolder.ContextControllerFactory;
import core.app.view.scene.GraphicController;
import javafx.fxml.FXML;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.util.HashMap;

/**
 * Contrôleur de la fenêtre contextuelle graphicContext.fxml
 */
public class GraphicContextController extends ContextControllerFactory {
    /**
     * Element du fxml : texte de la valeur min de l'axe x
     */
    @FXML
    private TextField textfieldXMin;
    /**
     * Element du fxml : texte de la valeur max de l'axe x
     */
    @FXML
    private TextField textfieldXMax;
    /**
     * Element du fxml : texte de la valeur min de l'axe y
     */
    @FXML
    private TextField textfieldYMin;
    /**
     * Element du fxml : texte de la valeur max de l'axe y
     */
    @FXML
    private TextField textfieldYMax;

    /**
     * Element du fxml : spinner de la graduation de l'axe x
     */
    @FXML
    private Spinner<Double> spinnerScaleX;
    /**
     * Element du fxml : spinner de la graduation de l'axe y
     */
    @FXML
    private Spinner<Double> spinnerScaleY;

    /**
     * Valeur min des graduations
     */
    private final static Double SCALE_MIN = 0.01;
    /**
     * Valeur max des graduations
     */
    private final static Double SCALE_MAX = 20.0;
    /**
     * Pas des graduations
     */
    private final static Double SCALE_STEP = 0.5;

    /**
     * Remplissage des éléments fxml avec les valeurs initiales, passées en paramètre
     * @param args map clé valeur contenant toutes les données initiales
     * @see ContextControllerFactory
     */
    @Override
    public void setInitialValues(final HashMap args) {
        try {
            textfieldXMin.appendText("" + ((double) args.get("xMin")));
            textfieldXMax.appendText("" + ((double) args.get("xMax")));
            textfieldYMin.appendText("" + ((double) args.get("yMin")));
            textfieldYMax.appendText("" + ((double) args.get("yMax")));

            spinnerScaleX.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(SCALE_MIN, SCALE_MAX, (double) args.get("scaleX"), SCALE_STEP));
            spinnerScaleX.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            spinnerScaleY.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(SCALE_MIN, SCALE_MAX, (double) args.get("scaleY"), SCALE_STEP));
            spinnerScaleY.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * onAction du fxml : fermeture de la fenêtre contextuelle et répercussion des valeurs
     */
    @FXML
    private void validateButton() {
        double xMin=0, xMax=0, yMin=0, yMax=0, scaleX=0, scaleY=0;
        try {
            xMin = Double.parseDouble(textfieldXMin.getCharacters().toString().replace(',','.'));
            xMax = Double.parseDouble(textfieldXMax.getCharacters().toString().replace(',','.'));
            yMin = Double.parseDouble(textfieldYMin.getCharacters().toString().replace(',','.'));
            yMax = Double.parseDouble(textfieldYMax.getCharacters().toString().replace(',','.'));

            scaleX = Double.parseDouble(spinnerScaleX.getEditor().getText().replace(',','.'));
            scaleY = Double.parseDouble(spinnerScaleY.getEditor().getText().replace(',','.'));
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }


        if (xMin < xMax) {
            GraphicController.setXAxisLowerBound(xMin);
            GraphicController.setXAxisUpperBound(xMax);
        }
        if (yMin < yMax) {
            GraphicController.setYAxisLowerBound(yMin);
            GraphicController.setYAxisUpperBound(yMax);
        }

        if (scaleX > 0) GraphicController.setXAxisTickUnit(scaleX);
        if (scaleY > 0) GraphicController.setYAxisTickUnit(scaleY);

        GraphicController.updateGraphAxis();

        closeStage();
    }
}
