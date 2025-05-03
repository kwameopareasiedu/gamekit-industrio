package game.machines;

import dev.gamekit.core.IO;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Vector;
import game.Utils;
import game.factory.Factory;

import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.cycle;

public final class Belt extends Machine {
  private static final BufferedImage[] SPRITES = new BufferedImage[]{
    IO.getResourceImage("belts/1.png"),
    IO.getResourceImage("belts/2.png"),
    IO.getResourceImage("belts/3.png"),
    IO.getResourceImage("belts/4.png"),
    IO.getResourceImage("belts/5.png"),
    IO.getResourceImage("belts/6.png"),
    IO.getResourceImage("belts/7.png"),
  };

  public static final Info INFO = new Info("Belt", SPRITES[0]);

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

    if (in.hasResource() && !in.isResourceInBounds() && !out.hasResource()) {
      in.item.pos.set(position);
      in.transferResourceTo(out);
    }

    inputIndex = cycle(inputIndex + 1, 0, inputs.size() - 1);
  }

  @Override
  public BufferedImage getImage() {
    Machine topMachine = Factory.getMachineAt(index + Factory.GRID_SIZE);
    Machine rightMachine = Factory.getMachineAt(index + 1);
    Machine bottomMachine = Factory.getMachineAt(index - Factory.GRID_SIZE);
    Machine leftMachine = Factory.getMachineAt(index - 1);

    Belt topBelt = topMachine instanceof Belt ? (Belt) topMachine : null;
    Belt rightBelt = rightMachine instanceof Belt ? (Belt) rightMachine : null;
    Belt bottomBelt = bottomMachine instanceof Belt ? (Belt) bottomMachine : null;
    Belt leftBelt = leftMachine instanceof Belt ? (Belt) leftMachine : null;

    return switch (direction) {
      case UP -> {
        int spriteIndex = 0;
        if (bottomBelt != null && bottomBelt.direction == Direction.UP) spriteIndex += 1;
        if (leftBelt != null && leftBelt.direction == Direction.RIGHT) spriteIndex += 2;
        if (rightBelt != null && rightBelt.direction == Direction.LEFT) spriteIndex += 4;
        yield SPRITES[Math.max(0, spriteIndex - 1)];
      }
      case RIGHT -> {
        int spriteIndex = 0;
        if (leftBelt != null && leftBelt.direction == Direction.RIGHT) spriteIndex += 1;
        if (topBelt != null && topBelt.direction == Direction.DOWN) spriteIndex += 2;
        if (bottomBelt != null && bottomBelt.direction == Direction.UP) spriteIndex += 4;
        yield SPRITES[Math.max(0, spriteIndex - 1)];
      }
      case DOWN -> {
        int spriteIndex = 0;
        if (topBelt != null && topBelt.direction == Direction.DOWN) spriteIndex += 1;
        if (rightBelt != null && rightBelt.direction == Direction.LEFT) spriteIndex += 2;
        if (leftBelt != null && leftBelt.direction == Direction.RIGHT) spriteIndex += 4;
        yield SPRITES[Math.max(0, spriteIndex - 1)];
      }
      case LEFT -> {
        int spriteIndex = 0;
        if (rightBelt != null && rightBelt.direction == Direction.LEFT) spriteIndex += 1;
        if (bottomBelt != null && bottomBelt.direction == Direction.UP) spriteIndex += 2;
        if (topBelt != null && topBelt.direction == Direction.DOWN) spriteIndex += 4;
        yield SPRITES[Math.max(0, spriteIndex - 1)];
      }
    };
  }
}
