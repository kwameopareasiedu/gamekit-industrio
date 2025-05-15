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

public class Level1 extends FactoryController {
  public Level1() {
    super(
      1,
      new Machine.Info[]{ Extractor.INFO, Belt.INFO, },
      new Source[]{ Source.create(0, 0, PastelColor.WHITE), },
      new FactoryGoal(10, Shape.Type.CIRCLE, PastelColor.WHITE),
      () -> Application.getInstance().loadScene(new Level2())
    );
  }

  @Override
  protected int getGridSize() {
    return 5;
  }
}
