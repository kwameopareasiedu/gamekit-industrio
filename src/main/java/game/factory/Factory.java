package game.factory;

import dev.gamekit.core.Application;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;
import game.machines.DroppableMachine;
import game.machines.Machine;
import game.machines.Orientation;
import game.machines.Producer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dev.gamekit.utils.Math.toInt;

public class Factory extends Prop {
  private static final int TICK_RATE = 2;
  private static final double INV_TICK_RATE = 1.0 / TICK_RATE;
  private static final Color GRID_COLOR = new Color(0x29c0c0c0, true);

  public final int size;
  public final int pixelSize;
  private final Stroke outlineRenderStroke;
  private final Stroke innerRenderStroke;
  private final List<DroppableMachine> machines;
  private final Machine[] grid;

  private long tickTime;

  public Factory() {
    super("Factory");
    size = Constants.GRID_SIZE;
    grid = new Machine[size * size];
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
    Renderer.setColor(GRID_COLOR);
    Renderer.setStroke(outlineRenderStroke);
    Renderer.drawRect(0, 0, pixelSize, pixelSize);

    for (int i = 1; i < size; i++) {
      int x = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(GRID_COLOR);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineV(x, toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize));
    }

    for (int i = 1; i < size; i++) {
      int y = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(GRID_COLOR);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineH(toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize), y);
    }
  }

  public boolean createMachine(Position position, Machine.Info info, Orientation orientation) {
    int index = Utils.worldPositionToIndex(position);
    int row = Utils.indexToRow(index);
    int col = Utils.indexToCol(index);
    System.out.printf("R:%d, C:%d\n", row, col);

    if (grid[index] != null)
      return false;

    DroppableMachine machine = null;

    if (info == Producer.INFO) {
      machine = new Producer(index, orientation);
    }

    if (machine != null) {
      machines.add(machine);
      grid[index] = machine;
      addChild(machine);
      System.out.printf("Machine count:%d\n", machines.size());
    }

    return true;
  }

  public boolean isMachineAtPosition(Position position) {
    int index = Utils.worldPositionToIndex(position);
    return grid[index] != null;
  }

  public void connectMachines(List<Integer> draggedIndices) {

  }
}
