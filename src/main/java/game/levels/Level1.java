package game.levels;

import dev.gamekit.core.Application;
import game.factory.Factory;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level1 extends FactoryController {
  public Level1() {
    super(
      1,
      new Machine.Info[]{ Extractor.INFO, Belt.INFO, },
      new Source[]{ Source.create(Color.WHITE, 0, 0), },
      new FactoryGoal(10, Shape.Type.CIRCLE, Color.WHITE),
      () -> Application.getInstance().loadScene(new Level2())
    );
  }

  @Override
  protected void setGridSize() {
    Factory.GRID_SIZE = 5;
  }
}
