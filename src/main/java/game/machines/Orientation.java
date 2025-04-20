package game.machines;

import dev.gamekit.utils.Math;

public enum Orientation {
  UP, RIGHT, DOWN, LEFT;

  private static final Orientation[] VALUES = new Orientation[]{
    UP, RIGHT, DOWN, LEFT
  };

  public static Orientation cycle(Orientation value, int offset) {
    int index = -1;

    for (int i = 0; i < VALUES.length; i++) {
      if (VALUES[i] == value) {
        index = i;
        break;
      }
    }

    int newIndex = Math.cycle(index + offset, 0, VALUES.length - 1);
    return VALUES[newIndex];
  }

  public double toDeg() {
    return switch (this) {
      case UP -> 0;
      case RIGHT -> 90;
      case DOWN -> 180;
      case LEFT -> 270;
    };
  }
}
