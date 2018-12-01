import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

public class TestFoodData {
	
	private FoodData foodData;
	
	@Before
	public void setup() {
		foodData = new FoodData();
	}
	
	@Test
	public void test01LoadFoodItems() {
		try {
			foodData.loadFoodItems("foodItems.csv");
			assertEquals(250, foodData.getAllFoodItems().size());
		} catch (Exception e) {
			fail("Unexpecteldy caught: " + e);
		}
	}
	
	@Test
	public void test02AddFoodItem() {
		// 51c38f5d97c3e6d3d972f08a,Similac_FormulaSoyforDiarrheaReadytoFeed,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
		FoodItem foodItem = new FoodItem("51c38f5d97c3e6d3d972f08a", "Similac_FormulaSoyforDiarrheaReadytoFeed");
		try {
			foodData.addFoodItem(foodItem);
			assertEquals(1, foodData.getAllFoodItems().size());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void test03FoodDataConstructor() {
		try {
			foodData = new FoodData();
			assertNotNull(foodData);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void test04FilterByNameLowerCase() {
		try {
			foodData.loadFoodItems("foodItems.csv");
			List<FoodItem> actual = foodData.filterByName("soy");
			assertEquals(2, actual.size());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void test05FilterByNameUpperCase() {
		try {
			foodData.loadFoodItems("foodItems.csv");
			List<FoodItem> actual = foodData.filterByName("SOY");
			assertEquals(2, actual.size());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void test06FilterByNameNoReults() {
		try {
			foodData.loadFoodItems("foodItems.csv");
			List<FoodItem> actual = foodData.filterByName("project");
			assertEquals(0, actual.size());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void test07SaveFoodItemsAscendingOrder() {
		try {
			foodData.loadFoodItems("foodItems.csv");
			foodData.saveFoodItems("output.csv");
			List<FoodItem> items = foodData.filterByName("180Energy_X33CarbsLemonLimeCitrusBlast");
			assertEquals("180Energy_X33CarbsLemonLimeCitrusBlast", items.get(0).getName());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void test08SaveFoodItems() {
		try {
			foodData.loadFoodItems("foodItems.csv");
			foodData.saveFoodItems("output.csv");
			foodData.loadFoodItems("output.csv");
			List<FoodItem> items = foodData.filterByName("180Energy_X33CarbsLemonLimeCitrusBlast");
			assertEquals("180Energy_X33CarbsLemonLimeCitrusBlast", items.get(0).getName());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
    public void test09FilterByNutrientsInvalid() {
        try {         
            List<String> rules = Arrays.asList("calories <> 200.0", "sugar == 3.0", "invalid");
            foodData.loadFoodItems("foodItems.csv"); 
            assertTrue(foodData.filterByNutrients(rules).isEmpty());           
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test09FilterByNutrients() {
        try {         
            List<String> rules = Arrays.asList("calories >= 50.0", "calories <= 200.0", "fiber == 3.0");
            List<String> resultIDs = Arrays.asList("55578545507a5ec8205bd6bd", "553bd4071c0097d62ef82121", "55578538f6274ff415e61103", "5505dcc077b7c8f122a48282");
            foodData.loadFoodItems("foodItems.csv");            
            List<FoodItem> expected = foodData.getAllFoodItems().stream().filter(item -> resultIDs.contains(item.getID())).collect(Collectors.toList());            
            assertEquals(expected, foodData.filterByNutrients(rules));
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }

}
