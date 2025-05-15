package game.items;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Vector;
import game.factory.Factory;

import java.awt.*;

import static dev.gamekit.utils.Math.toInt;

public final class Shape extends Prop {
  private static final Stroke OUTLINE_STROKE =
    new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final int SIZE = toInt(0.15 * Factory.CELL_PIXEL_SIZE);

  public final Type type;
  public final PastelColor color;
  public final Vector pos;

  public Shape(Type type, PastelColor color) {
    super(type.name());
    this.type = type;
    this.color = color;
    this.pos = new Vector();
  }

  public Shape(Type type, PastelColor color, Vector position) {
    super(type.name());
    this.type = type;
    this.color = color;
    this.pos = new Vector(position);
  }

  @Override
  protected void render() {
    if (type == Type.CIRCLE) {
      Renderer.setColor(color);
      Renderer.fillCircle((int) pos.x, (int) pos.y, SIZE);
      Renderer.setColor(Color.BLACK);
      Renderer.setStroke(OUTLINE_STROKE);
      Renderer.drawCircle((int) pos.x, (int) pos.y, SIZE);
    } else if (type == Type.SQUARE) {
      Renderer.setColor(color);
      Renderer.fillRoundRect((int) pos.x, (int) pos.y, 2 * SIZE, 2 * SIZE, 4, 4);
      Renderer.setColor(Color.BLACK);
      Renderer.setStroke(OUTLINE_STROKE);
      Renderer.drawRoundRect((int) pos.x, (int) pos.y, 2 * SIZE, 2 * SIZE, 4, 4);
    }
  }

  public enum Type {
    CIRCLE,
    SQUARE,
  }
}
