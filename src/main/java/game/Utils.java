package game;

import dev.gamekit.utils.Position;
import game.factory.Factory;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.toInt;

public class Utils {
  private static final Position POSITION_CACHE = new Position();

  private Utils() { }

  public static int worldPositionToIndex(Position pos) {
    int gridPixelSize = Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
    int row = toInt(Math.floor((0.5 * gridPixelSize + pos.y) / Factory.CELL_PIXEL_SIZE));
    int col = toInt(Math.floor((0.5 * gridPixelSize + pos.x) / Factory.CELL_PIXEL_SIZE));
    row = clamp(row, 0, Factory.GRID_SIZE - 1);
    col = clamp(col, 0, Factory.GRID_SIZE - 1);
    return rowColToIndex(row, col);
  }

  public static Position indexToWorldPosition(int index) {
    int gridPixelSize = Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
    int col = indexToCol(index);
    int row = indexToRow(index);

    POSITION_CACHE.set(
      toInt((col + 0.5) * Factory.CELL_PIXEL_SIZE - 0.5 * gridPixelSize),
      toInt((row + 0.5) * Factory.CELL_PIXEL_SIZE - 0.5 * gridPixelSize)
    );

    return POSITION_CACHE;
  }

  public static int indexToRow(int index) {
    return index / Factory.GRID_SIZE;
  }

  public static int indexToCol(int index) {
    return index % Factory.GRID_SIZE;
  }

  public static int rowColToIndex(int row, int col) {
    return row * Factory.GRID_SIZE + col;
  }
}
