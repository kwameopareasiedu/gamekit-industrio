package game;

import dev.gamekit.utils.Position;

import static dev.gamekit.utils.Math.toInt;

public class Utils {
  private static final Position INDEX_CACHE = new Position();

  private Utils() { }

  public static Position positionToIndex(Position pos) {
    int gridPixelSize = Constants.GRID_SIZE * Constants.CELL_PIXEL_SIZE;

    INDEX_CACHE.set(
      toInt(Math.floor((0.5 * gridPixelSize + pos.x) / Constants.CELL_PIXEL_SIZE)),
      toInt(Math.floor((0.5 * gridPixelSize + pos.y) / Constants.CELL_PIXEL_SIZE))
    );

    return INDEX_CACHE;
  }

  public static Position indexToPosition(int row, int col) {
    int gridPixelSize = Constants.GRID_SIZE * Constants.CELL_PIXEL_SIZE;

    INDEX_CACHE.set(
      toInt((col + 0.5) * Constants.CELL_PIXEL_SIZE - 0.5 * gridPixelSize),
      toInt((row + 0.5) * Constants.CELL_PIXEL_SIZE - 0.5 * gridPixelSize)
    );

    return INDEX_CACHE;
  }
}
