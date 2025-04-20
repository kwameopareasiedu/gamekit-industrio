package game.components;

import dev.gamekit.core.*;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dev.gamekit.utils.Math.toInt;

public class Factory extends Prop {
  private static final int TICK_RATE = 2;
  private static final double INV_TICK_RATE = 1.0 / TICK_RATE;

  public final int size;
  public final int pixelSize;
  private final Stroke outlineRenderStroke;
  private final Stroke innerRenderStroke;
  private final List<Machine> machines;

  private long tickTime;

  public Factory() {
    super("Factory");
    size = Constants.GRID_SIZE;
    pixelSize = size * Constants.CELL_PIXEL_SIZE;
    outlineRenderStroke = new BasicStroke(
      2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
      0, new float[]{ 10 }, 5
    );
    innerRenderStroke = new BasicStroke(
      2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
      0, new float[]{ 10 }, 5
    );
    machines = new ArrayList<>();
  }

  @Override
  protected void update() {
    tickTime += Application.FRAME_TIME;

    if (tickTime >= INV_TICK_RATE) {
      tickTime = 0;
      machines.forEach(Machine::output);
      machines.forEach(Machine::process);
    }
  }

  @Override
  protected void render() {
    Renderer.setColor(Color.LIGHT_GRAY);
    Renderer.setStroke(outlineRenderStroke);
    Renderer.drawRect(0, 0, pixelSize, pixelSize);

    for (int i = 1; i < size; i++) {
      int x = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(Color.LIGHT_GRAY);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineV(x, toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize));
    }

    for (int i = 1; i < size; i++) {
      int y = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(Color.LIGHT_GRAY);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineH(toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize), y);
    }
  }

  public void createMachine(Machine.Info info) {
    Position pos = Input.getMousePosition();
    Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
    Position indexPos = Utils.positionToIndex(worldPos);
    System.out.printf("R:%d, C:%d\n", indexPos.y, indexPos.x);

    Machine machine = null;

    if (info == Conveyor.INFO) {
      machine = new Conveyor(indexPos.y, indexPos.x);
    } else if (info == Producer.INFO) {
      machine = new Producer(indexPos.y, indexPos.x);
    }

    if (machine != null) {
      machines.add(machine);
      addChild(machine);
    }
  }
}
