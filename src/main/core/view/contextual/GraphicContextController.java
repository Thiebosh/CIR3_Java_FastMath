package core.view.contextual;

import core.model.services.ContextController;
import core.view.GraphicController;
import javafx.fxml.FXML;

import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.util.HashMap;

public class GraphicContextController extends ContextController {
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

    @Override
    public void setInitialValues(HashMap args) {
        try {
            textfieldXMin.appendText("" + ((double) args.get("xMin")));
            textfieldXMax.appendText("" + ((double) args.get("xMax")));
            textfieldYMin.appendText("" + ((double) args.get("yMin")));
            textfieldYMax.appendText("" + ((double) args.get("yMax")));

            spinnerScaleX.setPromptText("" + ((double) args.get("scaleX")));
            spinnerScaleY.setPromptText("" + ((double) args.get("scaleY")));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void validateButton() {
        GraphicController.setxAxisLowerBound(Double.parseDouble(textfieldXMin.getCharacters().toString()));
        GraphicController.setxAxisUpperBound(Double.parseDouble(textfieldXMax.getCharacters().toString()));
        GraphicController.setyAxisLowerBound(Double.parseDouble(textfieldYMin.getCharacters().toString()));
        GraphicController.setyAxisUpperBound(Double.parseDouble(textfieldYMax.getCharacters().toString()));


        GraphicController.setxAxisTickUnit(Double.parseDouble(spinnerScaleX.getPromptText()));
        GraphicController.setyAxisTickUnit(Double.parseDouble(spinnerScaleY.getPromptText()));

        GraphicController.requireGraphUpdate();

        closeStage();
    }
}
