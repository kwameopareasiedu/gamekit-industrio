package game.world;

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
import game.Utils;
import game.machines.Direction;
import game.machines.Extractor;
import game.ui.MachineButton;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.lerp;

public interface WorldManager {
  World getWorld();

  WorldManagerState getState();

  default void updateState() {
    World world = getWorld();
    WorldManagerState state = getState();

    if (Input.isKeyPressed(Input.KEY_D)) {
      state.desiredX += state.navSpeed;
    } else if (Input.isKeyPressed(Input.KEY_A)) {
      state.desiredX -= state.navSpeed;
    }

    if (Input.isKeyPressed(Input.KEY_W)) {
      state.desiredY += state.navSpeed;
    } else if (Input.isKeyPressed(Input.KEY_S)) {
      state.desiredY -= state.navSpeed;
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
      state.action = WorldAction.CLEAR;
    } else if (Input.isButtonDown(Input.BUTTON_LMB) && state.action == WorldAction.DEFAULT) {
      if (world.isMachineAtPosition(getMouseWorldPosition()))
        state.action = WorldAction.FIND_PATH;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && state.action == WorldAction.FIND_PATH) {
      state.action = WorldAction.CONNECT;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && state.action == WorldAction.PICK) {
      state.action = WorldAction.PLACE;
    } else if (Input.isKeyReleased(Input.KEY_R) && state.action == WorldAction.PICK) {
      state.action = WorldAction.ROTATE;
    }
  }

  default void applyState() {
    World world = getWorld();
    WorldManagerState state = getState();

    Camera.lookAt(
      state.panX * state.zoom,
      state.panY * state.zoom
    );
    Camera.setZoom(state.zoom);

    switch (state.action) {
      case PLACE -> {
        Position pos = getMouseWorldPosition();

        if (world.createMachine(pos, state.machineInfo, state.direction))
          state.action = WorldAction.CLEAR;
        else state.action = WorldAction.PICK;
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
        world.connectMachines(state.pathIndices);
        state.action = WorldAction.CLEAR;
      }
      case ROTATE -> {
        state.direction = Direction.cycle(state.direction, 1);
        state.action = WorldAction.PICK;
      }
      case CLEAR -> state.reset();
    }
  }

  default void renderState() {
    WorldManagerState state = getState();

    if (state.action == WorldAction.PICK) {
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
    WorldManagerState state = getState();

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
                state.action = WorldAction.PICK;
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
