package game.ui;

import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Row;
import dev.gamekit.ui.widgets.Text;
import dev.gamekit.ui.widgets.Widget;
import game.items.PastelColor;
import game.items.Shape;
import game.machines.Reshaper;

import java.util.Arrays;

public class ReshaperHelpPanel extends MachineHelpPanel {
  private static final Row[] COMBINATION_WIDGET = Arrays.stream(
    new Shape[][]{
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY),
        new Shape(Shape.Type.CIRCLE, PastelColor.RED),
        new Shape(Shape.Type.SQUARE, PastelColor.RED)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY),
        new Shape(Shape.Type.CIRCLE, PastelColor.GREEN),
        new Shape(Shape.Type.SQUARE, PastelColor.GREEN)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY),
        new Shape(Shape.Type.CIRCLE, PastelColor.BLUE),
        new Shape(Shape.Type.SQUARE, PastelColor.BLUE)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY),
        new Shape(Shape.Type.CIRCLE, PastelColor.CYAN),
        new Shape(Shape.Type.SQUARE, PastelColor.CYAN)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY),
        new Shape(Shape.Type.CIRCLE, PastelColor.MAGENTA),
        new Shape(Shape.Type.SQUARE, PastelColor.MAGENTA)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY),
        new Shape(Shape.Type.CIRCLE, PastelColor.YELLOW),
        new Shape(Shape.Type.SQUARE, PastelColor.YELLOW)
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
