package game.world;

import game.machines.Direction;
import game.machines.Machine;

import java.util.ArrayList;
import java.util.List;

public class WorldManagerState {
  final List<Integer> pathIndices = new ArrayList<>();
  final double navSpeed = 10;
  final double invNavSpeed = 1 / navSpeed;
  final double zoomSpeed = 0.1;
  final double minZoom = 1;
  final double maxZoom = 3;

  WorldAction action = WorldAction.DEFAULT;
  Direction direction = Direction.UP;
  Machine.Info machineInfo;
  double desiredX = 0;
  double desiredY = 0;
  double desiredZoom = minZoom;
  double panX = 0;
  double panY = 0;
  double zoom = minZoom;

  public void reset() {
    pathIndices.clear();
    action = WorldAction.DEFAULT;
    direction = Direction.UP;
    machineInfo = null;
  }
}
