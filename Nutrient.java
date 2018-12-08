
public enum Nutrient {
  CALORIES ("calories", "calories"),
  FAT ("grams of fat", "fat"),
  CARBOHYDRATE ("grams of carbohydrates", "carbohydrate"),
  PROTEIN ("grams of protein", "protein"),
  FIBER("grams of fiber", "fiber");

  private final String text;
  private final String rule;
  Nutrient(String text, String rule){
    this.text = text;
    this.rule = rule;
  }

  public String getText() {
    return this.text;
  }

  public String getRule() {
    return this.rule;
  }
}