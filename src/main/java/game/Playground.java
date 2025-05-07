package game;

import game.factory.Factory;
import game.factory.FactoryScene;
import game.factory.FactoryState;
import game.resources.Source;

import java.awt.*;

public class Playground extends FactoryScene {
  public Playground() {
    super(
      11,
      new Factory(
        new Source[]{
          Source.create(Color.WHITE, 0, 0),
          Source.create(Color.BLACK, 8, 9),
          Source.create(Color.BLACK, 9, 9),
          Source.create(Color.BLACK, 10, 9),
          Source.create(Color.RED, 0, 9),
        },
        (resource) -> {
          Factory.removeItem(resource);
//          logger.debug("Consumed {}", resource.type);
        }
      ),
      new FactoryState(
        1, Color.WHITE, "15 white circles"
      )
    );
  }
}
