package game.machines;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Extractor extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("extractor.png");
  public static final Info INFO = new Info("Extractor", ICON);

  public Extractor(int gridIndex, Direction direction) {
    super("Extractor", gridIndex, direction,
      new Port(Port.Type.OUT), null, null, null);
  }

  @Override
  public void process() {
    Port outputPort = outputs.get(0);

  }

  @Override
  public BufferedImage getImage() { return ICON; }
}
