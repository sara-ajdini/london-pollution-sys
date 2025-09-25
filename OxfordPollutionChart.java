import java.util.List;

/**
 * OxfordPollutionChart creates a bar chart for Oxford pollution levels.
 * It retrieves filtered data points within Oxford and sets a title.
 * 
 *
 * @author Sara Ajdini
 * @version 1
 */
public class OxfordPollutionChart extends PollutionChart
{
    // The city name for this chart.   
    private final String city = "Oxford";
    /**
     * Constructor for OxfordPollutionChart.
     * 
     * @param data A instance of DataFiles used for 
     * retrieving pollution data.
     */
    public OxfordPollutionChart(DataFiles data)
    {
        super(data);
    }
    
    /**
     * Retrieves the filtered data points for a given year and
     * pollutant for Oxford.
     * 
     * @param the data of that year
     * @param the data of that pollutant type 
     * @return a list of DataPoints
     */
    public List<DataPoint> getDataPoints(String year, String pollutant){
         return data.getFilteredDataPoints(year,pollutant,city);
    }

    /**
     * Overrides and Sets the chart title for Oxford Pollution Levels
     */
    @Override
    public void setTitle(){
        bc.setTitle("Oxford Pollution Levels");
    }
}
