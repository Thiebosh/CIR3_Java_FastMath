package core.services.javafxCustom;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.util.Callback;

/**
 * TableCell personnalisé par l'intégration d'un spinner
 * @param <T> Type générique hérité de TableCell : instance d'entité
 */
public class SpinnerTableCell<T> extends TableCell<T, Integer> {
    /**
     * Spinner intégré à la cellule
     */
    private final Spinner<Integer> spinner = new Spinner();
    /**
     * Constructeur de l'instance : définit les paramètres du slider
     * @param min limite min
     * @param max limite max
     * @param step écart entre deux valeurs
     */
    private SpinnerTableCell(final int min, final int max, final int step) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, 0, step));
        spinner.setEditable(true);

        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != oldValue) { Platform.runLater(() -> { commitEdit(spinner.getValue()); }); }
        });
    }
    /**
     * Fabrique statique (accès au constructeur privé)
     * @param min limite min
     * @param max limite max
     * @param step écart entre deux valeurs
     * @param <T> entité du tableView
     * @return instance de spinnerTableCell
     */
    public static <T> Callback<TableColumn<T, Integer>, TableCell<T, Integer>> forTableColumn(final int min, final int max, final int step) {
        return (TableColumn<T, Integer> tableColumn) -> new SpinnerTableCell<T>(min, max, step);
    }


    /**
     * Rééecriture de la mise à jour d'élément de tableCell pour la rendre compatible avec le spinner
     * @param item élément mis à jour par la méthode
     * @param empty flag de modification
     */
    @Override
    public void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(empty ? null : spinner);

        // On établit une liaison avec la propriété actuelle
        /*final ObservableValue observableValue = getTableColumn().getCellObservableValue(getIndex());
        if (observableValue instanceof IntegerProperty) {
            //spinner.getValueFactory().valueProperty().bindBidirectional((Property<Integer>) observableValue);//pb here
        }
         */
    }
    /**
     * Rendre compatible le comitEdit avec le spinner
     * @param item élément mis à jour par la méthode
     */
    @Override
    public void commitEdit(Integer item) {
        // This block is necessary to support commit on losing focus, because
        // the baked-in mechanism sets our editing state to false before we can
        // intercept the loss of focus. The default commitEdit(...) method
        // simply bails if we are not editing...
        if (!isEditing()){// && !item.equals(getItem())) {
            TableView<T> table = getTableView();
            if (table != null) {
                TableColumn<T, Integer> column = getTableColumn();
                TableColumn.CellEditEvent<T, Integer> event = new TableColumn.CellEditEvent<>(
                        table, new TablePosition<T,Integer>(table, getIndex(), column),
                        TableColumn.editCommitEvent(), item
                );
                Event.fireEvent(column, event);
            }
        }

        super.commitEdit(item);
    }

}
