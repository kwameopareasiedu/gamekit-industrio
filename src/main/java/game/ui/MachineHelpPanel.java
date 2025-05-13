package game.ui;

import dev.gamekit.core.IO;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.*;
import game.machines.Machine;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MachineHelpPanel extends Compose {
  private static final BufferedImage MACHINES_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1152, 1720, 128, 144);

  protected MachineHelpPanel(Machine.Info info, Widget content) {
    super(
      Sized.create(
        Sized.options().fractionalWidth(0.2).intrinsicHeight(),
        Panel.create(
          Panel.options().background(MACHINES_PANEL_BG).ninePatch(20),
          Padding.create(
            Padding.options().padding(48, 36),
            Column.create(
              Column.options().gapSize(24).crossAxisAlignment(CrossAxisAlignment.CENTER),
              Sized.create(
                Sized.options().width(128).height(128),
                Image.create(info.image())
              ),
              Text.create(
                Text.options().color(Color.WHITE).fontSize(32).alignment(Alignment.CENTER),
                info.name()
              ),
              content
            )
          )
        )
      )
    );
  }
}
