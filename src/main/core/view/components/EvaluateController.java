package core.view.components;

import core.app.FastMath;
import core.model.db.Express;
import core.model.db.ExpressManager;
import core.model.mathlibrary.parser.Parser;
import core.model.mathlibrary.parser.util.ParserResult;
import core.model.mathlibrary.parser.util.Point;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EvaluateController implements Initializable {
    @FXML
    private ChoiceBox functionChoiceBox;

    @FXML
    private TextField valueFunction;

    @FXML
    private Label resultFunction;

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

    @FXML
    private void executeEvaluation() {
        String choice = (String) functionChoiceBox.getValue();

        if (ExpressManager.getExpressNames().contains(choice)) {
            String function = "";
            for (Express expression : ExpressManager.getExpressList()) {
                if (expression.getName().equals(choice)) {
                    function = expression.getFunction();
                    break;
                }
            }
            double value = Double.parseDouble(valueFunction.getCharacters().toString());

            ParserResult result = Parser.eval(function, new Point("x", value));

            resultFunction.setText(result.getValue().toString());
        }

        //tmp
        functionChoiceBox.getItems().setAll(ExpressManager.getExpressNames());//vieux refresh a l'arrache
        functionChoiceBox.setValue(choice);//visualiser choix précédent, si fonction pas modifiée
    }
}
