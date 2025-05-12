package game.factory;

import dev.gamekit.audio.AudioClip2D;
import dev.gamekit.audio.AudioGroup;
import dev.gamekit.core.*;
import dev.gamekit.settings.ImageInterpolation;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.enums.CrossAxisAlignment;
import dev.gamekit.ui.enums.MainAxisAlignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.Panel;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import dev.gamekit.utils.Task;
import game.levels.Menu;
import game.machines.*;
import game.resources.Shape;
import game.resources.Source;
import game.ui.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static dev.gamekit.utils.Math.clamp;
import static dev.gamekit.utils.Math.lerp;

public abstract class FactoryController extends Scene {
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
  private static final double NAV_SPEED = 3;
  private static final double NAV_LERP_SPEED = 0.02;
  private static final Color CLEAR_COLOR = new Color(0x202039);
  private static final Color SCRIM_COLOR = new Color(0x99000000, true);

  static {
    Audio.preload("placed", new AudioClip2D("audio/btn-hover.wav", AudioGroup.EFFECTS, 1));
    Audio.preload("removed", new AudioClip2D("audio/removed.wav", AudioGroup.EFFECTS, 1));
    Audio.preload("completed", new AudioClip2D("audio/completed.wav", AudioGroup.EFFECTS, 1));
  }

  protected final int level;
  protected final Machine.Info[] machineInfos;
  protected final FactoryGoal goal;
  protected final Task onCompleted;

  protected Factory factory;

  private FactoryAction action = FactoryAction.DEFAULT;
  private Direction direction = Direction.UP;
  private Machine.Info selectedMachineInfo;
  private Machine.Info hoverMachineInfo;
  private double desiredX = 0;
  private double desiredY = 0;
  private double panX = 0;
  private double panY = 0;
  private int prevDragRow = -1;
  private int prevDragCol = -1;

  private final double minBound;
  private final double maxBound;
  private boolean completed = false;

  public FactoryController(
    int level,
    Machine.Info[] machineInfos,
    Source[] sources,
    FactoryGoal goal,
    Task onCompleted
  ) {
    super("Factory Controller");
    int gridSize = getGridSize();

    this.minBound = -0.5 * gridSize * Factory.CELL_PIXEL_SIZE;
    this.maxBound = 0.5 * gridSize * Factory.CELL_PIXEL_SIZE;

    this.level = level;
    this.machineInfos = machineInfos;
    this.factory = new Factory(
      gridSize, sources,
      shape -> {
        factory.removeItem(shape);
        goal.track(shape);

        if (!goal.isCompleted()) {
          Audio.get("placed").play();
        } else if (goal.isCompleted() && !completed) {
          Audio.get("completed").play();
          factory.close();
          completed = true;
        }

        updateUI();
      }
    );
    this.goal = goal;
    this.onCompleted = onCompleted;
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

    if (action == FactoryAction.PICK || action == FactoryAction.DRAG_PLACE) {
      Position mousePos = getMouseWorldPosition();

      Renderer.withRotation(
        mousePos.x, mousePos.y, direction.getAngle(),
        () ->
          Renderer.drawImage(
            selectedMachineInfo.image(), mousePos.x, mousePos.y,
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
            Panel.options().background(LEVEL_PANEL_BG).ninePatch(32, 20),
            Padding.create(
              Padding.options().padding(32),
              Column.create(
                Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(4),
                Text.create(
                  Text.options().color(Color.LIGHT_GRAY).alignment(Alignment.CENTER),
                  "Campaign"
                ),
                Text.create(
                  Text.options().color(Color.WHITE).fontSize(32).fontStyle(Font.BOLD)
                    .alignment(Alignment.CENTER),
                  String.format("Level %d", level)
                ),
                Padding.create(
                  Padding.options().padding(0, 16),
                  Sized.create(
                    Sized.options().width(64).height(2),
                    Colored.create(Color.GRAY, 0)
                  )
                ),
                Text.create(
                  Text.options().color(Color.LIGHT_GRAY).alignment(Alignment.CENTER),
                  "Goal"
                ),
                Sized.create(
                  Sized.options().width(36).height(36),
                  Colored.create(goal.color, goal.type == Shape.Type.CIRCLE ? 50 : 12)
                ),
                Row.create(
                  Row.options().mainAxisAlignment(MainAxisAlignment.CENTER)
                    .crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(12),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(32).fontStyle(Font.BOLD),
                    String.valueOf(goal.getCount())
                  ),
                  Text.create(
                    Text.options().color(Color.LIGHT_GRAY).fontStyle(Font.BOLD),
                    "/"
                  ),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(32).fontStyle(Font.BOLD),
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
              Text.options().color(Color.WHITE).fontStyle(Font.BOLD)
                .fontSize(24).alignment(Alignment.CENTER),
              "Place machines on your factory floor to achieve your goal"
            ),
            Empty.create(),
            action == FactoryAction.PICK ?
              Column.create(
                Column.options().gapSize(6).crossAxisAlignment(CrossAxisAlignment.CENTER),
                Padding.create(
                  Padding.options().padding(0, 0, 12, 0),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(24).alignment(Alignment.CENTER),
                    "LEFT CLICK to place"
                  )
                ),
                Row.create(
                  Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Sized.create(
                    Sized.options().width(72).height(36),
                    Image.create(
                      Image.options().interpolation(ImageInterpolation.NEAREST),
                      KEY_ESC_IMG
                    )
                  ),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(24).alignment(Alignment.CENTER),
                    "or RIGHT CLICK to cancel"
                  )
                ),
                Row.create(
                  Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(
                      Image.options().interpolation(ImageInterpolation.NEAREST),
                      KEY_R_IMG
                    )
                  ),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(24).alignment(Alignment.CENTER),
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
                    Text.options().color(Color.WHITE).fontSize(24).alignment(Alignment.CENTER),
                    "Move around with"
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(
                      Image.options().interpolation(ImageInterpolation.NEAREST),
                      KEY_W_IMG
                    )
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(
                      Image.options().interpolation(ImageInterpolation.NEAREST),
                      KEY_A_IMG
                    )
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(
                      Image.options().interpolation(ImageInterpolation.NEAREST),
                      KEY_S_IMG
                    )
                  ),
                  Sized.create(
                    Sized.options().width(36).height(36),
                    Image.create(
                      Image.options().interpolation(ImageInterpolation.NEAREST),
                      KEY_D_IMG
                    )
                  )
                ),
                Padding.create(
                  Padding.options().padding(12, 0, 0, 0),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(24).alignment(Alignment.CENTER),
                    "RIGHT CLICK to delete"
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
          Row.create(
            Row.options().mainAxisAlignment(MainAxisAlignment.END),

            // Machine help panel
            hoverMachineInfo == null ?
              Empty.create() :
              hoverMachineInfo == Extractor.INFO ?
                new ExtractorHelpPanel() :
                hoverMachineInfo == Belt.INFO ?
                  new BeltHelpPanel() :
                  hoverMachineInfo == Mixer.INFO ?
                    new MixerHelpPanel() :
                    hoverMachineInfo == Reshaper.INFO ?
                      new ReshaperHelpPanel() :
                      hoverMachineInfo == HueShifter.INFO ?
                        new HueShifterHelpPanel() :
                        Empty.create(),

            // Machine select panel
            Panel.create(
              Panel.options().background(MACHINES_PANEL_BG).ninePatch(0, 20),
              Padding.create(
                Padding.options().padding(32),
                Column.create(
                  Column.options().gapSize(36).crossAxisAlignment(CrossAxisAlignment.CENTER),
                  Text.create(
                    Text.options().color(Color.WHITE).fontSize(24).fontStyle(Font.BOLD),
                    "Machines"
                  ),
                  Column.create(
                    Column.options().gapSize(32).crossAxisAlignment(CrossAxisAlignment.STRETCH),
                    Arrays.stream(machineInfos).map(info ->
                      MachineButton.create(info, (e) -> {
                        if (goal.isCompleted())
                          return;

                        if (e.type == MouseEvent.Type.ENTER) {
                          Application.getInstance().scheduleTask(() -> {
                            hoverMachineInfo = info;
                            updateUI();
                          });
                        } else if (e.type == MouseEvent.Type.EXIT) {
                          Application.getInstance().scheduleTask(() -> {
                            if (hoverMachineInfo == info)
                              hoverMachineInfo = null;
                            updateUI();
                          });
                        } else if (e.type == MouseEvent.Type.CLICK) {
                          Application.getInstance().scheduleTask(() -> {
                            selectedMachineInfo = info;
                            action = FactoryAction.PICK;
                          });
                          updateUI();
                        }
                      })
                    ).toArray(MachineButton[]::new)
                  )
                )
              )
            )
          )
        )
      ),

      // Level completed panel
      goal.isCompleted() ?
        Stack.create(
          Sized.create(
            Sized.options().fractionalWidth(1).fractionalHeight(1),
            Colored.create(SCRIM_COLOR, 0)
          ),

          Align.create(
            Align.options().horizontalAlignment(Alignment.CENTER)
              .verticalAlignment(Alignment.CENTER),
            Sized.create(
              Sized.options().fractionalWidth(0.75).intrinsicHeight(),
              Panel.create(
                Panel.options().background(COMPLETED_PANEL_BG).ninePatch(72, 32),
                Padding.create(
                  Padding.options().padding(80),
                  Column.create(
                    Column.options().crossAxisAlignment(CrossAxisAlignment.CENTER).gapSize(12),
                    Text.create(
                      Text.options().color(Color.WHITE).fontSize(56).fontStyle(Font.BOLD),
                      "Level Completed"
                    ),
                    Row.create(
                      Row.options().gapSize(12).crossAxisAlignment(CrossAxisAlignment.CENTER),
                      MenuButton.create(
                        "Start Next Level",
                        onCompleted::run
                      ),
                      MenuButton.create(
                        "Main Menu",
                        this::returnToMainMenu
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

  protected abstract int getGridSize();

  private void updateState() {
    if (goal.isCompleted()) {
      action = FactoryAction.DEFAULT;
      return;
    }

    if (Input.isKeyPressed(Input.KEY_D)) {
      desiredX = clamp(desiredX + NAV_SPEED, minBound, maxBound);
    } else if (Input.isKeyPressed(Input.KEY_A)) {
      desiredX = clamp(desiredX - NAV_SPEED, minBound, maxBound);
    }

    if (Input.isKeyPressed(Input.KEY_W)) {
      desiredY = clamp(desiredY + NAV_SPEED, minBound, maxBound);
    } else if (Input.isKeyPressed(Input.KEY_S)) {
      desiredY = clamp(desiredY - NAV_SPEED, minBound, maxBound);
    }

    if ((Input.isButtonReleased(Input.BUTTON_RMB) || Input.isKeyPressed(Input.KEY_ESCAPE)) && action == FactoryAction.PICK) {
      action = FactoryAction.CLEAR;
    } else if (Input.isButtonReleased(Input.BUTTON_RMB) && action == FactoryAction.DEFAULT) {
      action = FactoryAction.DELETE;
    } else if (Input.isButtonReleased(Input.BUTTON_RMB) && action == FactoryAction.DRAG_DELETE) {
      action = FactoryAction.CLEAR;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && (action == FactoryAction.PICK || action == FactoryAction.DRAG_PLACE)) {
      action = FactoryAction.PLACE;
    } else if (Input.isButtonReleased(Input.BUTTON_LMB) && action == FactoryAction.DRAG_PLACE) {
      action = FactoryAction.PICK;
      prevDragRow = prevDragCol = -1;
    } else if (Input.isButtonPressed(Input.BUTTON_RMB) && action == FactoryAction.DEFAULT) {
      action = FactoryAction.DRAG_DELETE;
    } else if (Input.isButtonPressed(Input.BUTTON_LMB) && action == FactoryAction.PICK && selectedMachineInfo == Belt.INFO) {
      action = FactoryAction.DRAG_PLACE;
    } else if (Input.isKeyReleased(Input.KEY_R) && action == FactoryAction.PICK) {
      action = FactoryAction.ROTATE;
    }
  }

  private void applyState() {
    panX = lerp(panX, desiredX, NAV_LERP_SPEED);
    panY = lerp(panY, desiredY, NAV_LERP_SPEED);
    Camera.lookAt(panX, panY);

    switch (action) {
      case PLACE -> {
        Position mousePos = getMouseWorldPosition();
        Position machinePos = factory.positionToGrid(mousePos);
        factory.createMachine(machinePos.y, machinePos.x, selectedMachineInfo, direction);
        Audio.get("placed").play();
        action = FactoryAction.PICK;
      }
      case DRAG_PLACE -> {
        Position mousePos = getMouseWorldPosition();
        Position currentDragGrid = factory.positionToGrid(mousePos);
        int currentDragRow = currentDragGrid.y, currentDragCol = currentDragGrid.x;

        int newIndex = factory.gridToIndex(currentDragRow, currentDragCol);
        int lastIndex = factory.gridToIndex(prevDragRow, prevDragCol);
        int diff = newIndex - lastIndex;

        if (diff == factory.getGridSize())
          direction = Direction.UP;
        else if (diff == 1)
          direction = Direction.RIGHT;
        else if (diff == -factory.getGridSize())
          direction = Direction.DOWN;
        else if (diff == -1)
          direction = Direction.LEFT;

        if (prevDragCol == -1 || lastIndex != newIndex) {
          if (prevDragCol != -1) {
            factory.createMachine(prevDragRow, prevDragCol, selectedMachineInfo, direction);
          }

          factory.createMachine(currentDragRow, currentDragCol, selectedMachineInfo, direction);
          prevDragRow = currentDragRow;
          prevDragCol = currentDragCol;
          Audio.get("placed").play();
        }
      }
      case ROTATE -> {
        direction = Direction.cycle(direction, 1);
        Audio.get("placed").play();
        action = FactoryAction.PICK;
      }
      case DELETE -> {
        Position mousePos = getMouseWorldPosition();
        Position machinePos = factory.positionToGrid(mousePos);

        if (factory.removeMachine(machinePos.y, machinePos.x))
          Audio.get("removed").play();

        action = FactoryAction.CLEAR;
      }
      case DRAG_DELETE -> {
        Position mousePos = getMouseWorldPosition();
        Position machinePos = factory.positionToGrid(mousePos);

        if (factory.removeMachine(machinePos.y, machinePos.x))
          Audio.get("removed").play();
      }
      case CLEAR -> {
        resetState();
        Application.getInstance().scheduleTask(
          this::updateUI
        );
      }
    }
  }

  private void resetState() {
    prevDragRow = prevDragCol = -1;
    action = FactoryAction.DEFAULT;
    direction = Direction.UP;
    selectedMachineInfo = null;
  }

  private Position getMouseWorldPosition() {
    Position mousePos = Input.getMousePosition();
    return Camera.screenToWorldPosition(mousePos.x, mousePos.y);
  }

  private void returnToMainMenu() {
    Application.getInstance().loadScene(
      new Menu()
    );
  }
}
