package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.machines.Belt;

import java.awt.image.BufferedImage;

public class BeltHelpPanel extends MachineHelpPanel {
  private static final BufferedImage[] SPRITES = new BufferedImage[]{
    IO.getResourceImage("belts.png", 0, 0, 192, 192),
    IO.getResourceImage("belts.png", 384, 0, 192, 192),
    IO.getResourceImage("belts.png", 192, 192, 192, 192),
    IO.getResourceImage("belts.png", 192, 0, 192, 192),
    IO.getResourceImage("belts.png", 384, 192, 192, 192),
    IO.getResourceImage("belts.png", 0, 192, 192, 192),
    IO.getResourceImage("belts.png", 0, 384, 192, 192),
  };

  public BeltHelpPanel() {
    super(
      Belt.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(12),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Use belts to connect your machines and move items between them."
        ),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "You can click and drag to create multiple belts."
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof BeltHelpPanel;
  }
}
