package core.app.view.scene_components;

import core.app.data.ExpressManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;

/**
 * Contrôleur du composant functionComboBox.fxml
 */
public class FunctionComboBoxController {
    /**
     * Liste des instances : garde un accès pour mettre à jour chque instance
     */
    private static ArrayList<FunctionComboBoxController> instanceList = new ArrayList<>();

    /**
     * Element du fxml : choix de la fonction ou écriture libre
     */
    @FXML
    private ComboBox comboBox;

    /**
     * Constructeur de la classe : ajoute l'instance à la liste
     */
    public FunctionComboBoxController() {
        instanceList.add(this);
    }

    /**
     * Met à jour pour chaque instance la liste des fonctions dans la comboBox
     */
    public static void updateList() {
        for (FunctionComboBoxController instance : instanceList) {
            Object selection = instance.comboBox.getValue();
            instance.comboBox.getItems().clear();
            instance.comboBox.getItems().addAll(ExpressManager.getExpressNames());
            instance.comboBox.setValue(selection);
        }
    }

    /**
     * Getter sécurisé du texte inscrit dans la comboBox
     * @return
     */
    public String getValue() {
        return (comboBox.getValue() instanceof String) ? comboBox.getValue().toString() : "";
    }

    /**
     * Getter sécurisé de la fonction sélectionnée ou écrite dans la comboBox
     * @return
     */
    public String getFunction() {
        if (comboBox.getValue() instanceof String) {
            String userChoice = comboBox.getValue().toString();
            int secu = 0;
            while(userChoice != ExpressManager.refactorWithExpress(userChoice))
            {
                userChoice = ExpressManager.refactorWithExpress(userChoice);
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
}
