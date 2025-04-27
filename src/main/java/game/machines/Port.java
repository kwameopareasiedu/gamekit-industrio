package game.machines;

import game.resources.Resource;

public class Port {
  public final Type type;
  public Port linked;
  public Resource payload;

  public Port(Type type) {
    this.type = type;
    this.linked = null;
    this.payload = null;
  }

  public enum Type {
    IN, OUT
  }
}
