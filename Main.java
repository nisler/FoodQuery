/**
 * Filename: Main.java Project: p5 - JavaFX Team Project Course: CS400 Authors: Benjamin Nisler,
 * Gabriella Cottiero, Olivia Gonzalez, Timothy James, Tollan Renner Due Date: Saturday, December
 * 15, 11:59pm
 *
 * Additional credits:
 *
 * Bugs or other notes:
 */

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Entry point of the FoodQuery Program Houses GUI code for now.
 */
public class Main extends Application {

  // Application's stage
  Stage mainStage;
  // Stage's Scene
  Scene mainScene;
  // Scene's overarching grid
  GridPane mainGrid, queryGrid;

  // Application's buttons
  Button loadFileButton, saveFileButton, newFoodButton, clearFoodEntry, addToMeal, removeFromMeal,
      queryButton, removeQueryButton, analyzeButton;

  // Applications Containers
  VBox transferButtons, queries;
  HBox listMenu, foodItemInsert;

  // Application's text fields
  TextField insertId, insertName, insertCals, insertCarbs, insertFat, insertProtein, insertFiber,
      nameQueryText, nutrientValue, counter;
  
  // nutrient rules
  GridPane nutrientRuleGrid;
  ChoiceBox<String> nutrientChoice, comparatorChoice;
  ObservableList<String> rules = FXCollections.observableArrayList();
  ListView<String> rulesList = new ListView<String>(rules);

  // Applications data structures
  // Underlying foodlist object
  FoodData masterFoodData, workingFoodData, queryFoodData;
  // current viewable food list as it's being altered by filters or additions
  ObservableList<FoodItem> foodObsList, mealObsList, queryObsList;
  // the list used in the menu
  List<FoodItem> filteredList;
  TableView<FoodItem> foodListTable, menuListTable;
  TableColumn<FoodItem, String> foodID, foodName, menuID, menuName;
  TableColumn<FoodItem, Double> foodCals, foodCarbs, foodFat, foodProtein, foodFiber, menuCals,
      menuCarbs, menuFat, menuProtein, menuFiber;

  Comparator<FoodItem> tableSort = new Comparator<FoodItem>() {
    @Override
    public int compare(FoodItem item1, FoodItem item2) {
      return item1.getName().compareTo(item2.getName());
    }
  };

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // MainStage Settings
    mainStage = new Stage();
    mainStage.setTitle("Meal Analyzer");
    mainStage.setMaximized(true);

    // Grid Settings
    mainGrid = new GridPane();
    // TODO remove setGridLinesVisible when
    mainGrid.setGridLinesVisible(false);
    mainGrid.setAlignment(Pos.CENTER);
    mainGrid.setVgap(5);
    mainGrid.setHgap(5);
    mainGrid.setPadding(new Insets(10, 10, 10, 10));


    masterFoodData = new FoodData();
    masterFoodData.loadFoodItems("foodItems.csv");
    workingFoodData = masterFoodData;

    foodObsList = FXCollections.observableArrayList(workingFoodData.getAllFoodItems());
    mealObsList = FXCollections.observableArrayList();

    // FOOD LIST MENU
    listMenu = new HBox();

    // FOOD LIST LOAD
    loadFileButton = new Button("Load Food List");
    loadFileButton.setOnAction(e -> {
      loadFile();
    });

    // FOOD LIST SAVE
    saveFileButton = new Button("Save Food List");
    saveFileButton.setOnAction(e -> {
      saveFile();
    });

    Region region = new Region();
    HBox.setHgrow(region, Priority.ALWAYS);

    newFoodButton = new Button("Add New Food Item");
    newFoodButton.setOnAction(e -> {
      addCustomFood();
    });


    clearFoodEntry = new Button("Clear New Food Text");
    clearFoodEntry.setOnAction(e -> {
      clearFoodEntry();
    });



    listMenu.prefWidth(750);
    listMenu.setPadding(new Insets(10, 10, 10, 10));
    listMenu.getChildren().addAll(loadFileButton, saveFileButton, region, newFoodButton,
        clearFoodEntry);
    GridPane.setConstraints(listMenu, 0, 0);

    // FOOD ITEM INSERTER
    foodItemInsert = new HBox();

    insertId = new TextField();
    insertId.setPrefWidth(165);
    insertId.setPromptText("New Item ID");
    insertName = new TextField();
    insertName.setPrefWidth(320);
    insertName.setPromptText("New Item Name");
    insertCals = new TextField();
    insertCals.setPrefWidth(50);
    insertCals.setPromptText("Calories");
    insertCarbs = new TextField();
    insertCarbs.setPrefWidth(50);
    insertCarbs.setPromptText("Carbs");
    insertFat = new TextField();
    insertFat.setPrefWidth(50);
    insertFat.setPromptText("Fat");
    insertProtein = new TextField();
    insertProtein.setPrefWidth(50);
    insertProtein.setPromptText("Protein");
    insertFiber = new TextField();
    insertFiber.setPrefWidth(50);
    insertFiber.setPromptText("Fiber");
    foodItemInsert.getChildren().addAll(insertId, insertName, insertCals, insertCarbs, insertFat,
        insertProtein, insertFiber);

    GridPane.setConstraints(foodItemInsert, 0, 1);


    // FOOD TABLE
    HBox foodTableHeader = new HBox();
    Label foodTableTitle = new Label("Food List");
    foodTableTitle.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    foodTableTitle.setAlignment(Pos.CENTER);
    HBox.setHgrow(foodTableTitle, Priority.ALWAYS);

    // Region foodTitleRegion = new Region();
    // HBox.setHgrow(foodTitleRegion, Priority.ALWAYS);

    Label counterLabel = new Label("Number of items in list: ");
    counterLabel.setAlignment(Pos.CENTER);
    counterLabel.setMaxHeight(Double.MAX_VALUE);

    counter = new TextField();
    counter.setMaxWidth(40);
    counter.setText(String.valueOf(foodObsList.size()));

    foodTableHeader.getChildren().addAll(foodTableTitle, counterLabel, counter);
    GridPane.setConstraints(foodTableHeader, 0, 2);
    // ID column
    foodID = new TableColumn<>("ID");
    foodID.setPrefWidth(165);
    foodID.setSortable(false);
    foodID.setCellValueFactory(new PropertyValueFactory<>("ID"));

    // Name column
    foodName = new TableColumn<>("Name");
    foodName.setPrefWidth(320);
    foodName.setSortable(false);
    foodName.setCellValueFactory(new PropertyValueFactory<>("Name"));

    // Calories column
    foodCals = new TableColumn<>("Calories");
    foodCals.setPrefWidth(50);
    foodCals.setSortable(false);
    foodCals.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

    // Carbs column
    foodCarbs = new TableColumn<>("Carbs");
    foodCarbs.setPrefWidth(50);
    foodCarbs.setSortable(false);
    foodCarbs.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("carbohydrate")).asObject());

    // Fat column
    foodFat = new TableColumn<>("Fat");
    foodFat.setPrefWidth(50);
    foodFat.setSortable(false);
    foodFat.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fat")).asObject());

    // Protein column
    foodProtein = new TableColumn<>("Protein");
    foodProtein.setPrefWidth(50);
    foodProtein.setSortable(false);
    foodProtein.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("protein")).asObject());

    // Fiber column
    foodFiber = new TableColumn<>("Fiber");
    foodFiber.setPrefWidth(50);
    foodFiber.setSortable(false);
    foodFiber.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fiber")).asObject());

    foodListTable = new TableView<FoodItem>(foodObsList);
    FXCollections.sort(foodObsList, tableSort);
    foodListTable.setPrefWidth(750);
    foodListTable.getColumns().setAll(foodID, foodName, foodCals, foodCarbs, foodFat, foodProtein,
        foodFiber);

    GridPane.setConstraints(foodListTable, 0, 3);

    // MENU TABLE
    Label mealTableTitle = new Label("Meal List");
    mealTableTitle.setAlignment(Pos.CENTER);
    mealTableTitle.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setConstraints(mealTableTitle, 2, 2);
    // ID column
    menuID = new TableColumn<>("ID");
    menuID.setPrefWidth(165);
    menuID.setSortable(false);
    menuID.setCellValueFactory(new PropertyValueFactory<>("ID"));

    // Name column
    menuName = new TableColumn<>("Name");
    menuName.setPrefWidth(320);
    menuName.setSortable(false);
    menuName.setCellValueFactory(new PropertyValueFactory<>("name"));

    // Calories column
    menuCals = new TableColumn<>("Calories");
    menuCals.setPrefWidth(50);
    menuCals.setSortable(false);
    menuCals.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

    // Carbs column
    menuCarbs = new TableColumn<>("Carbs");
    menuCarbs.setPrefWidth(50);
    menuCarbs.setSortable(false);
    menuCarbs.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("carbohydrate")).asObject());

    // Fat column
    menuFat = new TableColumn<>("Fat");
    menuFat.setPrefWidth(50);
    menuFat.setSortable(false);
    menuFat.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fat")).asObject());

    // Protein column
    menuProtein = new TableColumn<>("Protein");
    menuProtein.setPrefWidth(50);
    menuProtein.setSortable(false);
    menuProtein.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("protein")).asObject());

    // Fiber column
    menuFiber = new TableColumn<>("fiber");
    menuFiber.setPrefWidth(50);
    menuFiber.setSortable(false);
    menuFiber.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

    menuListTable = new TableView<FoodItem>(mealObsList);
    menuListTable.setPrefWidth(485);
    menuListTable.getColumns().setAll(menuID, menuName, menuCals, menuCarbs, menuFat, menuProtein,
        menuFiber);

    GridPane.setConstraints(menuListTable, 2, 3);

    // TRANSFER ITEM PANE
    transferButtons = new VBox();
    transferButtons.setAlignment(Pos.CENTER);
    transferButtons.setPadding(new Insets(10, 10, 10, 10));
    transferButtons.setSpacing(10);

    // ADD BUTTON
    addToMeal = new Button("Add Selected\nItem to Meal");
    addToMeal.setMaxWidth(Double.MAX_VALUE);
    addToMeal.setTextAlignment(TextAlignment.CENTER);
    addToMeal.setOnAction(e -> {
      addToMeal();
    });

    // REMOVE BUTTON
    removeFromMeal = new Button("Remove Selected\nItem from Meal");
    removeFromMeal.setMaxWidth(Double.MAX_VALUE);
    removeFromMeal.setTextAlignment(TextAlignment.CENTER);
    removeFromMeal.setOnAction(e -> {
      removeFromMeal();
    });

    transferButtons.getChildren().addAll(addToMeal, removeFromMeal);
    GridPane.setConstraints(transferButtons, 1, 3);

    // QUERY PANE
    nameQueryText = new TextField();
    nameQueryText.setMinWidth(500);
    nameQueryText.setPromptText("Filter by Name");
    GridPane.setConstraints(nameQueryText, 0, 0);

    queryButton = new Button("Apply Filters");
    queryButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    queryButton.setOnAction(e -> {
      applyFilters();
    });
    GridPane.setConstraints(queryButton, 1, 0);
    GridPane.setRowSpan(queryButton, 2);

    // nutrient rule filters   
    nutrientChoice = new ChoiceBox<String>(FXCollections.observableArrayList("calories", "fat", "carbohydrate", "fiber", "protein"));
    GridPane.setConstraints(nutrientChoice, 0, 0);
    comparatorChoice = new ChoiceBox<String>(FXCollections.observableArrayList(">=", "<=", "=="));
    GridPane.setConstraints(comparatorChoice, 1, 0);
    nutrientValue = new TextField();
    nutrientValue.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
          if (!newValue.matches("\\d*\\.?\\d*")) {  // only allow numerics
            nutrientValue.setText(oldValue);
          }
      }
    });
    GridPane.setConstraints(nutrientValue, 2, 0);
    rulesList.setMaxHeight(0);  
    rules.addListener(new ListChangeListener<String>() {
      @Override
      public void onChanged(Change<? extends String> c) {       
        rulesList.setMaxHeight(rules.size() * nameQueryText.getHeight());
      }   
    });   
    GridPane.setConstraints(rulesList, 3, 0);
    nutrientRuleGrid = new GridPane();
    nutrientRuleGrid.getChildren().addAll(nutrientChoice, comparatorChoice, nutrientValue, rulesList);
    GridPane.setConstraints(nutrientRuleGrid, 0, 1);

    removeQueryButton = new Button("Remove Filters");
    removeQueryButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    removeQueryButton.setOnAction(e -> {
      nameQueryText.clear();      
      clearFilters();
    });
    GridPane.setConstraints(removeQueryButton, 2, 0);
    GridPane.setRowSpan(removeQueryButton, 2);

    queryGrid = new GridPane();
    queryGrid.setAlignment(Pos.CENTER);
    queryGrid.getChildren().addAll(nameQueryText, queryButton, nutrientRuleGrid, removeQueryButton);
    GridPane.setConstraints(queryGrid, 0, 4);
    
    // ANALYSIS
    analyzeButton = new Button("Analyze Meal");
    analyzeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    analyzeButton.setOnAction(e -> {
      analyzeMeal();
    });
    GridPane.setConstraints(analyzeButton, 2, 4);

    // WINDOW SETUP
    mainGrid.getChildren().addAll(listMenu, foodItemInsert, foodTableHeader, foodListTable,
        menuListTable, mealTableTitle, transferButtons, queryGrid, analyzeButton);

    mainScene = new Scene(mainGrid);
    mainStage.setScene(mainScene);
    mainStage.show();

  }

  /**
   * Picks a file to open. The items of that file will replace the current Food Table items. FIXME
   * loadFile currently only adds to Food Table, does not replace.
   */
  private void loadFile() {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
    fc.setTitle("Load Food List");
    File file = fc.showOpenDialog(mainStage);
    if (file != null) {
      masterFoodData.loadFoodItems(file.toString());
      workingFoodData = masterFoodData;
      refreshFoodTable(masterFoodData.getAllFoodItems());
    }

    else {
      System.out.println("File not found");
    }
  }

  /**
   * Takes the current Food Table items and creates a save file from those items. FIXME does not
   * save filtered results yet
   */
  private void saveFile() {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
    fc.setTitle("Save Food List");
    File file = fc.showSaveDialog(mainStage);
    if (file != null) {
      masterFoodData.saveFoodItems(file.toString());
    } else {
      System.out.println("File not found");
    }
  }

  /**
   * Instantiates a FoodItem object from the information in the Food Entry Fields FIXME needs to
   * handle empty fields. Especially nutrient fields. Empty fields turned to "0.0"?
   */
  private void addCustomFood() {
    FoodItem addedFood = new FoodItem(insertId.getText(), insertName.getText());
    addedFood.addNutrient("calories", new Double(insertCals.getText()));
    addedFood.addNutrient("carbohydrate", new Double(insertCarbs.getText()));
    addedFood.addNutrient("fat", new Double(insertFat.getText()));
    addedFood.addNutrient("protein", new Double(insertProtein.getText()));
    addedFood.addNutrient("fiber", new Double(insertFiber.getText()));
    workingFoodData.addFoodItem(addedFood);
    refreshFoodTable(workingFoodData.getAllFoodItems());
  }

  /**
   * Erases all data in the food entry fields
   */
  private void clearFoodEntry() {
    insertId.clear();
    insertName.clear();
    insertCals.clear();
    insertCarbs.clear();
    insertFat.clear();
    insertProtein.clear();
    insertFiber.clear();
  }

  /**
   * Adds a selected item from the Food List to the Meal List.
   */
  private void addToMeal() {
    if (!foodListTable.getSelectionModel().isEmpty()) {
      mealObsList.add(foodListTable.getSelectionModel().getSelectedItem());
    }
  }

  /**
   * Removes a selected item from the Meal List
   */
  private void removeFromMeal() {
    if (!menuListTable.getSelectionModel().isEmpty()) {
      mealObsList.remove(menuListTable.getSelectionModel().getSelectedItem());
    }
  }

  /**
   * Applies the name filter and/or nutrient filter to the Food List FIXME Cannot yet save filtered
   * results to file
   */
  private void applyFilters() {
    queryFoodData = workingFoodData;
    List<FoodItem> nameFilter = queryFoodData.getAllFoodItems();
    List<FoodItem> ruleFilter = queryFoodData.getAllFoodItems();
    
    if (!nameQueryText.getText().isEmpty()) {
      nameFilter = queryFoodData.filterByName(nameQueryText.getText());
    }
    
    if (nutrientChoice.getValue() != null && comparatorChoice.getValue() != null && !nutrientValue.getText().isEmpty()) {
      StringBuilder ruleBuilder = new StringBuilder();
      ruleBuilder.append(nutrientChoice.getValue()).append(" ");
      ruleBuilder.append(comparatorChoice.getValue()).append(" ");
      ruleBuilder.append(nutrientValue.getText());
      String rule = ruleBuilder.toString();
      rules.add(rule);
      nutrientChoice.setValue(null);
      comparatorChoice.setValue(null);
      nutrientValue.clear();      
    }   
    if (!rules.isEmpty()) {
      ruleFilter = queryFoodData.filterByNutrients(rules);
    }
    
    filteredList = nameFilter.stream().filter(ruleFilter::contains).collect(Collectors.toList());

    refreshFoodTable(filteredList);
  }

  /**
   * Resets the food table to how it was before filtering
   */
  private void clearFilters() {
    rules.clear();
    nutrientChoice.setValue(null);
    comparatorChoice.setValue(null);
    nutrientValue.clear();
    refreshFoodTable(workingFoodData.getAllFoodItems());
  }

  /**
   * Updates the food list table. Triggered after nearly every change to the food items in the
   * table. FIXME loading a file double adds to table, needs to replace
   *
   * @param list of FoodItems
   */
  private void refreshFoodTable(List<FoodItem> list) {
    foodObsList = FXCollections.observableArrayList(list);
    FXCollections.sort(foodObsList, tableSort);
    foodListTable.getItems().clear();
    foodListTable.setItems(foodObsList);
    counter.setText(String.valueOf(foodObsList.size()));
  }

  /**
   * Creates a popup window that has the aggregated food item and nutritional information of all
   * items in the meal table. TODO Get the food items in there and clean up layout
   */
  private void analyzeMeal() {
    Stage window = new Stage();

    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle("Meal Analysis");
    window.setMinWidth(250);

    Label label = new Label();
    label.setText("The analysis of your meal:");
    GridPane.setConstraints(label, 0, 0);
    GridPane.setColumnSpan(label, 2);
    GridPane.setHalignment(label, HPos.CENTER);

    Double totalCals = 0.0;
    Double totalCarbs = 0.0;
    Double totalFat = 0.0;
    Double totalProtein = 0.0;
    Double totalFiber = 0.0;

    String mealFoods = "Food items in this meal:\n";
    for (FoodItem i : mealObsList) {
      mealFoods.concat(i.getName() + "\n");
      totalCals += i.getNutrientValue("calories");
      totalCarbs += i.getNutrientValue("carbohydrate");
      totalFat += i.getNutrientValue("fat");
      totalProtein += i.getNutrientValue("protein");
      totalFiber += i.getNutrientValue("fiber");
    }
    Text foodList = new Text(mealFoods);
    GridPane.setConstraints(foodList, 0, 1);

    String nutriStr = "Nutrient values for this meal:\n" + totalCals + " calories\n" + totalCarbs
        + " carbohydrates\n" + totalFat + " fat\n" + totalProtein + " protein\n" + totalFiber
        + " fiber";
    Text nutriTxt = new Text(nutriStr);
    GridPane.setConstraints(nutriTxt, 1, 1);

    Button closeButton = new Button("OK");
    closeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    closeButton.setOnAction(e -> window.close());
    GridPane.setConstraints(closeButton, 0, 2);
    GridPane.setColumnSpan(closeButton, 2);
    GridPane.setHalignment(closeButton, HPos.CENTER);

    GridPane grid = new GridPane();
    grid.getChildren().addAll(label, foodList, nutriTxt, closeButton);
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(20, 20, 20, 20));
    grid.setHgap(10);
    grid.setVgap(10);

    Scene scene = new Scene(grid);
    window.setScene(scene);
    window.showAndWait();
  }

  /**
   * Displays a dialog box when an exception is thrown with information about the exception.
   *
   * @param e Exception being thrown.
   */
  public void showExceptionDialog(Exception e) {
    // TODO add functionality
  }
}