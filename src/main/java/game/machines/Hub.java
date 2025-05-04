package game.machines;

import dev.gamekit.core.IO;
import game.resources.Shape;

import java.awt.image.BufferedImage;

public final class Hub extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("hub.png");

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
  public BufferedImage getImage() { return ICON; }

  public interface Notifier {
    void notify(Shape shape);
  }
}
