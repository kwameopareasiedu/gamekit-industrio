package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Row;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.machines.HueShifter;
import game.resources.Shape;

import java.awt.*;
import java.util.Arrays;

public class HueShifterHelpPanel extends MachineHelpPanel {
  private static final Row[] COMBINATION_WIDGET = Arrays.stream(
    new Shape[][]{
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.BLACK),
        new Shape(Shape.Type.CIRCLE, Color.RED),
        new Shape(Shape.Type.CIRCLE, Color.GREEN)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.BLACK),
        new Shape(Shape.Type.CIRCLE, Color.GREEN),
        new Shape(Shape.Type.CIRCLE, Color.BLUE)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.BLACK),
        new Shape(Shape.Type.CIRCLE, Color.BLUE),
        new Shape(Shape.Type.CIRCLE, Color.RED)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.BLACK),
        new Shape(Shape.Type.CIRCLE, Color.CYAN),
        new Shape(Shape.Type.CIRCLE, Color.MAGENTA)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.BLACK),
        new Shape(Shape.Type.CIRCLE, Color.MAGENTA),
        new Shape(Shape.Type.CIRCLE, Color.YELLOW)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.BLACK),
        new Shape(Shape.Type.CIRCLE, Color.YELLOW),
        new Shape(Shape.Type.CIRCLE, Color.CYAN)
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
