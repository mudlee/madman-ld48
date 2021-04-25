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
    label = new Label("%d decibel".formatted(decibel), Styles.bigLabel(Font.DEFAULT, 48));
    add(label).expand().right().top().pad(20);

    MessageBus.register(Event.CITIZEN_HYPNOTIZED, () -> {
      decibel -= Constants.DECREASE_DECIBEL_PER_CITIZEN;
      this.label.setText("%d decibel".formatted(decibel));
      if(decibel <= Constants.DECIBEL_GOAL) {
        MessageBus.broadcast(Event.DECIBEL_GOAL_REACHED);
      }
    });
  }
}
