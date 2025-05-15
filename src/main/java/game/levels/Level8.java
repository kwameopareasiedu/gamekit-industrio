package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.*;

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
        Source.create(0, 0, PastelColor.WHITE),
        Source.create(0, 10, PastelColor.BLACK),
        Source.create(10, 10, PastelColor.RED),
      },
      new FactoryGoal(30, Shape.Type.SQUARE, PastelColor.MAGENTA),
      () -> Application.getInstance().loadScene(new Level9())
    );
  }

  @Override
  protected int getGridSize() {
    return 11;
  }
}
