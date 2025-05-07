package game.factory;

import dev.gamekit.core.*;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import game.machines.*;
import game.ui.MachineButton;

import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.lerp;

public interface FactoryManager {
  BufferedImage LEVEL_PANEL_BG = IO.getResourceImage("menu-ui-red.png", 2744, 2048, 144, 128);
  BufferedImage GOAL_PANEL_BG = IO.getResourceImage("menu-ui-red.png", 256, 1176, 128, 80);
  BufferedImage MACHINES_PANEL_BG = IO.getResourceImage("menu-ui-red.png", 1152, 1720, 128, 144);
  double MIN_BOUND = -0.5 * Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
  double MAX_BOUND = 0.5 * Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;

  Factory getFactory();

  FactoryManagerState getState();

  default void startState() { }

  default void updateState() {
    FactoryManagerState state = getState();

    if (Input.isKeyPressed(Input.KEY_D)) {
      state.desiredX = clamp(state.desiredX + state.navSpeed / state.zoom, MIN_BOUND, MAX_BOUND);
    } else if (Input.isKeyPressed(Input.KEY_A)) {
      state.desiredX = clamp(state.desiredX - state.navSpeed / state.zoom, MIN_BOUND, MAX_BOUND);
    }

    if (Input.isKeyPressed(Input.KEY_W)) {
      state.desiredY = clamp(state.desiredY + state.navSpeed / state.zoom, MIN_BOUND, MAX_BOUND);
    } else if (Input.isKeyPressed(Input.KEY_S)) {
      state.desiredY = clamp(state.desiredY - state.navSpeed / state.zoom, MIN_BOUND, MAX_BOUND);
    }

    if (Input.isKeyPressed(Input.KEY_E)) {
      state.zoom = clamp(
        state.zoom + state.zoomSpeed,
        state.minZoom, state.maxZoom
      );
    } else if (Input.isKeyPressed(Input.KEY_Q)) {
      state.zoom = clamp(
        state.zoom - state.zoomSpeed,
        state.minZoom, state.maxZoom
      );
    }

    state.panX = lerp(state.panX, state.desiredX, state.navLerpSpeed);
    state.panY = lerp(state.panY, state.desiredY, state.navLerpSpeed);

    if (state.action == FactoryAction.PICK &&
      (Input.isButtonReleased(Input.BUTTON_RMB) || Input.isKeyPressed(Input.KEY_ESCAPE))) {
      state.action = FactoryAction.CLEAR;
    } else if (Input.isButtonReleased(Input.BUTTON_RMB)) {
      state.action = FactoryAction.DELETE;
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
        factory.createMachine(pos, state.machineInfo, state.direction);
        state.action = FactoryAction.PICK;
      }
      case ROTATE -> {
        state.direction = Direction.cycle(state.direction, 1);
        state.action = FactoryAction.PICK;
      }
      case DELETE -> {
        Position pos = getMouseWorldPosition();
        factory.removeMachine(pos);
        state.action = FactoryAction.CLEAR;
      }
      case CLEAR -> state.reset();
    }
  }

  default void renderState() {
    FactoryManagerState state = getState();

    Renderer.setBackground(state.clearColor);
    Renderer.clear();

    if (state.action == FactoryAction.PICK) {
      Position mousePos = getMouseWorldPosition();

      Renderer.withRotation(
        mousePos.x, mousePos.y, state.direction.getAngle(),
        () ->
          Renderer.drawImage(
            state.machineInfo.image(), mousePos.x, mousePos.y,
            Factory.CELL_PIXEL_SIZE, Factory.CELL_PIXEL_SIZE
          )
      );
    }
  }

  default Widget renderUI() {
    FactoryManagerState state = getState();

    return Stack.create(
      Align.create(
        Align.options().horizontalAlignment(Alignment.CENTER).verticalAlignment(Alignment.START),
        Padding.create(
          Padding.options().padding(48),
          Column.create(
            Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER),
            Sized.create(
              Sized.options().width(224).height(96),
              Panel.create(
                Panel.options().background(LEVEL_PANEL_BG).padding(20, 0),
                Column.create(
                  Column.options().crossAxisAlignment(CrossAxisAlignment.STRETCH).gapSize(4),
                  Text.create(
                    Text.options().color(Color.GRAY).fontSize(12).alignment(Alignment.CENTER),
                    "Campaign"
                  ),
                  Text.create(
                    Text.options().color(Color.BLACK).fontSize(24).fontStyle(Font.BOLD)
                      .alignment(Alignment.CENTER),
                    String.format("Level %d", state.level)
                  )
                )
              )
            ),
            Panel.create(
              Panel.options().background(GOAL_PANEL_BG).padding(0, 12),
              Padding.create(
                Padding.options().padding(24, 0),
                Text.create(
                  Text.options().color(Color.BLACK).fontSize(12).alignment(Alignment.CENTER),
                  state.goalDescription
                )
              )
            )
          )
        )
      ),
      Align.create(
        Align.options().horizontalAlignment(Alignment.END),
        Padding.create(
          Padding.options().padding(48, 48, 0, 0),
          Panel.create(
            Panel.options().background(MACHINES_PANEL_BG).padding(0, 20),
            Padding.create(
              Padding.options().padding(24, 48),
              Column.create(
                Column.options().gapSize(32).crossAxisAlignment(CrossAxisAlignment.CENTER),
                MachineButton.create(Extractor.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(() -> {
                      state.machineInfo = Extractor.INFO;
                      state.action = FactoryAction.PICK;
                    });
                  }
                }),
                MachineButton.create(Belt.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(() -> {
                      state.machineInfo = Belt.INFO;
                      state.action = FactoryAction.PICK;
                    });
                  }
                }),
                MachineButton.create(Mixer.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(() -> {
                      state.machineInfo = Mixer.INFO;
                      state.action = FactoryAction.PICK;
                    });
                  }
                }),
                MachineButton.create(Reshaper.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(() -> {
                      state.machineInfo = Reshaper.INFO;
                      state.action = FactoryAction.PICK;
                    });
                  }
                }),
                MachineButton.create(HueShifter.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    Application.getInstance().scheduleTask(() -> {
                      state.machineInfo = HueShifter.INFO;
                      state.action = FactoryAction.PICK;
                    });
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
