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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.lang.Math;
import javafx.scene.control.DialogPane;

/**
 * The comparison tab.
 * It allows the user to use a dropdown menu and select 2 years to compare the average
 * and highest pollution levels of, displayed in tables.
 *
 * @author Maria Plesinska
 * @version 1.0
 */
public class ComparisonPage
{
    private DataFiles dataFiles;
    private String leftChosenYear;
    private String rightChosenYear;
    private ComboBox<String> leftYearDropDown;
    private ComboBox<String> rightYearDropDown;
    private TableView<ComparisonTableContents> leftTable;
    private ObservableList<ComparisonTableContents> leftTableContents;
    private TableView<ComparisonTableContents> rightTable;
    private ObservableList<ComparisonTableContents> rightTableContents;
    private TableView<ComparisonTableContents> differenceTable;
    private ObservableList<ComparisonTableContents> differenceTableContents;
    private BorderPane mainPane;
    private GridPane gridPane;
    private VBox leftVBox;
    private VBox rightVBox;
    private Label leftTitle;
    private Label rightTitle;
    private Button compareButton;
    private BorderPane leftBorderPane;
    private BorderPane rightBorderPane;
    
    private double no2LeftAverage;
    private double pm25LeftAverage;
    private double pm10LeftAverage;
    private double no2LeftHighest;
    private double pm25LeftHighest;
    private double pm10LeftHighest;
    
    private double no2RightAverage;
    private double pm25RightAverage;
    private double pm10RightAverage;
    private double no2RightHighest;
    private double pm25RightHighest;
    private double pm10RightHighest;

    /**
     * Constructor for objects of class ComparisonPage
     */
    public ComparisonPage(DataFiles dataFiles)
    {
        this.dataFiles = dataFiles;
        
        gridPane = new GridPane();
        
        leftVBox = new VBox();
        leftVBox.setAlignment(Pos.TOP_CENTER);
        rightVBox = new VBox();
        rightVBox.setAlignment(Pos.TOP_CENTER);
    
        
        // Dropdowns
        leftYearDropDown = yearDropDown();
        rightYearDropDown = yearDropDown();
        
        compareButton = new Button("Compare");
        compareButton.setOnAction(this::updateComparison);
        
        leftTable();
        rightTable();
        differenceTable();
        
        leftBorderPane = new BorderPane(leftTable,leftYearDropDown,null,null,null);
        rightBorderPane = new BorderPane(rightTable,rightYearDropDown,null,null,null);
        
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.add(leftVBox,1,1);
        gridPane.add(compareButton,2,1);
        gridPane.add(rightVBox,3,1);
    
        leftVBox.getChildren().addAll(leftBorderPane);
        rightVBox.getChildren().addAll(rightBorderPane);  
        
        VBox mainVBox = new VBox();
        mainVBox.getChildren().addAll(gridPane, differenceTable);
        mainVBox.setSpacing(25);
        mainVBox.setPadding(new Insets(20));
            
        HBox mainHBox = new HBox();
        mainHBox.getChildren().addAll(mainVBox);
        
        // Main pane of the page
        mainPane = new BorderPane(mainVBox,null,null,null,null);
    }
    
    /**
     * Returns the root pane
     * @return the root pane
     */
    public Pane getRootPane(){
        return mainPane;
    }

    /**
     * Updates the information stored in the tables when the compare button is pressed.
     * @param event the ActionEvent
     */
    private void updateComparison(ActionEvent event){
        if (leftYearDropDown.getValue() != null && rightYearDropDown.getValue() != null && !leftYearDropDown.getValue().equals(rightYearDropDown.getValue())){
            leftChosenYear = leftYearDropDown.getValue();
            rightChosenYear = rightYearDropDown.getValue();
        } else {
            dropDownAlert();
            leftYearDropDown.setValue(leftChosenYear);
            rightYearDropDown.setValue(rightChosenYear);
            return;
        }
        
        leftTableContents = resetLeftTableData();
        leftTable.setItems(leftTableContents);
        rightTableContents = resetRightTableData();
        rightTable.setItems(rightTableContents);
        differenceTableContents = resetDifferenceTableData();
        differenceTable.setItems(differenceTableContents);
        
    }

    /**
     * Creates a drop down for the years.
     * @return a ComboBox of the years
     */
    private ComboBox<String> yearDropDown(){
        ObservableList<String> years = FXCollections.observableArrayList("2018","2019","2020","2021","2022","2023");
        ComboBox<String> yearDropDown = new ComboBox<String>(years);   
        return yearDropDown;
    }
    
    /**
     * Creates a table that displays the average and highest pollution based on the chosen year from the left drop down.
     */
    private void leftTable(){
        leftTable = new TableView<>();
        leftTableContents = resetLeftTableData();
        
        // table columns
        TableColumn pollutionType = new TableColumn("Pollutant");
        pollutionType.setCellValueFactory(new PropertyValueFactory<>("pollutionType"));
        pollutionType.setMinWidth(90);
        
        TableColumn averagePollutionLevel = new TableColumn("Average");
        averagePollutionLevel.setCellValueFactory(new PropertyValueFactory<>("averagePollution"));
        averagePollutionLevel.setMinWidth(90);
        
        TableColumn highestPollutionLevel = new TableColumn("Highest");
        highestPollutionLevel.setCellValueFactory(new PropertyValueFactory<>("highestPollution"));
        highestPollutionLevel.setMinWidth(90);
        
        // table contents
        leftTable.setItems(leftTableContents);
        leftTable.getColumns().addAll(pollutionType, averagePollutionLevel, highestPollutionLevel);
        
        leftTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        leftTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        leftTable.setPrefHeight(170);
    }
    
    /**
     * Returns the updated contents of the left comparison table.
     * @return an ObservableList with the updated contents of the left comparison table
     */
    private ObservableList<ComparisonTableContents> resetLeftTableData(){
        if (leftChosenYear != null  && rightChosenYear != null && leftChosenYear != rightChosenYear){
            no2LeftAverage = getAverageYearPollution(leftChosenYear, "no2");
            pm25LeftAverage = getAverageYearPollution(leftChosenYear, "pm2.5");
            pm10LeftAverage = getAverageYearPollution(leftChosenYear, "pm10");
            
            no2LeftHighest = getHighestPollution(leftChosenYear, "no2");
            pm25LeftHighest = getHighestPollution(leftChosenYear, "pm2.5");
            pm10LeftHighest = getHighestPollution(leftChosenYear, "pm10");

        
            leftTableContents=
            FXCollections.observableArrayList(
            new ComparisonTableContents("NO2", no2LeftAverage,no2LeftHighest),
            new ComparisonTableContents("pm2.5", pm25LeftAverage,pm25LeftHighest),
            new ComparisonTableContents("pm10", pm10LeftAverage,pm10LeftHighest)
            ); 
        }
        return leftTableContents;
    }
    
    /**
     * Creates a table that displays the average and highest pollution based on the chosen year from the right drop down.
     */
    private void rightTable(){
        rightTable = new TableView<>();
        rightTableContents = resetRightTableData();
        
        // table columns
        TableColumn pollutionType = new TableColumn("Pollutant");
        pollutionType.setCellValueFactory(new PropertyValueFactory<>("pollutionType"));
        pollutionType.setMinWidth(90);
        
        TableColumn averagePollutionLevel = new TableColumn("Average");
        averagePollutionLevel.setCellValueFactory(new PropertyValueFactory<>("averagePollution"));
        averagePollutionLevel.setMinWidth(90);
        
        TableColumn highestPollutionLevel = new TableColumn("Highest");
        highestPollutionLevel.setCellValueFactory(new PropertyValueFactory<>("highestPollution"));
        highestPollutionLevel.setMinWidth(90);
        
        // table contents
        rightTable.setItems(rightTableContents);
        rightTable.getColumns().addAll(pollutionType, averagePollutionLevel, highestPollutionLevel);
        
        rightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rightTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        rightTable.setPrefHeight(170);
    }
    
    /**
     * Returns the updated contents of the right comparison table.
     * @return an ObservableList with the updated contents of the right comparison table
     */
    private ObservableList<ComparisonTableContents> resetRightTableData(){
        if (leftChosenYear != null  && rightChosenYear != null && leftChosenYear != rightChosenYear){
            no2RightAverage = getAverageYearPollution(rightChosenYear, "no2");
            pm25RightAverage = getAverageYearPollution(rightChosenYear, "pm2.5");
            pm10RightAverage = getAverageYearPollution(rightChosenYear, "pm10");
            
            no2RightHighest = getHighestPollution(rightChosenYear, "no2");
            pm25RightHighest = getHighestPollution(rightChosenYear, "pm2.5");
            pm10RightHighest = getHighestPollution(rightChosenYear, "pm10");

        
            rightTableContents=
            FXCollections.observableArrayList(
            new ComparisonTableContents("NO2", no2RightAverage,no2RightHighest),
            new ComparisonTableContents("pm2.5", pm25RightAverage,pm25RightHighest),
            new ComparisonTableContents("pm10", pm10RightAverage,pm10RightHighest)
            ); 
        }
        return rightTableContents;
    }
    
    /**
     * Creates a table that displays the difference between the two selected years' average and highest pollution levels.
     */
    private void differenceTable(){
        differenceTable = new TableView<>();
        differenceTableContents = resetDifferenceTableData();
        
        // table columns
        TableColumn pollutionType = new TableColumn("Pollutant");
        pollutionType.setCellValueFactory(new PropertyValueFactory<>("pollutionType"));
        pollutionType.setMinWidth(90);
        
        TableColumn averagePollutionLevel = new TableColumn("Average Pollution Difference");
        averagePollutionLevel.setCellValueFactory(new PropertyValueFactory<>("averagePollution"));
        averagePollutionLevel.setMinWidth(90);
        
        TableColumn highestPollutionLevel = new TableColumn("Highest Pollution Difference");
        highestPollutionLevel.setCellValueFactory(new PropertyValueFactory<>("highestPollution"));
        highestPollutionLevel.setMinWidth(90);
        
        // table contents
        differenceTable.setItems(rightTableContents);
        differenceTable.getColumns().addAll(pollutionType, averagePollutionLevel, highestPollutionLevel);
        
        differenceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        differenceTable.setPrefWidth(Region.USE_COMPUTED_SIZE);
        differenceTable.setPrefHeight(170);
    }
    
    /**
     * Returns the updated contents of the difference table.
     * @return an ObservableList with the updated contents of the difference table
     */
    private ObservableList<ComparisonTableContents> resetDifferenceTableData(){
        if (leftChosenYear != null  && rightChosenYear != null && leftChosenYear != rightChosenYear){
            double no2AverageDifference = Math.abs(no2LeftAverage - no2RightAverage);
            double pm25AverageDifference = Math.abs(pm25LeftAverage - pm25RightAverage);
            double pm10AverageDifference = Math.abs(pm10LeftAverage - pm10RightAverage);
            
            double no2HighestDifference = Math.abs(no2LeftHighest - no2RightHighest);
            double pm25HighestDifference = Math.abs(pm25LeftHighest - pm25RightHighest);
            double pm10HighestDifference = Math.abs(pm10LeftHighest - pm10RightHighest);

        
            differenceTableContents=
            FXCollections.observableArrayList(
            new ComparisonTableContents("NO2", no2AverageDifference,no2HighestDifference),
            new ComparisonTableContents("pm2.5", pm25AverageDifference,pm25HighestDifference),
            new ComparisonTableContents("pm10", pm10AverageDifference,pm10HighestDifference)
            ); 
        }
        return differenceTableContents;
    }
    
    
    /**
     * Returns the DataPoint with the highest pollution level of a given year and pollutant
     * @param year the year you want to find at the data for
     * @param pollutant the pollutant you want to find the data for
     * @return the DataPoint with the highest pollution level for the given year and pollutant
     */
    private double getHighestPollution(String year, String pollutant){
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
        
        return highestPollutionLevel;
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
     * Creates and displays an alert informing the user that their selected years are incorrect.
     */
    private void dropDownAlert(){
        Alert incorrectDropDownAlert = new Alert(AlertType.INFORMATION);
        incorrectDropDownAlert.setTitle("Incorrect Selection");
        incorrectDropDownAlert.setContentText("Please select two different years.");
        DialogPane dialogPane = incorrectDropDownAlert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-alert");
        
        incorrectDropDownAlert.show();
    }
    
    /**
     * Displays text about the pollution graph functionality.

     * @return a string describing how to use the pollution graph page
     */
    public String getInfoText(){
        String infoString = "Choose two different years and press the compare button to \nsee data for both years and their differences.";
        return infoString;
    }
    // Getters
    public ComboBox<String> getLeftYearDropDown(){
        return leftYearDropDown;
    }
    
    public ComboBox<String> getRightYearDropDown(){
        return rightYearDropDown;
    }
    
    public TableView<ComparisonTableContents> getLeftTable(){
        return leftTable;
    }
    
    public TableView<ComparisonTableContents> getRightTable(){
        return rightTable;
    }
    
    public BorderPane getMainPane(){
        return mainPane;
    }
    
    public GridPane getGridPane(){
        return gridPane;
    }
    
    public VBox getLeftVBox(){
        return leftVBox;
    }
    
    public VBox getRightVBox(){
        return rightVBox;
    }
    
    public Button getCompareButton(){
        return compareButton;
    }
    
    public BorderPane getLeftBorderPane(){
        return leftBorderPane;
    }
    
    public BorderPane getRightBorderPane(){
        return rightBorderPane;
    }
}
