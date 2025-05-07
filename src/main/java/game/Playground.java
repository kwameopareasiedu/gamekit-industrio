package game;

import game.factory.FactoryGoal;
import game.factory.FactoryScene;
import game.machines.Belt;
import game.machines.Extractor;
import game.machines.Machine;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;

public class Playground extends FactoryScene {
  public Playground() {
    super(
      1,
      5,
      new Machine.Info[]{
        Extractor.INFO,
        Belt.INFO,
      },
      new Source[]{
        Source.create(Color.WHITE, 0, 0),
//        Source.create(Color.BLACK, 8, 9),
//        Source.create(Color.BLACK, 9, 9),
//        Source.create(Color.BLACK, 10, 9),
//        Source.create(Color.RED, 0, 9),
      },
      new FactoryGoal(10, Shape.Type.CIRCLE, Color.WHITE)
    );
  }
}
