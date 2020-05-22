package core.app.data;

import core.app.error.ErrorCode;
import core.services.windowHolder.StageService;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

/**
 * Entité caractérisant la donnée "expression mathématique"
 */
public class Express {
    /**
     * Constructeur de la classe, applique des paramètres par défaut pour l'affichage graphique
     * @param name le nom désignant l'expression
     * @param function la fonction associée au nom
     */
    public Express(final String name, final String function) {
        this.name.set(name);
        this.function.set(function);

        this.isActive.set(true);
        this.sampling.set(10);
        this.samplingBefore = 10;
        this.color = Color.BLACK;//==new Color(0,0,0,1);
    }


    /**
     * l'identifiant de l'expression
     */
    private final StringProperty name = new SimpleStringProperty();
    /**
     * getter de la StringProperty du nom
     * @return StringProperty du nom sur lequel on peut attacher un listener
     */
    public StringProperty nameProperty() {
        return name;
    }
    /**
     * getter du nom
     * @return nom de l'expression
     */
    public String getName() {
        return name.get();
    }
    /**
     * setter du nom
     * @param name nouveau nom
     */
    public void setName(final String name) {
        this.name.set(name);
    }


    /**
     * la fonction mathématique de l'expression
     */
    private final StringProperty function = new SimpleStringProperty();
    /**
     * getter de la StringProperty de la fonction
     * @return StringProperty de la fonction sur lequel on peut attacher un listener
     */
    public StringProperty functionProperty() {
        return function;
    }
    /**
     * getter de la fonction
     * @return fonction de l'expression
     */
    public String getFunction() {
        return function.get();
    }
    /**
     * setter sécurisé de la fonction
     * @param function nouvelle fonction
     */
    public void setFunction(final String function) {
        if (function.length() != 0) this.function.set(function.replace(" ", ""));
        else StageService.Holder.openErrorWindows("messageOnly", ErrorCode.UnhautorizedDeletion);
    }


    /**
     * le flag d'activation de la courbe (affichage graphique)
     * @see core.app.view.scene.GraphicController
     */
    private final BooleanProperty isActive = new SimpleBooleanProperty();
    /**
     * getter du flag d'activation de la courbe
     * @return booléen actif ou non
     * @see core.app.view.scene.GraphicController
     */
    public boolean isActive() {
        return isActive.get();
    }
    /**
     * setter du flag d'activation de la courbe
     * @param isActive nouvel état
     * @see core.app.view.scene.GraphicController
     */
    public void setIsActive(final boolean isActive) {
        this.isActive.set(isActive);
    }


    /**
     * le nombre de dérivation ou primitivation (-1) à appliquer
     */
    private final IntegerProperty degree = new SimpleIntegerProperty(0);
    /**
     * getter du nombre de dérivation/primitivation
     * @return dérivée ou primitive
     */
    public int getDegree() {
        return degree.get();
    }
    /**
     * setter du nombre de dérivation/primitivation
     * @param degree nouveau degré
     */
    public void setDegree(int degree) {
        this.degree.set(degree);
    }


    /**
     * le nombre de points à calculer pour la courbe (affichage graphique)
     * @see core.app.view.scene.GraphicController
     */
    private final IntegerProperty sampling = new SimpleIntegerProperty();//nbPoints
    /**
     * getter du IntegerProperty de la fonction
     * @return IntegerProperty du sampling sur lequel on peut attacher un listener
     * @see core.app.view.scene.GraphicController
     */
    public IntegerProperty samplingProperty() { return sampling; }
    /**
     * getter du sampling de la courbe
     * @return nombre de points à appliquer
     * @see core.app.view.scene.GraphicController
     */
    public int getSampling() {
        return sampling.get();
    }
    /**
     * setter du nouveau sampling de la courbe
     * @param sampling nombre de point à calculer
     * @see core.app.view.scene.GraphicController
     */
    public void setSampling(final Integer sampling) {
        this.sampling.set(sampling);
    }


    /**
     * le sampling précédent pour limiter le nombre de rafraichissement (affichage graphique)
     * @see core.app.view.scene.GraphicController
     */
    private int samplingBefore;
    /**
     * getter de l'ancien sampling de la courbe
     * @return nombre de points appliqués à la courbe
     * @see core.app.view.scene.GraphicController
     */
    public int getSamplingBefore() {
        return samplingBefore;
    }
    /**
     * setter de l'ancien sampling de la courbe
     * @param sampling nombre de point calculés
     * @see core.app.view.scene.GraphicController
     */
    public void setSamplingBefore(final int sampling) {
        this.samplingBefore = sampling;
    }


    /**
     * la couleur de la courbe (affichage graphique)
     * @see core.app.view.scene.GraphicController
     */
    private Color color;
    /**
     * getter de la couleur de la courbe
     * @return instance de Color appliquée à la courbe
     * @see core.app.view.scene.GraphicController
     */
    public Color getColor() { return color;/*Color.web(color.get());*/ }
    /**
     * setter de la couleur de la courbe
     * @param color nouvelle couleur à appliquer
     * @see core.app.view.scene.GraphicController
     */
    public void setColor(final Color color) {
        this.color = color;
    }
}
