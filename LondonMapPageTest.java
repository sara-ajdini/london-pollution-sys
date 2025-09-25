

import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import java.util.HashMap;
import javafx.scene.control.ComboBox;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * The test class MapPageTest.
 *
 * @author  Sara Ajdini
 * @version (a version number or a date)
 */
public class LondonMapPageTest
{
    /**
     * Default constructor for test class MapPageTest
     */
    private MapPage mapPage;
    private Pane testPane;
    private ComboBox pollutantComboBox,yearComboBox;
    private String pollutant;
    private TabController tabController;
    private ArrayList<DataPoint> testDataPoints;
    private DataFiles dataFiles;
    
    public LondonMapPageTest()
    {  
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUpMap()
    {   dataFiles = new DataFiles();
        mapPage = new LondonMapPage(dataFiles);
        testPane = new Pane();
    }
    
    /**
     *  Tests the initialisation of GUI components to ensure they are not null.
     *  these components cannot be null
     */
    @Test
    public void testMapInitalisiation(){
        // testing gui panes
        assertNotNull(mapPage.getRootPane(), "Root Pane can not be null");
        assertNotNull(mapPage.getScrollPane(), "Scroll Pane can not be null");
        
        //testing gui components
        assertNotNull(mapPage.getYearComboBox(), "Year ComboBox can not be null");
        assertNotNull(mapPage.getPollutantComboBox(), "Pollutant ComboBox can not be null");
        assertNotNull(mapPage.getPollutedCheckBox(), "Pollutant CheckBox can not be null");
        assertNotNull(mapPage.getFiltersHBox(), "Filters HBox can not be null");
        assertNotNull(mapPage.getTooltip(), "Tooltip can not be null");
        
        // testing map components
        assertNotNull(mapPage.getMapCanvas(), "Map canvas can not be null");
        assertNotNull(mapPage.getMapImagePath(), "Map image path can not be null");
        assertNotNull(mapPage.getUpdateMapButton(), "Button can not be null");
    }
    
    /**
     * Tests whether a mark is drawn correctly on the map.
     * A mark should always be added to the HashMap after being drawn.
     */
    @Test 
    public void testPaintMark(){
        mapPage.paintMarks(testPane);
        // drawn map should always contain marks
        assertFalse(mapPage.getMarks().isEmpty());
        assertFalse(testPane.getChildren().isEmpty());
    }
    
    /**
     * Tests whether the map is cleared correctly.
     * Marks should be removed from both the HashMap and the Pane.
     */
    @Test 
    public void testClearMap(){
        mapPage.paintMarks(testPane);
        
        // before clearing map marks should be present
        assertFalse(mapPage.getMarks().isEmpty());
        assertFalse(testPane.getChildren().isEmpty());
        
        mapPage.clearMap(testPane);
        
        //after map cleared marks must be empty
        assertTrue(mapPage.getMarks().isEmpty());
        assertTrue(testPane.getChildren().isEmpty());
    }
    
    /**
     * Tests whether the correct color is assigned to NO2 pollution levels.
     */
     @Test
    public void testGetMarkColorNO2(){
        mapPage.setPollutant("no2");
        assertEquals(mapPage.getMarkColor(7),Color.web("rgba(0,255,0,0.3)"));
        assertEquals(mapPage.getMarkColor(16),Color.web("rgba(255,255,0,0.3)"));
        assertEquals(mapPage.getMarkColor(22),Color.web("rgba(255,165,0,0.3)"));
        assertEquals(mapPage.getMarkColor(30),Color.web("rgba(255,0,0,0.3)"));
    }   
    
    /**
     * Tests whether the correct color is assigned to PM10 pollution levels.
     */
    @Test
    public void testGetMarkColorPM10(){
        mapPage.setPollutant("pm10");
        assertEquals(mapPage.getMarkColor(3),Color.web("rgba(0,255,0,0.3)"));
        assertEquals(mapPage.getMarkColor(17),Color.web("rgba(255,255,0,0.3)"));
        assertEquals(mapPage.getMarkColor(18),Color.web("rgba(255,165,0,0.3)"));
        assertEquals(mapPage.getMarkColor(25),Color.web("rgba(255,0,0,0.3)"));
    }   
    
    /**
     * Tests whether the correct color is assigned to PM2.5 pollution levels.
     */
    @Test
    public void testGetMarkColorPM2_5(){
        mapPage.setPollutant("pm2.5");
        assertEquals(mapPage.getMarkColor(0),Color.web("rgba(0,255,0,0.3)"));
        assertEquals(mapPage.getMarkColor(9),Color.web("rgba(255,255,0,0.3)"));
        assertEquals(mapPage.getMarkColor(11),Color.web("rgba(255,165,0,0.3)"));
        assertEquals(mapPage.getMarkColor(13),Color.web("rgba(255,0,0,0.3)"));
    }   
    
    /**
     * Tests whether the method correctly returns the highest pollution data points.
     * The returned list should contain maximum 100 elements.
     */
    @Test
    public void testGetHighestPollution(){
        File directory = new File("UKAirPollutionData");
        List<String> pollutionFiles = dataFiles.findFiles(directory, new ArrayList<>());
        List<DataPoint> testDataPoints = mapPage.getDataPoints();
        List<DataPoint> testHighestPollutionDatapoints = new ArrayList<>();
        
        testHighestPollutionDatapoints = mapPage.getHighestPollution(testDataPoints);
        assertTrue(testHighestPollutionDatapoints.size() <= 100,"cannot have more than 100 elements");
        
    }   
    
    /**
     * Tests whether the update map button performs within the expected time.
     * it should complete within 300 milliseconds.
     */
    @Test
    public void testUpdateMapButtonTimeout(){
        assertTimeoutPreemptively(Duration.ofMillis(300), () -> {
            mapPage.getUpdateMapButton().fire(); 
        });
    }   
    
    /**
     * Tests that only valid pollutants can be set.
     * Including lowercase and uppercase entries
     */
    @Test
    public void testSetPollutantValid(){
        mapPage.setPollutant("no2");
        assertEquals("no2",mapPage.getPollutant());
        
        mapPage.setPollutant("PM10");
        assertEquals("pm10",mapPage.getPollutant());
        
    }   
    
    /**
     * Tests that invalid pollutant inputs throw an exception.
     */
    @Test
    public void testSetPollutantInvalid(){
        assertThrows(IllegalArgumentException.class, () -> mapPage.setPollutant(""));
        assertThrows(IllegalArgumentException.class, () -> mapPage.setPollutant(null));
        
    }   
    
    /**
     * Tests whether the tab controller is properly imported.
     */
    @Test
    @Disabled
    public void testImportTabController(){
        assertNotNull(mapPage.getTabController());
    }   
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
         mapPage = null;
         testPane = null;
    }
}
