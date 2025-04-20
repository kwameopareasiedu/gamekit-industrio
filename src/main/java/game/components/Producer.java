package game.components;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Producer extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("producer.png");
  public static final Info INFO = new Info("Producer", ICON);

  public Producer(int row, int col) {
    super("Producer", row, col, new Port[0], new Port[]{ new Port() });
  }

  @Override
  protected void start() {
    super.start();
  }

  @Override
  public void performProcess() {

  }

  @Override
  public BufferedImage getImage() {
    return ICON;
  }
}
