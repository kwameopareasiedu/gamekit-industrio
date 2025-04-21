package game.machines;

import dev.gamekit.utils.Math;

public enum Direction {
  UP, RIGHT, DOWN, LEFT;

  private static final Direction[] VALUES = new Direction[]{
    UP, RIGHT, DOWN, LEFT
  };

  public static Direction cycle(Direction dir, int offset) {
    int index = -1;

    for (int i = 0; i < VALUES.length; i++) {
      if (VALUES[i] == dir) {
        index = i;
        break;
      }
    }

    int newIndex = Math.cycle(index + offset, 0, VALUES.length - 1);
    return VALUES[newIndex];
  }

  public double getAngle() {
    return switch (this) {
      case UP -> 0;
      case RIGHT -> 90;
      case DOWN -> 180;
      case LEFT -> 270;
    };
  }
}
