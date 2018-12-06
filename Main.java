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
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Entry point of the FoodQuery Program Houses GUI code for now.
 */
public class Main extends Application {

  // Application's stage
  Stage mainStage, exceptStage;
  // Stage's Scene
  Scene mainScene;
  // Scene's overarching grid
  GridPane mainGrid, queryGrid;

  // Application's buttons
  Button addToMeal, removeFromMeal, queryButton, removeQueryButton, analyzeButton;

  // Applications Containers
  VBox transferButtons, queries;
  HBox listMenu, foodItemInsert;

  // Application's text fields
  TextField insertId, insertName, insertCals, insertCarbs, insertFat, insertProtein, insertFiber,
      nameQueryText, nutrientValue, nameFilterText;

  Label counterLabel;

  // nutrient rules
  GridPane nutrientRuleGrid;
  ComboBox<String> nutrientChoice;
  ComboBox<String> comparatorChoice;
  ObservableList<String> rules = FXCollections.observableArrayList();
  ListView<String> rulesList = new ListView<String>(rules);

  // Applications data structures
  // Underlying foodlist object
  FoodData masterFoodData, workingFoodData, queryFoodData;
  // current viewable food list as it's being altered by filters or additions
  ObservableList<FoodItem> foodObsList, mealObsList, queryObsList;
  // the list used in the menu
  List<FoodItem> filteredList;
  TableView<FoodItem> foodListTable, mealListTable;
  TableColumn<FoodItem, String> foodID, foodName, menuID, mealName;
  TableColumn<FoodItem, Double> foodCals, foodCarbs, foodFat, foodProtein, foodFiber, mealCals,
      mealCarbs, mealFat, mealProtein, mealFiber;

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
    mainStage.setMaximized(false);


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

    // Menubar
    Menu fileMenu = new Menu("Food List");
    MenuItem loadList = new MenuItem("Load Food List From File");
    loadList.setOnAction(e -> loadFile());
    MenuItem saveList = new MenuItem("Save Current Food List");
    saveList.setOnAction(e -> saveFile());
    fileMenu.getItems().addAll(loadList, saveList);

    Menu foodItemMenu = new Menu("Food Item");
    MenuItem addFood = new MenuItem("Add New Food Item To List");
    addFood.setOnAction(e -> addCustomFood());
    foodItemMenu.getItems().addAll(addFood);

    MenuBar menuBar = new MenuBar(fileMenu, foodItemMenu);

    // FOOD TABLE
    HBox foodTableHeader = new HBox();
    Label foodTableTitle = new Label("Food List");
    foodTableTitle.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    foodTableTitle.setAlignment(Pos.CENTER);
    HBox.setHgrow(foodTableTitle, Priority.ALWAYS);

    counterLabel = new Label("Number of items in list: " + foodObsList.size());
    counterLabel.setAlignment(Pos.CENTER);
    counterLabel.setMaxHeight(Double.MAX_VALUE);

    foodTableHeader.getChildren().addAll(foodTableTitle, counterLabel);
    GridPane.setConstraints(foodTableHeader, 0, 0);


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

    // Fat column
    foodFat = new TableColumn<>("Fat");
    foodFat.setPrefWidth(50);
    foodFat.setSortable(false);
    foodFat.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fat")).asObject());

    // Carbs column
    foodCarbs = new TableColumn<>("Carbs");
    foodCarbs.setPrefWidth(50);
    foodCarbs.setSortable(false);
    foodCarbs.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("carbohydrate")).asObject());

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
    foodListTable.setPrefWidth(585);
    foodListTable.getColumns().setAll(foodName, foodCals, foodFat, foodCarbs, foodProtein,
        foodFiber);

    GridPane.setConstraints(foodListTable, 0, 1);

    // MENU TABLE
    Label mealTableTitle = new Label("Meal List");
    mealTableTitle.setAlignment(Pos.CENTER);
    mealTableTitle.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    GridPane.setConstraints(mealTableTitle, 2, 0);

    // Name column
    mealName = new TableColumn<>("Name");
    mealName.setPrefWidth(320);
    mealName.setSortable(false);
    mealName.setCellValueFactory(new PropertyValueFactory<>("name"));

    // Calories column
    mealCals = new TableColumn<>("Calories");
    mealCals.setPrefWidth(50);
    mealCals.setSortable(false);
    mealCals.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

    // Fat column
    mealFat = new TableColumn<>("Fat");
    mealFat.setPrefWidth(50);
    mealFat.setSortable(false);
    mealFat.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fat")).asObject());

    // Carbs column
    mealCarbs = new TableColumn<>("Carbs");
    mealCarbs.setPrefWidth(50);
    mealCarbs.setSortable(false);
    mealCarbs.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("carbohydrate")).asObject());

    // Protein column
    mealProtein = new TableColumn<>("Protein");
    mealProtein.setPrefWidth(50);
    mealProtein.setSortable(false);
    mealProtein.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("protein")).asObject());

    // Fiber column
    mealFiber = new TableColumn<>("fiber");
    mealFiber.setPrefWidth(50);
    mealFiber.setSortable(false);
    mealFiber.setCellValueFactory(
        c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

    mealListTable = new TableView<FoodItem>(mealObsList);
    mealListTable.setPrefWidth(585);
    mealListTable.getColumns().setAll(mealName, mealCals, mealFat, mealCarbs, mealProtein,
        mealFiber);

    GridPane.setConstraints(mealListTable, 2, 1);

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
    GridPane.setConstraints(transferButtons, 1, 1);

    // FILTER PANE
    Label currentFilterLabel = new Label("Current filter: ");
    currentFilterLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    TextField currentFilter = new TextField();
    currentFilter.setPromptText("Food List currently has no filters");
    currentFilter.setEditable(false);
    currentFilter.setMaxWidth(Double.MAX_VALUE);
    Button clearFilters = new Button("Clear Filters");
    HBox filterStatus = new HBox(currentFilterLabel, currentFilter, clearFilters);

    Label filterLabel = new Label("Food List Filter");
    filterLabel.setAlignment(Pos.CENTER);

    Label nameFilter1 = new Label("Show food with ");
    nameFilter1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    nameFilterText = new TextField();
    nameFilterText.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    nameFilterText.textProperty().addListener((observable, oldValue, newValue) -> {
      applyFilters();
    });
    Label nameFilter2 = new Label(" in the name.");
    nameFilter2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    HBox nameFilter = new HBox(nameFilter1, nameFilterText, nameFilter2);

    Label andOr = new Label("Show food that has ");
    andOr.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    comparatorChoice = new ComboBox<String>(FXCollections
        .observableArrayList("More than or equal to", "Exactly", "Less than or equal to"));
    comparatorChoice.setPromptText("-More or Less than-");
    nutrientValue = new TextField();
    nutrientValue.setPromptText("-amount-");
    nutrientValue.setPrefWidth(75);
    nutrientValue.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        if (!newValue.matches("\\d*\\.?\\d*")) { // only allow numerics
          nutrientValue.setText(oldValue);
        }
      }
    });
    nutrientChoice = new ComboBox<String>(FXCollections.observableArrayList("calories",
        "grams of fat", "carbohydrates", "grams of fiber", "grams of protein"));
    nutrientChoice.setPromptText("-nutrient-");
    Button addFilter = new Button("Add Filter");
    HBox filterRow = new HBox(andOr, comparatorChoice, nutrientValue, nutrientChoice, addFilter);

    VBox filterPane = new VBox(filterLabel, filterStatus, nameFilter, filterRow);

    GridPane.setConstraints(filterPane, 0, 2);

    // ANALYSIS
    analyzeButton = new Button("Analyze Meal");
    analyzeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    analyzeButton.setOnAction(e -> {
      analyzeMeal();
    });
    GridPane.setConstraints(analyzeButton, 2, 2);

    // WINDOW SETUP
    mainGrid.getChildren().addAll(foodTableHeader, foodListTable, mealListTable, mealTableTitle,
        transferButtons, filterPane, analyzeButton);
    VBox mainLayout = new VBox(menuBar, mainGrid);

    mainScene = new Scene(mainLayout);
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
      masterFoodData = new FoodData();
      masterFoodData.loadFoodItems(file.toString());
      workingFoodData = masterFoodData;
      refreshFoodTable(workingFoodData.getAllFoodItems());
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
    Stage addItemStage = new Stage();
    addItemStage.initStyle(StageStyle.UTILITY);

    Text addItemText = new Text(
        "Please use the fields below to enter information about your food. Remember to fill all fields and nutrient values must be numbers.");
    addItemText.setWrappingWidth(300);

    // FOOD ITEM INSERTER
    insertId = new TextField();
    insertId.setPrefWidth(165);
    insertId.setPromptText("New Item ID");

    insertName = new TextField();
    insertName.setPrefWidth(320);
    insertName.setPromptText("New Item Name");

    insertCals = new TextField();
    insertCals.setPrefWidth(60);
    insertCals.setPromptText("Calories");

    insertCarbs = new TextField();
    insertCarbs.setPrefWidth(60);
    insertCarbs.setPromptText("Carbs");

    insertFat = new TextField();
    insertFat.setPrefWidth(60);
    insertFat.setPromptText("Fat");

    insertProtein = new TextField();
    insertProtein.setPrefWidth(60);
    insertProtein.setPromptText("Protein");

    insertFiber = new TextField();
    insertFiber.setPrefWidth(60);
    insertFiber.setPromptText("Fiber");

    foodItemInsert = new HBox(insertId, insertName, insertCals, insertCarbs, insertFat,
        insertProtein, insertFiber);

    Button addItem = new Button("OK");

    addItem.setOnAction(e -> {
      FoodItem addedFood = new FoodItem(insertId.getText(), insertName.getText());
      addedFood.addNutrient("calories", new Double(insertCals.getText()));
      addedFood.addNutrient("carbohydrate", new Double(insertCarbs.getText()));
      addedFood.addNutrient("fat", new Double(insertFat.getText()));
      addedFood.addNutrient("protein", new Double(insertProtein.getText()));
      addedFood.addNutrient("fiber", new Double(insertFiber.getText()));
      workingFoodData.addFoodItem(addedFood);
      refreshFoodTable(workingFoodData.getAllFoodItems());
      addItemStage.close();
    });

    Button cancel = new Button("Cancel");
    cancel.setOnAction(e -> addItemStage.close());

    HBox stageButtons = new HBox(addItem, cancel);
    stageButtons.setPadding(new Insets(20, 20, 20, 20));
    stageButtons.setAlignment(Pos.CENTER);

    VBox addItemLayout = new VBox(addItemText, foodItemInsert, stageButtons);
    addItemLayout.setPadding(new Insets(20, 20, 20, 20));
    addItemLayout.setAlignment(Pos.CENTER);

    addItemStage.setScene(new Scene(addItemLayout));
    addItem.requestFocus();
    addItemStage.showAndWait();
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
    if (!mealListTable.getSelectionModel().isEmpty()) {
      mealObsList.remove(mealListTable.getSelectionModel().getSelectedItem());
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

    if (!nameFilterText.getText().isEmpty()) {
      nameFilter = queryFoodData.filterByName(nameFilterText.getText());
    }

    if (nutrientChoice.getValue() != null && comparatorChoice.getValue() != null
        && !nutrientValue.getText().isEmpty()) {
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
    counterLabel.setText("Number of items in list: " + foodObsList.size());
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
  public static void showExceptionAlert(Exception e) {
    Alert exception = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.OK);
    exception.showAndWait();
  }
}
