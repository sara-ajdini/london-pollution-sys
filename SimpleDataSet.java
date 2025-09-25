import java.util.List;
import java.util.ArrayList;

/**
 * The Simple Data Set is a simplified version of the Data Set containing only the information the rest of the classes will use.
 * 
 * @author Michael Kölling
 * @version 1.0
 */
public class SimpleDataSet
{
    private String pollutant;
    private String year;
    private List<DataPoint> data;

    /**
     * Constructor for objects of class DataSet
     */
    public SimpleDataSet(String pollutant, String year)
    {
        this.pollutant = pollutant;
        this.year = year;
        
        data = new ArrayList<DataPoint>();
    }

    /**
     * Return the pollutant information for this dataset.
     */
    public String getPollutant()
    {
        return pollutant;
    }
    
    /**
     * Return the year information for this dataset.
     */
    public String getYear()
    {
        return year;
    }
    
    /**
     * Return the data points of this dataset.
     */
    public List<DataPoint> getData()
    {
        return data;
    }
    
    public void addData(DataPoint dataPoint){
        data.add(dataPoint);
    }
}
