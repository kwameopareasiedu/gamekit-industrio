package game.ui;

import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;

import java.awt.*;
import java.util.Objects;

public class MenuButton extends InteractiveButton {
  private final String text;

  public static MenuButton create(String text, ClickListener listener) {
    return new MenuButton(text, listener, Font.BOLD);
  }

  public static MenuButton create(String text, ClickListener listener, int fontStyle) {
    return new MenuButton(text, listener, fontStyle);
  }

  public MenuButton(String text, ClickListener listener, int fontStyle) {
    super(
      ev -> {
        if (ev.type == MouseEvent.Type.CLICK)
          listener.click();
      },
      Text.create(
        Text.options().color(Color.WHITE).fontSize(32).fontStyle(fontStyle),
        text
      )
    );

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
