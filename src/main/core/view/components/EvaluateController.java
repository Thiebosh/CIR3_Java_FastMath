package core.view.components;

import core.model.data.Express;
import core.model.data.ExpressManager;
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
        functionChoiceBox.setValue(functionChoiceBox.getItems().get(0));
        //tester ça pour un rafraichissement automatique : https://stackoverflow.com/questions/21854146/javafx-2-0-choice-box-issue-how-to-update-a-choicebox-which-represents-a-list
    }

    @FXML
    private void executeEvaluation() {
        String choice = (String) functionChoiceBox.getValue();

        if (ExpressManager.containsExpress(choice)) {
            String function = ExpressManager.getExpress(choice).getFunction();
            Point value = new Point("x", Double.parseDouble((String) valueFunction.getCharacters()));

            resultFunction.setText(Double.toString(Parser.eval(function, value).getValue()));
        }

        //tmp
        functionChoiceBox.getItems().setAll(ExpressManager.getExpressNames());//vieux refresh a l'arrache
        functionChoiceBox.setValue(choice);//visualiser choix précédent, si fonction pas modifiée
    }
}
