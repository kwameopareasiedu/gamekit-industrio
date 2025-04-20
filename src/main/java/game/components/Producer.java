package game.components;

import dev.gamekit.core.IO;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;

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
  public void input() {

  }

  @Override
  public void process() {

  }

  @Override
  public void output() {

  }

  @Override
  protected void render() {
    Position pos = Utils.indexToPosition(row, col);
    Renderer.drawImage(ICON, pos.x, pos.y, Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE);
  }
}
