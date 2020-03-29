package core.view;

import javafx.fxml.FXML;

/**
 *
 */
public class HomePageController {


    @FXML
    public void initialize() {
        //si on remplit des anchor pane
        /*
        FXMLLoader loader = new  FXMLLoader();
        loader.setLocation(ContactApp.class.getResource("/view/ContactViewOnClick.fxml"));
        try {
            AnchorPane pane = loader.load();
            detailedView.getChildren().add(pane);
            AnchorPane.setBottomAnchor(pane, (double) 0);
            AnchorPane.setTopAnchor(pane, (double)0);
            AnchorPane.setLeftAnchor(pane, (double) 0);
            AnchorPane.setRightAnchor(pane, (double) 0);
            detailedView.getChildren().setAll(pane);
            detailedView.setVisible(false);
        }
        catch (IOException e ) {
            e.printStackTrace();
        }

        controllerOnClick = loader.getController();
         */
    }


}
