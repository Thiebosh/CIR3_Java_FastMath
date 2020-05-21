package core.app.view.scene_contextual;

import core.services.mathLibrary.function.FunctionX;
import core.services.windowHolder.ContextControllerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.util.HashMap;


public class ComputeContextController extends ContextControllerFactory {
    /**
     * Liste contenant les token utilisés par la bibliothèque mathématique
     */
    private static final ObservableList<String> WRITING_CONVENTION = FXCollections.observableArrayList(
            "Symboles : + - * /",
            "Pi : "+FunctionX.getPI(),
            "",
            "Puissance : ^()",
            "Racine : "+FunctionX.getSQRT()+"()",
            "Racine cubique : "+FunctionX.getCBRT()+"()",
            "",
            "Exponentielle : "+FunctionX.getE()+"^()",
            "Logarithme népérien : "+FunctionX.getLN()+"()",
            "Logarithme base 10 : "+FunctionX.getLOG()+"()",
            "",
            "Cosinus : "+FunctionX.getCOS()+"()",
            "Sinus : "+FunctionX.getSIN()+"()",
            "Tangente : "+FunctionX.getTAN()+"()",
            "",
            "Cosinus inverse : "+FunctionX.getACOS()+"()",
            "Sinus inverse : "+FunctionX.getASIN()+"()",
            "Tangente inverse : "+FunctionX.getATAN()+"()",
            "",
            "Cosinus hyperbolique : "+FunctionX.getCOSH()+"()",
            "Sinus hyperbolique : "+FunctionX.getSINH()+"()",
            "Tangente hyperbolique : "+FunctionX.getTANH()+"()"
    );

    /**
     * liste des écritures des fonctions de base
     */
    @FXML
    private ListView listViewBaseFunction;

    /**
     * Remplissage des éléments fxml avec les valeurs initiales, passées en paramètre
     * @param args map clé valeur contenant toutes les données initiales
     * @see ContextControllerFactory
     */
    @Override
    public void setInitialValues(final HashMap args) {
        listViewBaseFunction.setItems(WRITING_CONVENTION);
    }
}