package game.factory;

import dev.gamekit.core.*;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import game.machines.*;
import game.resources.Source;
import game.ui.MachineButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.lerp;

public abstract class FactoryScene extends Scene {
  private static final BufferedImage LEVEL_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 2744, 2048, 144, 128);
  private static final BufferedImage GOAL_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 2744, 2048, 144, 128);
  private static final BufferedImage MACHINES_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1152, 1720, 128, 144);
  private static final double MIN_BOUND = -0.5 * Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
  private static final double MAX_BOUND = 0.5 * Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
  private static final Color CLEAR_COLOR = new Color(0x202039);

  protected final int level;
  protected final Factory factory;
  protected final Machine.Info[] machineInfos;
  protected final FactoryState state;
  protected final FactoryGoal goal;

  public FactoryScene(
    int level,
    int gridSize,
    Machine.Info[] machineInfos,
    Source[] sources,
    FactoryGoal goal
  ) {
    super("Factory Scene");

    Factory.GRID_SIZE = gridSize;
    this.level = level;
    this.machineInfos = machineInfos;
    this.factory = new Factory(sources, shape -> {
      goal.track(shape);
      updateUI(() -> { });
    });
    this.state = new FactoryState();
    this.goal = goal;
  }

  @Override
  protected void start() {
    addChild(factory);
  }

  @Override
  protected final void update() {
    updateState();
    applyState();
  }

  @Override
  protected final void render() {
    Renderer.setBackground(CLEAR_COLOR);
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

  @Override
  public Widget createUI() {
    return Stack.create(
      Align.create(
        Align.options().horizontalAlignment(Alignment.START).verticalAlignment(Alignment.START),
        Padding.create(
          Padding.options().padding(48),
          Sized.create(
            Sized.options().width(224).height(96),
            Panel.create(
              Panel.options().background(LEVEL_PANEL_BG).padding(20, 0),
              Column.create(
                Column.options().crossAxisAlignment(CrossAxisAlignment.STRETCH).gapSize(4),
                Text.create(
                  Text.options().color(Color.LIGHT_GRAY).fontSize(12).alignment(Alignment.CENTER),
                  "Campaign"
                ),
                Text.create(
                  Text.options().color(Color.WHITE).fontSize(24).fontStyle(Font.BOLD)
                    .alignment(Alignment.CENTER),
                  String.format("Level %d", level)
                )
              )
            )
          )
        )
      ),

      Align.create(
        Align.options().horizontalAlignment(Alignment.START).verticalAlignment(Alignment.END),
        Padding.create(
          Padding.options().padding(48),
          Panel.create(
            Panel.options().background(GOAL_PANEL_BG).padding(32, 16),
            Padding.create(
              Padding.options().padding(32, 0),
              Column.create(
                Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(16),
                Text.create(
                  Text.options().color(Color.LIGHT_GRAY).fontSize(12).alignment(Alignment.CENTER),
                  "Goal"
                ),
                Sized.create(
                  Sized.options().width(36).height(36),
                  Colored.create(
                    Colored.options().color(goal.color).borderRadius(12),
                    Empty.create()
                  )
                ),
                Row.create(
                  Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
                    .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(12),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(20).fontStyle(Font.BOLD),
                    String.valueOf(goal.getCount())
                  ),
                  Text.create(
                    Text.options().color(Color.LIGHT_GRAY).fontSize(12).fontStyle(Font.BOLD),
                    "/"
                  ),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(20).fontStyle(Font.BOLD),
                    String.valueOf(goal.required)
                  )
                )
              )
            )
          )
        )
      ),

      Align.create(
        Align.options().horizontalAlignment(Alignment.END),
        Padding.create(
          Padding.options().padding(48),
          Panel.create(
            Panel.options().background(MACHINES_PANEL_BG).padding(0, 20),
            Padding.create(
              Padding.options().padding(24),
              Column.create(
                Column.options().gapSize(36).crossAxisAlignment(CrossAxisAlignment.CENTER),
                Text.create(
                  Text.options().color(Color.WHITE).fontSize(16).fontStyle(Font.BOLD),
                  "Machines"
                ),
                Column.create(
                  Column.options().gapSize(32).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Arrays.stream(machineInfos).map(info ->
                    MachineButton.create(info, (e) -> {
                      if (e.type == MouseEvent.Type.CLICK) {
                        Application.getInstance().scheduleTask(() -> {
                          state.machineInfo = info;
                          state.action = FactoryAction.PICK;
                        });
                      }
                    })
                  ).toArray(MachineButton[]::new)
                )
              )
            )
          )
        )
      )
    );
  }

  private void updateState() {
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

  private void applyState() {
    Camera.lookAt(state.panX * state.zoom, state.panY * state.zoom);
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

  private Position getMouseWorldPosition() {
    Position mousePos = Input.getMousePosition();
    return Camera.screenToWorldPosition(mousePos.x, mousePos.y);
  }
}
