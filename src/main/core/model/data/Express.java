package core.model.data;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class Express {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty function = new SimpleStringProperty();

    private final BooleanProperty isActive = new SimpleBooleanProperty();
    private final IntegerProperty sampling = new SimpleIntegerProperty();//nbPoints
    private int samplingBefore;//limiter nombre de rafraichissement
    private Color color;

    public Express(String name, String function) {
        this.name.set(name);
        this.function.set(function);

        this.isActive.set(true);
        this.sampling.set(10);
        this.samplingBefore = 10;
        this.color = Color.BLACK;//==new Color(0,0,0,1);
    }

    public void setName(String name) {
        if (name.length() != 0) this.name.set(name);
        else System.out.println("afficher une erreur nom de fonction vide");
    }

    public void setFunction(String function) {
        if (function.length() != 0) this.function.set(function);
        else System.out.println("afficher une erreur expression de fonction vide");
    }

    public String getName() {
        return name.get();
    }

    public String getFunction() {
        return function.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty functionProperty() {
        return function;
    }

    public boolean isActive() {
        return isActive.get();
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public int getSampling() {
        return sampling.get();
    }

    public IntegerProperty samplingProperty() {
        return sampling;
    }

    public void setSampling(Integer sampling) { this.sampling.set(sampling); }

    public int getSamplingBefore() { return samplingBefore; }

    public void setSamplingBefore(int sampling) { this.samplingBefore = sampling; }

    public Color getColor() {
        return color;//Color.web(color.get());
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
