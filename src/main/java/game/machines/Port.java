package game.machines;

import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Vector;
import game.factory.Factory;
import game.items.Shape;

public class Port {
  public static final int TOP = 0;
  public static final int RIGHT = 1;
  public static final int BOTTOM = 2;
  public static final int LEFT = 3;
  public static double MOVE_SPEED = 0.85;

  public final Type type;
  public final Direction direction;
  public Shape item;

  private final Bounds bounds;

  public static Port create(Type type, Direction direction, Machine machine) {
    if (type == null || direction == null || machine == null)
      return null;

    return new Port(type, direction, machine);
  }

  private Port(Type type, Direction direction, Machine machine) {
    this.type = type;
    this.direction = direction;
    this.item = null;

    Vector pos = machine.position;

    bounds = switch (direction) {
      case UP -> new Bounds(
        pos.x - 0.5 * Factory.CELL_PIXEL_SIZE,
        pos.y,
        Factory.CELL_PIXEL_SIZE,
        0.5 * Factory.CELL_PIXEL_SIZE
      );
      case RIGHT -> new Bounds(
        pos.x,
        pos.y - 0.5 * Factory.CELL_PIXEL_SIZE,
        0.5 * Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
      case DOWN -> new Bounds(
        pos.x - 0.5 * Factory.CELL_PIXEL_SIZE,
        pos.y - 0.5 * Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE,
        0.5 * Factory.CELL_PIXEL_SIZE
      );
      case LEFT -> new Bounds(
        pos.x - 0.5 * Factory.CELL_PIXEL_SIZE,
        pos.y - 0.5 * Factory.CELL_PIXEL_SIZE,
        0.5 * Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      );
    };
  }

  public boolean moveResource() {
    if (!hasItem() || !isItemInBounds())
      return false;

    switch (type) {
      case IN -> {
        switch (direction) {
          case UP -> item.pos.y -= MOVE_SPEED;
          case RIGHT -> item.pos.x -= MOVE_SPEED;
          case DOWN -> item.pos.y += MOVE_SPEED;
          case LEFT -> item.pos.x += MOVE_SPEED;
        }
      }
      case OUT -> {
        switch (direction) {
          case UP -> item.pos.y += MOVE_SPEED;
          case RIGHT -> item.pos.x += MOVE_SPEED;
          case DOWN -> item.pos.y -= MOVE_SPEED;
          case LEFT -> item.pos.x -= MOVE_SPEED;
        }
      }
    }

    return true;
  }

  public void transferResourceTo(Port port) {
    port.item = item;
    item = null;
  }

  public boolean isInput() {
    return type == Type.IN;
  }

  public boolean isOutput() {
    return type == Type.OUT;
  }

  public boolean hasItem() {
    return item != null;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean isItemInBounds() {
    return bounds.contains(item.pos.x, item.pos.y);
  }

  public enum Type {
    IN, OUT
  }
}
