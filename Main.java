/**
 * Filename:   Main.java
 * Project:    p5 - JavaFX Team Project
 * Course:     CS400
 * Authors:    Benjamin Nisler, Gabriella Cottiero, Olivia Gonzalez,
 * 			   Timothy James, TOllan Renner
 * Due Date:   Saturday, December 15, 11:59pm
 *
 * Additional credits:
 *
 * Bugs or other notes: //TODO implement load button, save button, filters, add individual foods with nutrient info, analyze meal dialog popup.
 */

import java.io.File;
import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Entry point of the FoodQuery Program
 * Houses GUI code for now.
 */
public class Main extends Application {

	// Application's stage
	Stage mainStage;
	// Stage's Scene
	Scene mainScene;
	// Scene's overarching grid
	GridPane grid;

	// Application's buttons
	Button loadFileButton, saveFileButton, newFoodButton, clearFoodEntry, addToMenu, removeFromMenu, nameQueryButton, nutriQueryButton, clearQueries, analyzeMeal;

	// Applications Containers
	VBox transferButtons, queries;
	HBox listMenu, nameQuery, nutriQuery, foodItemInsert;

	// Application's text fields
	TextField insertId, insertName, insertCals, insertCarbs, insertFat, insertProtein, insertFiber, nameText, queryAmount, counter;
	Text orText;

	// Applications data structures
	// Underlying foodlist object
	FoodData mainFoodList;
	// current viewable food list as it's being altered by filters or additions
	ObservableList<FoodItem> tempFoodList;
	// the list used in the menu
	ObservableList<FoodItem> menuList;

	TableView<FoodItem> foodListTable, menuListTable;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		// MainStage Settings
		mainStage = new Stage();
		mainStage.setTitle("Menu Selector");
		mainStage.setMaximized(false);

		// Grid Settings
		grid = new GridPane();
		grid.setGridLinesVisible(false);
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setPadding(new Insets(10,10,10,10));



		mainFoodList = new FoodData();
		mainFoodList.loadFoodItems("foodItems.txt");

		tempFoodList = FXCollections.observableArrayList(mainFoodList.getAllFoodItems());
//        tempFoodList.addListener(new ListChangeListener<FoodItem>() {
//            @Override
//            public void onChanged(Change<? extends FoodItem> c) {
//                while (c.next()) {
//                    if (c.wasPermutated()) {
//                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
//                            System.out.println("Permuted: " + i + " " + tempFoodList.get(i));
//                        }
//                    } else if (c.wasUpdated()) {
//                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
//                            System.out.println("Updated: " + i + " " + tempFoodList.get(i));
//                        }
//                    } else {
//                        for (FoodItem removedItem : c.getRemoved()) {
//                            System.out.println("Removed: " + removedItem);
//                        }
//                        for (FoodItem addedItem : c.getAddedSubList()) {
//                            System.out.println("Added: " + addedItem);
//                        }
//                    }
//                }
//            }
//        });
		menuList = FXCollections.observableArrayList();


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

		newFoodButton = new Button("Add");
		newFoodButton.setOnAction(e -> {
			addCustomFood();
		});


		clearFoodEntry = new Button("Clear");
		clearFoodEntry.setOnAction(e -> {
			clearFoodEntry();
		});
		
		Label counterLabel = new Label("Number of items in list:");
		counterLabel.setAlignment(Pos.BOTTOM_CENTER);
		counter = new TextField();
		counter.setText(String.valueOf(tempFoodList.size()));
		counter.setMaxWidth(50);
	

		listMenu.prefWidth(750);
		listMenu.setPadding(new Insets(10,10,10,10));
		listMenu.getChildren().addAll(loadFileButton, saveFileButton, region, counterLabel, counter, newFoodButton, clearFoodEntry);
		GridPane.setConstraints(listMenu, 0, 0);
		GridPane.setColumnSpan(listMenu, 2);

		// FOOD ITEM INSERTER
		foodItemInsert = new HBox();

		insertId = new TextField();
		insertId.setPrefWidth(165);
		insertId.setPromptText("ID");
		insertName = new TextField();
		insertName.setPrefWidth(320);
		insertName.setPromptText("Name");
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
		foodItemInsert.getChildren().addAll(insertId, insertName, insertCals,
				insertCarbs, insertFat, insertProtein, insertFiber);

		GridPane.setConstraints(foodItemInsert, 0, 1);
		GridPane.setColumnSpan(foodItemInsert, 3);


		// LIST
		// ID column
		TableColumn<FoodItem,String> foodID = new TableColumn<>("ID");
		foodID.setPrefWidth(165); foodID.setSortable(false);
		foodID.setCellValueFactory(new PropertyValueFactory<>("ID"));

		// Name column
		TableColumn<FoodItem,String> foodName = new TableColumn<>("Name");
		foodName.setPrefWidth(320); foodName.setSortable(true); foodName.setSortType(SortType.ASCENDING);
		foodName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Calories column
		TableColumn<FoodItem,Double> foodCals = new TableColumn<>("Calories");
		foodCals.setPrefWidth(50); foodCals.setSortable(false);
		foodCals.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

		// Carbs column
		TableColumn<FoodItem,Double> foodCarbs = new TableColumn<>("Carbs");
		foodCarbs.setPrefWidth(50); foodCarbs.setSortable(false);
		foodCarbs.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("carbohydrate")).asObject());

		// Fat column
		TableColumn<FoodItem,Double> foodFat = new TableColumn<>("Fat");
		foodFat.setPrefWidth(50); foodFat.setSortable(false);
		foodFat.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fat")).asObject());

		// Protein column
		TableColumn<FoodItem,Double> foodProtein = new TableColumn<>("Protein");
		foodProtein.setPrefWidth(50); foodProtein.setSortable(false);
		foodProtein.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("protein")).asObject());

		// Fiber column
		TableColumn<FoodItem,Double> foodFiber = new TableColumn<>("Fiber");
		foodFiber.setPrefWidth(50); foodFiber.setSortable(false);
		foodFiber.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fiber")).asObject());

		foodListTable = new TableView<FoodItem>(tempFoodList);
		foodListTable.setPrefWidth(750); 
		foodListTable.getColumns().setAll(foodID, foodName, foodCals, foodCarbs, foodFat, foodProtein, foodFiber);
		foodListTable.getSortOrder().add(foodName);


		GridPane.setConstraints(foodListTable, 0, 2);
		GridPane.setColumnSpan(foodListTable, 2);


		// MENU
		// ID column
		TableColumn<FoodItem,String> menuID = new TableColumn<>("ID");
		menuID.setPrefWidth(165); menuID.setSortable(false);
		menuID.setCellValueFactory(new PropertyValueFactory<>("ID"));

		// Name column
		TableColumn<FoodItem,String> menuName = new TableColumn<>("Name");
		menuName.setPrefWidth(320); menuName.setSortable(false);
		menuName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Calories column
		TableColumn<FoodItem,Double> menuCals = new TableColumn<>("Calories");
		menuCals.setPrefWidth(50); menuCals.setSortable(false);
		menuCals.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

		// Carbs column
		TableColumn<FoodItem,Double> menuCarbs = new TableColumn<>("Carbs");
		menuCarbs.setPrefWidth(50); menuCarbs.setSortable(false);
		menuCarbs.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("carbohydrate")).asObject());

		// Fat column
		TableColumn<FoodItem,Double> menuFat = new TableColumn<>("Fat");
		menuFat.setPrefWidth(50); menuFat.setSortable(false);
		menuFat.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("fat")).asObject());

		// Protein column
		TableColumn<FoodItem,Double> menuProtein = new TableColumn<>("Protein");
		menuProtein.setPrefWidth(50); menuProtein.setSortable(false);
		menuProtein.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("protein")).asObject());

		// Fiber column
		TableColumn<FoodItem,Double> menuFiber = new TableColumn<>("fiber");
		menuFiber.setPrefWidth(50); menuFiber.setSortable(false);
		menuFiber.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getNutrientValue("calories")).asObject());

		menuListTable = new TableView<FoodItem>(menuList);
		menuListTable.setPrefWidth(750);
		menuListTable.getColumns().setAll(menuID, menuName, menuCals, menuCarbs, menuFat, menuProtein, menuFiber);

		GridPane.setConstraints(menuListTable, 3, 2);

		// LIST ITEM TRANSFER BUTTONS
		VBox transferButton = new VBox();
		transferButton.setAlignment(Pos.CENTER);
		transferButton.setPadding(new Insets(10, 10, 10, 10));

		// ADD BUTTON
		Button addToMenu = new Button("Add ->");
		addToMenu.setOnAction(e -> {
			transferToMenu();
		});

		// REMOVE BUTTON
		Button removeFromMenu = new Button("<- Remove");
		removeFromMenu.setOnAction(e -> {
			for (FoodItem i : menuListTable.getSelectionModel().getSelectedItems()){
				menuList.remove(i);}
		});
		transferButton.getChildren().addAll(addToMenu, removeFromMenu);

		GridPane.setConstraints(transferButton, 2, 2);

		// QUERIES
		VBox queries = new VBox();
		queries.setAlignment(Pos.CENTER);
		// QUERY BY NAME
		HBox nameQuery = new HBox();
		TextField nameText = new TextField();
		nameText.setMinWidth(500);
		nameText.setPromptText("Filter by Name");
		Button nameQueryButton = new Button("Add Name Filter");
		nameQueryButton.setOnAction(e -> {
			nameQuery();

		});
		nameQuery.getChildren().addAll(nameText, nameQueryButton);

		Text orText = new Text("- OR - ");


		// QUERY BY NUTRIENTS
		HBox nutriQuery = new HBox();
		TextField queryRules = new TextField();
		queryRules.setMinWidth(500);
		queryRules.setPromptText("Nutrient Filter Rules");
		Button nutriQueryButton = new Button("Add Nutrient Filter");
		nutriQueryButton.setOnAction(e -> {
			String[] queryArray = queryRules.getText().split(",");
			mainFoodList.filterByNutrients(Arrays.asList(queryArray));
		});
		nutriQuery.getChildren().addAll(queryRules, nutriQueryButton);

		queries.getChildren().addAll(nameQuery, orText, nutriQuery);

		GridPane.setConstraints(queries, 0, 3);

		Button clearQuery = new Button("Clear Queries");
		clearQuery.setOnAction(e -> {
			nameText.clear();
			queryRules.clear();
		});
		GridPane.setConstraints(clearQuery, 1, 3);

		// ANALYSIS
		Button analyze = new Button("Analyze Meal");
		analyze.setOnAction(e -> {
			analyzeMeal();
		});
		GridPane.setConstraints(analyze, 3, 3);

		//		TextArea mealAnalysis = new TextArea();
		//		GridPane.setConstraints(mealAnalysis, 3, 4);


		grid.getChildren().addAll(listMenu, foodItemInsert, foodListTable,
				menuListTable, transferButton, queries, clearQuery, analyze);

		mainScene = new Scene(grid);
		mainStage.setScene(mainScene);
		mainStage.show();

	}

	private void loadFile(){
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv", "*.txt"));
		fc.setTitle("Load Food List");
		File file = fc.showOpenDialog(mainStage);
		if (file != null) {
			mainFoodList.loadFoodItems(file.toString());
		}

		else {
			System.out.println("File not found");
		}
	}

	private void saveFile() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Save Food List");
		File file = fc.showSaveDialog(mainStage);
		if (file != null) {
			mainFoodList.saveFoodItems(file.toString());
		}
		else {
			System.out.println("File not found");
		}
	}

	private void addCustomFood() {
		FoodItem addedFood = new FoodItem(insertId.getText(), insertName.getText());
		addedFood.addNutrient("calories", new Double(insertCals.getText()));
		addedFood.addNutrient("carbohydrate", new Double(insertCarbs.getText()));
		addedFood.addNutrient("fat", new Double(insertFat.getText()));
		addedFood.addNutrient("protein", new Double(insertProtein.getText()));
		addedFood.addNutrient("fiber", new Double(insertFiber.getText()));
		tempFoodList.add(addedFood);
		foodListTable.refresh();
		
	}

	private void clearFoodEntry() {
		insertId.clear();
		insertName.clear();
		insertCals.clear();
		insertCarbs.clear();
		insertFat.clear();
		insertProtein.clear();
		insertFiber.clear();
	}

	private void nameQuery() {
		tempFoodList = FXCollections.observableArrayList(mainFoodList.filterByName(nameText.getText()));
		foodListTable.refresh();
	}

	private void transferToMenu() {
		for (FoodItem i : foodListTable.getSelectionModel().getSelectedItems())	{
			menuList.add(i);
			menuListTable.refresh();
		}
	}

	private void analyzeMeal() {
		Stage window = new Stage();
		window.setTitle("Your Meal Analysis");
		window.setMinWidth(250);

		String meal = new String("Meal analysis information\nYada Yada");
		Text analysis = new Text(meal);
		Button closeButton = new Button ("Close the Window");
		closeButton.setOnAction(e -> window.close());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(analysis, closeButton);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}
