package game.machines;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;
import game.world.World;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine extends Prop {
  public static double RESOURCE_MOVE_SPEED = 0.5;

  public final int index;
  public final Direction direction;
  public final Port topPort;
  public final Port rightPort;
  public final Port bottomPort;
  public final Port leftPort;
  protected final List<Port> inputs;
  protected final List<Port> outputs;

  public Machine(
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


//    this.topBounds = new Bounds(
//      (int) (pos.x - 0.5 * Constants.CELL_PIXEL_SIZE),
//      pos.y,
//      Constants.CELL_PIXEL_SIZE,
//      (int) (0.5 * Constants.CELL_PIXEL_SIZE)
//    );
//
//    this.rightBounds = new Bounds(
//      pos.x,
//      (int) (pos.y + 0.5 * Constants.CELL_PIXEL_SIZE),
//      (int) (0.5 * Constants.CELL_PIXEL_SIZE),
//      Constants.CELL_PIXEL_SIZE
//    );
//
//    this.bottomBounds = new Bounds(
//      (int) (pos.x - 0.5 * Constants.CELL_PIXEL_SIZE),
//      (int) (pos.y - 0.5 * Constants.CELL_PIXEL_SIZE),
//      Constants.CELL_PIXEL_SIZE,
//      (int) (0.5 * Constants.CELL_PIXEL_SIZE)
//    );
//
//    this.leftBounds = new Bounds(
//      (int) (pos.x - 0.5 * Constants.CELL_PIXEL_SIZE),
//      (int) (pos.y + 0.5 * Constants.CELL_PIXEL_SIZE),
//      (int) (0.5 * Constants.CELL_PIXEL_SIZE),
//      Constants.CELL_PIXEL_SIZE
//    );

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
          Machine topMachine = World.getMachine(topIndex);

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


//    position.x += switch (direction) {
//      case RIGHT -> MOVE_SPEED;
//      case LEFT -> -MOVE_SPEED;
//      default -> 0;
//    };
//
//    position.y += switch (direction) {
//      case UP -> MOVE_SPEED;
//      case DOWN -> -MOVE_SPEED;
//      default -> 0;
//    };
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
}
