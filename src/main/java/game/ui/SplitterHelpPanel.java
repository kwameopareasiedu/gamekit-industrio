package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.machines.Splitter;

public class SplitterHelpPanel extends MachineHelpPanel {
  public SplitterHelpPanel() {
    super(
      Splitter.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Splitters distribute their input items across all three (3) of their outputs."
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof SplitterHelpPanel;
  }
}
