package game.components;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;

import java.awt.*;

public abstract class Machine extends Prop {
  private final Position position;
  private final Machine[] in;
  private final Machine[] out;

  public Machine(
    String name,
    Position position,
    int inCount,
    int outCount
  ) {
    super(name);
    this.position = new Position(position);
    this.in = new Machine[inCount];
    this.out = new Machine[outCount];
  }

  @Override
  protected void onRender() {
    super.onRender();
    Renderer.setColor(Color.DARK_GRAY);
    Renderer.fillRect(
      position.x, position.y,
      Constants.CELL_PX_SIZE,
      Constants.CELL_PX_SIZE
    );
  }
}
