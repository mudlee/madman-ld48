package hu.mudlee.actors.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;

public class CompletionTime extends Table {
  public CompletionTime(boolean won, int timeSecs) {
    setFillParent(true);
    Label label;
    if(won) {
      label = new Label("Congratulations! Completed In: %ds".formatted(timeSecs), Styles.bigLabel(Font.DEFAULT, 48));
    }
    else {
      label = new Label("I'm really sorry. But GAME OVER!", Styles.bigLabel(Font.DEFAULT, 48));
    }
    add(label).expand().center().top().pad(20);
  }
}
