package core.view.components;

import core.app.FastMath;
import core.model.db.Express;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EvaluateController {
    FunctionChoiceBoxController functionChoiceBox = new FunctionChoiceBoxController();

    @FXML
    private TextField valueFunction;

    @FXML
    private Label resultFunction;

    @FXML
    private void executeEvaluation() {
        String choice = functionChoiceBox.getFunctionChoice();

        if (FastMath.getExpressNames().contains(choice)) {
            String function = "";
            for (Express expression : FastMath.getExpressList()) {
                if (expression.getName().equals(choice)) {
                    function = expression.getFunction();
                    break;
                }
            }
            double value = Double.parseDouble(valueFunction.getCharacters().toString());

            //old library
            //ParserResult result = Parser.eval(function, new Point("x", value));

            resultFunction.setText(/*result.getValue().toString()*/"résultat du calcul");
        }

        System.out.println("partie 2");
        //tmp
        //functionChoiceBox.getFunctionChoiceBox().getItems().setAll(FastMath.getExpressNames());//vieux refresh a l'arrache
        //functionChoiceBox.getFunctionChoiceBox().setValue(choice);//visualiser choix précédent, si fonction pas modifiée
    }
}
