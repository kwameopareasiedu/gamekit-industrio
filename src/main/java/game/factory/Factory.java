package game.factory;

import dev.gamekit.core.Application;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Utils;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;

import java.awt.*;
import java.util.ArrayList;

import static dev.gamekit.utils.Math.toInt;

public class Factory extends Prop {
  public static int GRID_SIZE = 11;
  public static final int CELL_PIXEL_SIZE = 60;
  private static final int TICK_INTERVAL_MS = 100;
  private static final Color GRID_COLOR = new Color(0x2f000000, true);
  private static final Stroke OUTER_GRID_STROKE = new BasicStroke(
    1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{ 10 }, 5
  );
  private static final Stroke INNER_GRID_STROKE = new BasicStroke(
    1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{ 10 }, 5
  );
  private static Factory instance;

  public final int pixelSize;
  private final Prop machineContainer;
  private final Prop sourceContainer;
  private final Prop itemContainer;
  private final Source[] sourceGrid;
  private final Machine[] machineGrid;
  private final Source[] initialSources;
  private final Hub hub;
  private final ArrayList<Machine> machines;

  private long tickTime;

  public Factory(Source[] initialSources) {
    super("Factory");

    this.initialSources = initialSources;
    pixelSize = GRID_SIZE * CELL_PIXEL_SIZE;
    machineGrid = new Machine[GRID_SIZE * GRID_SIZE];
    sourceGrid = new Source[GRID_SIZE * GRID_SIZE];
    hub = Hub.create((GRID_SIZE * GRID_SIZE) / 2, Direction.UP, (resource) -> {
      Factory.removeItem(resource);
      logger.debug("Consumed {}", resource.type);
    });
    machineContainer = new Prop("Machines") { };
    sourceContainer = new Prop("Sources") { };
    itemContainer = new Prop("Items") { };
    machines = new ArrayList<>();
    Factory.instance = this;
  }

  public static void addResource(Shape item) {
    instance.itemContainer.addChild(item);
  }

  public static void removeItem(Shape item) {
    instance.itemContainer.removeChild(item);
  }

  public static Machine getMachineAt(int index) {
    if (index < 0 || index >= instance.machineGrid.length)
      return null;

    return instance.machineGrid[index];
  }

  @Override
  protected void start() {
    super.start();
    addChild(sourceContainer);
    addChild(machineContainer);
    addChild(itemContainer);

    addMachine(hub);

    for (Source source : initialSources)
      addSource(source);
  }

  @Override
  protected void update() {
    tickTime += Application.FRAME_TIME_MS;

    if (tickTime >= TICK_INTERVAL_MS) {
      tickTime = 0;
      machines.forEach(Machine::tick);
    }
  }

  @Override
  protected void render() {
    Renderer.setColor(GRID_COLOR);
    Renderer.setStroke(OUTER_GRID_STROKE);
    Renderer.drawRect(0, 0, pixelSize, pixelSize);

    for (int i = 1; i < GRID_SIZE; i++) {
      int x = toInt(i * CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(GRID_COLOR);
      Renderer.setStroke(INNER_GRID_STROKE);
      Renderer.drawLineV(x, toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize));
    }

    for (int i = 1; i < GRID_SIZE; i++) {
      int y = toInt(i * CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(GRID_COLOR);
      Renderer.setStroke(INNER_GRID_STROKE);
      Renderer.drawLineH(toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize), y);
    }
  }

  public void createMachine(Position position, Machine.Info info, Direction direction) {
    int index = Utils.worldPositionToIndex(position);

    if (machineGrid[index] == hub)
      return;

    if (machineGrid[index] != null)
      removeMachine(position);

    Machine machine = null;

    if (info == Extractor.INFO) {
      machine = Extractor.create(index, direction, sourceGrid[index]);
    } else if (info == Belt.INFO) {
      machine = Belt.create(index, direction);
    } else if (info == Mixer.INFO) {
      machine = Mixer.create(index, direction);
    } else if (info == Reshaper.INFO) {
      machine = Reshaper.create(index, direction);
    }

    if (machine != null)
      addMachine(machine);
  }

  public void removeMachine(Position position) {
    int index = Utils.worldPositionToIndex(position);
    Machine machine = machineGrid[index];

    if (machine == null || machine == hub)
      return;

    machines.remove(machine);
    machineContainer.removeChild(machine);
    machineGrid[index] = null;
  }

  private void addSource(Source source) {
    sourceGrid[source.index] = source;
    sourceContainer.addChild(source);
  }

  private void addMachine(Machine machine) {
    machineGrid[machine.index] = machine;
    machineContainer.addChild(machine);
    machines.add(machine);
  }
}
