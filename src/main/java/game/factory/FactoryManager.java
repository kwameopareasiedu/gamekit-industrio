package game.factory;

import dev.gamekit.core.Camera;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import game.Constants;
import game.machines.Orientation;
import game.machines.Producer;
import game.ui.MachineButton;

public interface FactoryManager {
  Factory getFactory();

  FactoryManagerState getState();

  default void updateState() {
    Factory factory = getFactory();
    FactoryManagerState state = getState();

    if (Input.isButtonClicked(Input.BUTTON_RMB)) {
      state.action = FactoryAction.CLEAR;
    } else if (Input.isButtonPressed(Input.BUTTON_LMB) && state.action == FactoryAction.DEFAULT) {
      Position mousePos = Input.getMousePosition();
      Position worldPos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);
      if (factory.isMachineAtPosition(worldPos))
        state.action = FactoryAction.CONNECT;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && state.action == FactoryAction.CONNECT) {
      state.action = FactoryAction.DEFAULT;
    } else if (Input.isButtonClicked(Input.BUTTON_LMB) && state.action == FactoryAction.PICK) {
      state.action = FactoryAction.PLACE;
    } else if (Input.isKeyReleased(Input.KEY_R) && state.action == FactoryAction.PICK) {
      state.action = FactoryAction.ROTATE_CW;
    } else if (Input.isKeyReleased(Input.KEY_E) && state.action == FactoryAction.PICK) {
      state.action = FactoryAction.ROTATE_CCW;
    }
  }

  default void applyState() {
    Factory factory = getFactory();
    FactoryManagerState state = getState();

    switch (state.action) {
      case PLACE -> {
        Position mousePos = Input.getMousePosition();
        Position worldPos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);

        if (factory.createMachine(worldPos, state.machineInfo, state.orientation))
          state.action = FactoryAction.CLEAR;
        else state.action = FactoryAction.PICK;
      }
      case CONNECT -> {
        System.out.println("Connecting");
      }
      case ROTATE_CW -> {
        state.orientation = Orientation.cycle(state.orientation, 1);
        state.action = FactoryAction.PICK;
      }
      case ROTATE_CCW -> {
        state.orientation = Orientation.cycle(state.orientation, -1);
        state.action = FactoryAction.PICK;
      }
      case CLEAR -> state.reset();
    }
  }

  default void renderState() {
    FactoryManagerState state = getState();

    if (state.action == FactoryAction.PICK) {
      Position mousePos = Input.getMousePosition();
      Position worldPos = Camera.screenToWorldPosition(mousePos.x, mousePos.y);
      Renderer.withRotation(
        worldPos.x, worldPos.y, state.orientation.toDeg(),
        () ->
          Renderer.drawImage(
            state.machineInfo.icon(), worldPos.x, worldPos.y,
            Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE
          )
      );
    }
  }

  default Widget renderUI() {
    FactoryManagerState state = getState();

    return Align.create(
      AlignParam.horizontalAlignment(Alignment.END),
      AlignParam.child(
        Padding.create(
          PaddingParam.padding(new Spacing(24, 48)),
          PaddingParam.child(
            Column.create(
              ColumnParam.gapSize(12),
              ColumnParam.children(
                MachineButton.create(Producer.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    state.machineInfo = Producer.INFO;
                    state.action = FactoryAction.PICK;
                  }
                })
              )
            )
          )
        )
      )
    );
  }
}
