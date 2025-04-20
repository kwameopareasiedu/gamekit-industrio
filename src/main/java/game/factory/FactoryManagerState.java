package game.factory;

import game.machines.Machine;
import game.machines.Orientation;

public class FactoryManagerState {
  FactoryAction action = FactoryAction.DEFAULT;
  Orientation orientation = Orientation.UP;
  Machine.Info machineInfo;

  public void reset() {
    action = FactoryAction.DEFAULT;
    orientation = Orientation.UP;
    machineInfo = null;
  }
}
