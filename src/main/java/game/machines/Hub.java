package game.machines;

import dev.gamekit.core.IO;
import game.components.Cargo;

import java.awt.image.BufferedImage;

public class Hub extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("hub.png");

  private final Notifier notifier;

  public Hub(int gridIndex, Direction direction, Notifier notifier) {
    super("Hub", gridIndex, direction,
      new Port(Port.Type.IN), new Port(Port.Type.IN),
      new Port(Port.Type.IN), new Port(Port.Type.IN));
    this.notifier = notifier;
  }

  @Override
  public void process() {
    for (Port port : inputs) {
      if (port.cargo != null) {
        notifier.onCargoReceived(port.cargo);
      }
    }
  }

  @Override
  public BufferedImage getImage() { return ICON; }

  public interface Notifier {
    void onCargoReceived(Cargo cargo);
  }
}
