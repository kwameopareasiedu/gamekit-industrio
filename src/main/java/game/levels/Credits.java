package game.levels;

import dev.gamekit.core.Application;
import dev.gamekit.core.Camera;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.*;
import game.factory.Factory;
import game.machines.Belt;
import game.machines.Direction;
import game.machines.Extractor;
import game.resources.Source;
import game.ui.MenuButton;
import game.ui.StarGlowLetter;

import java.awt.*;

public final class Credits extends Scene {
  private static final Color CLEAR_COLOR = new Color(0x202039);
  private static final Color SCRIM_COLOR = new Color(0x99000000, true);

  private Factory factory;

  public Credits() {
    super("Menu Scene");

    this.factory = new Factory(
      11,
      new Source[]{
        Source.create(1, 0, Color.CYAN),
        Source.create(6, 7, Color.RED),
      },
      item -> factory.removeItem(item)
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
        Padding.create(
          Padding.options().padding(96),
          Column.create(
            Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(64),
            Row.create(
              Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
                .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(4),
              StarGlowLetter.from("Industrio", 192)
            ),
            Column.create(
              Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(24),
              Row.create(
                Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
                  .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(0),
                Text.create(
                  Text.options().fontSize(36).alignment(Alignment.CENTER),
                  "Industrio is an open source game and developed by "
                ),
                Text.create(
                  Text.options().color(Color.CYAN).fontSize(36).alignment(Alignment.CENTER),
                  "Kwame Opare Asiedu"
                ),
                Text.create(
                  Text.options().fontSize(36).alignment(Alignment.CENTER),
                  "."
                )
              ),
              Row.create(
                Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
                  .crossAxisAlignment(CrossAxisAlignment.CENTER),
                Text.create(
                  Text.options().fontSize(36).alignment(Alignment.CENTER),
                  "This game is built entirely using the free and open source"
                ),
                Text.create(
                  Text.options().color(Color.CYAN).fontSize(36).alignment(Alignment.CENTER),
                  "GameKit"
                ),
                Text.create(
                  Text.options().fontSize(36).alignment(Alignment.CENTER),
                  "2D game engine."
                )
              ),
              Row.create(
                Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
                  .crossAxisAlignment(CrossAxisAlignment.CENTER),
                Text.create(
                  Text.options().fontSize(36).alignment(Alignment.CENTER),
                  "Special thanks to the"
                ),
                Text.create(
                  Text.options().color(Color.CYAN).fontSize(36).alignment(Alignment.CENTER),
                  "Valadria Discord"
                ),
                Text.create(
                  Text.options().fontSize(36).alignment(Alignment.CENTER),
                  "for the support."
                )
              )
            ),
            MenuButton.create("Back to Menu", this::backToMainMenu)
          )
        )
      )
    );
  }

  private void setupFactory() {
    Application.getInstance().scheduleTask(() -> {
      factory.createMachine(1, 0, Extractor.INFO, Direction.UP);
      factory.createMachine(6, 7, Extractor.INFO, Direction.LEFT);

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

        factory.createMachine(row, col, Belt.INFO, dir);
      }
    });
  }

  private void backToMainMenu() {
    Application.getInstance().loadScene(new Menu());
  }
}
