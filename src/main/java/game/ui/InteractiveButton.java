package game.ui;

import dev.gamekit.audio.AudioClip2D;
import dev.gamekit.audio.AudioGroup;
import dev.gamekit.core.Audio;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;

import static game.ui.MachineButton.DEFAULT_BG;
import static game.ui.MachineButton.HOVER_BG;

public abstract class InteractiveButton extends Compose {
  static {
    Audio.preload("hover", new AudioClip2D("audio/btn-hover.wav", AudioGroup.EFFECTS, 1));
    Audio.preload("click", new AudioClip2D("audio/btn-click.wav", AudioGroup.EFFECTS, 1));
  }

  public InteractiveButton(MouseEvent.Listener listener, Widget child) {
    this(listener, 36, child);
  }

  public InteractiveButton(MouseEvent.Listener listener, int padding, Widget child) {
    super(Empty.create());

    updateChild(
      Button.create(
        Button.options().defaultBackground(DEFAULT_BG).hoverBackground(HOVER_BG)
          .pressedBackground(HOVER_BG).ninePatch(36).mouseListener(ev -> {
            switch (ev.type) {
              case ENTER -> Audio.get("hover").play();
              case EXIT -> Audio.get("hover").stop();
              case CLICK -> Audio.get("click").play();
            }

            listener.handleEvent(ev);
          }),
        Padding.create(
          Padding.options().padding(padding),
          child
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof InteractiveButton;
  }
}
