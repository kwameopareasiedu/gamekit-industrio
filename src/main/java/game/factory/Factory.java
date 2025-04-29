package game.factory;

import dev.gamekit.core.Application;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;
import game.machines.*;
import game.resources.Deposit;
import game.resources.Resource;

import java.awt.*;
import java.util.ArrayList;

import static dev.gamekit.utils.Math.toInt;

public class Factory extends Prop {
  private static final int TICK_INTERVAL = 100;
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
  private final Prop depositContainer;
  private final Prop resourceContainer;
  private final Deposit[] depositGrid;
  private final Machine[] machineGrid;
  private final Hub hub;
  private final ArrayList<Machine> machines;

  private long tickTime;

  public Factory() {
    super("Factory");
    pixelSize = Constants.GRID_SIZE * Constants.CELL_PIXEL_SIZE;
    machineGrid = new Machine[Constants.GRID_SIZE * Constants.GRID_SIZE];
    depositGrid = new Deposit[Constants.GRID_SIZE * Constants.GRID_SIZE];
    hub = Hub.create((Constants.GRID_SIZE * Constants.GRID_SIZE) / 2, Direction.UP, (resource) -> {
      instance.resourceContainer.removeChild(resource);
      logger.debug("Consumed {}", resource.type);
    });
    machineContainer = new Prop("Machines") { };
    depositContainer = new Prop("Deposits") { };
    resourceContainer = new Prop("Resources") { };
    machines = new ArrayList<>();
    Factory.instance = this;
  }

  public static void addResource(Resource item) {
    instance.resourceContainer.addChild(item);
  }

  public static void removeResource(Resource item) {
    instance.resourceContainer.removeChild(item);
  }

  public static Machine getMachineAt(int index) {
    return instance.machineGrid[index];
  }

  @Override
  protected void start() {
    super.start();
    addChild(depositContainer);
    addChild(machineContainer);
    addChild(resourceContainer);

    addMachine(hub);
    addDeposit(Deposit.create(Resource.Type.ROCK, 10, 10));
    addDeposit(Deposit.create(Resource.Type.ROCK, 11, 10));
    addDeposit(Deposit.create(Resource.Type.ROCK, 11, 11));
    addDeposit(Deposit.create(Resource.Type.ROCK, 12, 11));
  }

  @Override
  protected void update() {
    tickTime += Application.FRAME_TIME_MS;

    if (tickTime >= TICK_INTERVAL) {
      tickTime = 0;
      machines.forEach(Machine::tick);
    }
  }

  @Override
  protected void render() {
    Renderer.setColor(GRID_COLOR);
    Renderer.setStroke(OUTER_GRID_STROKE);
    Renderer.drawRect(0, 0, pixelSize, pixelSize);

    for (int i = 1; i < Constants.GRID_SIZE; i++) {
      int x = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(GRID_COLOR);
      Renderer.setStroke(INNER_GRID_STROKE);
      Renderer.drawLineV(x, toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize));
    }

    for (int i = 1; i < Constants.GRID_SIZE; i++) {
      int y = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(GRID_COLOR);
      Renderer.setStroke(INNER_GRID_STROKE);
      Renderer.drawLineH(toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize), y);
    }
  }

  public void createMachine(Position position, Machine.Info info, Direction direction) {
    int index = Utils.worldPositionToIndex(position);
    int row = Utils.indexToRow(index);
    int col = Utils.indexToCol(index);
    System.out.printf("R:%d, C:%d\n", row, col);

    if (machineGrid[index] != null)
      removeMachine(position);

    Machine machine = null;

    if (info == Extractor.INFO) {
      machine = Extractor.create(index, direction, depositGrid[index]);
    } else if (info == Conveyor.INFO) {
      machine = Conveyor.create(index, direction);
    }

    if (machine != null)
      addMachine(machine);
  }

  public void removeMachine(Position position) {
    int index = Utils.worldPositionToIndex(position);
    Machine machine = machineGrid[index];

    if (machine == null)
      return;

    machines.remove(machine);
    machineContainer.removeChild(machine);
    machineGrid[index] = null;
  }

  private void addDeposit(Deposit deposit) {
    depositGrid[deposit.index] = deposit;
    depositContainer.addChild(deposit);
  }

  private void addMachine(Machine machine) {
    machineGrid[machine.index] = machine;
    machineContainer.addChild(machine);
    machines.add(machine);
  }
}
