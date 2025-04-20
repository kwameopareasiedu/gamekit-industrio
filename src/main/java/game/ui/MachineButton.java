package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import game.machines.Machine;

import java.awt.*;

public class MachineButton extends Compose {
  public MachineButton(Machine.Info machineInfo, MouseEvent.Listener mouseListener) {
    super(
      Column.create(
        ColumnParam.crossAxisAlignment(CrossAxisAlignment.CENTER),
        ColumnParam.gapSize(12),
        ColumnParam.children(
          Sized.create(
            SizedParam.width(64),
            SizedParam.height(64),
            SizedParam.child(
              Button.create(
                ButtonParam.defaultBackground(null),
                ButtonParam.mouseListener(mouseListener),
                ButtonParam.child(
                  Image.create(
                    ImageParam.image(machineInfo.icon())
                  )
                )
              )
            )
          ),
          Text.create(
            TextParam.text(machineInfo.name()),
            TextParam.alignment(Alignment.CENTER),
            TextParam.color(Color.DARK_GRAY),
            TextParam.fontStyle(Font.BOLD),
            TextParam.fontSize(12)
          )
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
