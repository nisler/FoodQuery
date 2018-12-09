import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
    public void test10FilterByNutrientsNull() {
        try {         
            List<String> rules = Arrays.asList("calories >= 200.0", "calories <= 100.0");
            foodData.loadFoodItems("foodItems.csv"); 
            assertTrue(foodData.filterByNutrients(rules).isEmpty());           
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test11FilterByNutrientsBasic() {
        try {         
            List<String> rules = Arrays.asList("carbohydrate <= 30.0");
            foodData.loadFoodItems("foodItems.csv"); 
            assertEquals(215, foodData.filterByNutrients(rules).size());
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test12FilterByNutrientsAndName() {
        try { 
            foodData.loadFoodItems("foodItems.csv");
            List<String> rules = Arrays.asList("fiber >= 8.0");
            List<FoodItem> nutrientFilter = foodData.filterByNutrients(rules);
            List<FoodItem> nameFilter = foodData.filterByName("bean");
            List<String> expected = Arrays.asList("51c36bf997c3e69de4b07e5a", "51d2fbbecc9bff111580de97");
            List<String> actual = nutrientFilter.stream().filter(nameFilter::contains).map(item -> item.getID()).collect(Collectors.toList());            
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test13FilterByNutrientsMany() {
        try {         
            List<String> rules = Arrays.asList("calories >= 100.0", "calories <= 150.0", "fat >= 10.0", "protein >= 2.0");
            List<String> resultIDs = Arrays.asList("5538875b43a4145306cef38f", "561b02c51657bb352c688bb9", "51c37afc97c3e6d272824755");
            foodData.loadFoodItems("foodItems.csv");            
            List<FoodItem> expected = foodData.getAllFoodItems().stream().filter(item -> resultIDs.contains(item.getID())).collect(Collectors.toList());            
            assertEquals(expected, foodData.filterByNutrients(rules));
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test14FilterByNutrientsEqualsTo() {
        try {         
            List<String> rules = Arrays.asList("carbohydrate == 5.0");            
            foodData.loadFoodItems("foodItems.csv");            
            assertEquals(7, foodData.filterByNutrients(rules).size());
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test15FilterByNutrientsLikeEquals() {
        try {
            foodData.loadFoodItems("foodItems.csv"); 
            List<String> rules = Arrays.asList("calories >= 200.0", "calories <= 200.0");
            List<String> resultIDs = Arrays.asList("51d2f996cc9bff111580d05c", "51c3727597c3e69de4b0b62e", "53fde028852aa5123bb6dcd0", "558d9556c375fbae0f93c927");
            List<FoodItem> expected = foodData.getAllFoodItems().stream().filter(item -> resultIDs.contains(item.getID())).collect(Collectors.toList());            
            assertEquals(expected, foodData.filterByNutrients(rules));           
        } catch (Exception e) {
            fail("Unexpectedly caught: " + e);
        }
    }
	
	@Test
    public void test16FilterByNutrientsExample() {
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
