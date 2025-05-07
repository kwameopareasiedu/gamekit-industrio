package game;

import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.Widget;
import game.factory.Factory;
import game.factory.FactoryManager;
import game.factory.FactoryManagerState;
import game.resources.Source;

import java.awt.*;

public class Playground extends Scene implements FactoryManager {
  private final FactoryManagerState factoryManagerState;
  private final Factory factory;

  public Playground() {
    super("Playground");

    Factory.GRID_SIZE = 11;

    factory = new Factory(
      new Source[]{
        Source.create(Color.WHITE, 0, 0),
        Source.create(Color.BLACK, 8, 9),
        Source.create(Color.BLACK, 9, 9),
        Source.create(Color.BLACK, 10, 9),
        Source.create(Color.RED, 0, 9),
      },
      (resource) -> {
        Factory.removeItem(resource);
        logger.debug("Consumed {}", resource.type);
      }
    );

    factoryManagerState = new FactoryManagerState(
      1, Color.WHITE, "15 white circles"
    );
  }

  @Override
  public Factory getFactory() {
    return factory;
  }

  @Override
  public FactoryManagerState getState() {
    return factoryManagerState;
  }

  @Override
  protected void start() {
    addChild(factory);
    startState();
  }

  @Override
  protected void update() {
    updateState();
    applyState();
  }

  @Override
  protected void render() {
    renderState();
  }

  @Override
  public Widget createUI() {
    return renderUI();
  }
}
