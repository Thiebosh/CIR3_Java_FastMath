package core.services.javafxCustom;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

/**
 * TableCell personnalisé par l'intégration d'un colorPicker
 * @param <T> Type générique hérité de TableCell : instance d'entité
 */
public class ColorTableCell<T> extends TableCell<T, Color> {
    /**
     * Gestionnaire de couleur intégré à la cellule
     */
    private final ColorPicker colorPicker;

    /**
     * Constructeur de l'instance :
     * lie le colorPicker à l'entité générique pour répercuter les modifications
     * configure le colorPicker pour qu'il fonctionne de façon idéale
     * affiche le colorPicker dans la fenêtre
     * @param column colonne à convertir en colorTableCell
     */
    public ColorTableCell(TableColumn<T, Color> column) {
        this.colorPicker = new ColorPicker();
        this.colorPicker.editableProperty().bind(column.editableProperty());
        this.colorPicker.disableProperty().bind(column.editableProperty().not());
        this.colorPicker.setOnShowing(event -> {
            final TableView<T> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
        });

        this.colorPicker.setOnHiding(event -> { if (isEditing()) { commitEdit(this.colorPicker.getValue()); } });

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * Rééecriture de la mise à jour d'élément de tableCell pour la rendre compatible avec le colorPicker
     * @param item élément mis à jour par la méthode
     * @param empty flag de modification
     */
    @Override
    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if(empty) {
            setGraphic(null);
        } else {
            this.colorPicker.setValue(item);
            this.setGraphic(this.colorPicker);
        }
    }
}