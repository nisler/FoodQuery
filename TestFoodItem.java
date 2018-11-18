import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TestFoodItem {

	private FoodItem foodItem;
	
	@Before
	public void setup() {
		// 51c38f5d97c3e6d3d972f08a,Similac_FormulaSoyforDiarrheaReadytoFeed,calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
		foodItem = new FoodItem("51c38f5d97c3e6d3d972f08a", "Similac_FormulaSoyforDiarrheaReadytoFeed");
	}
	
	@Test
	public void test01FoodItemConstructor() {
		try {
			foodItem = new FoodItem("51c38f5d97c3e6d3d972f08a", "Similac_FormulaSoyforDiarrheaReadytoFeed");
			assertNotNull(foodItem);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes02GetName() {
		String expected = "Similac_FormulaSoyforDiarrheaReadytoFeed";
		String actual = "";
		try {
			actual = foodItem.getName();
			assertEquals(expected, actual);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes03GetID() {
		String expected = "51c38f5d97c3e6d3d972f08a";
		String actual = "";
		try {
			actual = foodItem.getID();
			assertEquals(expected, actual);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes04GetNutrients() {
		try {
			assertNotNull(foodItem.getNutrients());
			assertEquals(0, foodItem.getNutrients().size());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes05AddNutrient() {
		// calories,100,fat,0,carbohydrate,0,fiber,0,protein,3
		String nutrient = "calories";
		double value = 100;
		try {
			foodItem.addNutrient(nutrient, value);
			assertNotNull(foodItem.getNutrients());
			assertEquals(1, foodItem.getNutrients().size());
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes06GetNutrientValue() {
		String nutrient = "calories";
		Double expected = (double) 100;
		Double actual = 0.0;
		try {
			foodItem.addNutrient(nutrient, expected);
			actual = foodItem.getNutrientValue(nutrient);
			assertEquals(expected, actual);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes07GetNutrientValueNoNutrient() {
		String nutrient = "fat";
		Double value = (double) 0;
		Double expected = 0.0;
		Double actual = 0.0;
		try {
			foodItem.addNutrient("calories", 100);
			actual = foodItem.getNutrientValue(nutrient);
			assertEquals(expected, actual);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
	@Test
	public void tes08UpdateNutrient() {
		String nutrient = "calories";
		Double original = (double) 100;
		Double updated = (double) 50;
		Double actual = 0.0;
		try {
			foodItem.addNutrient(nutrient, original);
			actual = foodItem.getNutrientValue(nutrient);
			assertEquals(original, actual);
			
			foodItem.addNutrient(nutrient, updated);
			actual = foodItem.getNutrientValue(nutrient);
			assertEquals(updated, actual);
		} catch (Exception e) {
			fail("Unexpectedly caught: " + e);
		}
	}
	
}
