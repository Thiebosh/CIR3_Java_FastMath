package core.view;

import core.model.services.StageService;
import javafx.fxml.FXML;

/**
 * Contrôleur du fichier menu.fxml
 */
public class MenuController {
    /**
     * callback bouton - charger la page d'accueil
     */
    @FXML
    public void handleHomePageButton() {
        StageService.Holder.loadScene("home");
    }

    /**
     * callback bouton - charger la page d'édition
     */
    @FXML
    public void handleEditPageButton() {
        StageService.Holder.loadScene("edit");
    }

    /**
     * callback bouton - charger la page de visualisation
     */
    @FXML
    public void handleVisualPageButton() {
        StageService.Holder.loadScene("visual");
    }
}
