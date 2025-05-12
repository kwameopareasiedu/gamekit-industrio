package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Vector;
import game.factory.Factory;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.cycle;

public final class Belt extends Machine {
  public static final Info INFO =
    new Info("Belt", IO.getResourceImage("belts.png", 192, 384, 192, 192));

  private static final BufferedImage[] SPRITES = new BufferedImage[]{
    IO.getResourceImage("belts.png", 0, 0, 192, 192),
    IO.getResourceImage("belts.png", 384, 0, 192, 192),
    IO.getResourceImage("belts.png", 192, 192, 192, 192),
    IO.getResourceImage("belts.png", 192, 0, 192, 192),
    IO.getResourceImage("belts.png", 384, 192, 192, 192),
    IO.getResourceImage("belts.png", 0, 192, 192, 192),
    IO.getResourceImage("belts.png", 0, 384, 192, 192),
  };

  private final Vector position;
  private int inputIndex = 0;

  public Belt(int row, int col, Factory factory, Direction direction) {
    super(
      "Belt", row, col, factory, direction,
      Port.Type.OUT, Port.Type.IN, Port.Type.IN, Port.Type.IN
    );
    position = new Vector(super.position.x, super.position.y);
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
    Machine topMachine = factory.getMachineAt(row + 1, col);
    Machine rightMachine = factory.getMachineAt(row, col + 1);
    Machine bottomMachine = factory.getMachineAt(row - 1, col);
    Machine leftMachine = factory.getMachineAt(row, col - 1);

    int spriteIndex = 0;

    switch (direction) {
      case UP -> {
        if (bottomMachine != null && bottomMachine.portHasDirection(Port.TOP, Direction.UP))
          spriteIndex += 1;
        if (leftMachine != null && leftMachine.portHasDirection(Port.RIGHT, Direction.RIGHT))
          spriteIndex += 2;
        if (rightMachine != null && rightMachine.portHasDirection(Port.LEFT, Direction.LEFT))
          spriteIndex += 4;
      }
      case RIGHT -> {
        if (leftMachine != null && leftMachine.portHasDirection(Port.RIGHT, Direction.RIGHT))
          spriteIndex += 1;
        if (topMachine != null && topMachine.portHasDirection(Port.BOTTOM, Direction.DOWN))
          spriteIndex += 2;
        if (bottomMachine != null && bottomMachine.portHasDirection(Port.TOP, Direction.UP))
          spriteIndex += 4;
      }
      case DOWN -> {
        if (topMachine != null && topMachine.portHasDirection(Port.BOTTOM, Direction.UP))
          spriteIndex += 1;
        if (rightMachine != null && rightMachine.portHasDirection(Port.LEFT, Direction.LEFT))
          spriteIndex += 2;
        if (leftMachine != null && leftMachine.portHasDirection(Port.RIGHT, Direction.RIGHT))
          spriteIndex += 4;
      }
      case LEFT -> {
        if (rightMachine != null && rightMachine.portHasDirection(Port.LEFT, Direction.LEFT))
          spriteIndex += 1;
        if (bottomMachine != null && bottomMachine.portHasDirection(Port.TOP, Direction.UP))
          spriteIndex += 2;
        if (topMachine != null && topMachine.portHasDirection(Port.BOTTOM, Direction.DOWN))
          spriteIndex += 4;
      }
    }

    return SPRITES[Math.max(0, spriteIndex - 1)];
  }
}
