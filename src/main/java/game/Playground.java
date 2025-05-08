package game;

import game.factory.Factory;
import game.factory.FactoryGoal;
import game.factory.FactoryScene;
import game.machines.Belt;
import game.machines.Extractor;
import game.machines.Machine;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Playground extends FactoryScene {
  static {
    Factory.GRID_SIZE = 5;
  }

  public Playground() {
    super(
      1,
      5,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
      },
      new Source[]{
        Source.create(Color.WHITE, 2, 0),
      },
      new FactoryGoal(0, Shape.Type.CIRCLE, Color.WHITE)
    );
  }
}
