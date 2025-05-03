package game;

import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.Widget;
import game.factory.Factory;
import game.factory.FactoryManager;
import game.factory.FactoryManagerState;
import game.resources.Shade;
import game.resources.Source;

import java.awt.*;

public class Playground extends Scene implements FactoryManager {
  private static final Color CLEAR_COLOR = new Color(0xe1e1e1);
  private final FactoryManagerState factoryManagerState;
  private final Factory factory;

  public Playground() {
    super("Playground");

    Factory.GRID_SIZE = 7;

    factory = new Factory(
      new Source[] {
        Source.create(Shade.Type.WHITE_CIRCLE, 0, 0),
        Source.create(Shade.Type.BLACK_CIRCLE, 6, 6)
      }
    );

    factoryManagerState = new FactoryManagerState();
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
  public Color getClearColor() {
    return CLEAR_COLOR;
  }

  @Override
  protected void start() {
    addChild(factory);
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
