package game.ui;

import dev.gamekit.audio.AudioClip2D;
import dev.gamekit.audio.AudioGroup;
import dev.gamekit.core.Audio;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.*;

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
        Padding.create(
          Padding.options().padding(36),
          Text.create(
            Text.options().color(Color.WHITE).fontSize(32).fontStyle(fontStyle),
            text
          )
        )
      )
    );

    try {
      Audio.preload(text + "hover", new AudioClip2D("audio/btn-hover.wav", AudioGroup.EFFECTS, 1));
    } catch (Exception ignored) { }

    try {
      Audio.preload(text + "click", new AudioClip2D("audio/btn-click.wav", AudioGroup.EFFECTS, 1));
    } catch (Exception ignored) { }

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
