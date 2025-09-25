import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class AirQualityApp extends Application {

    private AirQualityAPI airQualityAPI;
    private HBox airQualityDataPane; // Pane to hold air quality data

    @Override
    public void start(Stage primaryStage) {

        airQualityAPI = new AirQualityAPI();

        // ComboBox with the three cities
        ComboBox<String> citySelector = new ComboBox<>();
        citySelector.getItems().addAll("London", "Brighton", "Leeds");
        citySelector.setValue("London"); // Default selection

        // Button to trigger data update
        Button updateButton = new Button("Update");

        // Pane where air quality data will be displayed
        airQualityDataPane = new HBox();
        airQualityDataPane.setSpacing(10);

        // Initial data display for the default city
        updateAirQualityData(citySelector.getValue());

        // Handle update button click
        updateButton.setOnAction(event -> {
            String selectedCity = citySelector.getValue();
            updateAirQualityData(selectedCity);
        });

        // Layout containing dropdown, button, and data pane
        HBox layout = new HBox(10);
        layout.getChildren().addAll(citySelector, updateButton, airQualityDataPane);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Live Air Quality Data");
        primaryStage.show();
    }

    /**
     * Updates the air quality data display for the selected city
     */
    private void updateAirQualityData(String selectedCity) {
        System.out.println("Fetching data for " + selectedCity);

        airQualityAPI.updateApiPane(selectedCity);
        HBox dataPane = airQualityAPI.getPollutionPane();
        // Replace old data with new data
        airQualityDataPane.getChildren().clear();
        airQualityDataPane.getChildren().addAll(dataPane.getChildren());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
