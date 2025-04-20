package game.components;

import dev.gamekit.core.Prop;

public abstract class Machine extends Prop {
  public final int row;
  public final int col;
  protected final Port[] inPorts;
  protected final Port[] outPorts;

  public Machine(
    String name, int row, int col,
    Port[] inPorts, Port[] outPorts
  ) {
    super(name);
    this.row = row;
    this.col = col;
    this.inPorts = inPorts;
    this.outPorts = outPorts;
  }

  public abstract void input();

  public abstract void process();

  public abstract void output();
}
