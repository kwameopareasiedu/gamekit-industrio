package game.resources;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Utils;
import game.factory.Factory;

import java.awt.*;

import static dev.gamekit.utils.Math.toInt;

public final class Source extends Prop {
  private static final int SIZE = toInt(0.4 * Factory.CELL_PIXEL_SIZE);

  public final Color color;
  public final int index;

  public Source(Color color, int row, int col) {
    super(String.format("%s deposit", color));
    this.color = color;
    this.index = Utils.rowColToIndex(row, col);
  }

  public static Source create(Color color, int row, int col) {
    return new Source(color, row, col);
  }

  public Shape extract() {
    return new Shape(Shape.Type.CIRCLE, color, index);
  }

  @Override
  protected void render() {
    Position pos = Utils.indexToWorldPosition(index);
    Renderer.setColor(color);
    Renderer.fillRoundRect(pos.x, pos.y, SIZE, SIZE, 8, 8);
  }
}
