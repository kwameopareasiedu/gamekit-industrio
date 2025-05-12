package game;

import game.factory.FactoryController;
import game.factory.FactoryGoal;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Playground extends FactoryController {
  public Playground() {
    super(
      1,
      new Machine.Info[]{ Extractor.INFO, Belt.INFO, Mixer.INFO, Reshaper.INFO, HueShifter.INFO },
      new Source[]{ Source.create(1, 0, Color.WHITE), },
      new FactoryGoal(10, Shape.Type.CIRCLE, Color.WHITE),
      () -> { }
    );
  }

  @Override
  protected int getGridSize() {
    return 11;
  }
}
