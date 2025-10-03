import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import java.util.List;

/**
 * Allows for tab switch commands to be added to any class.
 *
 * @author Layla Abdelmaksoud
 * @version 1.0
 */
public class TabController
{
    TabPane tabs;
    /**
     * Constructor for objects of class TabController
     */
    public TabController(TabPane tp)
    {
        tabs = tp;
    }

    /**
     *Switches to specified tab index
     */
    public void switchTab(int x)
    {
        tabs.getSelectionModel().select(x);
    }
    
    /**
     *Switches to next tab
     */
    public void nextTab(){
        tabs.getSelectionModel().selectNext();
    }
    
    /**
     * Removes a given tab
     * 
     * @param tab to be removed
     */
    public void removeTab(Tab tab){
         tabs.getTabs().remove(tab);
    }
    
    /**
     * Returns list of tabs in the tabpane
     * 
     * @return tab list
     */
    public List<Tab> getTabs(){
        return tabs.getTabs();
    }
}
