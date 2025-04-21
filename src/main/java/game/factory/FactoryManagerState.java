package game.factory;

import game.machines.Machine;
import game.machines.Direction;

import java.util.ArrayList;
import java.util.List;

public class FactoryManagerState {
  final List<Integer> pathIndices;
  FactoryAction action;
  Direction direction;
  Machine.Info machineInfo;

  public FactoryManagerState() {
    pathIndices = new ArrayList<>();
    action = FactoryAction.DEFAULT;
    direction = Direction.UP;
  }

  public void reset() {
    pathIndices.clear();
    action = FactoryAction.DEFAULT;
    direction = Direction.UP;
    machineInfo = null;
  }
}
