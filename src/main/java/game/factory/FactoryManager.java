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
import game.Utils;
import game.machines.Direction;
import game.machines.Extractor;
import game.ui.MachineButton;

public interface FactoryManager {
  Factory getFactory();

  FactoryManagerState getState();

  default void updateState() {
    Factory factory = getFactory();
    FactoryManagerState state = getState();

    if (Input.isButtonClicked(Input.BUTTON_RMB)) {
      state.action = FactoryAction.CLEAR;
    } else if (Input.isButtonDown(Input.BUTTON_LMB) &&state.action == FactoryAction.DEFAULT) {
      if (factory.isMachineAtPosition(getMouseWorldPosition()))
        state.action = FactoryAction.FIND_PATH;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && state.action == FactoryAction.FIND_PATH) {
      state.action = FactoryAction.CONNECT;
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
        Position pos = getMouseWorldPosition();

        if (factory.createMachine(pos, state.machineInfo, state.direction))
          state.action = FactoryAction.CLEAR;
        else state.action = FactoryAction.PICK;
      }
      case FIND_PATH -> {
        Position pos = getMouseWorldPosition();
        int gridIndex = Utils.worldPositionToIndex(pos);
        int idx = state.pathIndices.indexOf(gridIndex);

        if (idx != -1) {
          state.pathIndices.subList(
            idx, state.pathIndices.size()
          ).clear();
        }

        state.pathIndices.add(gridIndex);
      }
      case CONNECT -> {
        factory.connectMachines(state.pathIndices);
        state.action = FactoryAction.CLEAR;
      }
      case ROTATE_CW -> {
        state.direction = Direction.cycle(state.direction, 1);
        state.action = FactoryAction.PICK;
      }
      case ROTATE_CCW -> {
        state.direction = Direction.cycle(state.direction, -1);
        state.action = FactoryAction.PICK;
      }
      case CLEAR -> state.reset();
    }
  }

  default void renderState() {
    FactoryManagerState state = getState();

    if (state.action == FactoryAction.PICK) {
      Position worldPos = getMouseWorldPosition();

      Renderer.withRotation(
        worldPos.x, worldPos.y, state.direction.getAngle(),
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
                MachineButton.create(Extractor.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    state.machineInfo = Extractor.INFO;
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

  private Position getMouseWorldPosition() {
    Position mousePos = Input.getMousePosition();
    return Camera.screenToWorldPosition(mousePos.x, mousePos.y);
  }
}
