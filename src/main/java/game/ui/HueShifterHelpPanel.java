package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Row;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.items.PastelColor;
import game.items.Shape;
import game.machines.HueShifter;

import java.util.Arrays;

public class HueShifterHelpPanel extends MachineHelpPanel {
  private static final Row[] COMBINATION_WIDGET = Arrays.stream(
    new Shape[][]{
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.RED),
        new Shape(Shape.Type.CIRCLE, PastelColor.GREEN)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.GREEN),
        new Shape(Shape.Type.CIRCLE, PastelColor.BLUE)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.BLUE),
        new Shape(Shape.Type.CIRCLE, PastelColor.RED)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.CYAN),
        new Shape(Shape.Type.CIRCLE, PastelColor.MAGENTA)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.MAGENTA),
        new Shape(Shape.Type.CIRCLE, PastelColor.YELLOW)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.YELLOW),
        new Shape(Shape.Type.CIRCLE, PastelColor.CYAN)
      },
    }
  ).map(MixerHelpPanel::getShapeComboRow).toArray(Row[]::new);

  public HueShifterHelpPanel() {
    super(
      HueShifter.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(20),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Hue Shifter changes the hue of the connected colors."
        ),
        Column.create(
          Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
          COMBINATION_WIDGET
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof HueShifterHelpPanel;
  }
}
