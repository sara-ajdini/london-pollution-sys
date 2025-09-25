import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.BarChart;

/**
 * Test class for LondonPollutionChart.
 * This class verifies that the LondonPollutionChart is correctly initialised,
 * that valid and invalid filtering entries are handled, and that null or empty
 * inputs throw the correct exceptions.
 * 
 * @author Sara Ajdini
 * @version 1.0
 */
public class LondonPollutionChartTest
{
    LondonPollutionChart testChart;
    DataFiles testData;
    /**
     * Default constructor for test class LondonPollutionChartTest.
     */
    public LondonPollutionChartTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        DataFiles dataFiles = new DataFiles();
        testChart = new LondonPollutionChart(dataFiles);
    }
    
    /**
     * Tests that the chart components are properly initialised.
     * Verifies that the root pane, BarChart, and data are not null.
     */
    @Test
    public void testChartInitialisation()
    {
        assertNotNull(testChart.getRootPane(), "Root Pane can not be null");
        assertNotNull(testChart.getBarChart(), "BarChart can not be null");
        assertNotNull(testChart.getData(), "Data can not be null");
    }
    
    /**
     * Tests that valid entries return true.
     * Including lowercase and uppercase entries
     */
    @Test
    public void testisValidEntryTrue()
    {   
        assertTrue(testChart.isValidEntry("2020","PM10"));
        assertTrue(testChart.isValidEntry("2020","pm10"));
    }
    
    /**
     * Tests that invalid entries return false.
     * Including entries that are not included in the list of accepted
     * years and pollutants
     */
    @Test
    public void testIsValidEntryFalse(){
        assertFalse(testChart.isValidEntry("2000","pm10"), "Invalid year");
        assertFalse(testChart.isValidEntry("2020","test"), "Invalid pollutant");
    }
    
    /**
     * Tests that null or empty input values throw 
     * IllegalArgumentException.
     */
    @Test
    public void testIsValidEntryNullorEmpty(){
        assertThrows(IllegalArgumentException.class, () -> testChart.isValidEntry("","pm10"), "Value cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> testChart.isValidEntry("2020",""), "Value cannot be empty");
        assertThrows(IllegalArgumentException.class, () -> testChart.isValidEntry("",""), "Value cannot be empty");
        
        assertThrows(IllegalArgumentException.class, () -> testChart.isValidEntry(null,null), "Value cannot be null");
        assertThrows(IllegalArgumentException.class, () -> testChart.isValidEntry("2020",null),"Value cannot be null");
        assertThrows(IllegalArgumentException.class, () -> testChart.isValidEntry(null,"pm10"),"Value cannot be null");
    }
    
     /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
        testChart = null;
    }
}
