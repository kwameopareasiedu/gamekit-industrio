package game.machines;

import dev.gamekit.core.IO;
import game.resources.Deposit;

import java.awt.image.BufferedImage;

public class Extractor extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("extractor.png");
  public static final Info INFO = new Info("Extractor", ICON);

  public final Deposit deposit;

  public Extractor(int gridIndex, Direction direction, Deposit deposit) {
    super("Extractor", gridIndex, direction,
      new Port(Port.Type.OUT), null, null, null);
    this.deposit = deposit;
  }

  @Override
  public void process() {
    Port outputPort = outputs.get(0);
//    Resource res = deposit.extract(row, col)
  }

  @Override
  public BufferedImage getImage() { return ICON; }
}
