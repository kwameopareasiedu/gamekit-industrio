package game;

import game.factory.Factory;
import game.factory.FactoryGoal;
import game.factory.FactoryScene;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Playground extends FactoryScene {
  static {
    Factory.GRID_SIZE = 11;
  }

  public Playground() {
    super(
      1,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
        Mixer.INFO,
        Reshaper.INFO,
        HueShifter.INFO,
      },
      new Source[]{
        Source.create(Color.WHITE, 1, 0),
      },
      new FactoryGoal(10, Shape.Type.CIRCLE, Color.WHITE)
    );
  }
}
