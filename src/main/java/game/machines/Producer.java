package game.machines;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Producer extends DroppableMachine {
  private static final BufferedImage ICON = IO.getResourceImage("producer.png");
  public static final Info INFO = new Info("Producer", ICON);

  public Producer(int gridIndex, Orientation orientation) {
    super("Producer", gridIndex, orientation,
      new Port[0], new Port[]{ new Port() });
  }

  @Override
  public void performProcess() {

  }

  @Override
  public BufferedImage getImage() { return ICON; }
}
