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

  public final Shade.Type type;
  public final int index;

  public Source(Shade.Type type, int row, int col) {
    super(String.format("%s deposit", type));
    this.type = type;
    this.index = Utils.rowColToIndex(row, col);
  }

  public static Source create(Shade.Type type, int row, int col) {
    return new Source(type, row, col);
  }

  public Shade extract() {
    return switch (type) {
      case WHITE_CIRCLE -> new Shade(Shade.Type.WHITE_CIRCLE, index);
      case BLACK_CIRCLE -> new Shade(Shade.Type.BLACK_CIRCLE, index);
      case GRAY_CIRCLE -> new Shade(Shade.Type.GRAY_CIRCLE, index);
    };
  }

  @Override
  protected void render() {
    Position pos = Utils.indexToWorldPosition(index);

    Color color = switch (type) {
      case WHITE_CIRCLE -> Color.WHITE;
      case BLACK_CIRCLE -> Color.BLACK;
      case GRAY_CIRCLE -> Color.GRAY;
    };

    Renderer.setColor(color);
    Renderer.fillRoundRect(pos.x, pos.y, SIZE, SIZE, 8, 8);
  }
}
