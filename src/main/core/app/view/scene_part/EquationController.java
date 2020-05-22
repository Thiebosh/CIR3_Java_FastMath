package core.app.view.scene_part;

import core.app.data.ExpressManager;
import core.app.view.scene_components.FunctionComboBoxController;
import core.services.mathLibrary.equation.EquationX;
import core.services.mathLibrary.exception.CalculatorException;
import core.services.mathLibrary.parser.Parser;
import core.services.mathLibrary.parser.util.ParserResult;
import core.services.mathLibrary.parser.util.Point;
import core.services.mathLibrary.util.Round;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Contrôleur de l'élément de fenêtre equation.fxml
 */
public class EquationController {
    /**
     * Element du fxml : comboBox listant les fonctions - subController
     * @see FunctionComboBoxController
     */
    @FXML
    private FunctionComboBoxController functionComboEquation1Controller;

    /**
     * Element du fxml : comboBox listant les fonctions - subController
     * @see FunctionComboBoxController
     */
    @FXML
    private FunctionComboBoxController functionComboEquation2Controller;

    /**
     * Element du fxml : texte affichant le résultat
     */
    @FXML
    private Label resultEquation;

    /**
     * onAction du fxml : résoud l'équation et affiche le résultat
     */
    @FXML
    public void executeEquation() {
        String choiceA = functionComboEquation1Controller.getFunction();
        String choiceB = functionComboEquation2Controller.getFunction();

        Parser.setDegree(ExpressManager.getDegree());
        EquationX eq = new EquationX((choiceA + "=" + choiceB), ExpressManager.getDegree());
        Double res = 0.0;
        Double resB = 0.0;
        boolean resBExist = false;
        try {
            res = Round.rint(eq.getRoot(0.0, 0.1e-8), 8);
            if(res <= 1e-6 && res >= -1e-6)
                res = 0.0;
            //if(res > 10e4 || res < -10e4 || (res > -10e-4 && res < 10e-4 && res != 0)) {
            //if(Round.rint(res, 8) != res){
                boolean loop = false;
                int factor = 1;
                do {
                    loop = false;
                    try {
                        resB = Round.rint(eq.getRoot(-1 * factor, 1 * factor, 0.001, 0.1e-8), 8);
                        resBExist = true;
                    } catch (CalculatorException e) {
                        factor *= 2;
                        loop = true;
                        //e.printStackTrace();
                    }
                } while (loop && factor < 1024);
                if(resB <= 1e-6 && resB >= -1e-6)
                    resB = 0.0;
            //}

            if(resBExist)
                res = resB;

            Point value = new Point("x", res);
            ParserResult verif = Parser.eval(EquationX.simplify(choiceA+"="+choiceB), value);
            if(!verif.isComplex() && verif.getValue()>(resBExist?-1e-6:-1e-1) && verif.getValue()<(resBExist?1e-6:1e-1))
                resultEquation.setText(Double.toString(res));
            else
                resultEquation.setText("Résolution impossible");
        } catch (CalculatorException e) {
            e.printStackTrace();
        }
    }
}
