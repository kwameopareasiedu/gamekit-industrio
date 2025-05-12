package game.machines;

import dev.gamekit.core.IO;
import game.factory.Factory;
import game.resources.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Mixer extends Machine {
  private static final BufferedImage SPRITE = IO.getResourceImage("mixer.png");

  public static final Info INFO = new Info("Mixer", SPRITE);

  private final ArrayList<Shape> inputShapes;

  public Mixer(int row, int col, Factory factory, Direction direction) {
    super(
      "Mixer", row, col, factory, direction,
      Port.Type.OUT, Port.Type.IN, Port.Type.IN, Port.Type.IN
    );
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

    if (inputsWithItems >= 2 && !out.hasItem()) {
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
    return SPRITE;
  }

  private Shape combine(ArrayList<Shape> shapes) {
    boolean hasWhiteCircle = false;
    boolean hasBlackCircle = false;
    boolean hasRedCircle = false;
    boolean hasGreenCircle = false;
    boolean hasBlueCircle = false;

    boolean hasWhiteSquare = false;
    boolean hasBlackSquare = false;
    boolean hasRedSquare = false;
    boolean hasGreenSquare = false;
    boolean hasBlueSquare = false;
    boolean hasCyanSquare = false;
    boolean hasMagentaSquare = false;
    boolean hasYellowSquare = false;

    for (Shape shape : shapes) {
      if (shape.type == Shape.Type.CIRCLE) {
        if (shape.color == Color.WHITE) hasWhiteCircle = true;
        if (shape.color == Color.BLACK) hasBlackCircle = true;
        if (shape.color == Color.RED) hasRedCircle = true;
        if (shape.color == Color.GREEN) hasGreenCircle = true;
        if (shape.color == Color.BLUE) hasBlueCircle = true;
//        if (shape.color == Color.CYAN) hasCyanCircle = true;
//        if (shape.color == Color.MAGENTA) hasMagentaCircle = true;
//        if (shape.color == Color.YELLOW) hasYellowCircle = true;
      } else if (shape.type == Shape.Type.SQUARE) {
        if (shape.color == Color.WHITE) hasWhiteSquare = true;
        if (shape.color == Color.BLACK) hasBlackSquare = true;
        if (shape.color == Color.RED) hasRedSquare = true;
        if (shape.color == Color.GREEN) hasGreenSquare = true;
        if (shape.color == Color.BLUE) hasBlueSquare = true;
        if (shape.color == Color.CYAN) hasCyanSquare = true;
        if (shape.color == Color.MAGENTA) hasMagentaSquare = true;
        if (shape.color == Color.YELLOW) hasYellowSquare = true;
      }
    }

    if (hasWhiteCircle && hasBlackCircle)
      return new Shape(Shape.Type.CIRCLE, Color.GRAY, position);
    else if (hasWhiteCircle && hasRedCircle && hasGreenCircle)
      return new Shape(Shape.Type.CIRCLE, Color.YELLOW, position);
    else if (hasWhiteCircle && hasRedCircle && hasBlueCircle)
      return new Shape(Shape.Type.CIRCLE, Color.MAGENTA, position);
    else if (hasWhiteCircle && hasGreenCircle && hasBlueCircle)
      return new Shape(Shape.Type.CIRCLE, Color.CYAN, position);
    else if (hasCyanSquare && hasMagentaSquare && hasYellowSquare)
      return new Shape(Shape.Type.SQUARE, Color.BLACK, position);
    else if (hasRedSquare && hasGreenSquare && hasBlueSquare)
      return new Shape(Shape.Type.SQUARE, Color.WHITE, position);
    else if (hasWhiteSquare && hasBlackSquare)
      return new Shape(Shape.Type.SQUARE, Color.GRAY, position);

    return null;
  }
}
