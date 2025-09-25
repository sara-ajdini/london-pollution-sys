import java.util.List;

/**
 * Contains methods and data specific to the leeds map.
 *
 * @author Layla Abdelmaksoud & Sara Ajdini
 * @version 1.0
 */
public class LeedsMapPage extends MapPage
{
    private final String city = "Leeds";
    public LeedsMapPage(DataFiles dataFiles){
        super(dataFiles);
    }
        
    /**
     * returns Leeds data points for current year and pollutant 
     * 
     * @return List of datapoints
     */
    public  List<DataPoint> getDataPoints(){
        return dataFiles.getFilteredDataPoints(year,pollutant,city);
    }

    @Override
    protected  String getMapImagePath(){
        return "Leeds.png";
    }
    
    @Override
    protected int getOriginX() {
        return 408304;
    }

    @Override
    protected int getOriginY() {
        return 420952;
    }
    
    @Override
    protected int getMaxX() {
        return 451385;
    }

    @Override
    protected int getMaxY() {
        return 447012;
    }
    
}
