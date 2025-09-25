import javafx.scene.control.Tab;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;

/**
 * Abstract class that provides a template for displaying content of instruction alerts.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class InstructionsPopup
{
    Alert alert;
    /**
     * Constructor for objects of class InstructionTemplate
     */
    public InstructionsPopup(String text)
    {
        // create alert dialog and set default title and header
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Instructions");
        alert.setContentText(text);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(450);
        dialogPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

        dialogPane.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
    }
    
    /**
     * Displays the instruction alert dialog.
     */
    public void show(){
        alert.showAndWait();
    }
}
