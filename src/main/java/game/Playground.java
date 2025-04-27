package game;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.widgets.Widget;
import game.world.World;
import game.world.WorldManager;
import game.world.WorldManagerState;

import java.awt.*;

public class Playground extends Scene implements WorldManager {
  private static final Color BG_COLOR = new Color(0xffc0c0c0, true);
  private final WorldManagerState worldManagerState;
  private final World world;

  public Playground() {
    super("Playground");
    world = new World();
    worldManagerState = new WorldManagerState();
  }

  @Override
  public World getWorld() {
    return world;
  }

  @Override
  public WorldManagerState getState() {
    return worldManagerState;
  }

  @Override
  protected void start() {
    addChild(world);
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
