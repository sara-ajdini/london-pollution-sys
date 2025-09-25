import java.util.List;

/**
 * LondonPollutionChart creates a bar chart for London pollution levels.
 * It retrieves filtered data points within London and sets a title.
 * 
 *
 * @author Sara Ajdini
 * @version 1
 */
public class LondonPollutionChart extends PollutionChart
{
    // The city name for this chart.
    private final String city = "London";
    /**
     * Constructor for LondonPollutionChart.
     * 
     * @param data A instance of DataFiles used for 
     * retrieving pollution data.
     */
    public LondonPollutionChart(DataFiles data)
    {
        super(data);
    }
    
    /**
     * Retrieves the filtered data points for a given year and pollutant
     * for London.
     * 
     * @param the data of that year
     * @param the data of that pollutant type 
     * @return a list of DataPoints
     */
    public List<DataPoint> getDataPoints(String year, String pollutant){
         return data.getFilteredDataPoints(year,pollutant,city);
    }
    
    /**
     * Overrides and Sets the chart title for London Pollution Levels
     */
    @Override
    public void setTitle(){
        bc.setTitle("London Pollution Levels");
    }

}
