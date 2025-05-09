package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Utils;
import game.factory.Factory;
import game.resources.Shape;

import java.awt.image.BufferedImage;

public final class Hub extends Machine {
  private static final BufferedImage[] SPRITES = new BufferedImage[]{
    IO.getResourceImage("hub.png", 0, 0, 192, 192),
    IO.getResourceImage("hub.png", 192, 0, 192, 192),
    IO.getResourceImage("hub.png", 384, 0, 192, 192),
    IO.getResourceImage("hub.png", 0, 192, 192, 192),
    IO.getResourceImage("hub.png", 192, 192, 192, 192),
  };

  private final Notifier notifier;

  public static Hub create(int index, Direction direction, Notifier notifier) {
    if (direction == null || notifier == null)
      return null;
    return new Hub(index, direction, notifier);
  }

  private Hub(int index, Direction direction, Notifier notifier) {
    super("Hub", index, direction, Port.Type.IN, Port.Type.IN, Port.Type.IN, Port.Type.IN);
    this.notifier = notifier;
  }

  @Override
  protected void update() {
    super.update();

    for (Port in : inputs) {
      if (in.hasItem() && !in.isItemInBounds()) {
        notifier.notify(in.item);
        in.item = null;
      }
    }
  }

  @Override
  public BufferedImage getImage() {
    return null;
  }

  @Override
  protected void render() {
    Position pos = Utils.indexToWorldPosition(index);

    Renderer.drawImage(
      SPRITES[0], pos.x, pos.y,
      Factory.CELL_PIXEL_SIZE,
      Factory.CELL_PIXEL_SIZE
    );

    Machine topMachine = Factory.getMachineAt(index + Factory.GRID_SIZE);
    Machine rightMachine = Factory.getMachineAt(index + 1);
    Machine bottomMachine = Factory.getMachineAt(index - Factory.GRID_SIZE);
    Machine leftMachine = Factory.getMachineAt(index - 1);

    if (topMachine != null && topMachine.portHasDirection(Port.BOTTOM, Direction.DOWN)) {
      Renderer.drawImage(
        SPRITES[3], pos.x, pos.y,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }

    if (rightMachine != null && rightMachine.portHasDirection(Port.LEFT, Direction.LEFT)) {
      Renderer.drawImage(
        SPRITES[4], pos.x, pos.y,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }

    if (bottomMachine != null && bottomMachine.portHasDirection(Port.TOP, Direction.UP)) {
      Renderer.drawImage(
        SPRITES[1], pos.x, pos.y,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }

    if (leftMachine != null && leftMachine.portHasDirection(Port.RIGHT, Direction.RIGHT)) {
      Renderer.drawImage(
        SPRITES[2], pos.x, pos.y,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }
  }

  public interface Notifier {
    void notify(Shape shape);
  }
}
