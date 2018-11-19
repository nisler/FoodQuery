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

/**TODO
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
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
     * TODO
     */
    @Override
    public void loadFoodItems(String filePath) {
        // TODO : Complete loading nutrients when BPTree is implemented, add more exception handling
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
     * TODO
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
     * TODO
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	foodItemList.add(foodItem);
    }

    /**
     * TODO
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


    /**
     * TODO
     */
	@Override
	public void saveFoodItems(String filename) {
		// TODO add nutrients and more exception handling
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
