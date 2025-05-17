package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.widgets.*;
import game.items.PastelColor;
import game.machines.Extractor;

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
          "Extractors produce the shapes needed by your factory."
        ),
        Text.create(
          Text.options().alignment(Alignment.CENTER).fontSize(20),
          "When placed on the factory floor, extractors start produce circles which you can work " +
            "with."
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
                Colored.create(PastelColor.WHITE, 4)
              ),
              Sized.create(
                Sized.options().width(20).height(20),
                Image.create(EQUAL_IMG)
              ),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(PastelColor.WHITE, 32)
              )
            ),
            Row.create(
              Row.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(8),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(PastelColor.GREEN, 4)
              ),
              Sized.create(
                Sized.options().width(20).height(20),
                Image.create(EQUAL_IMG)
              ),
              Sized.create(
                Sized.options().width(32).height(32),
                Colored.create(PastelColor.GREEN, 32)
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
