package game;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.Widget;
import game.factory.Factory;
import game.factory.FactoryManager;
import game.factory.FactoryManagerState;

import java.awt.*;

public class Playground extends Scene implements FactoryManager {
  private static final Color BG_COLOR = new Color(0xffc0c0c0, true);
  private final FactoryManagerState factoryManagerState;
  private final Factory factory;

  public Playground() {
    super("Playground");
    factory = new Factory();
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
    Renderer.setBackground(BG_COLOR);
    Renderer.clear();
    renderState();
  }

  @Override
  public Widget createUI() {
    return renderUI();
  }
}
