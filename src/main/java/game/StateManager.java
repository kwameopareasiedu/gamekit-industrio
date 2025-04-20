package game;

import dev.gamekit.core.Camera;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.components.Factory;
import game.machines.Machine;
import game.machines.Orientation;

public interface StateManager {
  Factory getFactory();

  Machine.Info getMachineInfo();

  Orientation getOrientation();

  void setOrientation(Orientation orientation);

  State getState();

  void setState(State state);

  default void updateState() {
    State state = getState();

    if (Input.isButtonClicked(Input.BUTTON_RMB)) {
      setState(State.CLEAR);
    } else if (Input.isButtonClicked(Input.BUTTON_LMB)) {
      if (state == State.PICK)
        setState(State.PLACE);
    } else if (Input.isKeyReleased(Input.KEY_R)) {
      if (state == State.PICK)
        setState(State.ROTATE_CW);
    } else if (Input.isKeyReleased(Input.KEY_E)) {
      if (state == State.PICK)
        setState(State.ROTATE_CCW);
    }
  }

  default void applyState() {
    Factory factory = getFactory();
    Orientation orientation = getOrientation();
    Machine.Info machineInfo = getMachineInfo();
    State state = getState();

    switch (state) {
//      case DEFAULT -> { }
//      case PICK -> { }
      case PLACE -> {
        if (factory.createMachine(machineInfo, orientation))
          resetState();
        else setState(State.PICK);
      }
      case ROTATE_CW -> {
        setOrientation(Orientation.cycle(orientation, 1));
        setState(State.PICK);
      }
      case ROTATE_CCW -> {
        setOrientation(Orientation.cycle(orientation, -1));
        setState(State.PICK);
      }
      case CLEAR -> {
        resetState();
        setState(State.DEFAULT);
      }
    }
  }

  default void renderState() {
    State state = getState();
    Orientation orientation = getOrientation();
    Machine.Info machineInfo = getMachineInfo();

    if (state == State.PICK) {
      Position pos = Input.getMousePosition();
      Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
      Renderer.withRotation(
        worldPos.x, worldPos.y, orientation.toDeg(),
        () ->
          Renderer.drawImage(
            machineInfo.icon(), worldPos.x, worldPos.y,
            Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE
          )
      );
    }
  }

  private void resetState() {
    setState(State.DEFAULT);
    setOrientation(Orientation.UP);
  }

  enum State {
    DEFAULT, CONNECT, PICK, PLACE, ROTATE_CW, ROTATE_CCW, CLEAR
  }
}
