package game.machines;

import game.items.Item;

public class Port {
  public final Type type;
  public Port linked;
  public Item item;

  public Port(Type type) {
    this.type = type;
    this.linked = null;
    this.item = null;
  }

  public enum Type {
    IN, OUT
  }
}
