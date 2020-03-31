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
        StageService.Holder.loadScene("homePage");
    }

    /**
     * callback bouton - charger la page principale
     */
    @FXML
    public void handleMainPageButton() {
        StageService.Holder.loadScene("workPage");
    }

    /**
     * callback bouton - charger la page de stockage
     */
    @FXML
    public void handleMemoryPageButton() {
        StageService.Holder.loadScene("memoryPage");
    }
}
