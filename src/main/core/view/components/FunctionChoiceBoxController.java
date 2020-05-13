package core.view.components;

import core.model.data.ExpressManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionChoiceBoxController implements Initializable {
    @FXML
    private ChoiceBox functionChoiceBox;

    /**
     * Chargement initial des fonctions
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        functionChoiceBox.getItems().addAll(ExpressManager.getExpressNames());
        functionChoiceBox.setValue("f");
        //tester ça pour un rafraichissement automatique : https://stackoverflow.com/questions/21854146/javafx-2-0-choice-box-issue-how-to-update-a-choicebox-which-represents-a-list
    }

    public String getFunctionChoice() {
        System.out.println("entrée dans getFunctionChoice");

        ChoiceBox test1 = functionChoiceBox;
        System.out.println("choice box acquis");

        Object test2 = functionChoiceBox.getValue();
        System.out.println("object acquis");

        String choice = (String) functionChoiceBox.getValue();
        System.out.println("String acquis : "+choice);

        return choice;
    }
}
