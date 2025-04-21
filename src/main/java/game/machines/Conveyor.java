package game.machines;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Conveyor extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("conveyor.png");

  public Conveyor(int gridIndex, Direction direction) {
    super("Conveyor", gridIndex, direction,
      null, new Port(Port.Type.OUT), null, new Port(Port.Type.IN));
  }

  @Override
  public void process() {
    Port inputPort = inputs.get(0);
    Port outputPort = outputs.get(0);

    if (outputPort.cargo == null) {
      outputPort.cargo = inputPort.cargo;
      inputPort.cargo = null;
    }
  }

  @Override
  public BufferedImage getImage() { return ICON; }
}
