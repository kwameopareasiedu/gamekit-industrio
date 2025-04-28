package game.factory;

import dev.gamekit.core.Application;
import dev.gamekit.core.Camera;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Align;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Padding;
import dev.gamekit.ui.widgets.Widget;
import dev.gamekit.utils.Position;
import game.Constants;
import game.machines.Conveyor;
import game.machines.Direction;
import game.machines.Extractor;
import game.ui.MachineButton;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.lerp;

public interface FactoryManager {
  Factory getFactory();

  FactoryManagerState getState();

  default void updateState() {
    FactoryManagerState state = getState();

    if (Input.isKeyPressed(Input.KEY_D)) {
      state.desiredX += state.navSpeed / state.zoom;
    } else if (Input.isKeyPressed(Input.KEY_A)) {
      state.desiredX -= state.navSpeed / state.zoom;
    }

    if (Input.isKeyPressed(Input.KEY_W)) {
      state.desiredY += state.navSpeed / state.zoom;
    } else if (Input.isKeyPressed(Input.KEY_S)) {
      state.desiredY -= state.navSpeed / state.zoom;
    }

    if (Input.isKeyPressed(Input.KEY_E)) {
      state.desiredZoom = clamp(
        state.desiredZoom + state.zoomSpeed,
        state.minZoom, state.maxZoom
      );
    } else if (Input.isKeyPressed(Input.KEY_Q)) {
      state.desiredZoom = clamp(
        state.desiredZoom - state.zoomSpeed,
        state.minZoom, state.maxZoom
      );
    }

    state.panX = lerp(state.panX, state.desiredX, state.navLerpSpeed);
    state.panY = lerp(state.panY, state.desiredY, state.navLerpSpeed);
    state.zoom = lerp(state.zoom, state.desiredZoom, state.zoomLerpSpeed);

    if (Input.isButtonReleased(Input.BUTTON_RMB)) {
      state.action = FactoryAction.CLEAR;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && state.action == FactoryAction.PICK) {
      state.action = FactoryAction.PLACE;
    } else if (Input.isKeyReleased(Input.KEY_R) && state.action == FactoryAction.PICK) {
      state.action = FactoryAction.ROTATE;
    }
  }

  default void applyState() {
    Factory factory = getFactory();
    FactoryManagerState state = getState();

    Camera.lookAt(
      state.panX * state.zoom,
      state.panY * state.zoom
    );
    Camera.setZoom(state.zoom);

    switch (state.action) {
      case PLACE -> {
        Position pos = getMouseWorldPosition();

        if (factory.createMachine(pos, state.machineInfo, state.direction))
          state.action = FactoryAction.CLEAR;
        else state.action = FactoryAction.PICK;
      }
      case ROTATE -> {
        state.direction = Direction.cycle(state.direction, 1);
        state.action = FactoryAction.PICK;
      }
      case CLEAR -> state.reset();
    }
  }

  default void renderState() {
    FactoryManagerState state = getState();

    if (state.action == FactoryAction.PICK) {
      Position mousePos = getMouseWorldPosition();

      Renderer.withRotation(
        mousePos.x, mousePos.y, state.direction.getAngle(),
        () ->
          Renderer.drawImage(
            state.machineInfo.image(), mousePos.x, mousePos.y,
            Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE
          )
      );
    }
  }

  default Widget renderUI() {
    FactoryManagerState state = getState();

    return Align.create(
      Align.options().horizontalAlignment(Alignment.END),
      Padding.create(
        Padding.options().padding(new Spacing(24, 48)),
        Column.create(
          Column.options().gapSize(12),
          MachineButton.create(Extractor.INFO, (e) -> {
            if (e.type == MouseEvent.Type.CLICK) {
              Application.getInstance().scheduleTask(() -> {
                state.machineInfo = Extractor.INFO;
                state.action = FactoryAction.PICK;
              });
            }
          }),
          MachineButton.create(Conveyor.INFO, (e) -> {
            if (e.type == MouseEvent.Type.CLICK) {
              Application.getInstance().scheduleTask(() -> {
                state.machineInfo = Conveyor.INFO;
                state.action = FactoryAction.PICK;
              });
            }
          })
        )
      )
    );
  }

  private Position getMouseWorldPosition() {
    Position mousePos = Input.getMousePosition();
    return Camera.screenToWorldPosition(mousePos.x, mousePos.y);
  }
}
