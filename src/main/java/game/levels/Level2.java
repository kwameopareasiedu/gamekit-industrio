package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.Belt;
import game.machines.Extractor;
import game.machines.Machine;
import game.machines.Mixer;

public class Level2 extends FactoryController {
  public Level2() {
    super(
      2,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO
      },
      new Source[]{
        Source.create(0, 0, PastelColor.WHITE),
        Source.create(0, 4, PastelColor.BLACK),
      },
      new FactoryGoal(15, Shape.Type.CIRCLE, PastelColor.GRAY),
      () -> Application.getInstance().loadScene(new Level3())
    );
  }

  @Override
  protected int getGridSize() {
    return 5;
  }
}
