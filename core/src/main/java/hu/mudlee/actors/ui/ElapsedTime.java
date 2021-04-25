package hu.mudlee.actors.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;

public class ElapsedTime extends Table {
  private final Label label;
  public static int timeSecs = 0;

  public ElapsedTime() {
    setFillParent(true);
    label = new Label("Elapsed Time: %ds".formatted(timeSecs), Styles.bigLabel(Font.DEFAULT, 48));
    add(label).expand().center().top().pad(20);
  }

  public void increase() {
    label.setText("Elapsed Time: %ds".formatted(++timeSecs));
  }
}
