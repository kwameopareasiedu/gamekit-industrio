package game.machines;

import dev.gamekit.core.IO;
import game.factory.Factory;
import game.resources.Deposit;
import game.resources.Resource;

import java.awt.image.BufferedImage;

public class Extractor extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("extractor.png");
  public static final Info INFO = new Info("Extractor", IMAGE);
  public static int TICKS_FOR_EXTRACTION = 7;

  private final Deposit deposit;
  private int tickCounter = TICKS_FOR_EXTRACTION;

  public Extractor(int index, Direction direction, Deposit deposit) {
    super("Extractor", index, direction, Port.Type.OUT, null, null, null);
    this.deposit = deposit;
  }

  @Override
  public void tick() {
    tickCounter -= 1;

    if (tickCounter <= 0) {
      Port outputPort = outputs.get(0);

      if (!outputPort.hasResource()) {
        Resource res = deposit.extract();
        outputPort.resource = res;
        Factory.addResource(res);
      }

      tickCounter = TICKS_FOR_EXTRACTION;
    }
  }

  @Override
  public BufferedImage getImage() { return IMAGE; }
}
