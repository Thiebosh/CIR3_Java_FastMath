package core.view;

import core.model.services.StageService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuController {
    @FXML
    private static VBox screenAnchorPane;

    private void switchPage(final String pageName) {
        final FXMLLoader loader = new  FXMLLoader();
        loader.setLocation(getClass().getResource("/view/"+pageName+".fxml"));
        try {
            screenAnchorPane = loader.load();
            StageService.getInstance().getCurrentStage().setScene(new Scene(screenAnchorPane));
            StageService.getInstance().getCurrentStage().show();

        }
        catch (IOException e ) {
            e.printStackTrace();// projet maven puis https://www.baeldung.com/logback
        }
    }

    @FXML
    public void handleHomePageButton() {
        switchPage("homePage");
    }

    @FXML
    public void handleEditPageButton() {
        switchPage("editPage");
    }

    @FXML
    public void handleVisualPageButton() {
        switchPage("visualPage");
    }
}
