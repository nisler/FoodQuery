/**
 * Filename: Comparer.java
 * Project: p5 - JavaFX Team Project
 * Course: CS400 Fall 2018
 *
 * @author Gabriella Cottiero, gcottiero@wisc.edu
 * @author Olivia Gonzalez, odgonzalez2@wisc.edu
 * @author Timothy James, twjames2@wis.edu
 * @author Benjamin Nisler, nisler@wisc.edu
 * @author Tollan Renner, trenner@wisc.edu
 *
 * Due Date: Saturday, December 15, 11:59pm
 */

public enum Comparer {
  MORE ("More than or equal to (>=)", ">="),
  EQUALS ("Exactly (==)", "=="),
  LESS ("Less than or equal to (<=)", "<=");

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

