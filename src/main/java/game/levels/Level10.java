package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.*;

public class Level10 extends FactoryController {
  public Level10() {
    super(
      10,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        Reshaper.INFO,
        HueShifter.INFO,
        Splitter.INFO,
        Bridge.INFO,
      },
      new Source[]{
        Source.create(0, 0, PastelColor.WHITE),
        Source.create(0, 12, PastelColor.BLACK),
        Source.create(12, 12, PastelColor.RED),
      },
      new FactoryGoal(40, Shape.Type.SQUARE, PastelColor.BLACK),
      () -> Application.getInstance().loadScene(new Credits())
    );
  }

  @Override
  protected int getGridSize() {
    return 13;
  }
}
