package game.components;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Conveyor extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("conveyor.png");
  public static final Info INFO = new Info("Conveyor", ICON);

  public Conveyor(int row, int col) {
    super(
      "Conveyor", row, col,
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
  public BufferedImage getImage() {
    return ICON;
  }
}
