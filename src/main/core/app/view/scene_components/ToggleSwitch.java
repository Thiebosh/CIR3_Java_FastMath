package core.app.view.scene_components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class ToggleSwitch extends HBox {

    private final Label label = new Label();
    private final Button button = new Button();

    public Label getLabel() { return label; }

    public Button getButton() { return button; }

    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);
    public SimpleBooleanProperty switchedOnProperty() { return switchedOn; }

    private void init() {

        label.setText("Degrés");

        getChildren().addAll(label, button);
        /*button.setOnAction((e) -> {
            switchedOn.set(!switchedOn.get());
        });
        label.setOnMouseClicked((e) -> {
            switchedOn.set(!switchedOn.get());
        });*/
        setStyle();
        bindProperties();
    }

    private void setStyle() {
        //Default Width
        setWidth(80);
        label.setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 4;");
        setAlignment(Pos.CENTER_LEFT);
    }

    private void bindProperties() {
        label.prefWidthProperty().bind(widthProperty().divide(2));
        label.prefHeightProperty().bind(heightProperty());
        button.prefWidthProperty().bind(widthProperty().divide(2));
        button.prefHeightProperty().bind(heightProperty());
    }

    public ToggleSwitch() {
        init();
        switchedOn.addListener((a,b,c) -> {
            if (!c) {
                label.setText("Radians");
                setStyle("-fx-background-color: green;");
                label.toFront();
            }
            else {
                label.setText("Degrés");
                setStyle("-fx-background-color: grey;");
                button.toFront();
            }
        });
    }
}