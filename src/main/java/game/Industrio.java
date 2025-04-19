package game;

import dev.gamekit.core.Application;
import dev.gamekit.utils.Config;
import dev.gamekit.utils.Resolution;

public class Industrio extends Application {
  public Industrio() {
    super(new Config("Industrio", Resolution.HD, false));
  }

  public static void main(String[] args) {
    final Industrio game = new Industrio();
    game.run();
  }
}
