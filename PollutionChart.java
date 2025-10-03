import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;


/**
 * PollutionChart is an abstract parent class for creating bar charts 
 * that display average pollutant values over multiple years.
 *
 * It uses a DataFiles instance to retrieve filtered DataPoints.
 * 
 * @author Sara Ajdini
 * @version 1.0
 */
public abstract class PollutionChart
{
    private BorderPane rootPane;
    protected DataFiles data;
    protected BarChart bc;
    private List<DataPoint> dataPointsList;
    
    private ComboBox cityComboBox;
    private final ArrayList<String> years;
    private final ArrayList<String> pollutants;
    
    private XYChart.Series<String, Number> series;
    /**
     * Constructor for objects of class PollutionChart.
     * 
     * @param subclass instance of DataFiles to be used for data retrieval.
     */
    public PollutionChart(DataFiles data)
    {
        this.data=data; 

        rootPane = new BorderPane();
        
        years = new ArrayList<>();
        Collections.addAll(years,"2018", "2019", "2020", "2021", "2022", "2023");
        
        pollutants = new ArrayList<>();
        Collections.addAll(pollutants,"NO2", "PM10", "PM2.5");
                
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        bc = new BarChart<String,Number>(xAxis,yAxis);
        
        setTitle();
        
        xAxis.setLabel("Pollutant Type");       
        yAxis.setLabel("Average Pollutant"); 
        
        initaliseChart();

        rootPane.setCenter(bc);
    }
    
    /**
     * Abstract method to retrieve a list of DataPoints for a given 
     * year and pollutant.
     * 
     * @param the data of that year
     * @param the data of that pollutant type 
     * @return a list of DataPoints
     */
    public abstract List<DataPoint> getDataPoints(String year, String pollutant);
    
    /**
     * Calculates the average pollutant value for the given year and pollutant.
     * 
     * @param year the year to filter data by.
     * @param pollutant the pollutant type to filter data by.
     * @return the average pollutant value, or 0 if the list is empty.
     */
    public double getPollutantData(String year, String pollutant){
        dataPointsList = getDataPoints(year,pollutant);
        double totalPollutantValue = 0;
        
        // If the list is empty return 0
        if (dataPointsList.isEmpty()){
            return 0;
        }
        
        // Sum the pollutant values
        for (DataPoint dataPoint: dataPointsList){
            totalPollutantValue += dataPoint.value();
        }
        
        // Return the average pollutant value
        return totalPollutantValue/dataPointsList.size();
    }
    
    /**
     * Adds chart data to the bar chart for each year and pollutant.
     * Only valid entries are added.
     */
    public void addChartData(){
        for (String year: years){
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(year);
            for(String pollutant : pollutants){
                if (isValidEntry(year,pollutant)){
                    double totalPollutantValue = getPollutantData(year,pollutant);
                    series.getData().add(new XYChart.Data<>(pollutant, totalPollutantValue));
                }
            }
            bc.getData().add(series);
        }
    }
    
    /**
     * Initialises the chart by adding chart data and setting
     * spacing and layout.
     */
    public void initaliseChart(){
        addChartData();
        
        bc.setBarGap(5);
        bc.setCategoryGap(30);  

        rootPane.setCenter(bc);
    }
    
    /**
     * Checks if the given year and pollutant are valid entries.
     * 
     * @param year the year to validate.
     * @param pollutant the pollutant type to validate.
     * @return true if both are valid years and pollutants.
     * @throws IllegalArgumentException if year or pollutant is null or empty.
     */
    public boolean isValidEntry(String year, String pollutant){
        if (year == null || year.isEmpty()) {
            throw new IllegalArgumentException("Cannot set year to null or empty value");
        }
        
        if (pollutant == null || pollutant.isEmpty()) {
            throw new IllegalArgumentException("Cannot set pollutant to null or empty value");
        }
        
        pollutant = pollutant.toUpperCase();
        
        // Return true only if both are in the valid lists.
        return years.contains(year) && pollutants.contains(pollutant);
    }
    
    /**
     * Sets the default title for the bar chart.
     */
    public void setTitle(){
        bc.setTitle("Pollution Levels");
    }
    
     //-------------------METHODS ADDED FOR TESTING-------------------\\

    /**
     * Returns the root pane containing the chart.
     * 
     * @return the BorderPane that is the root of the chart UI.
     */
    public BorderPane getRootPane() {
        return rootPane;
    }
    
    /**
     * Returns the BarChart used for displaying data.
     * 
     * @return the BarChart instance.
     */
    public BarChart getBarChart() {
        return bc;
    }
    
    /**
     * Returns the DataFiles instance used for data retrieval.
     * 
     * @return the data source.
     */
    public DataFiles getData() {
        return data;
    }
    
    /**
     * Returns the list of valid years.
     * 
     * @return an ArrayList of year strings.
     */
    public ArrayList<String> getYears() {
        return years;
    }
    
    /**
     * Returns the list of valid pollutant types.
     * 
     * @return an ArrayList of pollutant strings.
     */
    public ArrayList<String> getPollutants() {
        return pollutants;
    }
    
    /**
     * Displays text about the charts.
     * 
     * @return a string describing how to use the pollution graph page
     */
    public String getInfoText(){
        String infoString = "Scroll sideways to view charts for 3 different locations and their data.";
        return infoString;
    }
}