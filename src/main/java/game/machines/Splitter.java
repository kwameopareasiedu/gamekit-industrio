package game.machines;

import dev.gamekit.core.IO;
import game.factory.Factory;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.cycle;

public class Splitter extends Machine {
  private static final BufferedImage SPRITE = IO.getResourceImage("machines/splitter.png");

  public static final Info INFO = new Info("Splitter", SPRITE);

  private int outIndex = 0;

  public Splitter(int row, int col, Factory factory, Direction direction) {
    super(
      "Mixer", row, col, factory, direction,
      Port.Type.OUT, Port.Type.OUT, Port.Type.IN, Port.Type.OUT
    );
  }

  @Override
  protected void update() {
    super.update();
    Port in = inputs.get(0);

    if (in.hasItem() && !in.isItemInBounds()) {
      Port out = outputs.get(outIndex);
      if (!out.hasItem())
        in.transferResourceTo(out);
      outIndex = cycle(outIndex + 1, 0, outputs.size() - 1);
    }
  }

  @Override
  public BufferedImage getImage() {
    return SPRITE;
  }
}
