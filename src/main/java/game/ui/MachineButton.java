package game.ui;

import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import game.machines.Machine;

import java.awt.*;

public class MachineButton extends Compose {
  public MachineButton(Machine.Info machineInfo, MouseEvent.Listener mouseListener) {
    super(
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(6),
        Sized.create(
          Sized.options().width(64).height(64),
          Button.create(
            Button.options()
              .mouseListener(mouseListener)
              .padding(new Spacing(0)),
            Image.create(
              machineInfo.image()
            )
          )
        ),
        Text.create(
          Text.options()
            .alignment(Alignment.CENTER)
            .color(Color.DARK_GRAY)
            .fontStyle(Font.BOLD)
            .fontSize(12),
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
