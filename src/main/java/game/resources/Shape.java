package game.resources;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;
import game.Utils;
import game.factory.Factory;

import java.awt.*;

import static dev.gamekit.utils.Math.toInt;

public final class Shape extends Prop {
  private static final int SIZE = toInt(0.15 * Factory.CELL_PIXEL_SIZE);

  public final Type type;
  public final Color color;
  public final Vector pos;

  public Shape(Type type, Color color, int index) {
    super(type.name());
    this.type = type;
    this.color = color;

    Position pos = Utils.indexToWorldPosition(index);
    this.pos = new Vector(pos.x, pos.y);
  }

  @Override
  protected void render() {
    Renderer.setColor(color);
    if (type == Type.CIRCLE)
      Renderer.fillCircle((int) pos.x, (int) pos.y, SIZE);
    else if (type == Type.SQUARE)
      Renderer.fillRoundRect((int) pos.x, (int) pos.y, 2 * SIZE, 2 * SIZE, 4, 4);
  }

  public enum Type {
    CIRCLE,
    SQUARE,
  }
}
