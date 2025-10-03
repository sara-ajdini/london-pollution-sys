import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.awt.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.Scene;


/**
 * Welcome Page for the user
 *
 * @author Sara Ajdini
 * @version 1
 */
public class WelcomePage
{
    // instance variables 
    private BorderPane rootPane;
    
    private Button quitButton;
    private Button startButton;
    private Button instructionButton;
    
    private Label welcomeLabel;
    
    private VBox welcomeLayout;
    private HBox top;
    
    private Main mainApp;
    
    /**
     * Constructor for objects of class WelcomePage
     */
    public WelcomePage(Main mainApp)
    {
        // initialise instance variables
        this.mainApp = mainApp;
        
        rootPane = new BorderPane();
        InstructionsPopup defaultInfo = new InstructionsPopup(getInfoText());

        // initalising image for background
        Image image = new Image("welcomePage.png");
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.45);
        
        rootPane.getChildren().add(imageView);
        
        // initialise gui components 
        welcomeLabel = new Label("London Air Pollution");
        
        // creating instruction button 
        instructionButton = new Button("Instructions");
        instructionButton.setOnAction(event -> defaultInfo.show());
        instructionButton.setPrefSize(100,50);
        instructionButton.setPadding(new Insets(10,10,10,10));
        
        // creating start button 
        startButton = new Button("Start");
        startButton.setOnAction((ActionEvent ev) -> { mainApp.launch();});
        startButton.setPrefSize(100,50);
        startButton.setPadding(new Insets(10,10,10,10));
        
        quitButton = new Button("Quit");
        quitButton.setOnAction(event -> mainApp.quit());
                
        // layout for welcome page and setting it to center 
        HBox welcomeButtons = new HBox(20, instructionButton, startButton);
        welcomeButtons.setAlignment(Pos.CENTER);
        welcomeLayout = new VBox(35, welcomeLabel, welcomeButtons);
        welcomeLayout.setAlignment(Pos.CENTER);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        top = new HBox(10, spacer,quitButton);
    
        rootPane.setCenter(welcomeLayout);
        rootPane.setTop(top);
    }
    
    
    /**
     * Displays text about the map functionality.
     * 
     * @return a string describing how to use the map page
     */
    public String getInfoText(){
        String infoString = "Map:\nUse the filters to see different pollution levels based on year and pollutant type, click 'Update Map' to see changes. \nUse the dropdown to change region"+
                            "\n\nPollution Statisics Panel:\nUse the dropdowns to select year and pollutant, click Update to see changes."+
                            "\nSelect specific location by entering a gridcode or x, y coords, or by clicking on the London map."+
                            "\n\nComparison Page:\nChoose two different years and press the compare button to see data for both years." +
                            "\n\n Keyboard Shortcuts: \n Press Q to quit.";
        return infoString;
    }
    
    
    /**
     * returns the root pane of the window
     */
    public Pane getRootPane(){
        return rootPane;
    }
    
    /**
     * returns the root pane of the window
     */
    public Button getInstructionButton(){
        return instructionButton;
    }
    
    /**
     * returns the root pane of the window
     */
    public Button getStartButton(){
        return startButton;
    }
    
    /**
     * returns the root pane of the window
     */
    public Button getQuitButton(){
        return quitButton;
    }
    
    /**
     * returns the root pane of the window
     */
    public Label getWelcomeLabel(){
        return welcomeLabel;
    }
    
    /**
     * returns the root pane of the window
     */
    public VBox getWelcomeLayout(){
        return welcomeLayout;
    }
    
    /**
     * returns the root pane of the window
     */
    public HBox getTop(){
        return top;
    }
    
}
