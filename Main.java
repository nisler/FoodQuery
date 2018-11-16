import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		Stage mainStage = new Stage();
		mainStage.setTitle("Menu Selector");
		
		GridPane grid = new GridPane();
//		grid.setGridLinesVisible(true);
		grid.setAlignment(Pos.CENTER);
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setPadding(new Insets(10,10,10,10));
		
		ObservableList<FoodItem> foodList = FXCollections.observableArrayList();
		ObservableList<FoodItem> menuList = FXCollections.observableArrayList();
		
		
		// FOOD LIST INSERTER
		Button loadFileButton = new Button("Load Food Items");
		FileChooser loadFile = new FileChooser();
		//loadFileButton.setOnAction(e -> loadFile);
		GridPane.setConstraints(loadFileButton, 0, 0);
		
		
		// FOOD ITEM INSERTER
		HBox foodItemInsert = new HBox();
		
		TextField insertId = new TextField();
		insertId.setPrefWidth(50);
		insertId.setPromptText("ID");
		TextField insertName = new TextField();
		insertName.setPrefWidth(100);
		insertName.setPromptText("Name");
		TextField insertCals = new TextField();
		insertCals.setPrefWidth(70);
		insertCals.setPromptText("Calories");
		TextField insertCarbs = new TextField();
		insertCarbs.setPrefWidth(70);
		insertCarbs.setPromptText("Carbs");
		TextField insertFat = new TextField();
		insertFat.setPrefWidth(70);
		insertFat.setPromptText("Fat");
		TextField insertProtein = new TextField();
		insertProtein.setPrefWidth(70);
		insertProtein.setPromptText("Protein");
		TextField insertFiber = new TextField();
		insertFiber.setPrefWidth(70);
		insertFiber.setPromptText("Fiber");
		Button newFoodButton = new Button("Add");
		newFoodButton.setOnAction(e -> {
			foodList.add(new FoodItem(insertId.getText(), insertName.getText()));
		});
//		Button resetInsertFields = new Button("Reset");
//		resetInsertFields.setOnAction(e -> {
//			insertId.ins
//		});
		foodItemInsert.getChildren().addAll(insertId, insertName, insertCals, 
				insertCarbs, insertFat, insertProtein, insertFiber, newFoodButton);
		
		GridPane.setConstraints(foodItemInsert, 0, 1);
		GridPane.setColumnSpan(foodItemInsert, 2);
		
		
		// LIST
		// ID column
		TableColumn<FoodItem,String> foodID = new TableColumn<>("ID");
		foodID.setCellValueFactory(new PropertyValueFactory<>("id"));
		
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
		
		TableView<FoodItem> foodListTable = new TableView<FoodItem>(foodList);
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
			//TODO limit what shows in foodList
		});
		nameQuery.getChildren().addAll(nameText, nameQueryButton);
		
		Text orText = new Text("- OR - ");
		
		
		// QUERY BY NUTRIENTS
		HBox nutriQuery = new HBox();
		ComboBox<FoodItem> nutriDrop = new ComboBox<>();
		ComboBox<FoodItem> compDrop = new ComboBox<>();
		TextField queryAmount = new TextField();
		queryAmount.setPromptText("Amount");
		Button nutriQueryButton = new Button("Add Nutrient Filter");
		nutriQueryButton.setOnAction(e -> {
			//TODO limit what shows in foodList
		});
		nutriQuery.getChildren().addAll(nutriDrop, compDrop, queryAmount, nutriQueryButton);
		
		queries.getChildren().addAll(nameQuery, orText, nutriQuery);
		
		GridPane.setConstraints(queries, 0, 3);
		
		Button clearQuery = new Button("Clear Queries");
		clearQuery.setAlignment(Pos.BASELINE_CENTER);
		GridPane.setConstraints(clearQuery, 1, 3);
		
		// ANALYSIS
		Button analyze = new Button("Analyze Meal");
		GridPane.setConstraints(analyze, 3, 3);
		
		TextArea mealAnalysis = new TextArea();
		GridPane.setConstraints(mealAnalysis, 3, 4);
		
		
		grid.getChildren().addAll(loadFileButton, foodItemInsert, foodListTable, 
				menuListTable, transferButton, queries, clearQuery, analyze, mealAnalysis);

		Scene mainScene = new Scene(grid);		
		mainStage.setScene(mainScene);
		mainStage.show();

	}

}