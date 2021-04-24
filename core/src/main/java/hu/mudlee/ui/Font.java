package hu.mudlee.ui;

public enum Font {
  DEFAULT("fonts/Anita_semi_square.ttf");

  private final String font;

  Font(String font) {
    this.font = font;
  }

  public String getFont() {
    return font;
  }
}
