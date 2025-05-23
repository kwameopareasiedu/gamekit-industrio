package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.*;
import game.items.PastelColor;
import game.items.Shape;
import game.machines.Mixer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class MixerHelpPanel extends MachineHelpPanel {
  private static final BufferedImage PLUS_IMG = IO.getResourceImage("plus.png");
  private static final BufferedImage EQUAL_IMG = IO.getResourceImage("equal.png");

  private static final Row[] COMBINATION_WIDGET = Arrays.stream(
    new Shape[][]{
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.WHITE),
        new Shape(Shape.Type.CIRCLE, PastelColor.BLACK),
        new Shape(Shape.Type.CIRCLE, PastelColor.GRAY)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.WHITE),
        new Shape(Shape.Type.CIRCLE, PastelColor.RED),
        new Shape(Shape.Type.CIRCLE, PastelColor.GREEN),
        new Shape(Shape.Type.CIRCLE, PastelColor.YELLOW)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.WHITE),
        new Shape(Shape.Type.CIRCLE, PastelColor.RED),
        new Shape(Shape.Type.CIRCLE, PastelColor.BLUE),
        new Shape(Shape.Type.CIRCLE, PastelColor.MAGENTA)
      },
      new Shape[]{
        new Shape(Shape.Type.CIRCLE, PastelColor.WHITE),
        new Shape(Shape.Type.CIRCLE, PastelColor.GREEN),
        new Shape(Shape.Type.CIRCLE, PastelColor.BLUE),
        new Shape(Shape.Type.CIRCLE, PastelColor.CYAN)
      },

      new Shape[]{
        new Shape(Shape.Type.SQUARE, PastelColor.CYAN),
        new Shape(Shape.Type.SQUARE, PastelColor.MAGENTA),
        new Shape(Shape.Type.SQUARE, PastelColor.YELLOW),
        new Shape(Shape.Type.SQUARE, PastelColor.BLACK)
      },
      new Shape[]{
        new Shape(Shape.Type.SQUARE, PastelColor.RED),
        new Shape(Shape.Type.SQUARE, PastelColor.GREEN),
        new Shape(Shape.Type.SQUARE, PastelColor.BLUE),
        new Shape(Shape.Type.SQUARE, PastelColor.WHITE)
      },
      new Shape[]{
        new Shape(Shape.Type.SQUARE, PastelColor.WHITE),
        new Shape(Shape.Type.SQUARE, PastelColor.BLACK),
        new Shape(Shape.Type.SQUARE, PastelColor.GRAY)
      }
    }
  ).map(MixerHelpPanel::getShapeComboRow).toArray(Row[]::new);

  public static Row getShapeComboRow(Shape[] combos) {
    ArrayList<Widget> children = new ArrayList<>();

    for (int i = 0; i < combos.length; i++) {
      Shape shape = combos[i];

      children.add(
        Sized.create(
          Sized.options().width(24).height(24),
          Colored.create(shape.color, shape.type == Shape.Type.CIRCLE ? 24 : 0)
        )
      );

      if (i < combos.length - 2) {
        children.add(
          Sized.create(
            Sized.options().width(16).height(16),
            Image.create(PLUS_IMG)
          )
        );
      } else if (i == combos.length - 2) {
        children.add(
          Sized.create(
            Sized.options().width(16).height(16),
            Image.create(EQUAL_IMG)
          )
        );
      }
    }

    Widget[] childArray = new Widget[children.size()];
    childArray = children.toArray(childArray);

    return Row.create(
      Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
        .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(10),
      childArray
    );
  }

  public MixerHelpPanel() {
    super(
      Mixer.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(20),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Mixers combine two (2) or more colors to produce a new color."
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
    return widget instanceof MixerHelpPanel;
  }
}
