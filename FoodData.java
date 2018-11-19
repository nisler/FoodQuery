/**
 * Filename:   FoodData.java
 * Project:    p5 - JavaFX Team Project
 * Course:     CS400
 * Authors:    Benjamin Nisler, Gabriella Cottiero, Olivia Gonzalez, 
 * 			   Timothy James, TOllan Renner
 * Due Date:   Saturday, December 15, 11:59pm
 *
 * Additional credits:
 *
 * Bugs or other notes: none
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems including loading and saving food
 * items, filtering the food item list by name and nutrient, adding a food item 
 * and retrieving a food item.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    /**
     * Public constructor
     */
    public FoodData() {
    	foodItemList = new ArrayList<>();
    	indexes = new HashMap<>();
    }
    
    /**
     * Load food items contained in the file
     * 
     * @param filePath path to the file to load
     */
    @Override
    public void loadFoodItems(String filePath) {
        // TODO : Complete loading nutrients when BPTree is implemented
    	// TODO : add more exception handling and input checks
    	Stream<String> lines = null;
    	try {
			lines = Files.lines(Paths.get(filePath));
			lines
			.map(line -> {
				List<String> tokens = Arrays.asList(line.split(","));
				FoodItem foodItem = null;
				if (tokens.size() > 0) {
					foodItem = new FoodItem(tokens.get(0), tokens.get(1));	
				}
				return foodItem;
			})
			.forEach(item -> {
				if (item != null) {
					addFoodItem(item);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * Filters the food items by the parameter substring. Matching is case 
     * insensitive and will match on a food item if the whole substring is in 
     * the name.
     * 
     * @param substring the string of text to filter the name by
     * @return a list of filtered food items
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	List<FoodItem> filteredFoodItemList = foodItemList.stream()
    			.filter(item -> item.getName().toLowerCase().contains(substring.toLowerCase()))
    			.collect(Collectors.toList());
    	return filteredFoodItemList;
    }

    /**
     * TODO
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        // TODO : Complete
        return null;
    }

    /**
     * Adds a food item to the loaded data
     * 
     * @param foodItem the food item to be added
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	foodItemList.add(foodItem);
    }

    /**
     * Retrieves the list of all food items
     * 
     * @return list of all the food items
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


    /**
     * Save the list of food items in ascending order by name
     * 
     * @param filename the name of the file and location of it being saved
     */
	@Override
	public void saveFoodItems(String filename) {
		// TODO : add nutrients
		// TODO : more exception handling and file output checks
		List<FoodItem> sortedFoodItemList = foodItemList.stream()
				.sorted((item1, item2) -> item1.getName().compareToIgnoreCase(item2.getName()))
				.collect(Collectors.toList());
		
		try (PrintWriter printwriter = new PrintWriter(Files.newBufferedWriter(Paths.get("./" + filename)))) {
			sortedFoodItemList.forEach(item -> {
				StringBuilder itemString = new StringBuilder();
				itemString.append(item.getID() + ",");
				itemString.append(item.getName() + ",");
				
				Set<String> nutrientSet = item.getNutrients().keySet();
				for (String nutrient : nutrientSet) {
					itemString.append(nutrient + ",");
					itemString.append(item.getNutrientValue(nutrient) + ",");
				}
				
				printwriter.println(itemString.toString());
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
