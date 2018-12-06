
public enum Comparer {
  MORE ("More than or equal to", ">="),
  EQUALS ("Exactly", "=="),
  LESS ("Less than or equal to", "<=");

  private final String text;
  private final String rule;
  Comparer(String text, String rule){
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

