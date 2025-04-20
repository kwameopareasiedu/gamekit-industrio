package game.components;

public class Port {
  public Port linked;
  public Item item;

  public Port() {
    this.linked = null;
    this.item = null;
  }

  public boolean canSend() {
    return linked != null && linked.item == null;
  }
}
