package game;

import dev.gamekit.core.Application;
import dev.gamekit.settings.*;
import game.levels.Menu;

public class Industrio extends Application {
  public Industrio() {
    super(
      new Settings(
        "Industrio",
        Resolution.NATIVE,
        WindowMode.FULLSCREEN,
        Antialiasing.ON,
        ImageInterpolation.BICUBIC,
        AlphaInterpolation.SPEED,
        RenderingStrategy.SPEED,
        Dithering.OFF
      )
    );
  }

  public static void main(String[] args) {
    final Industrio game = new Industrio();
    game.loadScene(new Playground());
    game.run();
  }
}
