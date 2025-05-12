package game.levels;

import dev.gamekit.core.Application;
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
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO
      },
      new Source[]{
        Source.create(0, 0, Color.WHITE),
        Source.create(0, 4, Color.BLACK),
      },
      new FactoryGoal(15, Shape.Type.CIRCLE, Color.GRAY),
      () -> Application.getInstance().loadScene(new Level3())
    );
  }

  @Override
  protected int getGridSize() {
    return 5;
  }
}
