package core.services.windowHolder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.HashMap;

public class ErrorController extends ContextControllerFactory {
    @FXML
    private Label message;

    private Stage stage;

    @Override
    public void setInitialValues(HashMap args) {
        message.setText((String) args.get("error"));
        stage = (Stage) args.get("scene");
    }

    @FXML
    private void okButton() {
        closeStage();
    }
}
