package game.factory;

import dev.gamekit.animation.Animation;
import dev.gamekit.animation.AnimationCurve;
import dev.gamekit.core.*;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import game.machines.Direction;
import game.machines.Machine;
import game.resources.Shape;
import game.resources.Source;
import game.ui.MachineButton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.lerp;
import static game.ui.MachineButton.DEFAULT_BG;
import static game.ui.MachineButton.HOVER_BG;

public abstract class FactoryScene extends Scene {
  private static final BufferedImage LEVEL_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1152, 1720, 128, 144);
  private static final BufferedImage MACHINES_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 1152, 1720, 128, 144);
  private static final BufferedImage COMPLETED_PANEL_BG =
    IO.getResourceImage("menu-ui-cyan.png", 820, 144, 152, 96);
  private static final BufferedImage KEY_W_IMG =
    IO.getResourceImage("pixel-keys.png", 48, 48, 15, 16);
  private static final BufferedImage KEY_A_IMG =
    IO.getResourceImage("pixel-keys.png", 32, 64, 15, 16);
  private static final BufferedImage KEY_S_IMG =
    IO.getResourceImage("pixel-keys.png", 48, 64, 15, 16);
  private static final BufferedImage KEY_D_IMG =
    IO.getResourceImage("pixel-keys.png", 64, 64, 15, 16);
  private static final BufferedImage KEY_R_IMG =
    IO.getResourceImage("pixel-keys.png", 80, 48, 15, 16);
  private static final BufferedImage KEY_ESC_IMG =
    IO.getResourceImage("pixel-keys.png", 0, 0, 31, 16);
  private static final double MIN_BOUND = -0.5 * Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
  private static final double MAX_BOUND = 0.5 * Factory.GRID_SIZE * Factory.CELL_PIXEL_SIZE;
  private static final double NAV_SPEED = 3;
  private static final double NAV_LERP_SPEED = 0.02;
  private static final Color CLEAR_COLOR = new Color(0x202039);
  private static final Color SCRIM_COLOR = new Color(0x99000000, true);

  protected final int level;
  protected final Factory factory;
  protected final Machine.Info[] machineInfos;
  protected final FactoryGoal goal;

  private final java.util.List<Integer> pathIndices = new ArrayList<>();
  private FactoryAction action = FactoryAction.DEFAULT;
  private Direction direction = Direction.UP;
  private Machine.Info machineInfo;
  private double desiredX = 0;
  private double desiredY = 0;
  private double panX = 0;
  private double panY = 0;
  private Animation revealAnim;

  public FactoryScene(int level, Machine.Info[] machineInfos, Source[] sources, FactoryGoal goal) {
    super("Factory Scene");

    this.level = level;
    this.machineInfos = machineInfos;
    this.factory = new Factory(sources, shape -> {
      Factory.removeItem(shape);
      goal.track(shape);

      if (goal.isCompleted() && revealAnim == null) {
        revealAnim = new Animation(500, Animation.RepeatMode.NONE, AnimationCurve.EASE_OUT_CUBIC);
        revealAnim.setValueListener(val -> updateUI());
        revealAnim.start();
        Factory.close();
      }

      updateUI();
    });
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

    if (action == FactoryAction.PICK) {
      Position mousePos = getMouseWorldPosition();

      Renderer.withRotation(
        mousePos.x, mousePos.y, direction.getAngle(),
        () ->
          Renderer.drawImage(
            machineInfo.image(), mousePos.x, mousePos.y,
            Factory.CELL_PIXEL_SIZE, Factory.CELL_PIXEL_SIZE
          )
      );
    }
  }

  @Override
  public Widget createUI() {
    return Stack.create(
      // Level details
      Align.create(
        Align.options().horizontalAlignment(Alignment.START).verticalAlignment(Alignment.START),
        Padding.create(
          Padding.options().padding(48),
          Panel.create(
            Panel.options().background(LEVEL_PANEL_BG).padding(32, 20),
            Padding.create(
              Padding.options().padding(0, 24),
              Column.create(
                Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(10),
                Text.create(
                  Text.options().color(Color.GRAY).fontSize(12).alignment(Alignment.CENTER),
                  "Campaign"
                ),
                Text.create(
                  Text.options().color(Color.WHITE).fontSize(24).fontStyle(Font.BOLD)
                    .alignment(Alignment.CENTER),
                  String.format("Level %d", level)
                ),
                Padding.create(
                  Padding.options().padding(0, 24),
                  Sized.create(
                    Sized.options().width(64).height(2),
                    Colored.create(
                      Colored.options().color(Color.GRAY),
                      Empty.create()
                    )
                  )
                ),
                Text.create(
                  Text.options().color(Color.LIGHT_GRAY).fontSize(12).alignment(Alignment.CENTER),
                  "Goal"
                ),
                Sized.create(
                  Sized.options().width(36).height(36),
                  Colored.create(
                    Colored.options().color(goal.color)
                      .borderRadius(goal.type == Shape.Type.CIRCLE ? 50 : 12),
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

      // How to play
      Align.create(
        Align.options().horizontalAlignment(Alignment.CENTER),
        Padding.create(
          Padding.options().padding(80),
          Column.create(
            Column.options().crossAxisAlignment(CrossAxisAlignment.STRETCH),
            Text.create(
              Text.options().color(Color.WHITE).fontSize(16).fontStyle(Font.BOLD)
                .alignment(Alignment.CENTER),
              "Place machines on your factory floor to achieve your goal"
            ),
            Empty.create(),
            action == FactoryAction.PICK ?
              Column.create(
                Column.options().gapSize(6).crossAxisAlignment(CrossAxisAlignment.CENTER),
                Padding.create(
                  Padding.options().padding(0, 0, 12, 0),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(16).alignment(Alignment.CENTER),
                    "LEFT CLICK to place"
                  )
                ),
                Row.create(
                  Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Sized.create(
                    Sized.options().width(72).height(36),
                    Image.create(KEY_ESC_IMG)
                  ),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(16).alignment(Alignment.CENTER),
                    "or RIGHT CLICK to cancel"
                  )
                ),
                Row.create(
                  Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(KEY_R_IMG)
                  ),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(16).alignment(Alignment.CENTER),
                    "to rotate"
                  )
                )
              )
              :
              Column.create(
                Column.options().gapSize(4).crossAxisAlignment(CrossAxisAlignment.CENTER),
                Row.create(
                  Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(16).alignment(Alignment.CENTER),
                    "Move around with"
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(KEY_W_IMG)
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(KEY_A_IMG)
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(KEY_S_IMG)
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(KEY_D_IMG)
                  )
                ),
                Padding.create(
                  Padding.options().padding(12, 0, 0, 0),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(16).alignment(Alignment.CENTER),
                    "RIGHT CLICK to delete"
                  )
                )
              )
          )
        )
      ),

      // Machine select panel
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
                          machineInfo = info;
                          action = FactoryAction.PICK;
                          updateUI();
                        });
                      }
                    })
                  ).toArray(MachineButton[]::new)
                )
              )
            )
          )
        )
      ),

      // Level completed panel
      goal.isCompleted() && revealAnim != null ?
        Opacity.create(
          Opacity.options().opacity(revealAnim.getValue()),
          Stack.create(
            Fractional.create(
              Fractional.options().horizontalAlignment(Alignment.CENTER)
                .verticalAlignment(Alignment.CENTER),
              Colored.create(
                Colored.options().color(SCRIM_COLOR),
                Empty.create()
              )
            ),
            Fractional.create(
              Fractional.options().widthFactor(0.75).heightFactor(0.25)
                .horizontalAlignment(Alignment.CENTER).verticalAlignment(Alignment.CENTER),
              Panel.create(
                Panel.options().background(COMPLETED_PANEL_BG).padding(72, 32),
                Padding.create(
                  Padding.options().padding(76, 32),
                  Column.create(
                    Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(12),
                    Text.create(
                      Text.options().color(Color.WHITE).fontSize(24).fontStyle(Font.BOLD),
                      "Level Completed"
                    ),
                    Row.create(
                      Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                      Button.create(
                        Button.options().defaultBackground(DEFAULT_BG)
                          .hoverBackground(HOVER_BG).pressedBackground(HOVER_BG).padding(24),
                        Text.create(
                          Text.options().color(Color.WHITE).fontSize(24).fontStyle(Font.BOLD),
                          "Next Level"
                        )
                      ),
                      Button.create(
                        Button.options().defaultBackground(DEFAULT_BG)
                          .hoverBackground(HOVER_BG).pressedBackground(HOVER_BG).padding(24),
                        Text.create(
                          Text.options().color(Color.WHITE).fontSize(24).fontStyle(Font.BOLD),
                          "Main Menu"
                        )
                      )
                    )
                  )
                )
              )
            )
          )
        )
        : Empty.create()
    );
  }

  private void updateState() {
    if (Input.isKeyPressed(Input.KEY_D)) {
      desiredX = clamp(desiredX + NAV_SPEED, MIN_BOUND, MAX_BOUND);
    } else if (Input.isKeyPressed(Input.KEY_A)) {
      desiredX = clamp(desiredX - NAV_SPEED, MIN_BOUND, MAX_BOUND);
    }

    if (Input.isKeyPressed(Input.KEY_W)) {
      desiredY = clamp(desiredY + NAV_SPEED, MIN_BOUND, MAX_BOUND);
    } else if (Input.isKeyPressed(Input.KEY_S)) {
      desiredY = clamp(desiredY - NAV_SPEED, MIN_BOUND, MAX_BOUND);
    }

    panX = lerp(panX, desiredX, NAV_LERP_SPEED);
    panY = lerp(panY, desiredY, NAV_LERP_SPEED);

    if (action == FactoryAction.PICK &&
      (Input.isButtonReleased(Input.BUTTON_RMB) || Input.isKeyPressed(Input.KEY_ESCAPE))) {
      action = FactoryAction.CLEAR;
    } else if (Input.isButtonReleased(Input.BUTTON_RMB)) {
      action = FactoryAction.DELETE;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && action == FactoryAction.PICK) {
      action = FactoryAction.PLACE;
    } else if (Input.isKeyReleased(Input.KEY_R) && action == FactoryAction.PICK) {
      action = FactoryAction.ROTATE;
    }
  }

  private void applyState() {
    Camera.lookAt(panX, panY);

    switch (action) {
      case PLACE -> {
        Position pos = getMouseWorldPosition();
        factory.createMachine(pos, machineInfo, direction);
        action = FactoryAction.PICK;
        updateUI();
      }
      case ROTATE -> {
        direction = Direction.cycle(direction, 1);
        action = FactoryAction.PICK;
      }
      case DELETE -> {
        Position pos = getMouseWorldPosition();
        factory.removeMachine(pos);
        action = FactoryAction.CLEAR;
      }
      case CLEAR -> {
        resetState();
        updateUI();
      }
    }
  }

  private void resetState() {
    pathIndices.clear();
    action = FactoryAction.DEFAULT;
    direction = Direction.UP;
    machineInfo = null;
  }

  private Position getMouseWorldPosition() {
    Position mousePos = Input.getMousePosition();
    return Camera.screenToWorldPosition(mousePos.x, mousePos.y);
  }
}
