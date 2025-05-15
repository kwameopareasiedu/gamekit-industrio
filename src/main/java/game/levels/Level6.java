package game.levels;

import dev.gamekit.core.Application;
import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.*;

public class Level6 extends FactoryController {
  public Level6() {
    super(
      6,
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
        Source.create(0, 8, PastelColor.BLACK),
        Source.create(8, 2, PastelColor.RED),
      },
      new FactoryGoal(30, Shape.Type.SQUARE, PastelColor.YELLOW),
      () -> Application.getInstance().loadScene(new Level7())
    );
  }

  @Override
  protected int getGridSize() {
    return 9;
  }
}
