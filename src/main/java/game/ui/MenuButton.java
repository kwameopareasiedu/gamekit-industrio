package game.ui;

import dev.gamekit.audio.AudioClip2D;
import dev.gamekit.audio.AudioGroup;
import dev.gamekit.core.Audio;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Compose;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;

import java.awt.*;
import java.util.Objects;

import static game.ui.MachineButton.DEFAULT_BG;
import static game.ui.MachineButton.HOVER_BG;

public class MenuButton extends Compose {
  private final String text;

  public static MenuButton create(String text, ClickListener listener) {
    return new MenuButton(text, listener, Font.BOLD);
  }

  public static MenuButton create(String text, ClickListener listener, int fontStyle) {
    return new MenuButton(text, listener, fontStyle);
  }

  public MenuButton(String text, ClickListener listener, int fontStyle) {
    super(
      Button.create(
        Button.options().defaultBackground(DEFAULT_BG).hoverBackground(HOVER_BG)
          .pressedBackground(HOVER_BG).ninePatch(36).mouseListener(ev -> {
            switch (ev.type) {
              case ENTER -> Audio.<AudioClip2D>get(text + "hover").play();
              case EXIT -> Audio.<AudioClip2D>get(text + "hover").stop();
              case CLICK -> {
                Audio.<AudioClip2D>get(text + "click").play();
                listener.click();
              }
              default -> { }
            }
          }),
        Text.create(
          Text.options().color(Color.WHITE).fontSize(24).fontStyle(fontStyle),
          text
        )
      )
    );

    Audio.preload(text + "hover", new AudioClip2D("audio/btn-hover.wav", AudioGroup.EFFECTS, 1));
    Audio.preload(text + "click", new AudioClip2D("audio/btn-click.wav", AudioGroup.EFFECTS, 1));

    this.text = text;
  }

  @Override
  public boolean stateEquals(Widget widget) {
    if (widget instanceof MenuButton menuButtonWidget)
      return Objects.equals(text, menuButtonWidget.text);

    return false;
  }

  public interface ClickListener {
    void click();
  }
}
