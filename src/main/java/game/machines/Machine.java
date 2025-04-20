package game.machines;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine extends Prop {
  public final int row;
  public final int col;
  public final Orientation orientation;
  protected final Port[] inputs;
  protected final Port[] outputs;
  protected final List<Port> freeOutputs;

  public Machine(
    String name, int row, int col,
    Orientation orientation,
    Port[] inputs, Port[] outputs
  ) {
    super(name);
    this.row = row;
    this.col = col;
    this.inputs = inputs;
    this.outputs = outputs;
    this.orientation = orientation;
    this.freeOutputs = new ArrayList<>();
  }

  public final void process() {
    freeOutputs.clear();

    for (Port port : outputs) {
      if (port.item == null)
        freeOutputs.add(port);
    }

    if (!freeOutputs.isEmpty())
      performProcess();
  }

  protected abstract void performProcess();

  public final void output() {
    for (Port port : outputs) {
      if (port.canSend()) {
        port.out.item = port.item;
        port.item = null;
      }
    }
  }

  public abstract BufferedImage getImage();

  @Override
  protected void render() {
    BufferedImage icon = getImage();
    Position pos = Utils.indexToPosition(row, col);

    Renderer.withRotation(
      pos.x, pos.y, orientation.toDeg(),
      () -> Renderer.drawImage(icon, pos.x, pos.y, Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE)
    );
  }

  public record Info(String name, BufferedImage icon) { }
}
