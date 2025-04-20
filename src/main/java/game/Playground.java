package game;

import dev.gamekit.core.Renderer;
import dev.gamekit.core.Scene;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Align;
import dev.gamekit.ui.widgets.Column;
import dev.gamekit.ui.widgets.Padding;
import dev.gamekit.ui.widgets.Widget;
import game.components.Factory;
import game.machines.Conveyor;
import game.machines.Machine;
import game.machines.Orientation;
import game.machines.Producer;
import game.ui.MachineButton;

import java.awt.*;

import static dev.gamekit.ui.widgets.AlignParam.horizontalAlignment;
import static dev.gamekit.ui.widgets.FlexParam.gapSize;
import static dev.gamekit.ui.widgets.MultiChildParentParam.children;
import static dev.gamekit.ui.widgets.PaddingParam.padding;
import static dev.gamekit.ui.widgets.SingleChildParentParam.child;

public class Playground extends Scene implements StateManager {
  private final Factory factory;

  private State state = State.DEFAULT;
  private Orientation orientation = Orientation.UP;
  private Machine.Info machineInfo;

  public Playground() {
    super("Playground");

    factory = new Factory();
  }

  @Override
  public Factory getFactory() { return factory; }

  @Override
  public State getState() { return state; }

  @Override
  public void setState(State state) { this.state = state; }

  @Override
  public Orientation getOrientation() { return orientation; }

  @Override
  public void setOrientation(Orientation orientation) { this.orientation = orientation; }

  @Override
  public Machine.Info getMachineInfo() { return machineInfo; }

  @Override
  protected void start() { add(factory); }

  @Override
  protected void update() {
    updateState();
    applyState();
  }

  @Override
  protected void render() {
    Renderer.setBackground(Color.WHITE);
    Renderer.clear();
    renderState();
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
                    machineInfo = Producer.INFO;
                    state = State.PICK;
                  }
                }),
                MachineButton.create(Conveyor.INFO, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    machineInfo = Conveyor.INFO;
                    state = State.PICK;
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
