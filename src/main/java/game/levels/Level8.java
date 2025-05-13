package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level8 extends FactoryController {
  public Level8() {
    super(
      8,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        Reshaper.INFO,
        HueShifter.INFO,
        Splitter.INFO
      },
      new Source[]{
        Source.create(0, 0, Color.WHITE),
        Source.create(0, 10, Color.BLACK),
        Source.create(10, 10, Color.RED),
      },
      new FactoryGoal(30, Shape.Type.SQUARE, Color.MAGENTA),
      () -> Application.getInstance().loadScene(new Level9())
    );
  }

  @Override
  protected int getGridSize() {
    return 11;
  }
}
