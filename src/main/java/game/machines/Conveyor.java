package game.machines;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Conveyor extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("conveyor.png");
  public static final Info INFO = new Info("Conveyor", IMAGE);

  public Conveyor(int gridIndex, Direction direction) {
    super("Conveyor", gridIndex, direction, Port.Type.OUT, null, Port.Type.IN, null);
  }

  @Override
  public BufferedImage getImage() { return IMAGE; }
}
