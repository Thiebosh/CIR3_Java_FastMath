package core.app.view.scene_components;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


/**
 * Contrôleur du composant HBox personnalisé
 */
public class ToggleSwitch extends HBox {
    /**
     * Constructeur de la classe : met en place le listener
     */
    public ToggleSwitch() {
        label.setText("Degrés");
        getChildren().addAll(label, button);
        setStyle();
        bindProperties();

        switchedOn.addListener((a,b,c) -> {
            if (!c) {
                label.setText("Radians");
                setStyle("-fx-background-color: lightgreen; -fx-background-radius: 4;");
                label.toFront();
            }
            else {
                label.setText("Degrés");
                setStyle("-fx-background-color: lightblue; -fx-background-radius: 4;");
                button.toFront();
            }
        });
    }
    /**
     * Element de construction : style
     */
    private void setStyle() {
        //Default Width
        setWidth(90);
        label.setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: lightblue; -fx-text-fill:black; -fx-background-radius: 4;");
        setAlignment(Pos.CENTER_LEFT);
    }
    /**
     * Element de construction : mise à jour des données
     */
    private void bindProperties() {
        label.prefWidthProperty().bind(widthProperty().divide(2));
        label.prefHeightProperty().bind(heightProperty());
        button.prefWidthProperty().bind(widthProperty().divide(2));
        button.prefHeightProperty().bind(heightProperty());
    }


    /**
     * Element fxml texte
     */
    private final Label label = new Label();
    /**
     * getter du label
     * @return élément fxml
     */
    public Label getLabel() { return label; }

    /**
     * element fxml bouton
     */
    private final Button button = new Button();
    /**
     * getter du button
     * @return élément fxml
     */
    public Button getButton() { return button; }

    /**
     * Détecteur de clic (booléen)
     */
    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);
    /**
     * Getter de l'état du clic (activé ou non)
     * @return booléen changé ou non
     */
    public SimpleBooleanProperty switchedOnProperty() { return switchedOn; }
}