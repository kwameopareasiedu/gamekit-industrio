package game;

import dev.gamekit.core.Application;
import dev.gamekit.utils.Config;
import dev.gamekit.utils.Resolution;

public class Industrio extends Application {
  public Industrio() {
    super(new Config("Industrio", Resolution.NATIVE, true));
  }

  public static void main(String[] args) {
    final Industrio game = new Industrio();
    game.loadScene(new Playground());
    game.run();
  }
}
