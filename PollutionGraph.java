import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import java.util.*;
import java.util.Map;
import java.io.*;
import javafx.util.StringConverter;


/**
 * This creates the graph of pollution over time, updates it, and gathers the data for the graph.
 *
 * @author Muhlisa Husainova
 * @version 17/03/2025
 */
public class PollutionGraph {
    //constructor variables 
    private BorderPane rootPane;
    private LineChart<Number, Number> lineChart; //we make the graph with this
    private TreeMap<String, Double> pollutionLevels; //stores the data to be plotted on the graph
    private String pollutant;//stores the current pollutant selected by user
    private int gridcode;//stores current gridcode given by user

    public PollutionGraph(String paramPollutant, int paramGridcode, DataFiles dataFiles) {
        setPollutant(paramPollutant);
        setGridcode(paramGridcode);
        pollutionLevels = getPollutionData(dataFiles);
        
        VBox filterPane = new VBox();
        filterPane.setPadding(new Insets(10, 10, 10, 10));
        
        // create graph
        createGraph();

        // set layout for the root pane
        rootPane = new BorderPane();
        rootPane.setTop(filterPane);
        rootPane.setCenter(lineChart); 
    }

    public Pane getRootPane() {
        //returns the root pane for the main gui class
        return rootPane;
    }
    
    /**
     * Creates a series and goes through all the entries in the Treemap and adds them to the series.
     * This will appear as a line in the graph
     */
    private XYChart.Series<Number, Number> createDataSeries() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Pollutant: " + pollutant + " at GridCode " + gridcode);
    
        for (Map.Entry<String, Double> entry : pollutionLevels.entrySet()) 
        {
            int year = Integer.parseInt(entry.getKey());
            double pollutionValue = entry.getValue();
    
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(year, pollutionValue);
            series.getData().add(dataPoint);
        }
    
        return series;
    }

    /**
     * Creates the initial graph
     */
    private void createGraph() {
        // Create axes
        NumberAxis xAxis = new NumberAxis(2018, 2023, 1);
        xAxis.setLabel("Year");
        
        // Custom formatter to display the number as an integer without the commas 
        xAxis.setTickLabelFormatter(new StringConverter<Number>() 
        {
            @Override
            public String toString(Number object) 
            {
                return String.format("%.0f", object.doubleValue());  // No decimals or commas, written like 2020, 2021 instead of 2,021...
            }
        
            @Override
            public Number fromString(String string) 
            {
                return Double.parseDouble(string);
            }
        });
       
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Pollution Level");
       
        // Create the LineChart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Pollution Levels Over Time");
       
         // Get the series from the helper
        XYChart.Series<Number, Number> series = createDataSeries();
   
        // Add series to chart
        lineChart.getData().add(series);

    }
    
    /**
     * gets the data to be plotted 
     */
    public TreeMap<String, Double> getPollutionData(DataFiles dataFiles)
    {
        pollutionLevels = new TreeMap<>();
        File rootDir = new File("UKAirPollutionData");
        List<String> allFiles = new ArrayList<>();
        List<String> pollutionDataFiles = dataFiles.findFiles(rootDir,allFiles); //get all file paths
        
        List<String> filteredDataFiles = new LinkedList<>(); //stores the filtered files for the specified pollutant
    
        for (String filePath : pollutionDataFiles){
            if (filePath.toLowerCase().contains(pollutant.toLowerCase()))
            {
                filteredDataFiles.add(filePath);
            }
        }
        
        DataLoader loader = new DataLoader();
        
        for (String dataSetName : filteredDataFiles)//loop through filtered files
        {
            DataSet dataSet = loader.loadDataFile(dataSetName); //gets info about the file
            String year = dataSet.getYear();//get the year of the file
            for(DataPoint dp : dataSet.getData())//loop through all data points in the file
            {
                int dpGridCode = dp.gridCode();//get the gridcode 
                double pollutionValue = dp.value();//get pollution value for the specific data point
                if(dpGridCode == gridcode)//if the gridcode is the one we're searching for...
                {
                    pollutionLevels.put(year, pollutionValue);//...store this file's year and the pollution value of this data point in pollutionLevels hashmap
                }
            }
        }
        
        return pollutionLevels;
    }
    
    /**
     * Updates the graph when the user selects different factors.
     */
    public void updateGraph(String updatePollutant, int updateGridcode, DataFiles dataFiles)
    {
        setPollutant(updatePollutant);//set the new pollutant
        setGridcode(updateGridcode);//set the new gridcode

        pollutionLevels = getPollutionData(dataFiles); //get new data for the graph

        lineChart.getData().clear();//clear the old graph
        
        //plot the new graph
        XYChart.Series<Number, Number> series = createDataSeries();
        lineChart.getData().add(series);
    }
    
    /**
     * Plots two series (lines) on a graph to compare pollutants over time.
     */
    public void compareGraphs(String pollutant1, int gridcode1, String pollutant2, int gridcode2, DataFiles dataFiles)
    {
        setPollutant(pollutant1);
        setGridcode(gridcode1);
        pollutionLevels = getPollutionData(dataFiles);
        XYChart.Series<Number, Number> series1 = createDataSeries();
        series1.setName(pollutant1 + " at gridcode " + gridcode1);
        
        setPollutant(pollutant2);
        setGridcode(gridcode2);
        pollutionLevels = getPollutionData(dataFiles);
        XYChart.Series<Number, Number> series2 = createDataSeries();
        series2.setName(pollutant2 + " at gridcode " + gridcode2);
        
        lineChart.getData().clear();
        lineChart.getData().addAll(series1, series2);
    }
    
    /**
     * sets the pollutant variable
     * @param newPollutant new pollutant that the user selects
     */
    public void setPollutant(String newPollutant)
    {
        pollutant = newPollutant;
    }
    
    /**
     * sets the gridcode variable
     * @param newGridcode new gridcode that the user selects
     */
    public void setGridcode(int newGridcode)
    {
        gridcode = newGridcode;
    }
}
