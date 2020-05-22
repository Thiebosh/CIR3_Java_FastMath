package core.services.windowHolder;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;

public class ErrorController extends ContextControllerFactory {
    @FXML
    private AnchorPane messageLocation;

    @Override
    public void setInitialValues(HashMap args) {
        Label message = new Label();
        message.setText("\t"+args.get("error")+"\t");
        messageLocation.getChildren().add(message);
    }

    @FXML
    private void okButton() {
        closeStage();
    }
}
