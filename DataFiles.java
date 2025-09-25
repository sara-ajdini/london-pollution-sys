import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Stores and sorts through all the csv files with the pollution statistics.
 *
 * @author Maria Plesinska
 * @version 1.4
 */
public class DataFiles
{
    List<String> pollutionDataFiles;
    List<SimpleDataSet> locationOnlyDataSets;
       
       
    /**
     * Constructor for objects of class DataFiles
     */
    public DataFiles()
    {
        pollutionDataFiles = new LinkedList<>();
        File directory = new File("UKAirPollutionData");
        pollutionDataFiles = findFiles(directory, pollutionDataFiles);
        locationOnlyDataSets = locationOnlyData(pollutionDataFiles);
    }
    

    /**
     * Searches through a directory including all subfolders
     * and returns a List of all the file names found.
     * @param directory the directory and its subfolders you want all the files from
     * @param files the List in which you want all the file names to be stored in
     * @return a list of all file names and their directory in a given directory and its subfolders
     */
    public List<String> findFiles(File directory, List<String> files){
        for (File file : directory.listFiles()){
            if (file.isDirectory()){
                findFiles(file, files);
            } else {
                String fileName = file.getName();
                files.add(directory + "/" + fileName);
            }
        }
        return files;
    }
    
    /**
     * Return only the data points relevant to the filters you choose
     * @param year the year you want to get the file of
     * @param pollutant the pollutant you want to get the file of
     * @return a list of DataPoints that are in location, and have the specified year and pollutant
     */
    public List<DataPoint> getFilteredDataPoints(String year, String pollutant, String location) {
        List<DataPoint> filteredDataPoints = new LinkedList<>();
        pollutant = pollutant.toLowerCase();
    
        for (SimpleDataSet dataSet : locationOnlyDataSets) {
            if (dataSet.getYear().equals(year) && dataSet.getPollutant().toLowerCase().equals(pollutant)) {
                dataSet.getData().stream()
                       .filter(dp -> isInLocation(dp, location))
                       .forEach(filteredDataPoints::add);
            }
        }
    
        return filteredDataPoints;
    }

    
    /**
     * Return only the data points relevant to the filters you choose
     * @param year the year you want to get the file of
     * @param pollutant the pollutant you want to get the file of
     * @return a list of DataPoints that are in location, and have the specified year and pollutant
     */
    public List<DataPoint> getFilteredDataPoints(String pollutant){
        List<String> filteredDataFiles = new LinkedList<>();
        
        List<DataPoint> filteredDataPoints = new LinkedList<>();
        pollutant = pollutant.toLowerCase();
        
        for (SimpleDataSet dataSet : locationOnlyDataSets){
            if (dataSet.getPollutant().toLowerCase().equals(pollutant)){
                for (DataPoint dataPoint : dataSet.getData()){
                    filteredDataPoints.add(dataPoint);
                }
            }
        }
        return filteredDataPoints;
    }
    
    /**
     * Return only the data points relevant to the filters you choose
     * @param pollutant the pollutant you want to get the file of
     * @return a list of DataPoints that are in location, and have the specified pollutant
     */
    public List<DataPoint> getFilteredLocationDataPoints(int gridCode, String pollutant){
        List<DataPoint> dataPoints = new LinkedList<>();
        dataPoints = getFilteredDataPoints(pollutant);
        
        List<DataPoint> filteredDataPoints = new LinkedList<>();
        
        boolean found = false;
        int i = 0;
        while (!found && i < dataPoints.size()){
            if (dataPoints.get(i).gridCode() == gridCode){
                filteredDataPoints.add(dataPoints.get(i));
            }
            i++;
        }
        return filteredDataPoints;
    }
    
    /**
     * Returns a list of simple data sets with only the datapoints of the chosen location.
     * @param files The list of all files
     * @return a list of all the simple data sets with only the data points in the chosen location
     */
    public List<SimpleDataSet> locationOnlyData(List<String> files) {
        DataLoader loader = new DataLoader();
        List<SimpleDataSet> locationOnlyDataSet = new LinkedList<>();
    
        files.parallelStream()
             .map(file -> loader.loadDataFile(file))
             .map(dataSet -> filterDataSet(dataSet, "all"))  
             .forEach(locationOnlyDataSet::add);
    
        return locationOnlyDataSet;
    }


    /**
     * Returns a simple data set with only the data points in the chosen location.
     * @param dataSet the data set to search through
     * @return a simple data set of only the data points in the given location
     */
    public SimpleDataSet filterDataSet(DataSet dataSet, String location) {
        SimpleDataSet locationOnlyDataSet = new SimpleDataSet(dataSet.getPollutant(), dataSet.getYear());
        dataSet.getData().stream()
               .filter(dp -> isInLocation(dp, location))
               .forEach(locationOnlyDataSet::addData);
        return locationOnlyDataSet;
    }

    
    /**
     * Returns true if the dataPoint is in the given coordinates
     * @param dp the dataPoint to check
     * @return true if the dataPoint is in the given location
     */
    protected  boolean isInLocation (DataPoint dp,String location){
        return switch (location.toLowerCase()) {
                    case "london" -> isInLondon(dp);
                    case "leeds" -> isInLeeds(dp);
                    case "oxford" -> isInOxford(dp);
                    default -> isInLondon(dp) || isInLeeds(dp) || isInOxford(dp);  };
    }
    
    /**
     * Checks if a DataPoint is in London coords
     * 
     * @param dp DataPoint to be checked
     * @return true if in London
     */
    protected boolean isInLondon (DataPoint dp)
    {
        return (dp.x() > 510394 && dp.x() < 553297 && dp.y() > 168504 && dp.y() < 193305);
    }
    
    /**
     * Checks if a DataPoint is in Leeds coords
     * 
     * @param dp DataPoint to be checked
     * @return true if in Leeds
     */
    protected boolean isInLeeds (DataPoint dp)
    {
        return (dp.x() > 408304 && dp.x() < 451385 && dp.y() > 420952 && dp.y() < 447012);
    }
    
    /**
     * Checks if a DataPoint is in Oxford coords
     * 
     * @param dp DataPoint to be checked
     * @return true if in Oxford 
     */
    protected boolean isInOxford (DataPoint dp)
    {
        return (dp.x() > 449416 && dp.x() < 458115 && dp.y() > 201110 && dp.y() < 211085);
    }
    
    // Getters
    public List<String> getPollutionDataFiles(){
        return pollutionDataFiles;
    }
    
    public List<SimpleDataSet> getLocationOnlyDataSets(){
        return locationOnlyDataSets;
    }
}
