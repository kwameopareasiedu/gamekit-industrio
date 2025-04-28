package game.machines;

import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Position;
import game.Constants;
import game.resources.Resource;

public class Port {
  public final Type type;
  public final Direction direction;
  public Resource item;

  public final Bounds bounds;

  public static Port create(Type type, Direction direction, Position machinePos) {
    if (type == null || direction == null || machinePos == null)
      return null;

    return new Port(type, direction, machinePos);
  }

  private Port(Type type, Direction direction, Position machinePos) {
    this.type = type;
    this.direction = direction;
    this.item = null;

    bounds = switch (direction) {
      case UP -> new Bounds(
        (int) (machinePos.x - 0.5 * Constants.CELL_PIXEL_SIZE),
        machinePos.y,
        Constants.CELL_PIXEL_SIZE,
        (int) (0.5 * Constants.CELL_PIXEL_SIZE)
      );
      case RIGHT -> new Bounds(
        machinePos.x,
        (int) (machinePos.y + 0.5 * Constants.CELL_PIXEL_SIZE),
        (int) (0.5 * Constants.CELL_PIXEL_SIZE),
        Constants.CELL_PIXEL_SIZE
      );
      case DOWN -> new Bounds(
        (int) (machinePos.x - 0.5 * Constants.CELL_PIXEL_SIZE),
        (int) (machinePos.y - 0.5 * Constants.CELL_PIXEL_SIZE),
        Constants.CELL_PIXEL_SIZE,
        (int) (0.5 * Constants.CELL_PIXEL_SIZE)
      );
      case LEFT -> new Bounds(
        (int) (machinePos.x - 0.5 * Constants.CELL_PIXEL_SIZE),
        (int) (machinePos.y + 0.5 * Constants.CELL_PIXEL_SIZE),
        (int) (0.5 * Constants.CELL_PIXEL_SIZE),
        Constants.CELL_PIXEL_SIZE
      );
    };
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean moveItem() {
    if (bounds.contains(item.position.x, item.position.y)) {
      switch (type) {
        case IN -> {
          switch (direction) {
            case UP -> item.position.y -= Machine.RESOURCE_MOVE_SPEED;
            case RIGHT -> item.position.x -= Machine.RESOURCE_MOVE_SPEED;
            case DOWN -> item.position.y += Machine.RESOURCE_MOVE_SPEED;
            case LEFT -> item.position.x += Machine.RESOURCE_MOVE_SPEED;
          }
        }
        case OUT -> {
          switch (direction) {
            case UP -> item.position.y += Machine.RESOURCE_MOVE_SPEED;
            case RIGHT -> item.position.x += Machine.RESOURCE_MOVE_SPEED;
            case DOWN -> item.position.y -= Machine.RESOURCE_MOVE_SPEED;
            case LEFT -> item.position.x -= Machine.RESOURCE_MOVE_SPEED;
          }
        }
      }

      return true;
    }

    return false;
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

  public enum Type {
    IN, OUT
  }
}
