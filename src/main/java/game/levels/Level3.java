package game.levels;

import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level3 extends FactoryController {
  public Level3() {
    super(
      1,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        HueShifter.INFO
      },
      new Source[]{
        Source.create(0, 0, Color.WHITE),
        Source.create(0, 6, Color.BLACK),
        Source.create(6, 5, Color.RED),
      },
      new FactoryGoal(15, Shape.Type.CIRCLE, Color.GREEN),
      () -> { }
    );
  }

  @Override
  protected int getGridSize() {
    return 7;
  }
}
