import javafx.application.Application;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Comparator;
import java.util.PriorityQueue;

import javafx.geometry.Insets;
import javafx.collections.FXCollections;

/**
 * Map page contains a map canvas with marks drawn on top to mark different coordinates.
 * All elements within the map tab are created and used here.
 * All methods associated with the map tab are in this class.
 *
 * @author Layla Abdelmaksoud
 * @version 1.0
 */
public abstract class MapPage {
    protected double scaleX, scaleY;
    protected HashMap<Rectangle, DataPoint> marks;
    
    protected String year, pollutant;
    protected Image mapImage;
    protected Canvas map;
    
    protected BorderPane rootPane;
    protected ScrollPane scrollPane;
    protected Pane pane;
    
    protected ComboBox pollutantComboBox, yearComboBox;
    protected CheckBox highlyPollutedCheckBox;
    protected HBox filters;
    private HBox airQualityBox = new HBox(); 
    protected Button updateMapButton;
    protected Tooltip tooltip;
    
    protected TabController tabController;
    protected PollutionPanel pollutionPanel;
    
    protected DataFiles dataFiles;
    
    public MapPage(DataFiles dataFiles) {
        rootPane = new BorderPane();
        marks = new HashMap<>();
        mapImage = new Image(getMapImagePath());
        this.dataFiles = dataFiles;
        
             
        int coordsX = getMaxX() - getOriginX();
        scaleX = mapImage.getWidth() / coordsX;        
    
        int coordsY = getMaxY() - getOriginY();
        scaleY =  mapImage.getHeight()/ coordsY;
        setupUI();   
    }
    
    /**
     * creates all GUI elements for map
     */
    private void setupUI() {
        // initalise components
        rootPane = new BorderPane();
        marks = new HashMap<>();
       
        //creates map canvas
        map = new Canvas(mapImage.getWidth(),mapImage.getHeight());
        GraphicsContext gc = map.getGraphicsContext2D();
        gc.drawImage(mapImage,0,0);
    
        //filters
        yearComboBox = new ComboBox(FXCollections.observableArrayList("2018", "2019", "2020", "2021", "2022", "2023"));
        pollutantComboBox = new ComboBox(FXCollections.observableArrayList("no2", "pm2.5", "pm10"));
    
        yearComboBox.setValue("2023");
        pollutantComboBox.setValue("no2");
    
        year = (String) yearComboBox.getValue();
        pollutant = (String) pollutantComboBox.getValue();
    
        highlyPollutedCheckBox = new CheckBox("View most polluted areas only");
        updateMapButton = new Button("Update Map");

        filters= new HBox(new Label("Year: "),yearComboBox,new Label(" Pollutant: "),pollutantComboBox,new Label("\t"),updateMapButton,new Label("\t\t"), highlyPollutedCheckBox);
        filters.setPadding(new Insets(10,10,10,10));
        filters.getStyleClass().add("hbox");
    
        //creating panes
        pane = new Pane(map);
        pane.prefWidthProperty().bind(map.widthProperty());
        pane.prefHeightProperty().bind(map.heightProperty());
        paintMarks(pane);
        
        //buttons depending on pane
        updateMapButton.setOnAction((ActionEvent ev) -> {  clearMap(pane);
                                                           paintMarks(pane);}); 
    
        highlyPollutedCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {clearMap(pane);   
                                                                                                 paintMarks(pane); });                                                    
        
        scrollPane = new ScrollPane(pane);
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(filters, airQualityBox);
        rootPane.setTop(topContainer);

        rootPane.setCenter(scrollPane);     
    }
    
    
    /**
     * Clears marks hashmap and pane of all marks
     * 
     * @param Pane p  pane to be cleared
     */
    protected void clearMap(Pane p){
        year =(String) yearComboBox.getValue();
        pollutant =(String) pollutantComboBox.getValue();
        
        marks.clear();
        //clears all marks on the map
        for(int i = p.getChildren().size() - 1; i >= 0; i--) {
        if (p.getChildren().get(i) instanceof Rectangle) {
        p.getChildren().remove(i);
        }
        }
    }
    
    /**
     * iterates through data points 
     * calls drawMark for each datapoint in the list
     */
    public void paintMarks(Pane p){
        List<DataPoint> dataPoints = getDataPoints();
        if(highlyPollutedCheckBox.isSelected()){
            dataPoints = getHighestPollution(dataPoints);
        }
        
        for(DataPoint dp : dataPoints){
            drawMark(dp,p);
        }
    }
    
    /**
     * draw a mark for a given dataPoint.
     * 
     * @param dp  DataPoint to be drawn for
     * @param p Pane to be drawn on
     */
    protected void drawMark(DataPoint dp, Pane p) {
        double x = (dp.x() - getOriginX()) * scaleX;
        double y = (dp.y() - getOriginY()) * scaleY;
        Rectangle mark;
        if(getMapImagePath()=="London.png"){
             mark = new Rectangle(x, y, 41.5,44.5);
        }
        else if(getMapImagePath()=="Oxford.png"){
             mark = new Rectangle(x, y, 129,68);
        }
        else{
             mark = new Rectangle(x, y, 41,42);
        }
        mark.setFill(getMarkColor(dp.value()));
        p.getChildren().add(mark);
        marks.put(mark, dp);
        tooltip = new Tooltip("Coords: " + dp.x() + "," + dp.y() + "\nGridcode: " + dp.gridCode() + "\nPollution Level: " + dp.value());
        Tooltip.install(mark, tooltip);
        mark.setOnMouseEntered(e -> mark.setOpacity(0.5));
        mark.setOnMouseEntered(e -> mark.setStroke(Color.WHITE));
        mark.setOnMouseExited(e -> mark.setOpacity(1));
        mark.setOnMouseExited(e -> mark.setStroke(Color.TRANSPARENT));
        if(getMapImagePath()=="London.png"){
            mark.setOnMouseClicked(MouseEvent -> { tabController.nextTab();
                                                    pollutionPanel.updateFilter(dp.gridCode(), pollutant, year);
                                                    });
        }
    }
    
    
    /**
     * determines mark color based off of pollutant type and value
     * @param value  pollution level
     */
    protected Color getMarkColor(double value){
        
        //colors are relative to other london points
        if(pollutant.toLowerCase()=="no2"){
            if(value<=15){
            return Color.web("rgba(0,255,0,0.3)");  //green
            }
            else if(value>15 && value<=20){
            return Color.web("rgba(255,255,0,0.3)");    //yellow
            }
            else if(value>20 && value<=25){
            return Color.web("rgba(255,165,0,0.3)");    //orange
            }
            else{
            return Color.web("rgba(255,0,0,0.3)");  //red
            }
        }
        else if(pollutant.toLowerCase()=="pm10"){
            if(value<=15){
            return Color.web("rgba(0,255,0,0.3)"); //green
            }
            else if(value>15 && value<=17){
            return Color.web("rgba(255,255,0,0.3)");    //yellow
            }
            else if(value>17 && value<=19){
            return Color.web("rgba(255,165,0,0.3)");    //orange
            }
            else{
            return Color.web("rgba(255,0,0,0.3)");  //red
            }
        }
        else if(pollutant.toLowerCase()=="pm2.5"){   //pm2.5
            if(value<=8){
            return Color.web("rgba(0,255,0,0.3)");  //green
            }
            else if(value>8 && value<=10){
            return Color.web("rgba(255,255,0,0.3)");    //yellow
            }
            else if(value>10 && value<=12){
            return Color.web("rgba(255,165,0,0.3)");    //orange
            }
            else{
            return Color.web("rgba(255,0,0,0.3)");  //red
            }            
        }
        return Color.web("rgba(0,0,255,0.3)");  //if undefined, color is blue
    }
    
    
    /**
     * Gets highest 100 points in the list of datapoints
     * avoids unnecessary sorting of the whole list using minheap
     * 
     * @param dataPoints list of all datapoints 
     */
    protected List<DataPoint> getHighestPollution(List<DataPoint> dataPoints) {
        PriorityQueue<DataPoint> minHeap = new PriorityQueue<>(Comparator.comparingDouble(DataPoint::value));
        for (DataPoint dp : dataPoints) {
            minHeap.offer(dp);
            if (minHeap.size() > 100) minHeap.poll();
        }
        return new ArrayList<>(minHeap);
    }
    
    
    /**
     * imports the tabcontroller to be used
     * @param tc  tabcontroller
     */
    public void importTabController(TabController tc){
        tabController = tc;
    }
    
    /**
     * imports the PollutionPanel to be used by map marks
     * @param pp  PollutionPanel 
     */
    public void importPollutionPanel(PollutionPanel pp){
        pollutionPanel = pp;
    }
    
    /**
     * Displays text about the map functionality.
     * 
     * @return a string describing how to use the map page
     */
    public String getInfoText(){
        String infoString = "Use the filters to see different pollution levels of London based \non year and pollutant type, click 'Update Map' to see changes. \nTicking the checkbox shows only the 100 most \npolluted data points on the map\nUse the city dropdown to change city displayed.";
        return infoString;
    }
    
    public abstract List<DataPoint> getDataPoints();
    
    protected abstract String getMapImagePath();
    
    protected abstract int getOriginX();
    
    protected abstract int getOriginY();
    
    protected abstract int getMaxX();
    
    protected abstract int getMaxY();
    
    //METHODS ADDED FOR TESTING
    /**
     * Sets the pollutant type for the map page
     * 
     * @param newPollutant the pollutant type to be set
     */
    public void setPollutant (String newPollutant){
        List<String> validPollutants = new ArrayList<>(Arrays.asList("pm10", "no2", "pm2.5"));
        if (newPollutant == null){
        throw new IllegalArgumentException("Cannot set pollutant to null value");
        }
        newPollutant = newPollutant.toLowerCase();
        if (validPollutants.contains(newPollutant)){
        pollutant = newPollutant;
        }
        else{
        throw new IllegalArgumentException("Cannot set pollutant to invalid value");
        }
    }
    
    /**
     * Returns the filters HBox containing UI elements for selecting pollution data
     * 
     * @return the HBox that includes year selection, pollutant selection, and filtering options
     */
    public HBox getFiltersHBox(){
        return filters;
    }
    
    /**
     * Returns current pollutant
     * 
     * @return pollutant variable
     */
    public String getPollutant(){
        return pollutant;   
    }
    
    /**
     * Returns the root pane variable of the map page
     * 
     * @return the root pane containing all UI components
     */
    public Pane getRootPane(){
        return rootPane;
    }
    
    /**
     * Returns the scroll pane variable of the map page
     * 
     * @return the scroll pane used for navigating the map
     */
    public ScrollPane getScrollPane(){
        return scrollPane;
    }
    
    /**
     * Returns the hashmap storing the pollution data points
     * 
     * @return a hashmap mapping Rectangle markers to DataPoint objects
     */
    public HashMap getMarks(){
    return marks;
    }
    
    /**
     * Returns the tooltip used for displaying data point information
     * 
     * @return the tooltip instance attached to data point markers
     */
    public Tooltip getTooltip(){
        return tooltip;
    }
    
    /**
     * Returns the year combo box used for selecting pollution data by year
     * 
     * @return the combo box for year selection
     */
    public ComboBox getYearComboBox(){
        return yearComboBox;
    }
    
    /**
     * Returns the pollutant combo box used for selecting pollution type
     * 
     * @return the combo box for pollutant selection
     */
    public ComboBox getPollutantComboBox(){
        return pollutantComboBox;
    }
    
    /**
     * Returns the checkbox for filtering highly polluted areas
     * 
     * @return the checkbox instance used for enabling or disabling the filter
     */
    public CheckBox getPollutedCheckBox(){
        return highlyPollutedCheckBox;
    }
    
    /**
     * Returns the button used to update the map view
     * 
     * @return the button that triggers map updates based on selected filters
     */
    public Button getUpdateMapButton(){
        return updateMapButton;
    }
    
    /**
     * Returns the canvas on which the map image and pollution markers are drawn
     * 
     * @return the canvas used for rendering the map and data points
     */
    public Canvas getMapCanvas(){
        return map;
    }
    
    /**
     * Returns the tab controller managing tab switching within the application
     * 
     * @return the tab controller instance
     */
    public TabController getTabController(){
        return tabController;
    }
    
    /**
     * Returns the hbox storing api info
     * 
     * @return hbox for api data
     */
    public HBox getApiBox() {
    return airQualityBox;
    }   

}