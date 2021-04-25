package hu.mudlee.actors.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import hu.mudlee.Constants;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;

public class DecibelMeter extends Table {
  private final Label label;
  private int decibel = Constants.START_DECIBEL;

  public DecibelMeter() {
    setFillParent(true);
    label = new Label("%d decibel".formatted(decibel), Styles.decibel(Font.DEFAULT, 48));
    add(label).expand().right().top().pad(20);

    MessageBus.register(Event.CITIZEN_HYPNOTIZED, () -> {
      decibel -= Constants.DECREASE_DECIBEL_PER_CITIZEN;
      this.label.setText("%d decibel".formatted(decibel));
      if(decibel == 0) {
        MessageBus.broadcast(Event.ZERO_DECIBEL_REACHED);
      }
    });
  }
}
