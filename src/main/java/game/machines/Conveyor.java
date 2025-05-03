package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;
import game.Utils;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.cycle;

public final class Conveyor extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("conveyor.png");
  public static final Info INFO = new Info("Conveyor", IMAGE);

  private final Vector position;
  private int inputIndex = 0;

  public static Conveyor create(int index, Direction direction) {
    if (direction == null)
      return null;
    return new Conveyor(index, direction);
  }

  private Conveyor(int index, Direction direction) {
    super("Conveyor", index, direction, Port.Type.OUT, Port.Type.IN, Port.Type.IN, Port.Type.IN);
    Position pos = Utils.indexToWorldPosition(index);
    position = new Vector(pos.x, pos.y);
  }

  @Override
  public void update() {
    super.update();
    Port in = inputs.get(inputIndex);
    Port out = outputs.get(0);

    if (in.hasResource() && !in.isResourceInBounds() && !out.hasResource()) {
      in.item.pos.set(position);
      in.transferResourceTo(out);
    }

    inputIndex = cycle(inputIndex + 1, 0, inputs.size() - 1);
  }

  @Override
  public BufferedImage getImage() { return IMAGE; }
}
