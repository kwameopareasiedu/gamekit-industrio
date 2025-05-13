package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level6 extends FactoryController {
  public Level6() {
    super(
      6,
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
      new FactoryGoal(30, Shape.Type.SQUARE, Color.YELLOW),
      () -> Application.getInstance().loadScene(new Level7())
    );
  }

  @Override
  protected int getGridSize() {
    return 9;
  }
}
