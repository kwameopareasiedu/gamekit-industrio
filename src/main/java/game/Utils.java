package game;

import dev.gamekit.utils.Position;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.toInt;

public class Utils {
  private static final Position INDEX_CACHE = new Position();

  private Utils() { }

  public static int worldPositionToIndex(Position pos) {
    int gridPixelSize = Constants.GRID_SIZE * Constants.CELL_PIXEL_SIZE;
    int row = toInt(Math.floor((0.5 * gridPixelSize + pos.y) / Constants.CELL_PIXEL_SIZE));
    int col = toInt(Math.floor((0.5 * gridPixelSize + pos.x) / Constants.CELL_PIXEL_SIZE));
    row = clamp(row, 0, Constants.GRID_SIZE - 1);
    col = clamp(col, 0, Constants.GRID_SIZE - 1);
    return rowColToIndex(row, col);
  }

  public static Position indexToWorldPosition(int index) {
    int gridPixelSize = Constants.GRID_SIZE * Constants.CELL_PIXEL_SIZE;
    int col = indexToCol(index);
    int row = indexToRow(index);

    INDEX_CACHE.set(
      toInt((col + 0.5) * Constants.CELL_PIXEL_SIZE - 0.5 * gridPixelSize),
      toInt((row + 0.5) * Constants.CELL_PIXEL_SIZE - 0.5 * gridPixelSize)
    );

    return INDEX_CACHE;
  }

  public static int indexToRow(int index) {
    return index / Constants.GRID_SIZE;
  }

  public static int indexToCol(int index) {
    return index % Constants.GRID_SIZE;
  }

  public static int rowColToIndex(int row, int col) {
    return row * Constants.GRID_SIZE + col;
  }
}
