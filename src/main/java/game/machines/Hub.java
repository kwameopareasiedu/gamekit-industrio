package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.core.Renderer;
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

  public Hub(int row, int col, Factory factory, Direction direction, Notifier notifier) {
    super(
      "Hub", row, col, factory, direction,
      Port.Type.IN, Port.Type.IN, Port.Type.IN, Port.Type.IN
    );

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
    int posX = (int) position.x;
    int posY = (int) position.y;

    Renderer.drawImage(
      SPRITES[0], posX, posY,
      Factory.CELL_PIXEL_SIZE,
      Factory.CELL_PIXEL_SIZE
    );

    Machine topMachine = factory.getMachineAt(row + 1, col);
    Machine rightMachine = factory.getMachineAt(row, col + 1);
    Machine bottomMachine = factory.getMachineAt(row - 1, col);
    Machine leftMachine = factory.getMachineAt(row, col - 1);

    if (topMachine != null && topMachine.portHasDirection(Port.BOTTOM, Direction.DOWN)) {
      Renderer.drawImage(
        SPRITES[3], posX, posY,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }

    if (rightMachine != null && rightMachine.portHasDirection(Port.LEFT, Direction.LEFT)) {
      Renderer.drawImage(
        SPRITES[4], posX, posY,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }

    if (bottomMachine != null && bottomMachine.portHasDirection(Port.TOP, Direction.UP)) {
      Renderer.drawImage(
        SPRITES[1], posX, posY,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }

    if (leftMachine != null && leftMachine.portHasDirection(Port.RIGHT, Direction.RIGHT)) {
      Renderer.drawImage(
        SPRITES[2], posX, posY,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    }
  }

  public interface Notifier {
    void notify(Shape shape);
  }
}
