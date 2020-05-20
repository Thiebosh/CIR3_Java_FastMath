package core.app.view.scene_components;

import core.app.data.ExpressManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class FunctionChoiceBoxController {
    @FXML
    private ChoiceBox choiceBox;

    public FunctionChoiceBoxController() {
        Holder.instanceList.add(this);
    }

    public Object getValue() {
        return choiceBox.getValue();
    }

    public static class Holder {
        private static ArrayList<FunctionChoiceBoxController> instanceList = new ArrayList<>();

        public static void updateList() {
            for (FunctionChoiceBoxController instance : instanceList) {
                Object selection = instance.choiceBox.getValue();
                instance.choiceBox.getItems().clear();
                instance.choiceBox.getItems().addAll(ExpressManager.getExpressNames());
                instance.choiceBox.setValue(selection);
            }
        }
    }
}
