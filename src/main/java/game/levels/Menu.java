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
      Sized.create(
        Sized.options().fractionalWidth(1).fractionalHeight(1),
        Colored.create(
          Colored.options().color(SCRIM_COLOR)
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
            Text.create("Powered by GameKit")
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

      Object[][] belts = new Object[][]{
        new Object[]{ 2, 0, Direction.UP },
        new Object[]{ 3, 0, Direction.UP },
        new Object[]{ 4, 0, Direction.UP },
        new Object[]{ 5, 0, Direction.RIGHT },
        new Object[]{ 5, 1, Direction.RIGHT },
        new Object[]{ 5, 2, Direction.RIGHT },
        new Object[]{ 5, 3, Direction.RIGHT },
        new Object[]{ 5, 4, Direction.RIGHT },

        new Object[]{ 6, 6, Direction.LEFT },
        new Object[]{ 6, 5, Direction.DOWN },
      };

      for (Object[] beltConfig : belts) {
        int row = (int) beltConfig[0];
        int col = (int) beltConfig[1];
        Direction dir = (Direction) beltConfig[2];

        factory.createMachine(
          Utils.rowColToIndex(row, col),
          Belt.INFO,
          dir
        );
      }
    });
  }

  private void startCampaignMode() {
    Application.getInstance().loadScene(new Playground());
  }

  private void exitApplication() {
    Application.getInstance().quit();
  }
}
