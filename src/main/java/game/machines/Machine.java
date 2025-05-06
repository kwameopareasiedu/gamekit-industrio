package game.machines;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Utils;
import game.factory.Factory;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine extends Prop {
  public final int index;
  protected final Direction direction;
  protected final Port topPort;
  protected final Port rightPort;
  protected final Port bottomPort;
  protected final Port leftPort;
  protected final Port[] ports;
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

    this.topPort = switch (direction) {
      case UP -> Port.create(topPortType, Direction.UP, this);
      case RIGHT -> Port.create(leftPortType, Direction.UP, this);
      case DOWN -> Port.create(bottomPortType, Direction.UP, this);
      case LEFT -> Port.create(rightPortType, Direction.UP, this);
    };

    this.rightPort = switch (direction) {
      case UP -> Port.create(rightPortType, Direction.RIGHT, this);
      case RIGHT -> Port.create(topPortType, Direction.RIGHT, this);
      case DOWN -> Port.create(leftPortType, Direction.RIGHT, this);
      case LEFT -> Port.create(bottomPortType, Direction.RIGHT, this);
    };

    this.bottomPort = switch (direction) {
      case UP -> Port.create(bottomPortType, Direction.DOWN, this);
      case RIGHT -> Port.create(rightPortType, Direction.DOWN, this);
      case DOWN -> Port.create(topPortType, Direction.DOWN, this);
      case LEFT -> Port.create(leftPortType, Direction.DOWN, this);
    };

    this.leftPort = switch (direction) {
      case UP -> Port.create(leftPortType, Direction.LEFT, this);
      case RIGHT -> Port.create(bottomPortType, Direction.LEFT, this);
      case DOWN -> Port.create(rightPortType, Direction.LEFT, this);
      case LEFT -> Port.create(topPortType, Direction.LEFT, this);
    };

    ports = new Port[]{ topPort, rightPort, bottomPort, leftPort };
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
    updatePort(topPort);
    updatePort(rightPort);
    updatePort(bottomPort);
    updatePort(leftPort);
  }

  public abstract BufferedImage getImage();

  public void tick() { }

  @Override
  protected void render() {
    BufferedImage icon = getImage();
    Position pos = Utils.indexToWorldPosition(index);

    Renderer.withRotation(
      pos.x, pos.y, direction.getAngle(),
      () -> Renderer.drawImage(
        icon, pos.x, pos.y,
        Factory.CELL_PIXEL_SIZE,
        Factory.CELL_PIXEL_SIZE
      )
    );
  }

  @Override
  protected void dispose() {
    for (Port port : inputs) {
      if (port.item != null)
        Factory.removeItem(port.item);
    }

    for (Port port : outputs) {
      if (port.item != null)
        Factory.removeItem(port.item);
    }
  }

  private void updatePort(Port port) {
    if (port != null && port.hasItem()) {
      if (port.isOutput() && !port.moveResource()) {
        //noinspection ExtractMethodRecommender
        int adjacentMachineOffset =
          port == topPort ? Factory.GRID_SIZE
            : port == rightPort ? 1
            : port == bottomPort ? -Factory.GRID_SIZE
            : port == leftPort ? -1
            : 0;

        int adjacentIndex = index + adjacentMachineOffset;
        // TODO: If this machine is at an edge or corner,
        //  naively adding adjacentMachineOffset will cause it
        //  to grab the machine at first or last column of the
        //  lower or upper row of the grid, which is wrong.
        //  Fix this later
        Machine adjacentMachine = Factory.getMachineAt(adjacentIndex);

        if (adjacentMachine != null) {
          Port adjacentPort = port == topPort ? adjacentMachine.bottomPort
            : port == rightPort ? adjacentMachine.leftPort
            : port == bottomPort ? adjacentMachine.topPort
            : port == leftPort ? adjacentMachine.rightPort
            : null;

          if (adjacentPort != null && adjacentPort.isInput() && !adjacentPort.hasItem())
            port.transferResourceTo(adjacentPort);
        }
      } else if (port.isInput()) {
        port.moveResource();
      }
    }
  }

  public record Info(String name, BufferedImage image) { }
}
