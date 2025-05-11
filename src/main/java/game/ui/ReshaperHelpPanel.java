package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Row;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.machines.Reshaper;
import game.resources.Shape;

import java.awt.*;
import java.util.Arrays;

public class ReshaperHelpPanel extends MachineHelpPanel {
  private static final Row[] COMBINATION_WIDGET = Arrays.stream(
    new Shape[][]{
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.GRAY, 0),
        new Shape(Shape.Type.CIRCLE, Color.RED, 0),
        new Shape(Shape.Type.SQUARE, Color.RED, 0)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.GRAY, 0),
        new Shape(Shape.Type.CIRCLE, Color.GREEN, 0),
        new Shape(Shape.Type.SQUARE, Color.GREEN, 0)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.GRAY, 0),
        new Shape(Shape.Type.CIRCLE, Color.BLUE, 0),
        new Shape(Shape.Type.SQUARE, Color.BLUE, 0)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.GRAY, 0),
        new Shape(Shape.Type.CIRCLE, Color.CYAN, 0),
        new Shape(Shape.Type.SQUARE, Color.CYAN, 0)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.GRAY, 0),
        new Shape(Shape.Type.CIRCLE, Color.MAGENTA, 0),
        new Shape(Shape.Type.SQUARE, Color.MAGENTA, 0)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, Color.GRAY, 0),
        new Shape(Shape.Type.CIRCLE, Color.YELLOW, 0),
        new Shape(Shape.Type.SQUARE, Color.YELLOW, 0)
      },
    }
  ).map(MixerHelpPanel::getShapeComboRow).toArray(Row[]::new);

  public ReshaperHelpPanel() {
    super(
      Reshaper.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(20),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Reshaper turns circles into squares."
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
    return widget instanceof ReshaperHelpPanel;
  }
}
