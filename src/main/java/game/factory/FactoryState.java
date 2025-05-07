package game.factory;

import game.machines.Direction;
import game.machines.Machine;

import java.util.ArrayList;
import java.util.List;

public class FactoryState {
  final List<Integer> pathIndices = new ArrayList<>();
  final double navSpeed = 10;
  final double navLerpSpeed = 0.05;
  final double zoomSpeed = 0.01;
  final double minZoom = 1;
  final double maxZoom = 3;

  FactoryAction action = FactoryAction.DEFAULT;
  Direction direction = Direction.UP;
  Machine.Info machineInfo;
  double desiredX = 0;
  double desiredY = 0;
  double panX = 0;
  double panY = 0;
  double zoom = minZoom;

  public void reset() {
    pathIndices.clear();
    action = FactoryAction.DEFAULT;
    direction = Direction.UP;
    machineInfo = null;
  }
}
