
/**
 * Contains all the information that the comparison tables should contain.
 *
 * @author Maria Plesinska
 * @version 1.0
 */
public class ComparisonTableContents
{
    private String pollutionType;
    private double averagePollution;
    private double highestPollution;

    /**
     * Constructor for objects of class ComparisonTableContents
     */
    public ComparisonTableContents(String pollutionType, double averagePollution, double highestPollution)
    {
        this.pollutionType = pollutionType;
        this.averagePollution = averagePollution;
        this.highestPollution = highestPollution;
    }

    // Getters
    public String getPollutionType(){
        return pollutionType;
    }
    
    public double getAveragePollution(){
        return averagePollution;
    }
    
    public double getHighestPollution(){
        return highestPollution;
    }
    
    // Setters
    public void setPollutionType(String pollutionType){
        this.pollutionType = pollutionType;
    }
    
    public void setAveragePollution(double averagePollution){
        this.averagePollution = averagePollution;
    }
    
    public void setHighestPollution(double highestPollution){
        this.highestPollution = highestPollution;
    }
}
