package game;

import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.items.PastelColor;
import game.items.Shape;
import game.items.Source;
import game.machines.*;

public class Playground extends FactoryController {
  public Playground() {
    super(
      1,
      new Machine.Info[]{ Extractor.INFO, Belt.INFO, Mixer.INFO, Reshaper.INFO, HueShifter.INFO },
      new Source[]{ Source.create(1, 0, PastelColor.WHITE), },
      new FactoryGoal(10, Shape.Type.CIRCLE, PastelColor.WHITE),
      () -> { }
    );
  }

  @Override
  protected int getGridSize() {
    return 11;
  }
}
