import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
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
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
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
    	
        // TODO : Complete loading nutrients when BPTree is implemented
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	List<FoodItem> filtered = foodItemList.stream()
    			.filter(item -> item.getName().toLowerCase().contains(substring.toLowerCase()))
    			.collect(Collectors.toList());
    	return filtered;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	foodItemList.add(foodItem);
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


	@Override
	public void saveFoodItems(String filename) {
		// TODO Auto-generated method stub
		
	}

}
