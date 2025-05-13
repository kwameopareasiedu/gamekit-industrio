package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.machines.Belt;

public class BeltHelpPanel extends MachineHelpPanel {
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
