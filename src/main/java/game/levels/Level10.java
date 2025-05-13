package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Level10 extends FactoryController {
  public Level10() {
    super(
      10,
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
      new FactoryGoal(40, Shape.Type.SQUARE, Color.BLACK),
      () -> Application.getInstance().loadScene(new Level10())
    );
  }

  @Override
  protected int getGridSize() {
    return 11;
  }
}
