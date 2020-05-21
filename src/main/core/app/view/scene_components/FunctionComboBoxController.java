package core.app.view.scene_components;

import core.app.data.ExpressManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

public class FunctionComboBoxController {
    @FXML
    private ComboBox comboBox;

    public FunctionComboBoxController() {
        Holder.instanceList.add(this);
    }

    public String getValue() {
        return (comboBox.getValue() instanceof String) ? comboBox.getValue().toString() : "";
    }

    public String getFunction() {
        if (comboBox.getValue() instanceof String) {
            String userChoice = comboBox.getValue().toString();
            int secu = 0;
            while(userChoice != ExpressManager.refactorExpress(userChoice))
            {
                userChoice = ExpressManager.refactorExpress(userChoice);
                if(secu++ >= 50)
                {
                    System.out.println("Créer exception : récursivité au sein d'une fonction");
                    userChoice = "0";
                    break;
                }
            }
            return ExpressManager.containsExpress(userChoice) ? ExpressManager.getExpress(userChoice).getFunction() : userChoice;
        }
        else return "";
    }

    public static class Holder {
        private static ArrayList<FunctionComboBoxController> instanceList = new ArrayList<>();

        public static void updateList() {
            for (FunctionComboBoxController instance : instanceList) {
                Object selection = instance.comboBox.getValue();
                instance.comboBox.getItems().clear();
                instance.comboBox.getItems().addAll(ExpressManager.getExpressNames());
                instance.comboBox.setValue(selection);
            }
        }
    }
}
