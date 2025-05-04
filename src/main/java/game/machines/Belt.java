package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;
import game.Utils;
import game.factory.Factory;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.cycle;

public final class Belt extends Machine {
  public static final Info INFO = new Info("Belt", IO.getResourceImage("belts/ui.png"));

  private static final BufferedImage[] SPRITES = new BufferedImage[]{
    IO.getResourceImage("belts/1.png"),
    IO.getResourceImage("belts/2.png"),
    IO.getResourceImage("belts/3.png"),
    IO.getResourceImage("belts/4.png"),
    IO.getResourceImage("belts/5.png"),
    IO.getResourceImage("belts/6.png"),
    IO.getResourceImage("belts/7.png"),
  };

  private final Vector position;
  private int inputIndex = 0;

  public static Belt create(int index, Direction direction) {
    if (direction == null)
      return null;
    return new Belt(index, direction);
  }

  private Belt(int index, Direction direction) {
    super("Belt", index, direction, Port.Type.OUT, Port.Type.IN, Port.Type.IN, Port.Type.IN);
    Position pos = Utils.indexToWorldPosition(index);
    position = new Vector(pos.x, pos.y);
  }

  @Override
  public void update() {
    super.update();
    Port in = inputs.get(inputIndex);
    Port out = outputs.get(0);

    if (in.hasItem() && !in.isItemInBounds() && !out.hasItem()) {
      in.item.pos.set(position);
      in.transferResourceTo(out);
      inputIndex = cycle(inputIndex + 1, 0, inputs.size() - 1);
    }

    inputIndex = cycle(inputIndex + 1, 0, inputs.size() - 1);
  }

  @Override
  public BufferedImage getImage() {
    Machine topMachine = Factory.getMachineAt(index + Factory.GRID_SIZE);
    Machine rightMachine = Factory.getMachineAt(index + 1);
    Machine bottomMachine = Factory.getMachineAt(index - Factory.GRID_SIZE);
    Machine leftMachine = Factory.getMachineAt(index - 1);

    int spriteIndex = 0;

    switch (direction) {
      case UP -> {
        if (checkPortDirection(bottomMachine, Port.TOP, Direction.UP)) spriteIndex += 1;
        if (checkPortDirection(leftMachine, Port.RIGHT, Direction.RIGHT)) spriteIndex += 2;
        if (checkPortDirection(rightMachine, Port.LEFT, Direction.LEFT)) spriteIndex += 4;
      }
      case RIGHT -> {
        if (checkPortDirection(leftMachine, Port.RIGHT, Direction.RIGHT)) spriteIndex += 1;
        if (checkPortDirection(topMachine, Port.BOTTOM, Direction.DOWN)) spriteIndex += 2;
        if (checkPortDirection(bottomMachine, Port.TOP, Direction.UP)) spriteIndex += 4;
      }
      case DOWN -> {
        if (checkPortDirection(topMachine, Port.BOTTOM, Direction.UP)) spriteIndex += 1;
        if (checkPortDirection(rightMachine, Port.LEFT, Direction.LEFT)) spriteIndex += 2;
        if (checkPortDirection(leftMachine, Port.RIGHT, Direction.RIGHT)) spriteIndex += 4;
      }
      case LEFT -> {
        if (checkPortDirection(rightMachine, Port.LEFT, Direction.LEFT)) spriteIndex += 1;
        if (checkPortDirection(bottomMachine, Port.TOP, Direction.UP)) spriteIndex += 2;
        if (checkPortDirection(topMachine, Port.BOTTOM, Direction.DOWN)) spriteIndex += 4;
      }
    }


    return SPRITES[Math.max(0, spriteIndex - 1)];
  }

  private boolean checkPortDirection(Machine machine, int portIndex, Direction direction) {
    if (machine == null || machine.ports[portIndex] == null)
      return false;

    Port port = machine.ports[portIndex];
    return port.isOutput() && port.direction == direction;
  }
}
