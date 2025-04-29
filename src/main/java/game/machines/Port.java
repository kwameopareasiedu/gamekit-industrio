package game.machines;

import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;
import game.resources.Resource;

public class Port {
  public static double MOVE_SPEED = 0.5;

  public final Type type;
  public Resource resource;

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
    this.resource = null;

    Position pos = Utils.indexToWorldPosition(machine.index);

    bounds = switch (direction) {
      case UP -> new Bounds(
        pos.x - 0.5 * Constants.CELL_PIXEL_SIZE,
        pos.y,
        Constants.CELL_PIXEL_SIZE,
        0.5 * Constants.CELL_PIXEL_SIZE
      );
      case RIGHT -> new Bounds(
        pos.x,
        pos.y - 0.5 * Constants.CELL_PIXEL_SIZE,
        0.5 * Constants.CELL_PIXEL_SIZE,
        Constants.CELL_PIXEL_SIZE
      );
      case DOWN -> new Bounds(
        pos.x - 0.5 * Constants.CELL_PIXEL_SIZE,
        pos.y - 0.5 * Constants.CELL_PIXEL_SIZE,
        Constants.CELL_PIXEL_SIZE,
        0.5 * Constants.CELL_PIXEL_SIZE
      );
      case LEFT -> new Bounds(
        pos.x - 0.5 * Constants.CELL_PIXEL_SIZE,
        pos.y - 0.5 * Constants.CELL_PIXEL_SIZE,
        0.5 * Constants.CELL_PIXEL_SIZE,
        Constants.CELL_PIXEL_SIZE
      );
    };
  }

  public boolean moveResource() {
    if (!hasResource() || !isResourceInBounds())
      return false;

    switch (type) {
      case IN -> {
        switch (direction) {
          case UP -> resource.position.y -= MOVE_SPEED;
          case RIGHT -> resource.position.x -= MOVE_SPEED;
          case DOWN -> resource.position.y += MOVE_SPEED;
          case LEFT -> resource.position.x += MOVE_SPEED;
        }
      }
      case OUT -> {
        switch (direction) {
          case UP -> resource.position.y += MOVE_SPEED;
          case RIGHT -> resource.position.x += MOVE_SPEED;
          case DOWN -> resource.position.y -= MOVE_SPEED;
          case LEFT -> resource.position.x -= MOVE_SPEED;
        }
      }
    }

    return true;
  }

  public void transferResourceTo(Port port) {
    port.resource = resource;
    resource = null;
  }

  public boolean isInput() {
    return type == Type.IN;
  }

  public boolean isOutput() {
    return type == Type.OUT;
  }

  public boolean hasResource() {
    return resource != null;
  }

  public boolean isResourceInBounds() {
    return bounds.contains(resource.position.x, resource.position.y);
  }

  public enum Type {
    IN, OUT
  }
}
