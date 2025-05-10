package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.widgets.*;

import java.util.Arrays;

import static dev.gamekit.utils.Math.toInt;

public class StarGlowLetter extends Compose {
  private final char ch;
  private final int size;

  protected StarGlowLetter(char ch, int size) {
    super(
      Sized.create(
        Sized.options().width(toInt((ch == 'i' || ch == 'I' ? 0.25 : 0.5) * size)).height(size),
        ch == ' ' ? Sized.create(
          Sized.options().width(size).height(0),
          Empty.create()
        ) : Image.create(
          IO.getResourceImage(
            String.format("starglow/CK_StarGlowing_%s.png", String.valueOf(ch).toUpperCase())
          )
        )
      )
    );

    this.ch = ch;
    this.size = size;
  }

  public static StarGlowLetter[] from(String text) {
    return from(text, 48);
  }

  public static StarGlowLetter[] from(String text, int size) {
    return Arrays
      .stream(text.split(""))
      .map(str -> new StarGlowLetter(str.charAt(0), size))
      .toArray(StarGlowLetter[]::new);
  }

  @Override
  public boolean stateEquals(Widget widget) {
    if (widget instanceof StarGlowLetter glowLetterWidget)
      return ch == glowLetterWidget.ch &&
        size == glowLetterWidget.size;

    return false;
  }
}
