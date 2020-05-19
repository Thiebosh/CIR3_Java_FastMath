package core.services.windowHolder;

import javafx.stage.Stage;

import java.util.HashMap;

public abstract class ContextControllerFactory<T> {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public abstract void setInitialValues(HashMap<String, Object> args);

    public void closeStage() {
        stage.close();
    }
}
