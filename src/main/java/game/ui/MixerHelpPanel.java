package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import game.machines.Mixer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class MixerHelpPanel extends MachineHelpPanel {
  private static final BufferedImage PLUS_IMG = IO.getResourceImage("plus.png");
  private static final BufferedImage EQUAL_IMG = IO.getResourceImage("equal.png");

  private static final Row[] CIRCLE_COMBINATION_ROW = Arrays.stream(
    new Color[][]{
      new Color[]{ Color.WHITE, Color.BLACK, Color.GRAY },
      new Color[]{ Color.WHITE, Color.RED, Color.GREEN, Color.YELLOW },
      new Color[]{ Color.WHITE, Color.RED, Color.BLUE, Color.MAGENTA },
      new Color[]{ Color.WHITE, Color.GREEN, Color.BLUE, Color.CYAN },
    }
  ).map(combos -> MixerHelpPanel.generateColorRow(combos, 24)).toArray(Row[]::new);

  private static final Row[] SQUARE_COMBINATION_ROW = Arrays.stream(
    new Color[][]{
      new Color[]{ Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.BLACK },
      new Color[]{ Color.RED, Color.GREEN, Color.BLUE, Color.WHITE },
      new Color[]{ Color.WHITE, Color.BLACK, Color.GRAY },
    }
  ).map(combos -> MixerHelpPanel.generateColorRow(combos, 0)).toArray(Row[]::new);

  public static Row generateColorRow(Color[] combos, int borderRadius) {
    ArrayList<Widget> children = new ArrayList<>();

    for (int i = 0; i < combos.length; i++) {
      Color color = combos[i];

      children.add(
        Sized.create(
          Sized.options().width(24).height(24),
          Colored.create(color, borderRadius)
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
          CIRCLE_COMBINATION_ROW
        ),
        Column.create(
          Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
          SQUARE_COMBINATION_ROW
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof MixerHelpPanel;
  }
}
