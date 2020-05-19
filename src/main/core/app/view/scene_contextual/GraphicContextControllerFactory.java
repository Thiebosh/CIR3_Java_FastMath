package core.app.view.scene_contextual;

import core.services.windowHolder.ContextControllerFactory;
import core.app.view.scene.GraphicController;
import javafx.fxml.FXML;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class GraphicContextControllerFactory extends ContextControllerFactory {
    @FXML
    private TextField textfieldXMin;

    @FXML
    private TextField textfieldXMax;

    @FXML
    private TextField textfieldYMin;

    @FXML
    private TextField textfieldYMax;

    @FXML
    private Spinner<Double> spinnerScaleX;

    @FXML
    private Spinner<Double> spinnerScaleY;

    private final static Double SCALE_MIN = 0.01;
    private final static Double SCALE_MAX = 20.0;
    private final static Double SCALE_STEP = 0.5;

    @Override
    public void setInitialValues(HashMap args) {
        try {
            textfieldXMin.appendText(Double.toString((double) args.get("xMin")));
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

    @FXML
    private void validateButton() {
        double xMin = Double.parseDouble(textfieldXMin.getCharacters().toString()),
                xMax = Double.parseDouble(textfieldXMax.getCharacters().toString()),
                yMin = Double.parseDouble(textfieldYMin.getCharacters().toString()),
                yMax = Double.parseDouble(textfieldYMax.getCharacters().toString());

        if (xMin < xMax) {
            GraphicController.setXAxisLowerBound(xMin);
            GraphicController.setXAxisUpperBound(xMax);
        }
        if (yMin < yMax) {
            GraphicController.setYAxisLowerBound(yMin);
            GraphicController.setYAxisUpperBound(yMax);
        }

        GraphicController.setXAxisTickUnit(Double.parseDouble(spinnerScaleX.getEditor().getText().replace(',','.')));
        GraphicController.setYAxisTickUnit(Double.parseDouble(spinnerScaleY.getEditor().getText().replace(',','.')));

        GraphicController.requireGraphUpdate();

        closeStage();
    }
}
