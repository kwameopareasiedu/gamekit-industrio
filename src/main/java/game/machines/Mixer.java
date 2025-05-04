package game.machines;

import dev.gamekit.core.IO;
import game.factory.Factory;
import game.resources.Shade;

import java.awt.image.BufferedImage;

public class Mixer extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("mixer.png");
  public static final Info INFO = new Info("Mixer", IMAGE);

  public static Mixer create(int index, Direction direction) {
    if (direction == null)
      return null;
    return new Mixer(index, direction);
  }

  protected Mixer(int index, Direction direction) {
    super("Mixer", index, direction, Port.Type.OUT, Port.Type.IN, null, Port.Type.IN);
  }

  @Override
  protected void update() {
    super.update();
    Port in1 = inputs.get(0), in2 = inputs.get(1);
    Port out = outputs.get(0);

    if (in1.hasResource() && !in1.isResourceInBounds() &&
      in2.hasResource() && !in2.isResourceInBounds() &&
      !out.hasResource()) {
      Shade combinedShade = combine(in1.item, in2.item);

      if (combinedShade != null) {
        Factory.removeItem(in1.item);
        Factory.removeItem(in2.item);
        Factory.addResource(combinedShade);
        in1.item = null;
        in2.item = null;
        out.item = combinedShade;
      }
    }
  }

  @Override
  public BufferedImage getImage() {
    return IMAGE;
  }

  private Shade combine(Shade shade1, Shade shade2) {
    if ((shade1.type == Shade.Type.WHITE_CIRCLE && shade2.type == Shade.Type.BLACK_CIRCLE) ||
      (shade2.type == Shade.Type.WHITE_CIRCLE && shade1.type == Shade.Type.BLACK_CIRCLE))
      return new Shade(Shade.Type.GRAY_CIRCLE, index);

    return null;
  }
}
