package game.components;

import dev.gamekit.core.IO;

import java.awt.image.BufferedImage;

public class Producer extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("icons/mach1.png");

  public Producer(String name, int row, int col) {
    super(name, row, col, new Port[0], new Port[]{ new Port() });
  }

  @Override
  protected void start() {
    super.start();
  }

  @Override
  public void performProcess() {

  }

  @Override
  protected BufferedImage getImage() {
    return ICON;
  }
}
