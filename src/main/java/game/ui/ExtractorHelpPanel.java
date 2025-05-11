package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import game.machines.Extractor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ExtractorHelpPanel extends MachineHelpPanel {
  private static final BufferedImage PLUS_IMG = IO.getResourceImage("plus.png");
  private static final BufferedImage EQUAL_IMG = IO.getResourceImage("equal.png");

  public ExtractorHelpPanel() {
    super(
      Extractor.INFO,
      Column.create(
        Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "Extractors produce colored shapes when placed over a colored source."
        ),
        Row.create(
          Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
            .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(12),
          Sized.create(
            Sized.options().width(64).height(64),
            Image.create(Extractor.INFO.image())
          ),
          Sized.create(
            Sized.options().width(24).height(24),
            Image.create(PLUS_IMG)
          ),
          Column.create(
            Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
            Row.create(
              Row.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(8),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(Color.WHITE, 4)
              ),
              Sized.create(
                Sized.options().width(20).height(20),
                Image.create(EQUAL_IMG)
              ),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(Color.WHITE, 32)
              )
            ),
            Row.create(
              Row.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(8),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(Color.GREEN, 4)
              ),
              Sized.create(
                Sized.options().width(20).height(20),
                Image.create(EQUAL_IMG)
              ),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(Color.GREEN, 32)
              )
            )
          )
        )
      )
    );
  }

  @Override
  public boolean stateEquals(Widget widget) {
    return widget instanceof ExtractorHelpPanel;
  }
}
