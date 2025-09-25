import java.util.List;

/**
 * LeedsPollutionChart creates a bar chart for Leeds pollution levels.
 * It retrieves filtered data points within Leeds and sets a title.
 * 
 *
 * @author Sara Ajdini
 * @version 1
 */
public class LeedsPollutionChart extends PollutionChart
{
    // The city name for this chart.
    private final String city = "Leeds";
    /**
     * Constructor for LeedsPollutionChart.
     * 
     * @param data A instance of DataFiles used for 
     * retrieving pollution data.
     */
    public LeedsPollutionChart(DataFiles data)
    {
        super(data);
    }
    
    /**
     * Retrieves the filtered data points for a given year and
     * pollutant for Leeds.
     * 
     * @param the data of that year
     * @param the data of that pollutant type 
     * @return a list of DataPoints
     */
    public List<DataPoint> getDataPoints(String year, String pollutant){
         return data.getFilteredDataPoints(year,pollutant,city);
    }
    
    /**
     * Overrides and Sets the chart title for Leeds Pollution Levels
     */
    @Override
    public void setTitle(){
        bc.setTitle("Leeds Pollution Levels");
    }
}
