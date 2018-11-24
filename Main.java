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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Entry point of the FoodQuery Program
 * Houses GUI code for now.
 */
public class Main extends Application {

	Stage mainStage;
	GridPane grid;

	Button loadFileButton;
	Button saveFileButton;

	HBox foodItemInsert;
	TextField insertId;
	TextField insertName;
	TextField insertCals;
	TextField insertCarbs;
	TextField insertFat;
	TextField insertProtein;
	TextField insertFiber;
	Button newFoodButton;
	Button clearFoodEntry;

	FoodData mainFoodList;
	ObservableList<FoodItem> tempFoodList;
	ObservableList<FoodItem> menuList;

	VBox transferButtons;
	Button addToMenu;
	Button removeFromMenu;

	VBox queries;
	HBox nameQuery;
	TextField nameText;
	Button nameQueryButton;
	Text orText;
	HBox nutriQuery;
	TextField queryAmount;
	Button nutriQueryButton;
	Button clearQueries;

	Button analyzeMeal;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		mainStage = new Stage();
		mainStage.setTitle("Menu Selector");

		grid = new GridPane();
		//				grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setPadding(new Insets(10,10,10,10));

		mainFoodList = new FoodData();
		mainFoodList.addFoodItem(new FoodItem("12", "1 pound of Kale"));
		mainFoodList.addFoodItem(new FoodItem("1234lkj1243", "1 Leftover Turkey Drumstick"));

		tempFoodList = FXCollections.observableArrayList(mainFoodList.getAllFoodItems());
		menuList = FXCollections.observableArrayList();


		// FOOD LIST MENU
		HBox listMenu = new HBox();
		GridPane.setConstraints(listMenu, 0, 0);
		// FOOD LIST LOAD
		loadFileButton = new Button("Load Food List");
		loadFileButton.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Load Food List");
			File file = fc.showOpenDialog(mainStage);
			if (file != null) {
				mainFoodList.loadFoodItems(file.toString());

			}
			else {
				System.out.println("File not found");
			}

		});

		// FOOD LIST SAVE
		saveFileButton = new Button("Save Food List");
		saveFileButton.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Save Food List");
			File file = fc.showSaveDialog(mainStage);
			if (file != null) {
				mainFoodList.saveFoodItems(file.toString());

			}
			else {
				System.out.println("File not found");
			}

		});

		listMenu.getChildren().addAll(loadFileButton, saveFileButton);

		// FOOD ITEM INSERTER
		HBox foodItemInsert = new HBox();

		insertId = new TextField();
		insertId.setPrefWidth(50);
		insertId.setPromptText("ID");
		insertName = new TextField();
		insertName.setPrefWidth(100);
		insertName.setPromptText("Name");
		insertCals = new TextField();
		insertCals.setPrefWidth(70);
		insertCals.setPromptText("Calories");
		insertCarbs = new TextField();
		insertCarbs.setPrefWidth(70);
		insertCarbs.setPromptText("Carbs");
		insertFat = new TextField();
		insertFat.setPrefWidth(70);
		insertFat.setPromptText("Fat");
		insertProtein = new TextField();
		insertProtein.setPrefWidth(70);
		insertProtein.setPromptText("Protein");
		insertFiber = new TextField();
		insertFiber.setPrefWidth(70);
		insertFiber.setPromptText("Fiber");
		newFoodButton = new Button("Add");
		newFoodButton.setOnAction(e -> {
			FoodItem addedFood = new FoodItem(insertId.getText(), insertName.getText());
						addedFood.addNutrient("Calories", new Double(insertCals.getText()));
			//			addedFood.addNutrient("Carbs", new Double(insertCarbs.getText()));
			//			addedFood.addNutrient("Fat", new Double(insertFat.getText()));
			//			addedFood.addNutrient("Protein", new Double(insertProtein.getText()));
			//			addedFood.addNutrient("Fiber", new Double(insertFiber.getText()));
			tempFoodList.add(addedFood);
		});
		clearFoodEntry = new Button("Clear");
		clearFoodEntry.setOnAction(e -> {
			insertId.clear();
			insertName.clear();
			insertCals.clear();
			insertCarbs.clear();
			insertFat.clear();
			insertProtein.clear();

			insertFiber.clear();
		});
		foodItemInsert.getChildren().addAll(insertId, insertName, insertCals,
				insertCarbs, insertFat, insertProtein, insertFiber, newFoodButton, clearFoodEntry);

		GridPane.setConstraints(foodItemInsert, 0, 1);
		GridPane.setColumnSpan(foodItemInsert, 3);


		// LIST
		// ID column
		TableColumn<FoodItem,String> foodID = new TableColumn<>("ID");
		foodID.setCellValueFactory(new PropertyValueFactory<>("ID"));

		// Name column
		TableColumn<FoodItem,String> foodName = new TableColumn<>("Name");
		foodName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Calories column
		TableColumn<FoodItem,Integer> foodCals = new TableColumn<>("Calories");
		foodCals.setCellValueFactory(new PropertyValueFactory<>("Calories"));

		// Carbs column
		TableColumn<FoodItem,Integer> foodCarbs = new TableColumn<>("Carbs");
		foodCarbs.setCellValueFactory(new PropertyValueFactory<>("Carbs"));

		// Fat column
		TableColumn<FoodItem,Integer> foodFat = new TableColumn<>("Fat");
		foodFat.setCellValueFactory(new PropertyValueFactory<>("Fat"));

		// Protein column
		TableColumn<FoodItem,Integer> foodProtein = new TableColumn<>("Protein");
		foodProtein.setCellValueFactory(new PropertyValueFactory<>("Protein"));

		// Fiber column
		TableColumn<FoodItem,Integer> foodFiber = new TableColumn<>("Fiber");
		foodFiber.setCellValueFactory(new PropertyValueFactory<>("Fiber"));

		TableView<FoodItem> foodListTable = new TableView<FoodItem>(tempFoodList);
		foodListTable.getColumns().setAll(foodID, foodName, foodCals, foodCarbs, foodFat, foodProtein, foodFiber);

		GridPane.setConstraints(foodListTable, 0, 2);
		GridPane.setColumnSpan(foodListTable, 2);


		// MENU
		// ID column
		TableColumn<FoodItem,String> menuID = new TableColumn<>("ID");
		menuID.setCellValueFactory(new PropertyValueFactory<>("id"));

		// Name column
		TableColumn<FoodItem,String> menuName = new TableColumn<>("Name");
		menuName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Calories column
		TableColumn<FoodItem,Integer> menuCals = new TableColumn<>("Calories");
		menuCals.setCellValueFactory(new PropertyValueFactory<>("Calories"));

		// Carbs column
		TableColumn<FoodItem,Integer> menuCarbs = new TableColumn<>("Carbs");
		menuCarbs.setCellValueFactory(new PropertyValueFactory<>("Carbs"));

		// Fat column
		TableColumn<FoodItem,Integer> menuFat = new TableColumn<>("Fat");
		menuFat.setCellValueFactory(new PropertyValueFactory<>("Fat"));

		// Protein column
		TableColumn<FoodItem,Integer> menuProtein = new TableColumn<>("Protein");
		menuProtein.setCellValueFactory(new PropertyValueFactory<>("Protein"));

		// Fiber column
		TableColumn<FoodItem,Integer> menuFiber = new TableColumn<>("Fiber");
		menuFiber.setCellValueFactory(new PropertyValueFactory<>("Fiber"));

		TableView<FoodItem> menuListTable = new TableView<FoodItem>(menuList);
		menuListTable.getColumns().setAll(menuID, menuName, menuCals, menuCarbs, menuFat, menuProtein, menuFiber);

		GridPane.setConstraints(menuListTable, 3, 2);

		// LIST ITEM TRANSFER BUTTONS
		VBox transferButton = new VBox();
		transferButton.setAlignment(Pos.CENTER);
		transferButton.setPadding(new Insets(10, 10, 10, 10));
		// ADD BUTTON
		Button addToMenu = new Button("Add ->");
		addToMenu.setOnAction(e -> {
			for (FoodItem i : foodListTable.getSelectionModel().getSelectedItems()){
				menuList.add(i);}
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
		nameText.setPromptText("Filter by Name");
		Button nameQueryButton = new Button("Add Name Filter");
		nameQueryButton.setOnAction(e -> {
			tempFoodList = FXCollections.observableArrayList(mainFoodList.filterByName(nameText.getText()));
		});
		nameQuery.getChildren().addAll(nameText, nameQueryButton);

		Text orText = new Text("- OR - ");


		// QUERY BY NUTRIENTS
		HBox nutriQuery = new HBox();
		TextField queryRules = new TextField();
		queryRules.setPromptText("Nutrient Filter Rules");
		Button nutriQueryButton = new Button("Add Nutrient Filter");
		nutriQueryButton.setOnAction(e -> {
			String[] queryArray = queryAmount.getText().split(",");
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
		clearQuery.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(clearQuery, 1, 3);

		// ANALYSIS
		Button analyze = new Button("Analyze Meal");
		GridPane.setConstraints(analyze, 3, 3);

		TextArea mealAnalysis = new TextArea();
		GridPane.setConstraints(mealAnalysis, 3, 4);


		grid.getChildren().addAll(listMenu, foodItemInsert, foodListTable,
				menuListTable, transferButton, queries, clearQuery, analyze, mealAnalysis);

		Scene mainScene = new Scene(grid);
		mainStage.setScene(mainScene);
		mainStage.show();

	}

}
