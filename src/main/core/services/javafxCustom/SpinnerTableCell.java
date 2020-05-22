package core.services.javafxCustom;

//import com.sun.javafx.binding.BidirectionalBinding;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.util.Callback;


public class SpinnerTableCell<T> extends TableCell<T, Integer> {
    /**
     * Spinner intégré à la cellule
     */
    private final Spinner<Integer> spinner = new Spinner();


    private SpinnerTableCell(int min, int max, int step) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, 0, step));
        spinner.setEditable(true);

        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != oldValue) {
                Platform.runLater(() -> {
                    commitEdit(spinner.getValue());
                });
            }
        });
    }

    /**
     * Fabrique statique
     */
    public static <T> Callback<TableColumn<T, Integer>, TableCell<T, Integer>> forTableColumn(int min, int max, int step) {
        return (TableColumn<T, Integer> tableColumn) -> new SpinnerTableCell<T>(min, max, step);
    }

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