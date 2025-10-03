/**
 * Contains all the information that the highest pollution levels table should contain.
 *
 * @author Maria Plesinska 
 * @version 1.0
 */
public class HighestPollutionTableContents
{
    private String pollutionType;
    private double pollutionLevel;
    private int gridCode;
    private int x;
    private int y;
    
    /**
     * Constructor for objects of class PollutionTableContents
     */
    public HighestPollutionTableContents(String pollutionType, double pollutionLevel, int gridCode, int x, int y)
    {
        this.pollutionType = pollutionType;
        this.pollutionLevel = pollutionLevel;
        this.gridCode = gridCode;
        this.x = x;
        this.y = y;
    }

    // Getters
    public String getPollutionType(){
        return pollutionType;
    }
    
    public double getPollutionLevel(){
        return pollutionLevel;
    }
    
    public int getGridCode(){
        return gridCode;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    // Setters
    
    public void setPollutionType(String pollutionType){
        this.pollutionType = pollutionType;
    }
    
    public void setPollutionLevel(double pollutionLevel){
        this.pollutionLevel = pollutionLevel;
    }
    
    public void setGridCode(int gridCode){
        this.gridCode = gridCode;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }
}
