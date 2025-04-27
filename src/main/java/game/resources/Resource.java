package game.resources;

import dev.gamekit.core.IO;
import dev.gamekit.core.Prop;
import dev.gamekit.utils.Vector;

import java.awt.image.BufferedImage;

public final class Resource extends Prop {
  private static final BufferedImage ROCK_IMAGE = IO.getResourceImage("rocks.png");

  public final Type type;
  public final Vector position;

  public Resource(Type type, int row, int col) {
    super(type.name());
    this.type = type;
    position = new Vector();
  }

  public BufferedImage getImage() {
    return switch (type) {
      case ROCK -> ROCK_IMAGE;
    };
  }

  public enum Type {
    ROCK
  }
}
