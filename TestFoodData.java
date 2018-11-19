import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

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

}
