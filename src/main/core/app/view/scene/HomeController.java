package core.app.view.scene;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private AnchorPane container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("video.mp4").toExternalForm()));
        MediaView mediaView = new MediaView(player);

        container.getChildren().add(mediaView);

         */
    }
}
