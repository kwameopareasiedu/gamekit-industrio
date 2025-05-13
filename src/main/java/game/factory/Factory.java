package game.factory;

import dev.gamekit.core.Application;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;
import java.util.ArrayList;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.toInt;

public class Factory extends Prop {
  public static final int CELL_PIXEL_SIZE = 120;
  public static final int SOURCE_PIXEL_SIZE = toInt(0.4 * CELL_PIXEL_SIZE);
  private static final Stroke SOURCE_OUTLINE_STROKE =
    new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  private static final int TICK_INTERVAL_MS = 100;
  private static final Color GRID_COLOR = Color.LIGHT_GRAY;
  private static final Position POSITION_CACHE = new Position();

  private final int gridSize;
  private final int pixelSize;
  private final Prop machineParent;
  private final Prop itemParent;
  private final Source[] sourceGrid;
  private final Machine[] machineGrid;
  private final Hub hub;
  private final ArrayList<Machine> machines;

  private boolean active = true;
  private long tickTime;

  public Factory(int gridSize, Source[] sources, Hub.Notifier hubNotifier) {
    super("Factory");

    this.gridSize = gridSize;
    pixelSize = gridSize * CELL_PIXEL_SIZE;
    machineGrid = new Machine[gridSize * gridSize];
    sourceGrid = new Source[gridSize * gridSize];
    hub = new Hub(gridSize / 2, gridSize / 2, this, Direction.UP, hubNotifier);
    machineParent = new Prop("Machines") { };
    itemParent = new Prop("Items") { };
    machines = new ArrayList<>();

    for (Source source : sources) {
      int index = gridToIndex(source.row, source.col);
      sourceGrid[index] = source;
    }
  }

  public void createMachine(int row, int col, Machine.Info info, Direction direction) {
    int index = gridToIndex(row, col);
    Machine existing = machineGrid[index];

    if (existing == hub)
      return;

    if (existing != null) {
      if (info == Belt.INFO && !(existing instanceof Belt))
        return;

      removeMachine(row, col);
    }

    Machine machine = null;

    if (info == Extractor.INFO && sourceGrid[index] != null) {
      machine = new Extractor(row, col, this, direction, sourceGrid[index]);
    } else if (info == Belt.INFO) {
      machine = new Belt(row, col, this, direction);
    } else if (info == Mixer.INFO) {
      machine = new Mixer(row, col, this, direction);
    } else if (info == Reshaper.INFO) {
      machine = new Reshaper(row, col, this, direction);
    } else if (info == HueShifter.INFO) {
      machine = new HueShifter(row, col, this, direction);
    } else if (info == Splitter.INFO) {
      machine = new Splitter(row, col, this, direction);
    }

    if (machine != null)
      addMachine(machine);
  }

  public boolean removeMachine(int row, int col) {
    int index = gridToIndex(row, col);
    Machine machine = machineGrid[index];

    if (machine == null || machine == hub)
      return false;

    machines.remove(machine);
    machineParent.removeChild(machine);
    machineGrid[index] = null;
    return true;
  }

  public void addItem(Shape item) {
    itemParent.addChild(item);
  }

  public void removeItem(Shape item) {
    itemParent.removeChild(item);
  }

  public int getGridSize() {
    return gridSize;
  }

  public int gridToIndex(int row, int col) {
    return row * gridSize + col;
  }

  public Position gridToPosition(int row, int col) {
    int gridPixelSize = gridSize * CELL_PIXEL_SIZE;

    POSITION_CACHE.set(
      toInt((col + 0.5) * CELL_PIXEL_SIZE - 0.5 * gridPixelSize),
      toInt((row + 0.5) * CELL_PIXEL_SIZE - 0.5 * gridPixelSize)
    );

    return POSITION_CACHE;
  }

  public Position positionToGrid(Position pos) {
    int gridPixelSize = gridSize * CELL_PIXEL_SIZE;
    int row = toInt(Math.floor((0.5 * gridPixelSize + pos.y) / CELL_PIXEL_SIZE));
    int col = toInt(Math.floor((0.5 * gridPixelSize + pos.x) / CELL_PIXEL_SIZE));
    row = clamp(row, 0, gridSize - 1);
    col = clamp(col, 0, gridSize - 1);
    POSITION_CACHE.set(col, row);
    return POSITION_CACHE;
  }

  public Machine getMachineAt(int row, int col) {
    if (row < 0 || row >= gridSize || col < 0 || col >= gridSize)
      return null;

    int index = gridToIndex(row, col);
    return machineGrid[index];
  }

  public void close() {
    active = false;
  }

  @Override
  protected void start() {
    super.start();
    addChild(machineParent);
    addChild(itemParent);

    addMachine(hub);
  }

  @Override
  protected void update() {
    tickTime += Application.FRAME_TIME_MS;

    if (active && tickTime >= TICK_INTERVAL_MS) {
      tickTime = 0;
      machines.forEach(Machine::tick);
    }
  }

  @Override
  protected void render() {
    for (int i = 0; i <= gridSize; i++) {
      int x = toInt(i * CELL_PIXEL_SIZE - 0.5 * pixelSize);

      for (int j = 0; j <= gridSize; j++) {
        int y = toInt(j * CELL_PIXEL_SIZE - 0.5 * pixelSize);

        // Render grid
        Renderer.setColor(GRID_COLOR);
        Renderer.drawLineH(x - 8, x + 8, y);
        Renderer.setColor(GRID_COLOR);
        Renderer.drawLineV(x, y - 8, y + 8);

        // Render sources
        if (i < gridSize && j < gridSize) {
          int index = gridToIndex(i, j);
          Source source = sourceGrid[index];

          if (source != null) {
            Position pos = gridToPosition(source.row, source.col);
            Renderer.setColor(source.color);
            Renderer.fillRoundRect(
              pos.x, pos.y, SOURCE_PIXEL_SIZE, SOURCE_PIXEL_SIZE, 8, 8
            );

            Renderer.setColor(Color.BLACK);
            Renderer.setStroke(SOURCE_OUTLINE_STROKE);
            Renderer.drawRoundRect(
              pos.x, pos.y, SOURCE_PIXEL_SIZE, SOURCE_PIXEL_SIZE, 8, 8
            );
          }
        }
      }
    }
  }

  private void addMachine(Machine machine) {
    int index = gridToIndex(machine.row, machine.col);
    machineGrid[index] = machine;
    machineParent.addChild(machine);
    machines.add(machine);
  }
}
