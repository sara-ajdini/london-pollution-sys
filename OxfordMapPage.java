import java.util.List;

/**
 * Contains methods and data specific to the oxford map.
 *
 * @author Layla Abdelmaksoud & Sara Ajdini
 * @version 1.0
 */
public class OxfordMapPage extends MapPage

{    
    private final String city = "Oxford";
    public OxfordMapPage(DataFiles dataFiles){
        super(dataFiles);
        //this.dataFiles = dataFiles;
    } 
    
    /**
     * returns Oxford data points for current year and pollutant 
     * 
     * @return List of datapoints
     */
    public  List<DataPoint> getDataPoints(){
        return dataFiles.getFilteredDataPoints(year,pollutant,city);
    }

    @Override
    protected  String getMapImagePath(){
        return "Oxford.png";
    }
    
    @Override
    protected int getOriginX() {
        return 449416;
    }

    @Override
    protected int getOriginY() {
        return 201110;
    }
    
    @Override
    protected int getMaxX() {
        return 458115;
    }

    @Override
    protected int getMaxY() {
        return 211085;
    }
    
}