package game;

import dev.gamekit.core.Camera;
import dev.gamekit.core.Input;
import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Align;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Padding;
import dev.gamekit.ui.widgets.Widget;
import dev.gamekit.utils.Position;
import game.actions.Action;
import game.actions.SelectAction;
import game.components.Conveyor;
import game.components.Factory;
import game.components.Producer;
import game.ui.MachineButton;

import java.awt.*;

import static dev.gamekit.ui.widgets.AlignParam.horizontalAlignment;
import static dev.gamekit.ui.widgets.FlexParam.gapSize;
import static dev.gamekit.ui.widgets.MultiChildParentParam.children;
import static dev.gamekit.ui.widgets.PaddingParam.padding;
import static dev.gamekit.ui.widgets.SingleChildParentParam.child;

public class Playground extends Scene {
  private final Factory factory;

  private Action action = Action.NO_OP;

  public Playground() {
    super("Playground");

    factory = new Factory();
  }

  @Override
  protected void start() {
    add(factory);
  }

  @Override
  protected void update() {
    if (Input.isButtonClicked(Input.BUTTON_RMB)) {
      action = Action.NO_OP;
    } else if (Input.isButtonClicked(Input.BUTTON_LMB)) {
      if (action instanceof SelectAction selectAction) {
        factory.createMachine(selectAction.info);
        action = Action.NO_OP;
      }
    }
  }

  @Override
  protected void render() {
    Renderer.setBackground(Color.WHITE);
    Renderer.clear();

    if (action instanceof SelectAction selectAction) {
      Position pos = Input.getMousePosition();
      Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
      Renderer.drawImage(
        selectAction.info.icon(), worldPos.x, worldPos.y,
        Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE
      );
    }
  }

  @Override
  public Widget onCreateUI() {
    return Align.create(
      horizontalAlignment(Alignment.END),
      child(
        Padding.create(
          padding(new Spacing(24, 48)),
          child(
            Column.create(
              gapSize(12),
              children(
                MachineButton.create(Producer.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    action = new SelectAction(Producer.INFO);
                  }
                }),
                MachineButton.create(Conveyor.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    action = new SelectAction(Conveyor.INFO);
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
