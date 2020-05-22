package core.app.view.scene_components;

import core.services.windowHolder.StageService;
import javafx.fxml.FXML;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    /**
     * onAction du fxml : ouvrir la documentation
     */
    @FXML
    public void openDoc() {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI("https://github.com/Thiebosh/FastMath/blob/master/README.md");
            desktop.browse(oURL);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
