package game;

import dev.gamekit.core.Application;
import dev.gamekit.settings.*;

public class Industrio extends Application {
  public Industrio() {
    super(
      new Settings(
        "Industrio", Resolution.HD, false,
        Antialiasing.ON, AlphaInterpolation.QUALITY, ImageInterpolation.BICUBIC,
        RenderingStrategy.QUALITY, Dithering.ON
      )
    );
  }

  public static void main(String[] args) {
    final Industrio game = new Industrio();
    game.loadScene(new Playground());
    game.run();
  }
}
