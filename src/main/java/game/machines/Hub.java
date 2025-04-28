package game.machines;

import dev.gamekit.core.IO;
import game.resources.Resource;

import java.awt.image.BufferedImage;

public class Hub extends Machine {
  private static final BufferedImage ICON = IO.getResourceImage("hub.png");

  private final Notifier notifier;

  public Hub(int gridIndex, Direction direction, Notifier notifier) {
    super("Hub", gridIndex, direction, Port.Type.IN, Port.Type.IN, Port.Type.IN, Port.Type.IN);
    this.notifier = notifier;
  }

//  @Override
//  public void process() {
//    for (Port port : inputs) {
//      if (port.item != null) {
//        notifier.onPayloadReceived(port.item);
//      }
//    }
//  }

  @Override
  public BufferedImage getImage() { return ICON; }

  public interface Notifier {
    void onPayloadReceived(Resource resource);
  }
}
