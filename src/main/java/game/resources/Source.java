package game.resources;

import dev.gamekit.utils.Vector;

import java.awt.*;

public final class Source {
  public final int row;
  public final int col;
  public final Color color;

  public static Source create(int row, int col, Color color) {
    return new Source(row, col, color);
  }

  private Source(int row, int col, Color color) {
    this.row = row;
    this.col = col;
    this.color = color;
  }

  public Shape extract(Vector position) {
    return new Shape(Shape.Type.CIRCLE, color, position);
  }
}
