package game.machines;

import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Position;
import game.Utils;
import game.factory.Factory;
import game.resources.Shade;

public class Port {
  public static double MOVE_SPEED = 0.75;

  public final Type type;
  public Shade item;

  private final Direction direction;
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

    Position pos = Utils.indexToWorldPosition(machine.index);

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
    if (!hasResource() || !isResourceInBounds())
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

  public boolean hasResource() {
    return item != null;
  }

  public boolean isResourceInBounds() {
    return bounds.contains(item.pos.x, item.pos.y);
  }

  public enum Type {
    IN, OUT
  }
}
