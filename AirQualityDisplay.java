import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;

/**
 * Displays air quality data for a selected city.
 * City selection and refresh are triggered externally by the main GUI.
 * 
 * @author Muhlisa Husainova
 * @version 1.0
 */
public class AirQualityDisplay {

    private BorderPane rootPane;
    private AirQualityAPI airQualityAPI;
    private HBox airQualityHBox;

    /**
     * Constructor that initializes with the API handler.
     * @param api An instance of AirQualityAPI class
     */
    public AirQualityDisplay(AirQualityAPI api) {
        this.airQualityAPI = api;

        airQualityHBox = new HBox(10);
        airQualityHBox.setPadding(new Insets(15));
        // Optional styling:
        // airQualityHBox.setStyle("-fx-background-color: #f4f4f4;");

        rootPane = new BorderPane();
        rootPane.setCenter(airQualityHBox);
    }

    /**
     * Refreshes data display for the selected city.
     * This should be called by the main GUI with the selected city.
     * @param city Name of the city ("London", "Oxford", "Leeds", etc.)
     */
    public void refreshData(String city) {
        // Update the API's data pane
        airQualityAPI.updateApiPane(city);

        // Get the latest pollution display pane from the API
        Pane dataPane = airQualityAPI.getPollutionPane();


        // Clear the old display and add the updated one
        airQualityHBox.getChildren().clear();
        airQualityHBox.getChildren().add(dataPane);
    }


    // getters
    public HBox getAirQualityHBox() {
        return airQualityHBox;
    }

    public Pane getRootPane() {
        return rootPane;
    }
}
