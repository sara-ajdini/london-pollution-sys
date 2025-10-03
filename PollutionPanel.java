import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.LinkedList;
import java.util.ArrayList;
import javafx.util.Pair;
import java.io.File;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import java.util.HashMap;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.control.ChoiceDialog;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

/**
 * The Main class responsible for the Pollution Statistics Panel.
 * Includes information about the average pollution level,
 * highest pollution levels and trends over time.
 * 
 * The user is able to switch between statistics.
 *
 * @author Maria Plesinska
 * @version 1.1
 */
public class PollutionPanel
{
    private DataFiles dataFiles;
    private ComboBox<String> yearDropDown;
    private ComboBox<String> pollutantDropDown;
    private int chosenGridCode = 771285;
    private Label actualGridCodeLabel;
    private HashMap<String,String> selectedFilters = new HashMap<>();
    private TableView<HighestPollutionTableContents> highestPollutionLevelTable;
    private ObservableList<HighestPollutionTableContents> highestTableContents;
    private TableView<AveragePollutionTableContents> averageYearTable;
    private ObservableList<AveragePollutionTableContents> averageYearTableContents;
    private Label averageYearLabel = new Label();
    private TableView<AveragePollutionTableContents> averageLocationTable;
    private ObservableList<AveragePollutionTableContents> averageLocationTableContents;
    private Label averageLocationLabel = new Label();
    private PollutionGraph pollutionGraph;
    private ScrollPane mainPane;

    private VBox filterPane;
    private VBox statisticsPane;
    
    private Label yearLabel;
    private Label pollutantLabel;
    private Label gridCodeLabel;
    private Button locationButton;
    private Button updateButton;
    private Label averagePollutionLevel;
    private Label highestPollutionLevels;
    private Label trendsOverTime;
    private HBox averagePollution;
    private VBox averageYearColumn;
    private VBox averageLocationColumn;
    
     /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    public PollutionPanel(DataFiles dataFiles)
    {
        this.dataFiles = dataFiles;
        
        // Layout

        // Filter VBox
        filterPane = createFilterPane();
        
        // Statistics pane
        statisticsPane = statisticsPane();
        
        String paneTitle = "Pollution Statistics";
        Label name = new Label(paneTitle);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(filterPane);
        borderPane.setCenter(statisticsPane);
        
        mainPane = new ScrollPane(borderPane);
    }
    
    public VBox getRootPane(){
        return new VBox(mainPane);
    }

    /**
     * Creates the VBox pane that will hold all the 
     * filters the user can apply to choose what appears
     * in the statistics panel.
     * Filters only apply if the update button is pressed.
     * @return a VBox that contains all the filters that the user can apply
     */
    private VBox createFilterPane(){
        VBox filterPane = new VBox();
        filterPane.setSpacing(5);
        
        // Years dropdown box
        yearDropDown = yearDropDown();
        
        // Pollutants dropdown box
        pollutantDropDown = pollutantDropDown();
        
        // Initial filters
        yearLabel = new Label("Year:");
        selectedFilters.put("year",yearDropDown.getValue());
        
        pollutantLabel = new Label("Pollutant:");
        selectedFilters.put("pollutant", pollutantDropDown.getValue());
        
        gridCodeLabel = new Label("Current Gridcode:");
        actualGridCodeLabel = new Label(String.valueOf(chosenGridCode));
        locationButton = new Button("Choose Location");
        locationButton.setOnAction(this::chooseLocation);
        
        // Update button
        updateButton = new Button("Update Filters");
        updateButton.setOnAction(this::updateFilter);
        
        // Pane layout
        filterPane.getChildren().addAll(yearLabel,yearDropDown,pollutantLabel,pollutantDropDown,gridCodeLabel,actualGridCodeLabel,locationButton,updateButton);
        filterPane.setPadding(new Insets(10,10,10,10));
        filterPane.setAlignment(Pos.TOP_CENTER);
        
        return filterPane;
        
    }
    
    /**
     * Creates a drop down menu where the user can choose what year they would like to look at the data for
     * @return a ComboBox with all the years the user can choose between
     */
    private ComboBox<String> yearDropDown(){
        ObservableList<String> years = FXCollections.observableArrayList("2018","2019","2020","2021","2022","2023");
        ComboBox<String> yearDropDown = new ComboBox<String>(years);
        yearDropDown.getSelectionModel().select(0);     
        return yearDropDown;
    }
    
    /**
     * Creates a drop down menu where the user can choose what pollutant they would like to look at the data for
     * @return a ComboBox with all the pollutants the user can choose between
     */
    private ComboBox<String> pollutantDropDown(){
        ObservableList<String> pollutants = FXCollections.observableArrayList("no2", "pm2.5", "pm10");
        ComboBox<String> pollutantDropDown = new ComboBox<String>(pollutants);
        pollutantDropDown.getSelectionModel().select(0);
        return pollutantDropDown;
    }
    
    /**
     * When the update button is pressed, all the information on the statistics panel should update.
     */
    private void updateFilter(ActionEvent event)
    {
        selectedFilters.replace("year",yearDropDown.getValue());
        selectedFilters.replace("pollutant", pollutantDropDown.getValue());
        
        // Updates the highest pollution levels
        highestTableContents = resetHighestTableData();
        highestPollutionLevelTable.setItems(highestTableContents);
        
        // Updates average pollution levels
        averageYearTableContents = resetAverageYearTableData();
        averageYearTable.setItems(averageYearTableContents);
        
        averageLocationTableContents = resetAverageLocationTableData();
        averageLocationTable.setItems(averageLocationTableContents);
        
        actualGridCodeLabel.setText(String.valueOf(chosenGridCode));
        
        pollutionGraph.updateGraph(pollutantDropDown.getValue(),chosenGridCode, dataFiles);
    }
    
    /**
     * When the update button is pressed, all the information on the statistics panel should update.
     */
    public void updateFilter(int gridCode, String pollutant, String year)
    {
        selectedFilters.replace("year",year);
        selectedFilters.replace("pollutant", pollutant);
        yearDropDown.setValue(year);
        pollutantDropDown.setValue(pollutant);
        chosenGridCode = gridCode;
        
        // Updates the highest pollution levels
        highestTableContents = resetHighestTableData();
        highestPollutionLevelTable.setItems(highestTableContents);
        
        // Updates average pollution levels
        averageYearTableContents = resetAverageYearTableData();
        averageYearTable.setItems(averageYearTableContents);
        
        averageLocationTableContents = resetAverageLocationTableData();
        averageLocationTable.setItems(averageLocationTableContents);
        
        actualGridCodeLabel.setText(String.valueOf(chosenGridCode));
        
        pollutionGraph.updateGraph(pollutantDropDown.getValue(),chosenGridCode, dataFiles);
    }
    
    /**
     * The user can choose a location on the map or type in the gridcode or coordinates.
     */
    private void chooseLocation(ActionEvent event){
        ChoiceDialog locationDialog = locationDialog();

        Optional<String> locationResult = locationDialog.showAndWait();
        if (locationResult.isPresent()){
            if (locationResult.get().equals("Gridcode")){
                gridcodeChoice();
            } else if (locationResult.get().equals("Coordinates")){
                coordinatesChoice();
            }
        }
    }
    
    /**
     * Creates the dialog that appears when the choose location button is pressed.
     * @return a choice dialog that lets the user choose who they will pick a location
     */
    private ChoiceDialog locationDialog(){
        List<String> locationChoices = new ArrayList<>();
        locationChoices.add("Gridcode");
        locationChoices.add("Coordinates");
        ChoiceDialog locationDialog = new ChoiceDialog(locationChoices.get(0),locationChoices);
        locationDialog.setTitle("Choose Location");
        locationDialog.setHeaderText("Choose how you would like to select a location. You can also select a location from the London map.");
        
        return locationDialog;
    }
    
    /**
     * Gets called when the user wants to enter a gridcode for a location in London.
     * Checks whether the gridcode entered is valid and updates the location if it is.
     */
    private void gridcodeChoice(){
        TextInputDialog gridcodeDialog = new TextInputDialog();
        gridcodeDialog.setContentText("Enter Gridcode:");
        gridcodeDialog.setTitle("Gridcode");
        Optional<String> chosenGridcode = gridcodeDialog.showAndWait();
        String numbersOnly = "\\d+"; // only digits
        if (chosenGridcode.isPresent()){
            String gridcode = chosenGridcode.get().trim();
            if (gridcode.matches(numbersOnly)){
                List<DataPoint> dataPoints = new LinkedList<>();
                dataPoints = dataFiles.getFilteredDataPoints(yearDropDown.getValue(), pollutantDropDown.getValue(),"London");
                boolean found = false;
                int i = 0;
                while (!found && i < dataPoints.size()){
                    int datapointGridcode = dataPoints.get(i).gridCode();
                    String dpGridcode = String.valueOf(datapointGridcode);
                    if (dpGridcode.equals(gridcode)){
                        found = true;
                        chosenGridCode = datapointGridcode;
                        updateFilter(null);
                    }
                    i++;
                }
            }
        }
    }
    
    /**
     * Gets called when the user wants to enter a coordinate for a location in London.
     * Checks whether the coordinate entered is valid and updates the location if it is.
     */
    private void coordinatesChoice(){
        Dialog coordinateDialog = coordinateDialog();
        
        String numbersOnly = "\\d+"; // only digits
    
        Optional<Pair<String,String>> chosenCoordinates = coordinateDialog.showAndWait();
        if (chosenCoordinates.isPresent()){
            String xCoordinate = chosenCoordinates.get().getKey().trim();
            String yCoordinate = chosenCoordinates.get().getValue().trim();
            if (xCoordinate.matches(numbersOnly) && yCoordinate.matches(numbersOnly)){
                List<DataPoint> dataPoints = new LinkedList<>();
                dataPoints = dataFiles.getFilteredDataPoints(yearDropDown.getValue(), pollutantDropDown.getValue(),"London");
                boolean found = false;
                int i = 0;
                while (!found && i < dataPoints.size()){
                    int datapointxValue = dataPoints.get(i).x();
                    int datapointyValue = dataPoints.get(i).y();
                    String dpX = String.valueOf(datapointxValue);
                    String dpY = String.valueOf(datapointyValue);
                    if (dpX.equals(xCoordinate) && dpY.equals(yCoordinate)){
                        found = true;
                        chosenGridCode = dataPoints.get(i).gridCode();
                        updateFilter(null);
                    }
                    i++;
                }
            }
        }
    }

    /**
     * Creates the dialog box that appears when coordinates are selected.
     * @return a dialog box that allows the user to enter coordinates
     */
    private Dialog coordinateDialog(){
        Dialog<Pair<String,String>> coordinateDialog = new Dialog();
        coordinateDialog.setTitle("Coordinates");
        
        coordinateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        TextField xTextField = new TextField();
        xTextField.setPromptText("x coordinate");
        Label xLabel = new Label("x:");
        
        TextField yTextField = new TextField();
        yTextField.setPromptText("y coordinate");
        Label yLabel = new Label("y:");
        
        gridPane.add(xTextField, 1, 0);
        gridPane.add(xLabel, 0, 0);
        gridPane.add(yTextField, 1, 1);
        gridPane.add(yLabel, 0, 1);
        
        coordinateDialog.getDialogPane().setContent(gridPane);
        coordinateDialog.setResultConverter(dialogButton -> {if (dialogButton == ButtonType.OK){return new Pair<>(xTextField.getText(), yTextField.getText());} return null;});
        
        return coordinateDialog;
    }
    
    /**
     * Creates a VBox that will display the pollution statistics.
     * @return a VBox that will display the pollution statistics
     */
    private VBox statisticsPane(){
        statisticsPane = new VBox();
        
        // Average pollution level
        averagePollutionLevel = new Label("Average Pollution Level:");
        averagePollution = averagePollutionHBox();
        
        // Highest pollution levels
        highestPollutionLevels = new Label("\nHighest Pollution Levels:");
        highestPollutionTable();
        
        // Trends over time
        trendsOverTime = new Label("Trends over time:");
        pollutionGraph = new PollutionGraph(pollutantDropDown.getValue(), chosenGridCode, dataFiles);
        Pane pollutionGraphPane = pollutionGraph.getRootPane();
        
        //Layout
        statisticsPane.setPadding(new Insets(10,10,10,10));
        statisticsPane.setAlignment(Pos.TOP_LEFT);
        statisticsPane.getChildren().addAll(averagePollutionLevel,averagePollution,highestPollutionLevels,highestPollutionLevelTable,trendsOverTime, pollutionGraphPane);
        
        return statisticsPane;
    }
    
    /**
     * Retunrs a HBox that contains all the average pollution statistics.
     * @return a HBox that contains all the average pollution statistics
     */
    private HBox averagePollutionHBox(){
        HBox averagePollution = new HBox(20);
        averagePollutionTables();
        
        averageYearColumn = new VBox();
        averageYearColumn.getChildren().addAll(averageYearLabel, averageYearTable);
        
        averageLocationColumn = new VBox();
        averageLocationColumn.getChildren().addAll(averageLocationLabel, averageLocationTable);
        
        averagePollution.getChildren().addAll(averageYearColumn,averageLocationColumn);
        return averagePollution;
    }
    
    /**
     * Creates the pollution table that will display the highest pollution levels.
     */
    private void highestPollutionTable(){
        highestPollutionLevelTable = new TableView<>();
        highestTableContents = resetHighestTableData();
        
        // table columns
        TableColumn pollutionType = new TableColumn("Pollutant");
        pollutionType.setCellValueFactory(new PropertyValueFactory<>("pollutionType"));
        pollutionType.setMinWidth(100);
        
        TableColumn pollutionLevel = new TableColumn("Pollution Level");
        pollutionLevel.setCellValueFactory(new PropertyValueFactory<>("pollutionLevel"));
        pollutionLevel.setMinWidth(102);
        
        TableColumn location = new TableColumn("Location");
        
        TableColumn gridCode = new TableColumn("Gridcode");
        gridCode.setCellValueFactory(new PropertyValueFactory<>("gridCode"));
        gridCode.setMinWidth(100);
        
        TableColumn xColumn = new TableColumn("x");
        xColumn.setCellValueFactory(new PropertyValueFactory<>("x"));
        xColumn.setMinWidth(100);
        
        TableColumn yColumn = new TableColumn("y");
        yColumn.setCellValueFactory(new PropertyValueFactory<>("y"));
        yColumn.setMinWidth(100);
        
        // table contents
        highestPollutionLevelTable.setItems(highestTableContents);
        location.getColumns().addAll(gridCode, xColumn, yColumn);
        highestPollutionLevelTable.getColumns().addAll(pollutionType, pollutionLevel, location);
        
        highestPollutionLevelTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        highestPollutionLevelTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        highestPollutionLevelTable.setPrefHeight(200);
    }
    
    /**
     * Resets the table data and returns the new contents of the table according to the filters.
     * @return an ObservableList containing all the table contents
     */
    private ObservableList<HighestPollutionTableContents> resetHighestTableData(){
        DataPoint no2Point = getHighestPollution(yearDropDown.getValue(), "no2");
        DataPoint pm25Point = getHighestPollution(yearDropDown.getValue(), "pm2.5");
        DataPoint pm10Point = getHighestPollution(yearDropDown.getValue(), "pm10");
        
        highestTableContents=
        FXCollections.observableArrayList(
        new HighestPollutionTableContents("NO2", no2Point.value(), no2Point.gridCode(), no2Point.x(), no2Point.y()),
        new HighestPollutionTableContents("pm2.5", pm25Point.value(), pm25Point.gridCode(), pm25Point.x(), pm25Point.y()),
        new HighestPollutionTableContents("pm10", pm10Point.value(), pm10Point.gridCode(), pm10Point.x(), pm10Point.y())
        );
        
        return highestTableContents;
    }

    /**
     * Creates the two average pollution tables.
     */
    private void averagePollutionTables(){
        averageYearPollutionTable();
        averageLocationPollutionTable();
    }
    
    /**
     * Creates a table that displays the average pollution based on the chosen year.
     */
    private void averageYearPollutionTable(){
        averageYearTable = new TableView<>();
        averageYearTableContents = resetAverageYearTableData();
        
        // table columns
        TableColumn pollutionType = new TableColumn("Pollutant");
        pollutionType.setCellValueFactory(new PropertyValueFactory<>("pollutionType"));
        pollutionType.setMinWidth(100);
        
        TableColumn pollutionLevel = new TableColumn("Pollution Level");
        pollutionLevel.setCellValueFactory(new PropertyValueFactory<>("pollutionLevel"));
        pollutionLevel.setMinWidth(102);
        
        // table contents
        averageYearTable.setItems(averageYearTableContents);
        averageYearTable.getColumns().addAll(pollutionType, pollutionLevel);
        
        averageYearTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        averageYearTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        averageYearTable.setPrefHeight(170);
    }
    
    /**
     * Creates a table that displays the average pollution based on the chosen location.
     */
    private void averageLocationPollutionTable(){
        averageLocationTable = new TableView<>();
        averageLocationTableContents = resetAverageLocationTableData();
        
        // table columns
        TableColumn pollutionType = new TableColumn("Pollutant");
        pollutionType.setCellValueFactory(new PropertyValueFactory<>("pollutionType"));
        pollutionType.setMinWidth(100);
        
        TableColumn pollutionLevel = new TableColumn("Pollution Level");
        pollutionLevel.setCellValueFactory(new PropertyValueFactory<>("pollutionLevel"));
        pollutionLevel.setMinWidth(102);
        
        // table contents
        averageLocationTable.setItems(averageLocationTableContents);
        averageLocationTable.getColumns().addAll(pollutionType, pollutionLevel);
        
        averageLocationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        averageLocationTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        averageLocationTable.setPrefHeight(170);
    }
    
    /**
     * Returns the updated contents of the average pollution year table.
     * @return an ObservableList with the updated contents of the average pollution year table
     */
    private ObservableList<AveragePollutionTableContents> resetAverageYearTableData(){
        double no2Average = getAverageYearPollution(yearDropDown.getValue(), "no2");
        double pm25Average = getAverageYearPollution(yearDropDown.getValue(), "pm2.5");
        double pm10Average = getAverageYearPollution(yearDropDown.getValue(), "pm10");

        
        averageYearTableContents=
        FXCollections.observableArrayList(
        new AveragePollutionTableContents("NO2", no2Average),
        new AveragePollutionTableContents("pm2.5", pm25Average),
        new AveragePollutionTableContents("pm10", pm10Average)
        );
        
        updateAveragePollutionLabels();
        return averageYearTableContents;
    }
    
    /**
     * Returns the updated contents of the average pollution location table.
     * @return an ObservableList with the updated contents of the average pollution location table
     */
    private ObservableList<AveragePollutionTableContents> resetAverageLocationTableData(){
        double no2Average = getAverageLocationPollution(chosenGridCode, "no2");
        double pm25Average = getAverageLocationPollution(chosenGridCode, "pm2.5");
        double pm10Average = getAverageLocationPollution(chosenGridCode, "pm10");
        
        averageLocationTableContents=
        FXCollections.observableArrayList(
        new AveragePollutionTableContents("NO2", no2Average),
        new AveragePollutionTableContents("pm2.5", pm25Average),
        new AveragePollutionTableContents("pm10", pm10Average)
        );
        
        updateAveragePollutionLabels();
        return averageLocationTableContents;
    }
    
    /**
     * Updates the labels that display the chosen year and location for the average pollution tables.
     */
    private void updateAveragePollutionLabels(){
        averageYearLabel.setText(yearDropDown.getValue() + ":");
        averageLocationLabel.setText(String.valueOf(chosenGridCode) + ":");
    }
    
    /**
     * Returns the DataPoint with the highest pollution level of a given year and pollutant
     * @param year the year you want to find at the data for
     * @param pollutant the pollutant you want to find the data for
     * @return the DataPoint with the highest pollution level for the given year and pollutant
     */
    private DataPoint getHighestPollution(String year, String pollutant){
        List<DataPoint> filteredDataPoints = new ArrayList<>();
        filteredDataPoints = dataFiles.getFilteredDataPoints(year, pollutant,"London");
        
        DataPoint highestPollutionDp = null;
        double highestPollutionLevel = 0;
        for (DataPoint dataPoint : filteredDataPoints){
            if (dataPoint.value() > highestPollutionLevel){
                highestPollutionDp = dataPoint;
                highestPollutionLevel = dataPoint.value();
            }
        }
        return highestPollutionDp;
    }
    
    /**
     * Calculates and returns the average pollution level for the given year and pollutant.
     * @param year the chosen year
     * @param pollutant the chosen pollutant
     * @return the average pollution level for the given year and pollutant
     */
    private double getAverageYearPollution(String year, String pollutant){
        List<DataPoint> filteredDataPoints = new ArrayList<>();
        filteredDataPoints = dataFiles.getFilteredDataPoints(year, pollutant,"London");
        
        double totalPollution = 0;
        for (DataPoint dataPoint : filteredDataPoints){
            totalPollution = totalPollution + dataPoint.value();
        }
        double averagePollution = totalPollution / filteredDataPoints.size();
        return averagePollution;
    }

    /**
     * Calculates and returns the average pollution level for the given gridcode and pollutant.
     * @param gridcode the chosen location
     * @param pollutant the chosen pollutant
     * @return the average pollution level for the given gridcode and pollutant
     */
    private double getAverageLocationPollution(int gridcode, String pollutant){
        List<DataPoint> filteredDataPoints = new ArrayList<>();
        filteredDataPoints = dataFiles.getFilteredLocationDataPoints(gridcode, pollutant);
        
        double totalPollution = 0;
        for (DataPoint dataPoint : filteredDataPoints){
            totalPollution = totalPollution + dataPoint.value();
        }
        double averagePollution = totalPollution / filteredDataPoints.size();
        return averagePollution;
    }
    
    /**
     * Displays text about the pollution panel functionality.
     * @return a String describing how to use the pollution panel page
     */
    public String getInfoText(){
        String infoString = "Use the dropdowns to select year and pollutant, click Update to \nsee changes.\nSelect specific location by entering a gridcode or x, y coordinates,\nor by clicking on the map.";
        return infoString;
    }
    
    // Getters
    public ComboBox<String> getYearDropDown(){
        return yearDropDown;
    }
    
    public ComboBox<String> getPollutantDropDown(){
        return pollutantDropDown;
    }
    
    public TableView<HighestPollutionTableContents> getHighestPollutionLevelTable(){
        return highestPollutionLevelTable;
    }
    
    public TableView<AveragePollutionTableContents> getAverageYearTable(){
        return averageYearTable; 
    }
    
    public TableView<AveragePollutionTableContents> getAverageLocationTable(){
        return averageLocationTable;
    }

    public Label getActualGridCodeLabel(){
        return actualGridCodeLabel;
    }
    
    public Label averageYearLabel(){
        return averageYearLabel;
    }
    
    public Label averageLocationLabel(){
        return averageLocationLabel;
    }
    
    public ScrollPane getMainPane(){
        return mainPane;
    }
    
    public VBox getFilterPane(){
        return filterPane;
    }
    
    public VBox getStatisticsPane(){
        return statisticsPane;
    }
    
    public Label getYearLabel(){
        return yearLabel;
    }
    
    public Label getPollutantLabel(){
        return pollutantLabel;
    }
    
    public Label getGridCodeLabel(){
        return gridCodeLabel;
    }
    
    public Button getLocationButton(){
        return locationButton;
    }
    
    public Button getUpdateButton(){
        return updateButton;
    }
    
    public Label getAveragePollutionLevel(){
        return averagePollutionLevel;
    }
    
    public Label getHighestPollutionLevels(){
        return highestPollutionLevels;
    }
    
    public Label getTrendsOverTime(){
        return trendsOverTime;
    }
    
    public HBox getAveragePollution(){
        return averagePollution;
    }
    
}
    