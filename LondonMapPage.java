import java.util.List;

/**
 * Contains methods and data specific to the london map.
 *
 * @author Layla Abdelmaksoud
 * @version 1.0
 */
public class LondonMapPage extends MapPage
{
    private final String city = "London";
    public LondonMapPage(DataFiles dataFiles){
        super(dataFiles);
    }
    
    /**
     * returns London data points for current year and pollutant 
     * 
     * @return List of datapoints
     */
    public  List<DataPoint> getDataPoints(){
        return dataFiles.getFilteredDataPoints(year,pollutant,city);
    }

    @Override
    protected  String getMapImagePath(){
        return "London.png";
    }
    
    @Override
    protected int getOriginX() {
        return 510394;
    }

    @Override
    protected int getOriginY() {
        return 168504;
    }
    
    @Override
    protected int getMaxX() {
        return 553297;
    }

    @Override
    protected int getMaxY() {
        return 193305;
    }
}
    