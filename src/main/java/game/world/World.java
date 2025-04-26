package game.world;

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
import java.util.List;

import static dev.gamekit.utils.Math.toInt;

public class World extends Prop {
  private static final int TICK_RATE = 2;
  private static final double INV_TICK_RATE = 1.0 / TICK_RATE;
  private static final Color GRID_COLOR = new Color(0x2f000000, true);
  private static final Stroke OUTER_GRID_STROKE = new BasicStroke(
    1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{ 10 }, 5
  );
  private static final Stroke INNER_GRID_STROKE = new BasicStroke(
    1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{ 10 }, 5
  );

  public final int pixelSize;
  private final List<Machine> machines;
  private final Deposit[] depositGrid;
  private final Machine[] machineGrid;
  private final Hub hub;

  private long tickTime;

  public World() {
    super("World");
    pixelSize = Constants.GRID_SIZE * Constants.CELL_PIXEL_SIZE;
    machineGrid = new Machine[Constants.GRID_SIZE * Constants.GRID_SIZE];
    depositGrid = new Deposit[Constants.GRID_SIZE * Constants.GRID_SIZE];
    hub = new Hub((Constants.GRID_SIZE * Constants.GRID_SIZE) / 2, Direction.UP, (cargo) -> {
      // TODO: Consume cargo
    });
    machines = new ArrayList<>();
  }

  @Override
  protected void start() {
    super.start();
    addMachine(hub.gridIndex, hub);
    addResourceDeposit(new Deposit(Resource.Type.ROCK, 0, 0));
    addResourceDeposit(new Deposit(Resource.Type.ROCK, 1, 0));
    addResourceDeposit(new Deposit(Resource.Type.ROCK, 1, 1));
    addResourceDeposit(new Deposit(Resource.Type.ROCK, 2, 1));
  }

  @Override
  protected void update() {
    tickTime += Application.FRAME_TIME_MS;

    if (tickTime >= INV_TICK_RATE) {
      tickTime = 0;
      machines.forEach(Machine::output);
      machines.forEach(Machine::process);
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

  public boolean createMachine(Position position, Machine.Info info, Direction direction) {
    int index = Utils.worldPositionToIndex(position);
    int row = Utils.indexToRow(index);
    int col = Utils.indexToCol(index);
    System.out.printf("R:%d, C:%d\n", row, col);

    if (machineGrid[index] != null)
      return false;

    Machine machine = null;

    if (info == Extractor.INFO)
      machine = new Extractor(index, direction);

    if (machine != null)
      addMachine(index, machine);

    return true;
  }

  public boolean isMachineAtPosition(Position position) {
    int index = Utils.worldPositionToIndex(position);
    return machineGrid[index] != null;
  }

  public void connectMachines(List<Integer> pathIndices) {
    int machine1GridIndex = pathIndices.get(0);
    int machine2GridIndex = pathIndices.get(pathIndices.size() - 1);
    Machine sourceMachine = machineGrid[machine1GridIndex];
    Machine targetMachine = machineGrid[machine2GridIndex];

    if (sourceMachine == null ||
      targetMachine == null ||
      sourceMachine instanceof Conveyor ||
      targetMachine instanceof Conveyor)
      return;

    // Return if there is a machine in this path
    for (int i = 1; i < pathIndices.size() - 1; i++) {
      int index = pathIndices.get(i);
      Machine machine = machineGrid[index];
      if (machine != null) return;
    }

    // Create oriented conveyors on the path
    for (int i = 1; i < pathIndices.size() - 1; i++) {
      int previousIndex = pathIndices.get(i - 1);
      int currentIndex = pathIndices.get(i);
      int diff = currentIndex - previousIndex;
      Direction direction = switch (diff) {
        case 1 -> Direction.RIGHT;
        case -1 -> Direction.LEFT;
        case Constants.GRID_SIZE -> Direction.UP;
        case -Constants.GRID_SIZE -> Direction.DOWN;
        default -> null;
      };

      if (direction == null)
        continue;

      if (i == 1) {
        boolean draggedFromOutputPort = switch (direction) {
          case UP -> sourceMachine.topPort != null &&
            sourceMachine.topPort.type == Port.Type.OUT;
          case RIGHT -> sourceMachine.rightPort != null &&
            sourceMachine.rightPort.type == Port.Type.OUT;
          case DOWN -> sourceMachine.bottomPort != null &&
            sourceMachine.bottomPort.type == Port.Type.OUT;
          case LEFT -> sourceMachine.leftPort != null &&
            sourceMachine.leftPort.type == Port.Type.OUT;
        };

        if (!draggedFromOutputPort)
          return;
      }

      addMachine(currentIndex, new Conveyor(currentIndex, direction));
    }
  }

  private void addResourceDeposit(Deposit deposit) {
    int index = Utils.rowColToIndex(deposit.row, deposit.col);
    depositGrid[index] = deposit;
    addChild(deposit);
  }

  private void addMachine(int index, Machine machine) {
    machines.add(machine);
    machineGrid[index] = machine;
    addChild(machine);
  }
}
