package game;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.Widget;
import game.factory.Factory;
import game.factory.FactoryManager;
import game.factory.FactoryManagerState;

import java.awt.*;

public class Playground extends Scene implements FactoryManager {
  private final Factory factory;
  private final FactoryManagerState factoryManagerState;

  public Playground() {
    super("Playground");
    factory = new Factory();
    factoryManagerState = new FactoryManagerState();
  }

  @Override
  public Factory getFactory() { return factory; }

  @Override
  public FactoryManagerState getState() { return factoryManagerState; }

  @Override
  protected void start() { add(factory); }

  @Override
  protected void update() {
    updateState();
    applyState();
  }

  @Override
  protected void render() {
    Renderer.setBackground(Color.DARK_GRAY);
    Renderer.clear();
    renderState();
  }

  @Override
  public Widget onCreateUI() {
    return renderUI();
  }
}
