package game.levels;

import dev.gamekit.core.Application;
import dev.gamekit.core.Camera;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.*;
import game.Playground;
import game.Utils;
import game.factory.Factory;
import game.machines.Belt;
import game.machines.Direction;
import game.machines.Extractor;
import game.resources.Source;
import game.ui.MenuButton;
import game.ui.StarGlowLetter;

import java.awt.*;

public final class Menu extends Scene {
  private static final Color CLEAR_COLOR = new Color(0x202039);
  private static final Color SCRIM_COLOR = new Color(0x99000000, true);

  static {
    Factory.GRID_SIZE = 11;
  }

  private final Factory factory;

  public Menu() {
    super("Menu Scene");

    this.factory = new Factory(
      new Source[]{
        Source.create(Color.CYAN, 1, 0),
        Source.create(Color.RED, 6, 7),
      },
      Factory::removeItem
    );
  }

  @Override
  protected void start() {
    addChild(factory);
    setupFactory();

    Camera.lookAt(0, -384);
  }

  @Override
  protected void render() {
    Renderer.setBackground(CLEAR_COLOR);
    Renderer.clear();
  }

  @Override
  public Widget createUI() {
    return Stack.create(
      Fractional.create(
        Fractional.options(),
        Colored.create(
          Colored.options().color(SCRIM_COLOR),
          Empty.create()
        )
      ),

      Align.create(
        Align.options().verticalAlignment(Alignment.CENTER).horizontalAlignment(Alignment.CENTER),
        Column.create(
          Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(20),
          Row.create(
            Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
              .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(4),
            StarGlowLetter.from("Industrio", 192)
          ),
          MenuButton.create("Campaign", this::startCampaignMode),
          MenuButton.create("Sandbox (Coming Soon)", () -> { }, Font.ITALIC),
          MenuButton.create("Quit To Desktop", this::exitApplication)
        )
      ),

      Align.create(
        Align.options().verticalAlignment(Alignment.END).horizontalAlignment(Alignment.CENTER),
        Padding.create(
          Padding.options().padding(24),
          Column.create(
            Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(8),
            Text.create(
              Text.options().fontStyle(Font.BOLD),
              "Created by Kwame Opare Asiedu"
            ),
            Text.create("Powered by GameKit engine")
          )
        )
      )
    );
  }

  private void setupFactory() {
    Application.getInstance().scheduleTask(() -> {
      factory.createMachine(
        Utils.rowColToIndex(1, 0),
        Extractor.INFO,
        Direction.UP
      );

      factory.createMachine(
        Utils.rowColToIndex(6, 7),
        Extractor.INFO,
        Direction.LEFT
      );

      for (int i = 2; i <= 4; i++) {
        factory.createMachine(
          Utils.rowColToIndex(i, 0),
          Belt.INFO,
          Direction.UP
        );
      }

      for (int i = 0; i <= 4; i++) {
        factory.createMachine(
          Utils.rowColToIndex(5, i),
          Belt.INFO,
          Direction.RIGHT
        );
      }

      factory.createMachine(
        Utils.rowColToIndex(6, 6),
        Belt.INFO,
        Direction.LEFT
      );

      factory.createMachine(
        Utils.rowColToIndex(6, 5),
        Belt.INFO,
        Direction.DOWN
      );
    });
  }

  private void startCampaignMode() {
    Application.getInstance().loadScene(new Playground());
  }

  private void exitApplication() {
    Application.getInstance().quit();
  }
}
