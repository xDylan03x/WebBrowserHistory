package com.webbrowserhistory.webbrowserhistory;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MainApplication extends Application {
    private final Font headerFont = new Font(20);
    private final Font subHeaderFont = new Font(16);
    private final Insets defaultPadding = new Insets(15, 30, 30, 30);

    private TextArea functionCallLog;
    private WebView browserView;
    private ScrollPane historyScrollPane;
    private CheckBox browserLoad;
    private Label historyItemCounter;
    private final HistoryStackManager historyStackManager = new HistoryStackManager();

    private TabPane createTopLeft() {
        // Creates the top left container with the history and browser panels
        // History Tab
        Label historyHeader = new Label("Browser History");
        historyHeader.setFont(headerFont);

        TextField searchBox = new TextField();
        searchBox.setPromptText("Search for a website...");
        searchBox.setPrefWidth(200);
        searchBox.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                refreshHistory();
            } else {
                refreshHistory(newValue.toLowerCase().trim());
            }
        });
        Separator actionBarSeparator1 = new Separator();
        actionBarSeparator1.setOrientation(Orientation.VERTICAL);

        historyItemCounter = new Label();

        Separator actionBarSeparator2 = new Separator();
        actionBarSeparator2.setOrientation(Orientation.VERTICAL);

        Button clearHistoryButton = new Button("Clear History");
        clearHistoryButton.setOnAction((click) -> removeHistory());

        Separator actionBarSeparator3 = new Separator();
        actionBarSeparator3.setOrientation(Orientation.VERTICAL);

        Button add10Items = new Button("Add 10 Items");
        add10Items.setOnAction((click) -> addRandomItems(10));

        Button add100Items = new Button("Add 100 Items");
        add100Items.setOnAction((click) -> addRandomItems(100));

        Button add1000Items = new Button("Add 1,000 Items");
        add1000Items.setOnAction((click) -> addRandomItems(1000));

        Button add10000Items = new Button("Add 10,000 Items");
        add10000Items.setOnAction((click) -> addRandomItems(10000));

        HBox actionBarHBox = new HBox(searchBox, actionBarSeparator1, historyItemCounter, actionBarSeparator2, clearHistoryButton, actionBarSeparator3, add10Items, add100Items, add1000Items, add10000Items, new Label("Time will be logged to console."));
        actionBarHBox.setSpacing(10);
        actionBarHBox.setAlignment(Pos.CENTER_LEFT);

        historyScrollPane = new ScrollPane(buildHistory());
        historyScrollPane.fitToWidthProperty().set(true);
        historyScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        historyScrollPane.setStyle("-fx-background-color:transparent;");
        historyScrollPane.setPadding(new Insets(5, 5, 5, 5));

        VBox historyVBox = new VBox(historyHeader, actionBarHBox, historyScrollPane);
        historyVBox.setPadding(defaultPadding);
        historyVBox.setSpacing(5);

        Tab historyTab = new Tab("History", historyVBox);

        // Browser Tab
        Button backButton = new Button("Back");
        backButton.setOnAction((click) -> navigateBack());
        Button fowardButton = new Button("Forward");
        fowardButton.setOnAction((click) -> navigateForward());
        HBox navigationButtonHBox = new HBox(backButton, fowardButton);
        navigationButtonHBox.setSpacing(5);
        navigationButtonHBox.setPadding(defaultPadding);
        browserView = new WebView();
        VBox browserViewVBox = new VBox(navigationButtonHBox, browserView);
        browserViewVBox.setSpacing(5);

        Tab browserTab = new Tab("Browser", browserViewVBox);

        // Enclosing TabPane
        TabPane topLeftTabPane = new TabPane(historyTab, browserTab);
        topLeftTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        return topLeftTabPane;
    }

    private VBox createTopRight() {
        // Creates the top right container with the mock search results
        Label searchResultsHeader = new Label("Search results for 'Computer Science'");
        searchResultsHeader.setFont(headerFont);

        ScrollPane searchResultsScrollPane = new ScrollPane(buildSearchResults());
        searchResultsScrollPane.fitToWidthProperty().set(true);
        searchResultsScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        searchResultsScrollPane.setStyle("-fx-background-color:transparent;");
        searchResultsScrollPane.setPadding(new Insets(5, 5, 5, 5));

        VBox topRightVBox = new VBox(searchResultsHeader, searchResultsScrollPane);
        topRightVBox.setPadding(defaultPadding);
        topRightVBox.setSpacing(5);

        return topRightVBox;
    }

    private SplitPane createTop() {
        // Creates the top container
        return new SplitPane(createTopLeft(), createTopRight());
    }

    private VBox createBottom() {
        // Creates the bottom container with the function log and the browser load checkbox
        Label bottomHeader = new Label("Function Call Log");
        bottomHeader.setFont(headerFont);

        functionCallLog = new TextArea("-- Start of Call Log --");

        browserLoad = new CheckBox("Update Browser");

        VBox bottomVBox = new VBox(bottomHeader, functionCallLog, browserLoad);
        bottomVBox.setPadding(defaultPadding);
        bottomVBox.setSpacing(5);
        return bottomVBox;
    }

    private VBox buildSearchResults() {
        // Builds the search result list (from hardcoded results)
        Website result1 = new Website("https://en.wikipedia.org/wiki/Computer_science", "Wikipedia", "Computer science", "Computer science is the study of computation, information, and automation. Computer science spans theoretical disciplines to applied disciplines.");
        Website result2 = new Website("https://undergrad.cs.umd.edu/what-computer-science", "The University of Maryland", "What is Computer Science?", "Computer Science is the study of computers and computational systems.");
        Website result3 = new Website("https://www.britannica.com/science/computer-science", "Britannica", "Computer science | Definition, Types, & Facts", "Computer science, the study of computers and computing, including their theoretical and algorithmic foundations, hardware and software.");
        Website result4 = new Website("https://www.coursera.org/articles/what-is-computer-science", "Coursera", "What Is Computer Science and What Career Opportunities Does It Offer?", "Computer science is the study of computer hardware and software. When you enroll in a degree program, you can choose from many specialized areas.");
        Website result5 = new Website("https://www.computerscience.org", "ComputerScience.org", "ComputerScience.org: Code a New Career", "The most extensive site on Computer Science. Find out what it takes to enter the Computer Science field, and how to build a successful career.");
        Website result6 = new Website("https://www.topuniversities.com/courses/computer-science-information-systems/guide", "Top Universities", "Computer Science Degrees", "In a nutshell, computer science degrees deal with the theoretical foundations of information and computation, taking a scientific and practical approach to computation and its applications.");

        VBox searchResultsVBox = new VBox(createWebsiteDisplay(result1), createWebsiteDisplay(result2), createWebsiteDisplay(result3), createWebsiteDisplay(result4), createWebsiteDisplay(result5), createWebsiteDisplay(result6));
        searchResultsVBox.setSpacing(5);
        return searchResultsVBox;
    }

    private VBox buildHistory() {
        // Builds the history list
        VBox historyVBox = new VBox();
        historyVBox.setSpacing(5);

        LinkedList<WebsiteHistory> todayList = new LinkedList<>();
        LinkedList<WebsiteHistory> weekList = new LinkedList<>();
        LinkedList<WebsiteHistory> previousList = new LinkedList<>();
        LocalDate today = LocalDate.now();
        LocalDate weekOut = today.minusDays(7);

        for (int i=0; i<historyStackManager.getHistorySize(); i++) {
            WebsiteHistory website = historyStackManager.peekIndex(i);
            LocalDate convertedVisit = website.getVisited().toLocalDate();
            if (convertedVisit.isEqual(today)) {
                todayList.add(website);
            } else if (convertedVisit.isBefore(today) && convertedVisit.isAfter(weekOut)) {
                weekList.add(website);
            } else {
                previousList.add(website);
            }
        }

        if (!todayList.isEmpty()) {
            Label todayLabel = new Label("Today");
            todayLabel.setFont(subHeaderFont);
            Button deleteTodayButton = new Button("Clear");
            deleteTodayButton.setOnAction((click) -> removeTodayHistory());
            HBox todayHBox = new HBox(todayLabel, deleteTodayButton);
            todayHBox.setSpacing(5);
            todayHBox.setPadding(new Insets(20, 0, 0, 0));
            historyVBox.getChildren().add(todayHBox);
            for (WebsiteHistory website : todayList) {
                historyVBox.getChildren().add(createWebsiteDisplay(website));
            }
        }

        if (!weekList.isEmpty()) {
            Label weekLabel = new Label("This Week");
            weekLabel.setFont(subHeaderFont);
            Button deleteWeekButton = new Button("Clear");
            deleteWeekButton.setOnAction((click) -> removeWeekHistory());
            HBox weekHBox = new HBox(weekLabel, deleteWeekButton);
            weekHBox.setSpacing(5);
            weekHBox.setPadding(new Insets(20, 0, 0, 0));
            historyVBox.getChildren().add(weekHBox);
            for (WebsiteHistory website : weekList) {
                historyVBox.getChildren().add(createWebsiteDisplay(website));
            }
        }

        if (!previousList.isEmpty()) {
            Label previousLabel = new Label("Previous");
            previousLabel.setFont(subHeaderFont);
            Button deletePreviousButton = new Button("Clear");
            deletePreviousButton.setOnAction((click) -> removePreviousHistory());
            HBox previousHBox = new HBox(previousLabel, deletePreviousButton);
            previousHBox.setSpacing(5);
            previousHBox.setPadding(new Insets(20, 0, 0, 0));
            historyVBox.getChildren().add(previousHBox);
            for (WebsiteHistory website : previousList) {
                historyVBox.getChildren().add(createWebsiteDisplay(website));
            }
        }

        historyItemCounter.setText(String.valueOf(historyStackManager.getHistorySize()) + " items showing");

        return historyVBox;
    }

    private VBox buildHistory(String searchTerm) {
        // Builds the history list
        VBox historyVBox = new VBox();
        historyVBox.setSpacing(5);

        Set<String> searchTerms = new HashSet<>(List.of(searchTerm.split("\\s+"))); // Split the search terms into a set on each space
        int itemCounter = 0;

        for (int i=0; i<historyStackManager.getHistorySize(); i++) {
            WebsiteHistory website = historyStackManager.peekIndex(i);
            if (checkWebsiteContents(website, searchTerms)) {
                itemCounter++;
                historyVBox.getChildren().add(createWebsiteDisplay(website));
            }
        }
        historyItemCounter.setText(String.valueOf(itemCounter) + " items showing");

        return historyVBox;
    }

    private boolean checkWebsiteContents(WebsiteHistory website, Set<String> searchTerms) {
        for (String term: searchTerms) {
            if (website.getUrl().toLowerCase().contains(term)) {
                return true;
            } else if (website.getWebsiteTitle().toLowerCase().contains(term)) {
                return true;
            } else if (website.getPageTitle().toLowerCase().contains(term)) {
                return true;
            } else if (website.getDescription().toLowerCase().contains(term)) {
                return true;
            }
        }
        return false;
    }

    private VBox createWebsiteDisplay(Website website) {
        // Creates the website list (for search results and history)
        Label titleLabel = new Label(website.getPageTitle());
        titleLabel.setFont(new Font(18));
        titleLabel.setTextFill(Color.web("#0D47A1"));

        Label infoLabel = new Label(website.getWebsiteTitle() + " - " + website.getUrl());
        infoLabel.setTextFill(Color.web("#4CAF50"));

        Label descriptionLabel;
        VBox result;
        if (website instanceof WebsiteHistory websiteHistory) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d',' y 'at' h:mm:ss a");
            String formattedNow = websiteHistory.getVisited().format(formatter);
            descriptionLabel = new Label(formattedNow);
            Button deleteHistoryButton = new Button("Delete from History");
            deleteHistoryButton.setOnAction((click) -> removeHistory(websiteHistory));
            result = new VBox(titleLabel, infoLabel, descriptionLabel, deleteHistoryButton);
        }
        else {
            descriptionLabel = new Label(website.getDescription());
            result = new VBox(titleLabel, infoLabel, descriptionLabel);
        }

        result.setOnMouseClicked((click) -> websiteClicked(website));
        result.setPrefWidth(400);
        result.setSpacing(5);
        result.setPadding(new Insets(15, 15, 15, 15));
        result.setStyle("-fx-background-color: #EEEEEE; -fx-border-style: solid inside; -fx-border-color: #E0E0E0; -fx-border-radius: 5;");
        return result;
    }

    private void websiteClicked(Website website) {
        // Called when the user clicks a website in the search result or history panel
        historyStackManager.visit(new WebsiteHistory(website.getUrl(), website.getWebsiteTitle(), website.getPageTitle(), website.getDescription()));
        refreshHistory();
        if (browserLoad.isSelected()) {
            browserView.getEngine().load(website.getUrl());
        }
        updateFunctionCallLog(String.format("User clicked %s -> MainApplication.websiteClicked() -> HistoryStackManager.visit() -> Pushes website to the history stack", website.getUrl()));
    }

    private void addRandomItems(int numItems) {
        for (int i=0; i<numItems; i++) {
            if (i==numItems-1) {
                long startTime = System.nanoTime();
                historyStackManager.visit(new WebsiteHistory("url", "websiteTitle", "pageTitle", "description"));
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println(duration + " ns for " + historyStackManager.getHistorySize());
            } else {
                historyStackManager.visit(new WebsiteHistory("url", "websiteTitle", "pageTitle", "description"));
            }
        }
        refreshHistory();
        updateFunctionCallLog(String.format("User clicked 'Add %d Items' button -> MainApplication.addRandomItems() -> HistoryStackManager.visit() -> Pushes websites to the history stack", numItems));
    }

    private void navigateBack() {
        // Navigates to the previous page in the back stack
        WebsiteHistory backWebsite = historyStackManager.back();
        refreshHistory();
        if (browserLoad.isSelected()) {
            browserView.getEngine().load(backWebsite.getUrl());
        }
        updateFunctionCallLog("User clicked 'Back' button -> MainApplication.navigateBack() -> HistoryStackManager.back() -> Pops website from the back stack and adds it to the forward stack");
    }

    private void navigateForward() {
        // Navigates to the next page in the forward stack
        WebsiteHistory forwardWebsite = historyStackManager.forward();
        refreshHistory();
        if (browserLoad.isSelected()) {
            browserView.getEngine().load(forwardWebsite.getUrl());
        }
        updateFunctionCallLog("User clicked 'Forward' button -> MainApplication.navigateForward() -> HistoryStackManager.forward() -> Pops website from the forward stack and adds it to the back stack");
    }

    private void removeHistory() {
        // Clears the history entirely
        historyStackManager.deleteHistory();
        refreshHistory();
        updateFunctionCallLog("User clicked 'Clear History' button ->  MainApplication.removeHistory() -> HistoryStackManager.deleteHistory() -> Empties all of the history stacks");
    }

    private void removeHistory(WebsiteHistory websiteHistory) {
        // Deletes the provided WebHistory object from history
        historyStackManager.remove(websiteHistory);
        refreshHistory();
        updateFunctionCallLog("User clicked 'Delete from History' button ->  MainApplication.removeHistory() -> HistoryStackManager.remove() -> Removes the requested item from history");
    }

    public void removeTodayHistory() {
        LinkedList<WebsiteHistory> todayList = new LinkedList<>();
        LocalDate today = LocalDate.now();
        for (int i=0; i<historyStackManager.getHistorySize(); i++) {
            WebsiteHistory website = historyStackManager.peekIndex(i);
            LocalDate convertedVisit = website.getVisited().toLocalDate();
            if (convertedVisit.isEqual(today)) {
                todayList.add(website);
            }
        }
        for (WebsiteHistory website: todayList) {
            historyStackManager.remove(website);
        }
        refreshHistory();
        updateFunctionCallLog("User clicked 'Clear' button in the 'Today' group ->  MainApplication.removeTodayHistory() -> HistoryStackManager.remove() -> Removes the requested items from history");
    }

    public void removeWeekHistory() {
        LinkedList<WebsiteHistory> weekList = new LinkedList<>();
        LocalDate today = LocalDate.now();
        LocalDate weekOut = today.minusDays(7);
        for (int i=0; i<historyStackManager.getHistorySize(); i++) {
            WebsiteHistory website = historyStackManager.peekIndex(i);
            LocalDate convertedVisit = website.getVisited().toLocalDate();
            if (convertedVisit.isBefore(today) && convertedVisit.isAfter(weekOut)) {
                weekList.add(website);
            }
        }
        for (WebsiteHistory website: weekList) {
            historyStackManager.remove(website);
        }
        refreshHistory();
        updateFunctionCallLog("User clicked 'Clear' button in the 'Today' group ->  MainApplication.removeWeekHistory() -> HistoryStackManager.remove() -> Removes the requested items from history");
    }

    public void removePreviousHistory() {
        LinkedList<WebsiteHistory> previousList = new LinkedList<>();
        LocalDate today = LocalDate.now();
        LocalDate weekOut = today.minusDays(7);
        for (int i=0; i<historyStackManager.getHistorySize(); i++) {
            WebsiteHistory website = historyStackManager.peekIndex(i);
            LocalDate convertedVisit = website.getVisited().toLocalDate();
            if (convertedVisit.isBefore(weekOut)) {
                previousList.add(website);
            }
        }
        for (WebsiteHistory website: previousList) {
            historyStackManager.remove(website);
        }
        refreshHistory();
        updateFunctionCallLog("User clicked 'Clear' button in the 'Today' group ->  MainApplication.removePreviousHistory() -> HistoryStackManager.remove() -> Removes the requested items from history");
    }

    private void updateFunctionCallLog(String text) {
        // Used by multiple methods to explain what methods are being called
        functionCallLog.appendText("\n" + text);
    }

    private void refreshHistory() {
        // Called by multiple methods to refresh the history view
        historyScrollPane.setContent(buildHistory());
    }

    private void refreshHistory(String searchString) {
        // Called by the search box to refresh history view based on search terms
        historyScrollPane.setContent(buildHistory(searchString));
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Scene Setup
        Scene scene = new Scene(new VBox(createTop(), createBottom()), 1400, 800);
        stage.setTitle("Web Browser History Example");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}