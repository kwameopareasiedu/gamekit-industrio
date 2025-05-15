package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.*;

public class Level3 extends FactoryController {
  public Level3() {
    super(
      3,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        HueShifter.INFO
      },
      new Source[]{
        Source.create(0, 0, PastelColor.WHITE),
        Source.create(0, 6, PastelColor.BLACK),
        Source.create(6, 5, PastelColor.RED),
      },
      new FactoryGoal(15, Shape.Type.CIRCLE, PastelColor.GREEN),
      () -> Application.getInstance().loadScene(new Level4())
    );
  }

  @Override
  protected int getGridSize() {
    return 7;
  }
}
