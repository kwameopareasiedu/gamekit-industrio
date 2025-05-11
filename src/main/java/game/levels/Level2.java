package game.levels;

import game.factory.Factory;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.Belt;
import game.machines.Extractor;
import game.machines.Machine;
import game.machines.Mixer;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level2 extends FactoryController {
  public Level2() {
    super(
      1,
      new Machine.Info[]{ Extractor.INFO, Belt.INFO, Mixer.INFO },
      new Source[]{
        Source.create(Color.WHITE, 0, 0),
        Source.create(Color.BLACK, 0, 4),
      },
      new FactoryGoal(15, Shape.Type.CIRCLE, Color.GRAY),
      () -> {}
    );
  }

  @Override
  protected void setGridSize() {
    Factory.GRID_SIZE = 5;
  }
}
