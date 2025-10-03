import java.io.*;
import java.net.*;
import java.util.*;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import org.json.*;


/**
 * This class connects with OpenWeatherMap to use their air pollution
 * API to get data for air pollution for a selected city.
 *
 * @author Muhlisa Husainova
 * @version 1.0
 */
public class AirQualityAPI
{
    //private API key to make connection with API, provided by OpenWeatherMap
    private String apiKey = "072fba7cc436d47ef66acbffc511412e";
    //stores the coordinates of the cities we have maps for
    private Map<String, double[]> locationCoords;
    // Our VBox UI container for live data
    private HBox pollutionPane; 

    /**
     * Constructor for objects of class AirQualityAPI
     */
    public AirQualityAPI()
    {
        locationCoords = new HashMap<>();

        // Adding locations with lat/lon for the cities we added
        locationCoords.put("London", new double[]{51.5072, -0.1276});
        locationCoords.put("Oxford", new double[]{51.7520, -1.2577});
        locationCoords.put("Leeds", new double[]{53.8008, -1.5491});
        
        //initialing the pane
        pollutionPane = new HBox(10);
        pollutionPane.setPadding(new Insets(10));
        //pollutionPane.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
    }

    /**
     * Gets the live pollution data for the selected city in the form of JSON objects.
     * @param selectedCity The city that the user will have chosen from a dropdown.
     **/
    private JSONObject getLocationData(String selectedCity)
    {
        double[] currentCoords = getCoordinates(selectedCity);
        //validation just in case even though the city will be passed on from a dropdown
        if (currentCoords == null) 
        {
            System.out.println("Invalid city selected: " + selectedCity);
            return null;
        }
        double lat = currentCoords[0];
        double lon = currentCoords[1];
        
        String urlConnection = "http://api.openweathermap.org/data/2.5/air_pollution?lat="+ lat + "&lon=" + lon + "&appid=" + apiKey;
        
        try
        {
            //try to establish a connection
            HttpURLConnection apiConnection = fetchApiResponse(urlConnection);
            
            //if the response code is not 200 (OK) or no connection is 
            //established (null) then the program returns null and terminates early
            if(apiConnection.getResponseCode() != 200 || apiConnection == null)
            {
                System.out.println("Couldn't connect to API...");
                return null;
            }
            
            //read response and convert to store it as a string
            String jsonResponse = readApiResponse(apiConnection);
            
            // directly create a JSONObject from the response string
            JSONObject resultsJsonObj = new JSONObject(jsonResponse);
            
            // OpenWeatherMap's air pollution API returns data in the "list" array
            JSONArray locationData = resultsJsonObj.getJSONArray("list");
            JSONObject firstEntry = locationData.getJSONObject(0);
            
            // Return the components
            JSONObject components = firstEntry.getJSONObject("components");
            return components;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Build the pane that will display the data, and adds the data onto it.
     * @param selectedCity The city that the user will have chosen from a dropdown.
     **/
    public void buildApiPane(String selectedCity) {
        JSONObject components = getLocationData(selectedCity);
    
        if (components == null) 
        {
            pollutionPane.getChildren().add(new Label("No data available for " + selectedCity));
        }
    
        // Extract the pollutants from the components JSON
        double no2 = components.optDouble("no2", -1);
        double pm25 = components.optDouble("pm2_5", -1);
        double pm10 = components.optDouble("pm10", -1);
    
        // Add Labels for each pollutant
        pollutionPane.getChildren().add(new Label("Live Air Quality Data for " + selectedCity));
        pollutionPane.getChildren().add(new Label("NO₂: " + no2 + " μg/m³"));
        pollutionPane.getChildren().add(new Label("PM2.5: " + pm25 + " μg/m³"));
        pollutionPane.getChildren().add(new Label("PM10: " + pm10 + " μg/m³"));
    }
    
    /**
     * Clears old data from the pane, and puts the new one.
     * @param newSelectedCity The new city that the user will have chosen from a dropdown.
     */
    public void updateApiPane(String newSelectedCity)
    {
        pollutionPane.getChildren().clear(); // Clear old labels
        buildApiPane(newSelectedCity);
    }

    /**
     * Returns the VBox for JavaFX integration
     */
    public HBox getPollutionPane() 
    {
        return pollutionPane;
    }
    
    /**
     * Returns the coordinates (latitude and longitude) for a selected city.
     * @param city The city that is selected by the user from a dropdown.
     */
    public double[] getCoordinates(String city)
    {
        return locationCoords.get(city);
    }
    
    /**
     * Tries to establish a connection with the URL where we get the data from.
     * @param urlGiven The URL of the web server that we're trying to get the data from.
     */
    public HttpURLConnection fetchApiResponse(String urlGiven)
    {
        try
        {
            //atempt to establish a connection
            URL tryUrl = new URL(urlGiven);
            HttpURLConnection connection = (HttpURLConnection) tryUrl.openConnection();
            
            //set request method to get
            connection.setRequestMethod("GET");
            
            return connection;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        //when you can't make a connection
        return null;
    }
    
    /**
     * Gets the response of the server when access is requested to it.
     * @param apiConnection Connection to the web server we're trying to get data from.
     */
    public String readApiResponse(HttpURLConnection apiConnection)
    {
        try 
        {
            //create a StringBuilder to store resulting json data
            StringBuilder jsonDataResult = new StringBuilder();
            
            // Create a BufferedReader wrapped around an InputStreamReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
    
            String line;
    
            // Read each line until there are no more
            while ((line = reader.readLine()) != null)
            {
                jsonDataResult.append(line);
            }
    
            // Close the reader
            reader.close();
            
            return jsonDataResult.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        //return null in case of issue
        return null;
    }
}