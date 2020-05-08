package core.view.javafxCustom;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class SliderTableCell<T> extends TableCell<T, Integer> {

    /**
     * La dernière propriété sur laquelle l'éditeur a été lié.
     */
    private ObservableValue<Number> numberProperty;
    /**
     * L'éditeur.
     */
    private Slider slider = new Slider();

    public SliderTableCell(final int min, final int max) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(2);//avec flèches directionnelles
    }

    @Override
    protected void updateItem(Integer value, boolean empty) {
        super.updateItem(value, empty);
        setText(null);
        Node graphic = null;
        if (value != null && !empty) {
            graphic = slider;
            // On casse la liaison avec la propriété précédente.
            if (numberProperty instanceof IntegerProperty) {
                slider.valueProperty().unbindBidirectional((IntegerProperty) numberProperty);
            }
            // On établit une liaison avec la propriété actuelle.
            final int row = getIndex();
            final ObservableValue observableValue = getTableColumn().getCellObservableValue(row);
            if (observableValue instanceof IntegerProperty) {
                slider.valueProperty().bindBidirectional((IntegerProperty) observableValue);
                //commitEdit((int)this.slider.getValue());//envoyer à setOnEditCommit
            }
            // La réglette n'est pas activable si la cellule ou la colonne ou la table ne sont pas éditables.
            slider.disableProperty().bind(Bindings.not(
                    getTableView().editableProperty().and(getTableColumn().editableProperty()).and(editableProperty())
            ));
        }
        setGraphic(graphic);
    }

    /**
     * Fabrique statique.
     */
    public static <T> Callback<TableColumn<T, Integer>, TableCell<T, Integer>> forTableColumn(final int min, final int max) {
        return (TableColumn<T, Integer> tableColumn) -> new SliderTableCell<T>(min, max);
    }
}
