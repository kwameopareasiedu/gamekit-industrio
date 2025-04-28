package game.machines;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Bounds;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;
import game.factory.Factory;
import game.resources.Resource;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine extends Prop {
  public static double RESOURCE_MOVE_SPEED = 0.5;

  public final int index;
  protected final Direction direction;
  protected final Port topPort;
  protected final Port rightPort;
  protected final Port bottomPort;
  protected final Port leftPort;
  protected final List<Port> inputs;
  protected final List<Port> outputs;

  protected Machine(
    String name,
    int index,
    Direction direction,
    Port.Type topPortType,
    Port.Type rightPortType,
    Port.Type bottomPortType,
    Port.Type leftPortType
  ) {
    super(name);
    this.index = index;
    this.direction = direction;

    Position pos = Utils.indexToWorldPosition(index);

    this.topPort = switch (direction) {
      case UP -> Port.create(topPortType, Direction.UP, pos);
      case RIGHT -> Port.create(leftPortType, Direction.UP, pos);
      case DOWN -> Port.create(bottomPortType, Direction.UP, pos);
      case LEFT -> Port.create(rightPortType, Direction.UP, pos);
    };

    this.rightPort = switch (direction) {
      case UP -> Port.create(rightPortType, Direction.RIGHT, pos);
      case RIGHT -> Port.create(topPortType, Direction.RIGHT, pos);
      case DOWN -> Port.create(leftPortType, Direction.RIGHT, pos);
      case LEFT -> Port.create(bottomPortType, Direction.RIGHT, pos);
    };

    this.bottomPort = switch (direction) {
      case UP -> Port.create(bottomPortType, Direction.DOWN, pos);
      case RIGHT -> Port.create(rightPortType, Direction.DOWN, pos);
      case DOWN -> Port.create(topPortType, Direction.DOWN, pos);
      case LEFT -> Port.create(leftPortType, Direction.DOWN, pos);
    };

    this.leftPort = switch (direction) {
      case UP -> Port.create(leftPortType, Direction.LEFT, pos);
      case RIGHT -> Port.create(bottomPortType, Direction.LEFT, pos);
      case DOWN -> Port.create(rightPortType, Direction.LEFT, pos);
      case LEFT -> Port.create(topPortType, Direction.LEFT, pos);
    };

    inputs = new ArrayList<>();
    outputs = new ArrayList<>();

    List<Port> tempList = new ArrayList<>();
    tempList.add(this.topPort);
    tempList.add(this.rightPort);
    tempList.add(this.bottomPort);
    tempList.add(this.leftPort);

    for (Port port : tempList) {
      if (port == null) continue;

      switch (port.type) {
        case IN -> inputs.add(port);
        case OUT -> outputs.add(port);
      }
    }
  }

  @Override
  protected void update() {
    if (topPort != null && topPort.hasItem()) {
      if (topPort.isOutput()) {
        if (!topPort.moveItem()) {
          int topIndex = index + Constants.GRID_SIZE;
          Machine topMachine = Factory.getMachine(topIndex);

          if (topMachine != null &&
            topMachine.bottomPort != null &&
            topMachine.bottomPort.isInput() &&
            !topMachine.bottomPort.hasItem()) {
            topMachine.bottomPort.item = topPort.item;
            topPort.item = null;
          }
        }
      }
    }

    if (rightPort != null) {

    }

    if (bottomPort != null && bottomPort.hasItem()) {
      if (bottomPort.type == Port.Type.IN) {
        if (!bottomPort.moveItem()) {
          if (topPort != null && topPort.item == null) {
            topPort.item = bottomPort.item;
            bottomPort.item = null;
          }
        }
      }
    }

    if (leftPort != null) {

    }
  }

  public abstract BufferedImage getImage();

  @Override
  protected void render() {
    BufferedImage icon = getImage();
    Position pos = Utils.indexToWorldPosition(index);

    Renderer.withRotation(
      pos.x, pos.y, direction.getAngle(),
      () -> Renderer.drawImage(
        icon, pos.x, pos.y,
        Constants.CELL_PIXEL_SIZE,
        Constants.CELL_PIXEL_SIZE
      )
    );
  }

  public record Info(String name, BufferedImage image) { }

  protected static class Port {
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
              case UP -> item.position.y -= RESOURCE_MOVE_SPEED;
              case RIGHT -> item.position.x -= RESOURCE_MOVE_SPEED;
              case DOWN -> item.position.y += RESOURCE_MOVE_SPEED;
              case LEFT -> item.position.x += RESOURCE_MOVE_SPEED;
            }
          }
          case OUT -> {
            switch (direction) {
              case UP -> item.position.y += RESOURCE_MOVE_SPEED;
              case RIGHT -> item.position.x += RESOURCE_MOVE_SPEED;
              case DOWN -> item.position.y -= RESOURCE_MOVE_SPEED;
              case LEFT -> item.position.x -= RESOURCE_MOVE_SPEED;
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
}
