package game.machines;

import game.components.Cargo;

public class Port {
  public final Type type;
  public Port linked;
  public Cargo cargo;

  public Port(Type type) {
    this.type = type;
    this.linked = null;
    this.cargo = null;
  }

  public enum Type {
    IN, OUT
  }
}
