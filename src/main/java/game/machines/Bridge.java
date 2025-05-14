package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import game.factory.Factory;

import java.awt.image.BufferedImage;

public final class Bridge extends Machine {
  private static final BufferedImage SPRITE = IO.getResourceImage("machines/bridge.png");

  public static final Info INFO = new Info("Bridge", SPRITE);

  private final Vector position;

  public Bridge(int row, int col, Factory factory, Direction direction) {
    super(
      "Belt", row, col, factory, direction,
      Port.Type.OUT, Port.Type.OUT, Port.Type.IN, Port.Type.IN
    );
    position = new Vector(super.position.x, super.position.y);
  }

  @Override
  public void update() {
    super.update();
    Port in1 = inputs.get(0);
    Port out1 = outputs.get(direction == Direction.UP || direction == Direction.DOWN ? 0 : 1);
    Port in2 = inputs.get(1);
    Port out2 = outputs.get(direction == Direction.UP || direction == Direction.DOWN ? 1 : 0);

    if (in1.hasItem() && !in1.isItemInBounds() && !out1.hasItem()) {
      in1.item.pos.set(position);
      in1.transferResourceTo(out1);
    }

    if (in2.hasItem() && !in2.isItemInBounds() && !out2.hasItem()) {
      in2.item.pos.set(position);
      in2.transferResourceTo(out2);
    }
  }

  @Override
  public BufferedImage getImage() {
    return SPRITE;
  }
}
