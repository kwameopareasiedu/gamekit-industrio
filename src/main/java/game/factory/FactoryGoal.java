package game.factory;

import game.resources.Shape;

import java.awt.*;

public final class FactoryGoal {
  public final int required;
  public final Shape.Type type;
  public final Color color;

  private int count;

  public FactoryGoal(
    int required,
    Shape.Type type,
    Color color
  ) {
    this.required = required;
    this.type = type;
    this.color = color;
    this.count = 0;
  }

  public int getCount() {
    return count;
  }

  public void track(Shape shape) {
    if (count >= required)
      return;

    if (shape.type == type && shape.color == color)
      count++;
  }

  public boolean isCompleted() {
    return count == required;
  }
}
