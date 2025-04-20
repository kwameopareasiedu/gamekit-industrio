package game.machines;

public abstract class DroppableMachine extends Machine {
  public DroppableMachine(
    String name, int gridIndex,
    Orientation orientation,
    Port[] inputs, Port[] outputs
  ) {
    super(name, gridIndex, orientation, inputs, outputs);
  }
}
