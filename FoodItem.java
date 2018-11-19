/**
 * Filename:   FoodItem.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a food item with all its properties, which includes
 * the food item's ID, name and nutrient information.
 */
public class FoodItem {
    // The name of the food item.
    private String name;

    // The id of the food item.
    private String id;

    // Map of nutrients and value.
    private HashMap<String, Double> nutrients;
    
    /**
     * Constructor
     * Constructs a FoodItem using the parameter id and name. Instantiates the
     * FoodItem's nutrient hashmap.
     * 
     * @param name name of the food item
     * @param id unique id of the food item 
     */
    public FoodItem(String id, String name) {
        this.id = id;
        this.name = name;
        this.nutrients = new HashMap<>();
    }
    
    /**
     * Gets the name of the food item
     * 
     * @return name of the food item
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique id of the food item
     * 
     * @return id of the food item
     */
    public String getID() {
        return id;
    }
    
    /**
     * Gets the nutrients of the food item
     * 
     * @return nutrients of the food item
     */
    public HashMap<String, Double> getNutrients() {
        return nutrients;
    }

    /**
     * Adds a nutrient and its value to this food. 
     * If nutrient already exists, updates its value.
     * 
     * @param name the name of the nutrient
     * @param value the value of the nutrient
     */
    public void addNutrient(String name, double value) {
    	nutrients.put(name, value);
    }

    /**
     * Returns the value of the given nutrient for this food item. 
     * If not present, then returns 0.0
     * 
     * @param name the name of the nutrient
     * @return double nutrient value for the food item, if not present, returns 0.0
     */
    public double getNutrientValue(String name) {
        return !nutrients.containsKey(name) ? 0.0 : nutrients.get(name);
    }
    
}
