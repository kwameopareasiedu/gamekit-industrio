package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.machines.Bridge;

public class BridgeHelpPanel extends MachineHelpPanel {
  public BridgeHelpPanel() {
    super(
      Bridge.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Bridges allow items to cross from left-to-right and top-to-bottom without merging."
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof BridgeHelpPanel;
  }
}
