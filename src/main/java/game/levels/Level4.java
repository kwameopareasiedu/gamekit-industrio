package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level4 extends FactoryController {
  public Level4() {
    super(
      4,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        Reshaper.INFO,
        HueShifter.INFO
      },
      new Source[]{
        Source.create(0, 0, Color.WHITE),
        Source.create(0, 5, Color.BLACK),
        Source.create(0, 6, Color.BLACK),
        Source.create(6, 5, Color.RED),
      },
      new FactoryGoal(30, Shape.Type.SQUARE, Color.GREEN),
      () -> Application.getInstance().loadScene(new Level5())
    );
  }

  @Override
  protected int getGridSize() {
    return 7;
  }
}
