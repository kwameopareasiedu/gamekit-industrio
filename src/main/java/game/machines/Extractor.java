package game.machines;

import dev.gamekit.core.Application;
import dev.gamekit.core.IO;
import game.resources.Deposit;
import game.resources.Resource;
import game.world.World;

import java.awt.image.BufferedImage;

public class Extractor extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("extractor.png");
  public static final Info INFO = new Info("Extractor", IMAGE);
  public static long TIMER_MS = 2000;
  private final Deposit deposit;
  private long timerMs = TIMER_MS;

  public Extractor(int index, Direction direction, Deposit deposit) {
    super("Extractor", index, direction, Port.Type.OUT, null, null, null);
    this.deposit = deposit;
  }

  @Override
  public void update() {
    super.update();
    timerMs -= Application.FRAME_TIME_MS;

    if (timerMs <= 0) {
      Port outputPort = outputs.get(0);

      if (outputPort.item == null) {
        Resource res = deposit.extract();
        outputPort.item = res;
        World.getResources().addChild(res);
      }

      timerMs = TIMER_MS;
    }
  }

  @Override
  public BufferedImage getImage() { return IMAGE; }
}
