package game.resources;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;
import game.Utils;
import game.factory.Factory;

import java.awt.*;

import static dev.gamekit.utils.Math.toInt;

public final class Shade extends Prop {
  private static final Stroke OUTLINE_STROKE =
    new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final int SIZE = toInt(0.15 * Factory.CELL_PIXEL_SIZE);

  public final Type type;
  public final Vector pos;

  public Shade(Type type, int index) {
    super(type.name());
    this.type = type;
    Position pos = Utils.indexToWorldPosition(index);
    this.pos = new Vector(pos.x, pos.y);
  }

  @Override
  protected void render() {
    Color color = switch (type) {
      case WHITE_CIRCLE -> Color.WHITE;
      case BLACK_CIRCLE -> Color.BLACK;
      case GRAY_CIRCLE -> Color.GRAY;
    };

    Renderer.setColor(color);
    Renderer.fillCircle((int) pos.x, (int) pos.y, SIZE);
    Renderer.setColor(color);
    Renderer.setStroke(OUTLINE_STROKE);
    Renderer.drawCircle((int) pos.x, (int) pos.y, SIZE);
  }

  public enum Type {
    WHITE_CIRCLE, BLACK_CIRCLE, GRAY_CIRCLE
  }
}
