package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.*;

public class Level5 extends FactoryController {
  public Level5() {
    super(
      5,
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
        Source.create(0, 7, PastelColor.BLACK),
        Source.create(8, 2, PastelColor.RED),
      },
      new FactoryGoal(30, Shape.Type.SQUARE, PastelColor.BLUE),
      () -> Application.getInstance().loadScene(new Level6())
    );
  }

  @Override
  protected int getGridSize() {
    return 9;
  }
}
