package game.items;

import dev.gamekit.utils.Vector;

public final class Source {
  public final int row;
  public final int col;
  public final PastelColor color;

  public static Source create(int row, int col, PastelColor color) {
    return new Source(row, col, color);
  }

  private Source(int row, int col, PastelColor color) {
    this.row = row;
    this.col = col;
    this.color = color;
  }

  public Shape extract(Vector position) {
    return new Shape(Shape.Type.CIRCLE, color, position);
  }
}
