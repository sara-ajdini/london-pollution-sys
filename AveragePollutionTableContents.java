
/**
 * Contains all the information that the average pollution level tables should contain.
 *
 * @author Maria Plesinska
 * @version 1.0
 */
public class AveragePollutionTableContents
{
    private String pollutionType;
    private double pollutionLevel;

    /**
     * Constructor for objects of class AveragePollutionTableContents
     */
    public AveragePollutionTableContents(String pollutionType, double pollutionLevel)
    {
        this.pollutionType = pollutionType;
        this.pollutionLevel = pollutionLevel;
    }

    // Getters
    public String getPollutionType(){
        return pollutionType;
    }
    
    public double getPollutionLevel(){
        return pollutionLevel;
    }
    
    // Setters
    public void setPollutionType(String pollutionType){
        this.pollutionType = pollutionType;
    }
    
    public void setPollutionLevel(double pollutionLevel){
        this.pollutionLevel = pollutionLevel;
    }
}
