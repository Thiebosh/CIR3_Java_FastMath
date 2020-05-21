package core.app.view.scene_components;

import core.services.windowHolder.StageService;
import javafx.fxml.FXML;

/**
 * Contrôleur du composant menu.fxml
 */
public class MenuController {
    /**
     * onAction du fxml : charger la page d'accueil
     * @see StageService
     */
    @FXML
    public void handleHomePageButton() {
        StageService.Holder.loadMainWindowsScene("home");
    }

    /**
     * onAction du fxml : charger la page d'édition
     * @see StageService
     */
    @FXML
    public void handleComputePageButton() {
        StageService.Holder.loadMainWindowsScene("compute");
    }

    /**
     * onAction du fxml : charger la page de visualisation
     * @see StageService
     */
    @FXML
    public void handleGraphicPageButton() {
        StageService.Holder.loadMainWindowsScene("graphic");
    }
}
