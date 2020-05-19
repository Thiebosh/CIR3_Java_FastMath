package core.app.view.scene_components;

import core.app.data.ExpressManager;
import core.app.view.scene_part.EquationController;
import core.app.view.scene_part.EvaluateController;
import core.app.view.scene_part.TransformController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionChoiceBoxController implements Initializable {
    @FXML
    private ChoiceBox functionChoiceBox;

    private static BooleanProperty functionChoiceBoxUpdate = new SimpleBooleanProperty(false);

    public static void requireFunctionChoiceBoxUpdate() { functionChoiceBoxUpdate.setValue(true); }


    private static BooleanProperty functionChoiceUpdate = new SimpleBooleanProperty(false);

    public static void requireFunctionChoiceUpdate() { functionChoiceUpdate.setValue(true); }

    /**
     * Chargement initial des fonctions
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("call");

        //list auto-updater
        functionChoiceBoxUpdate.addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                functionChoiceBoxUpdate.setValue(false);

                Object selection = functionChoiceBox.getValue();
                functionChoiceBox.getItems().clear();
                functionChoiceBox.getItems().addAll(ExpressManager.getExpressNames());
                functionChoiceBox.setValue(selection);
            }
        });
        functionChoiceBox.setValue(true);//call listener


        //set result to controllers
        functionChoiceUpdate.addListener((observable, oldValue, newValue) -> {
            if (newValue==true) {
                functionChoiceUpdate.setValue(false);

                String choice = (String) functionChoiceBox.getValue();
                EvaluateController.setFunctionChoice(choice);
                EquationController.setFunctionChoice(choice);
                TransformController.setFunctionChoice(choice);
            }
        });
    }
}
