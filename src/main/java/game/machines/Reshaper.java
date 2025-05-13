package game.machines;

import dev.gamekit.core.IO;
import game.factory.Factory;
import game.resources.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Reshaper extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("machines/reshaper.png");
  public static final Info INFO = new Info("Reshaper", IMAGE);

  private final ArrayList<Shape> inputShapes;

  public Reshaper(int row, int col, Factory factory, Direction direction) {
    super("Mixer", row, col, factory, direction, Port.Type.OUT, Port.Type.IN, null, Port.Type.IN);
    inputShapes = new ArrayList<>();
  }

  @Override
  protected void update() {
    super.update();
    Port out = outputs.get(0);

    int inputsWithItems = 0;

    for (Port port : inputs) {
      if (port.hasItem() && !port.isItemInBounds()) {
        inputShapes.add(port.item);
        inputsWithItems++;
      }
    }

    if (inputsWithItems == 2 && !out.hasItem()) {
      Shape combinedShape = combine(inputShapes);

      if (combinedShape != null) {
        for (Port port : inputs) {
          factory.removeItem(port.item);
          port.item = null;
        }

        factory.addItem(combinedShape);
        out.item = combinedShape;
      }
    }

    inputShapes.clear();
  }

  @Override
  public BufferedImage getImage() {
    return IMAGE;
  }

  private Shape combine(ArrayList<Shape> shapes) {
    boolean hasGrayCircle = false;
    boolean hasRedCircle = false;
    boolean hasGreenCircle = false;
    boolean hasBlueCircle = false;
    boolean hasCyanCircle = false;
    boolean hasMagentaCircle = false;
    boolean hasYellowCircle = false;

    for (Shape shape : shapes) {
      if (shape.type == Shape.Type.CIRCLE) {
        if (shape.color == Color.GRAY) hasGrayCircle = true;
        if (shape.color == Color.RED) hasRedCircle = true;
        if (shape.color == Color.GREEN) hasGreenCircle = true;
        if (shape.color == Color.BLUE) hasBlueCircle = true;
        if (shape.color == Color.CYAN) hasCyanCircle = true;
        if (shape.color == Color.MAGENTA) hasMagentaCircle = true;
        if (shape.color == Color.YELLOW) hasYellowCircle = true;
      }
    }

    if (hasGrayCircle && hasRedCircle)
      return new Shape(Shape.Type.SQUARE, Color.RED, position);
    else if (hasGrayCircle && hasGreenCircle)
      return new Shape(Shape.Type.SQUARE, Color.GREEN, position);
    else if (hasGrayCircle && hasBlueCircle)
      return new Shape(Shape.Type.SQUARE, Color.BLUE, position);
    else if (hasGrayCircle && hasCyanCircle)
      return new Shape(Shape.Type.SQUARE, Color.CYAN, position);
    else if (hasGrayCircle && hasMagentaCircle)
      return new Shape(Shape.Type.SQUARE, Color.MAGENTA, position);
    else if (hasGrayCircle && hasYellowCircle)
      return new Shape(Shape.Type.SQUARE, Color.YELLOW, position);

    return null;
  }
}
