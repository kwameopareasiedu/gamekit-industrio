package game.machines;

import game.components.Item;

public class Port {
  public Port out;
  public Item item;

  public Port() {
    this.out = null;
    this.item = null;
  }

  public boolean canSend() {
    return out != null && out.item == null;
  }
}
