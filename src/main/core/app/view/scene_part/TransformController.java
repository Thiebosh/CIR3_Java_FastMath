package core.app.view.scene_part;

import core.app.view.scene_components.FunctionComboBoxController;
import javafx.fxml.FXML;

public class TransformController {
    /**
     * Element du fxml : comboBox listant les fonctions - subController
     * @see FunctionComboBoxController
     */
    @FXML
    private FunctionComboBoxController functionComboTransformController;

    /**
     * onAction du fxml : effectue la transformation et affiche le résultat
     */
    @FXML
    private void action() {
        String functionChoice = functionComboTransformController.getFunction();
    }
}
