package core.view.components;

import core.model.data.Express;
import core.model.data.ExpressManager;
import core.model.mathlibrary.equation.EquationX;
import core.model.mathlibrary.exception.CalculatorException;
import core.model.mathlibrary.util.Round;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EquationController  implements Initializable {
    @FXML
    private ChoiceBox functionChoiceBox;

    @FXML
    private ComboBox functionComboBox;

    @FXML
    private Label resultEquation;

    /**
     * Chargement initial des fonctions
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        functionChoiceBox.getItems().addAll(ExpressManager.getExpressNames());
        functionChoiceBox.setValue("f");

        functionComboBox.getItems().addAll(ExpressManager.getExpressNames());
        //tester ça pour un rafraichissement automatique : https://stackoverflow.com/questions/21854146/javafx-2-0-choice-box-issue-how-to-update-a-choicebox-which-represents-a-list
    }

    public void executeEquation(ActionEvent actionEvent) {
        String choiceA = (String) functionChoiceBox.getValue();
        String choiceB = (String) functionComboBox.getValue();

        if (ExpressManager.getExpressNames().contains(choiceA)) {
            for (Express expression : ExpressManager.getExpressList()) {
                if (expression.getName().equals(choiceA)) {
                    choiceA = expression.getFunction();
                    break;
                }
            }
        }
        if(ExpressManager.getExpressNames().contains(choiceB))
        {
            for (Express expression : ExpressManager.getExpressList()) {
                if (expression.getName().equals(choiceB)) {
                    choiceB = expression.getFunction();
                    break;
                }
            }
        }
        EquationX eq = new EquationX((choiceA + "=" + choiceB));

        try {
            resultEquation.setText(Double.toString(Round.rint(eq.getRoot(0,0.1e-10), 6)));
        } catch (CalculatorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
