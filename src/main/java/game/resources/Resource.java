package game.resources;

import dev.gamekit.core.IO;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;
import game.Constants;
import game.Utils;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.toInt;

public final class Resource extends Prop {
  private static final BufferedImage ROCK_IMAGE = IO.getResourceImage("rocks.png");

  public final Type type;
  public final Vector position;

  public Resource(Type type, int index) {
    super(type.name());
    this.type = type;
    Position pos = Utils.indexToWorldPosition(index);
    position = new Vector(pos.x, pos.y);
  }

  @Override
  protected void render() {
    BufferedImage image = getImage();

    Renderer.drawImage(
      image, (int) position.x, (int) position.y,
      toInt(0.6 * Constants.CELL_PIXEL_SIZE),
      toInt(0.6 * Constants.CELL_PIXEL_SIZE)
    );
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
