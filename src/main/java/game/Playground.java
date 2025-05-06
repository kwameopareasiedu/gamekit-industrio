package game;

import dev.gamekit.core.IO;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.*;
import game.factory.Factory;
import game.factory.FactoryManager;
import game.factory.FactoryManagerState;
import game.resources.Source;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Playground extends Scene implements FactoryManager {
  private static final BufferedImage PANEL_BG = IO.getResourceImage("panel.png");
  private static final Color CLEAR_COLOR = Color.WHITE;
  private final FactoryManagerState factoryManagerState;
  private final Factory factory;

  public Playground() {
    super("Playground");

    Factory.GRID_SIZE = 11;

    factory = new Factory(
      new Source[]{
        Source.create(Color.WHITE, 0, 0),
        Source.create(Color.BLACK, 8, 9),
        Source.create(Color.RED, 0, 9),
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
    return Stack.create(
      Align.create(
        Align.options().horizontalAlignment(Alignment.START),
        Padding.create(
          Padding.options().padding(new Spacing(128, 0, 0, 48)),
          Sized.create(
            Sized.options().width(256).height(128),
            Panel.create(
              Panel.options().background(PANEL_BG).padding(new Spacing(68, 28, 28, 64)),
              Text.create("Hello World")
            )
          )
        )
      ),
      renderUI()
    );
  }
}
