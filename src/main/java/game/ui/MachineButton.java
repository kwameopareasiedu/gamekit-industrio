package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import game.machines.Machine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MachineButton extends Compose {
  private static final BufferedImage DEFAULT_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1204, 792, 152, 80);
  private static final BufferedImage HOVER_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1204, 920, 152, 80);

  public MachineButton(Machine.Info machineInfo, MouseEvent.Listener mouseListener) {
    super(
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(6),
        Button.create(
          Button.options().mouseListener(mouseListener).defaultBackground(DEFAULT_BG)
            .hoverBackground(HOVER_BG).pressedBackground(HOVER_BG).padding(16),
          Sized.create(
            Sized.options().width(48).height(48),
            Image.create(machineInfo.image())
          )
        ),
        Text.create(
          Text.options()
            .alignment(Alignment.CENTER).color(Color.WHITE).fontStyle(Font.BOLD).fontSize(12),
          machineInfo.name()
        )
      )
    );
  }

  public static MachineButton create(
    Machine.Info machineInfo,
    MouseEvent.Listener mouseListener
  ) {
    return new MachineButton(machineInfo, mouseListener);
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof MachineButton;
  }
}
