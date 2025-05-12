package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import game.machines.Machine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MachineButton extends InteractiveButton {
  public static final BufferedImage DEFAULT_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1204, 792, 152, 80);
  public static final BufferedImage HOVER_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1204, 920, 152, 80);

  public static MachineButton create(Machine.Info machineInfo, MouseEvent.Listener mouseListener) {
    return new MachineButton(machineInfo, mouseListener);
  }

  public MachineButton(Machine.Info machineInfo, MouseEvent.Listener mouseListener) {
    super(
      mouseListener, 16,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(6),
        Sized.create(
          Sized.options().width(56).height(56),
          Image.create(machineInfo.image())
        ),
        Text.create(
          Text.options().alignment(Alignment.CENTER).color(Color.WHITE).fontSize(20),
          machineInfo.name()
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof MachineButton;
  }
}
