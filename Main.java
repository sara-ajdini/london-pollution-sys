import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javafx.event.EventHandler;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.shape.Ellipse;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.concurrent.ExecutionException;
import java.util.HashMap;

/**
 * Main initializes all pages and runs the application
 * also responsible switching tab content and styling some components
 * 
 * @author Sara Ajdini, Layla Abdelmaksoud, Maria Plesinska, Muhlisa Husainova
 * @version 1.0
 */
public class Main extends Application {
    private TabPane tabPane;
    private BorderPane mainLayout;
    private Stage mainStage;
    
    private DataFiles dataFilesAll;
    private MapPage currentMapPage;
    private PollutionPanel panel;
    private ComparisonPage comparisonPage;
    private LondonPollutionChart londonChart;
    private LeedsPollutionChart leedsChart;
    private OxfordPollutionChart oxfordChart;
    private WelcomePage welcomePage;
    private Tab mapTab,panelTab,comparisonTab,chartTab;
    private AirQualityAPI liveData;
    private AirQualityDisplay liveDisplay;
    private ComboBox<String> citySwitcher;
    private TabController tc;
    private HBox chartLayout;
    private Button quitButton;
    private Alert loading;
    private boolean isLaunching;
    
    private Task<DataFiles> dataLoaderTask;
    private HashMap<Tab, InstructionsPopup> instructionsMap = new HashMap<>();

    /**
     * start method runs when javafx application is run
     * initially, scene set to welcome screen 
     */
    @Override
    public void start(Stage mainStage) {
        this.mainStage = mainStage;
        mainStage.setTitle("London Pollution Data System");

        welcomePage = new WelcomePage(this);
        Scene welcomeScene = new Scene(welcomePage.getRootPane(), 1000, 600);

        welcomeScene.setOnKeyPressed(event -> {if (event.getCode() == KeyCode.Q) {quit();}});
        
        styleWelcome();

        welcomeScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        

        mainStage.setTitle("City Pollution Data System");
        mainStage.setScene(welcomeScene);
        mainStage.show();
        
        // Start loading data in background
        dataLoaderTask = new Task<>() {
            @Override
            protected DataFiles call() {
                dataFilesAll = new DataFiles(); // heavy loading
        
                // Initialize all non-UI logic-heavy objects
                currentMapPage = new LondonMapPage(dataFilesAll);
                panel = new PollutionPanel(dataFilesAll);
                comparisonPage = new ComparisonPage(dataFilesAll);
                londonChart = new LondonPollutionChart(dataFilesAll);
                leedsChart = new LeedsPollutionChart(dataFilesAll);
                oxfordChart = new OxfordPollutionChart(dataFilesAll);
        
                return dataFilesAll;
            }
        };
        new Thread(dataLoaderTask).start();
    }

    /**
     * when start button is pressed, checks state of datafiles 
     * Alerts user if still loading, calls launchMainApp if datafiles loaded
     */
    public void launch() {
        if (dataFilesAll != null) {
            launchMainApp();
            return;
        }
    
        if (!isLaunching) {
            isLaunching = true;
    
            loading = new Alert(Alert.AlertType.INFORMATION);
            loading.setTitle("Loading");
            loading.setHeaderText(null);
            loading.setContentText("Data is still loading. Please wait...");
            DialogPane dialogPane = loading.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
            dialogPane.getStyleClass().add("custom-alert");
            loading.show();
    
            dataLoaderTask.setOnSucceeded(e -> {
                dataFilesAll = dataLoaderTask.getValue();
                Platform.runLater(() -> {
                    loading.close();
                    launchMainApp();
                });
            });
    
            dataLoaderTask.setOnFailed(e -> {
                Platform.runLater(() -> {
                    loading.close();
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setContentText("Failed to load data.");
                    DialogPane dp = error.getDialogPane();
                    dp.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
                    dp.getStyleClass().add("custom-alert");
                    error.showAndWait();
                });
            });
        }
        else{
            loading.show();
        }
    }

    
    /**
     * Lauches main application after datafiles and pages have finished loading && start button pressed.
     * Creates and adds all tabs to tabPane
     */ 
    public void launchMainApp() {
        tabPane = new TabPane();
        tabPane.setPrefSize(1200, 600);
        BorderPane chartPane = new BorderPane();


        mapTab = new Tab("Map", currentMapPage.getRootPane());
        citySwitcher = new ComboBox<>();
        citySwitcher.getItems().addAll("London", "Leeds", "Oxford");
        citySwitcher.setValue("London");
        citySwitcher.setOnAction(e -> switchCity(citySwitcher.getValue(), mapTab));

        currentMapPage.getFiltersHBox().getChildren().addAll(new Label("\tSelect City:  "), citySwitcher);
        
        panelTab = new Tab("Statistics Panel", panel.getRootPane());

        comparisonTab = new Tab("Comparison Panel", comparisonPage.getRootPane());
        
        styleCharts(londonChart);
        styleCharts(leedsChart);
        styleCharts(oxfordChart);

        chartLayout = new HBox(20, londonChart.getRootPane(), leedsChart.getRootPane(), oxfordChart.getRootPane());
        chartPane.setCenter(chartLayout);
        ScrollPane sp = new ScrollPane(chartPane);
        chartTab = new Tab("Additional Statistics", sp);
        
        
        tabPane.getTabs().addAll(mapTab, panelTab, comparisonTab, chartTab);
        createInstructions();
        
        liveData = new AirQualityAPI();
        liveDisplay = new AirQualityDisplay(liveData);
        HBox airQualityPane = liveDisplay.getAirQualityHBox();
        liveDisplay.refreshData("London");
        Button instructionButton = new Button("Instructions");
        InstructionsPopup instruction = instructionsMap.get(mapTab);
        if (instruction != null) {
                instructionButton.setOnAction(e -> instruction.show());
            }
        quitButton = new Button("Quit");
        quitButton.setOnAction(event -> quit());
            
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        currentMapPage.getApiBox().getChildren().setAll(new ArrayList<Node>(){{addAll(airQualityPane.getChildren());
            add(spacer);
            add(instructionButton);
            add(quitButton);
            add(new Label("  "));
        }
        });    
        
        
        tabPane.getStyleClass().add("tab-pane");
        addTabHeaders();
    
        tc = new TabController(tabPane);
        currentMapPage.importTabController(tc);
        currentMapPage.importPollutionPanel(panel);

        styleMap();
        stylePanel();

        Scene mainScene = new Scene(tabPane, 1000, 600);
        mainScene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        mainScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Q) {
                quit();
            }
        });

        mainStage.setScene(mainScene);
        mainStage.show();
    }
    
    /**
     * Adds headers with instructions and quit buttons 
     * Skips map since map filters and buttons done differently, avoids havig too thick of a header
     */
    private void addTabHeaders(){
        for (Tab tab : tabPane.getTabs()) {
            if (tab==mapTab) continue;
            Button instructionButton = new Button("Instructions");
            InstructionsPopup instruction = instructionsMap.get(tab);
            if (instruction != null) {
                instructionButton.setOnAction(e -> instruction.show());
            }
            
            quitButton = new Button("Quit");
            quitButton.setOnAction(event -> quit());
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
           
            HBox top = new HBox(10, spacer,instructionButton, quitButton);
            top.getStyleClass().add("welcomeTopHBox");
            
            BorderPane bp = new BorderPane(tab.getContent(), top, null, null, null);
            tab.setContent(bp);
            tab.getStyleClass().add("tab");
        }
    }
    
    /**
     * Adds all maps and their instruction popups to hashmap
     */
    private void createInstructions(){
        instructionsMap.put(mapTab, new InstructionsPopup(currentMapPage.getInfoText()));
        instructionsMap.put(panelTab, new InstructionsPopup(panel.getInfoText()));
        instructionsMap.put(comparisonTab, new InstructionsPopup(comparisonPage.getInfoText()));
        instructionsMap.put(chartTab, new InstructionsPopup(londonChart.getInfoText()));
    }
    
    /**
     * Sets map content to the correct map
     * Updates mapTab content
     * 
     */
    private void switchCity(String city, Tab mapTab) {
        quitButton = new Button("Quit");
        quitButton.setOnAction(event -> quit());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (city.equalsIgnoreCase("London")) {
            currentMapPage = new LondonMapPage(dataFilesAll);
        } else if (city.equalsIgnoreCase("Leeds")) {
            currentMapPage = new LeedsMapPage(dataFilesAll);
        } else if (city.equalsIgnoreCase("Oxford")) {
            currentMapPage = new OxfordMapPage(dataFilesAll);
        }
        currentMapPage.getFiltersHBox().getChildren().addAll(new Label("\tSelect City:  "), citySwitcher);
        currentMapPage.importTabController(tc);
        currentMapPage.importPollutionPanel(panel);
        styleMap();
        mapTab.setContent(currentMapPage.getRootPane());
        
        BorderPane newMapPane = new BorderPane(currentMapPage.getRootPane());
        
        HBox airQualityPane = liveDisplay.getAirQualityHBox();
        liveDisplay.refreshData(city);
        
        Button instructionButton = new Button("Instructions");
        InstructionsPopup instruction = instructionsMap.get(mapTab);
        if (instruction != null) {
                instructionButton.setOnAction(e -> instruction.show());
            }
        currentMapPage.getApiBox().getChildren().setAll(new ArrayList<Node>(){{addAll(airQualityPane.getChildren()); 
                                                                                add(spacer);
                                                                                add(instructionButton);
                                                                                add(quitButton);
                                                                                add(new Label("  "));}});
        
        mapTab.setContent(newMapPane);
    }

    /**
     * Styles map filters
     */
    private void styleMap() {
        currentMapPage.getFiltersHBox().getStyleClass().add("filters");
        currentMapPage.getApiBox().getStyleClass().add("filters");   
    }

    /**
     * Styles panel filters and pane 
     */
    private void stylePanel() {
        panel.getStatisticsPane().getStyleClass().add("statsPane");
        panel.getFilterPane().getStyleClass().add("filters");
    }

    /**
     * Styles charts tab root pane
     */
    private void styleCharts(PollutionChart pc) {
        pc.getRootPane().getStyleClass().add("statsPane");
    }
    
    /**
     * Styles welcome page components 
     */
    private void styleWelcome(){
        welcomePage.getWelcomeLayout().getStyleClass().add("welcomeLayout");
        welcomePage.getTop().getStyleClass().add("welcomeTopHBox");
        welcomePage.getStartButton().getStyleClass().add("welcomeButtons");
        welcomePage.getInstructionButton().getStyleClass().add("welcomeButtons");
        welcomePage.getWelcomeLabel().getStyleClass().add("welcomeLabels");
    }

    /**
     * Quits application
     */
    public void quit() {
        System.exit(0);
    }
}