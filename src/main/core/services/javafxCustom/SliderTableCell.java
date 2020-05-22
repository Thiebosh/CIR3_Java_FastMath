package core.services.javafxCustom;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * TableCell personnalisé par l'intégration d'un slider
 * @param <T> Type générique hérité de TableCell : instance d'entité
 */
public class SliderTableCell<T> extends TableCell<T, Integer> {
    /**
     * Slider intégré à la cellule
     */
    private final Slider slider = new Slider();
    /**
     * Constructeur de l'instance : définit les paramètres du slider
     * @param min limite min
     * @param max limite max
     */
    private SliderTableCell(final int min, final int max) {
        slider.setMin(min);
        slider.setMax(max);
        SliderTableCell.max = max;
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(2);//avec flèches directionnelles
    }
    /**
     * Fabrique statique (accès au constructeur privé)
     * @param min limite min
     * @param max limite max
     * @param <T> Entité du tableView
     * @return instance de sliderTableCell
     */
    public static <T> Callback<TableColumn<T, Integer>, TableCell<T, Integer>> forTableColumn(final int min, final int max) {
        return (TableColumn<T, Integer> tableColumn) -> new SliderTableCell<T>(min, max);
    }


    /**
     * valeur max du slider
     */
    private static int max;
    /**
     * setter statique du slider
     * @param max nouveau maximum
     */
    public static void setMaxValue(final int max) {
        SliderTableCell.max = max;
    }


    /**
     * Rééecriture de la mise à jour d'élément de tableCell pour la rendre compatible avec le colorPicker
     * @param item élément mis à jour par la méthode
     * @param empty flag de modification
     */
    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        Node graphic = null;
        slider.setMax(SliderTableCell.max);

        if (item != null && !empty) {
            graphic = slider;

            // On établit une liaison avec la propriété actuelle.
            final ObservableValue observableValue = getTableColumn().getCellObservableValue(getIndex());
            if (observableValue instanceof IntegerProperty) {
                slider.valueProperty().bindBidirectional((IntegerProperty) observableValue);
            }

            // La réglette n'est pas activable si la cellule ou la colonne ou la table ne sont pas éditables.
            slider.disableProperty().bind(Bindings.not(
                    getTableView().editableProperty().and(getTableColumn().editableProperty()).and(editableProperty())
            ));
        }

        setGraphic(graphic);
    }
}
