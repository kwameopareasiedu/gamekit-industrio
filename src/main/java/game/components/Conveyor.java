package game.components;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Conveyor extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("conveyor.png");

  public Conveyor(String name, int row, int col) {
    super(
      name, row, col,
      new Port[]{ new Port() },
      new Port[]{ new Port() }
    );
  }

  @Override
  protected void performProcess() {
    Port inputPort = inputs[0];
    Port outputPort = freeOutputs.get(0);

    if (outputPort.item == null) {
      outputPort.item = inputPort.item;
      inputPort.item = null;
    }
  }

  @Override
  protected BufferedImage getImage() {
    return ICON;
  }
}
