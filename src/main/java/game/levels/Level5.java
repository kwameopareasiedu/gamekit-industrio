package game.levels;

import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level5 extends FactoryController {
  public Level5() {
    super(
      5,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        Reshaper.INFO,
        HueShifter.INFO
      },
      new Source[]{
        Source.create(0, 0, Color.BLACK),
        Source.create(0, 1, Color.WHITE),
        Source.create(0, 6, Color.BLACK),
        Source.create(0, 7, Color.BLACK),
        Source.create(8, 2, Color.RED),
      },
      new FactoryGoal(30, Shape.Type.SQUARE, Color.BLUE),
      () -> { }
    );
  }

  @Override
  protected int getGridSize() {
    return 9;
  }
}
