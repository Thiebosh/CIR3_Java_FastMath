package core.model.services;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StageService {
    //to test : pattern singleton plutot que classe interne
    private Stage currentStage;

    public static StageService getInstance() {
        return StageServiceHolder.INSTANCE;
    }

    private static class StageServiceHolder {
        private static final StageService INSTANCE = new StageService();
    }


    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(final Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void closeStage() {
        currentStage.fireEvent(new WindowEvent(currentStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
