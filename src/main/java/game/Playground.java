package game;

import dev.gamekit.core.*;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import game.components.Factory;
import game.enums.Action;

import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.gamekit.ui.widgets.AlignParam.horizontalAlignment;
import static dev.gamekit.ui.widgets.ButtonParam.defaultBackground;
import static dev.gamekit.ui.widgets.ButtonParam.mouseListener;
import static dev.gamekit.ui.widgets.FlexParam.gapSize;
import static dev.gamekit.ui.widgets.ImageParam.image;
import static dev.gamekit.ui.widgets.MultiChildParentParam.children;
import static dev.gamekit.ui.widgets.PaddingParam.padding;
import static dev.gamekit.ui.widgets.SingleChildParentParam.child;
import static dev.gamekit.ui.widgets.SizedParam.height;
import static dev.gamekit.ui.widgets.SizedParam.width;

public class Playground extends Scene {
  private final BufferedImage mach1Icon = IO.getResourceImage("icons/mach1.png");
  private final BufferedImage mach2Icon = IO.getResourceImage("icons/mach2.png");
  private final BufferedImage mach3Icon = IO.getResourceImage("icons/mach3.png");
  private final Factory factory;

  private Action action = Action.NONE;
  private BufferedImage clickedImage = null;

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
      action = Action.NONE;
      clickedImage = null;
      factory.setAction(action);
    } else if (Input.isButtonClicked(Input.BUTTON_LMB)) {
      if (action == Action.SELECT) {
        action = Action.PLACE;
        factory.setAction(action);
      }
    }
  }

  @Override
  protected void render() {
    Renderer.setBackground(Color.WHITE);
    Renderer.clear();

    if (action == Action.SELECT) {
      Position pos = Input.getMousePosition();
      Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
      Renderer.drawImage(clickedImage, worldPos.x, worldPos.y, Constants.CELL_PIXEL_SIZE, Constants.CELL_PIXEL_SIZE);
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
                MachineButton.create(mach1Icon, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    clickedImage = mach1Icon;
                    action = Action.SELECT;
                  }
                }),
                MachineButton.create(mach2Icon, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    clickedImage = mach2Icon;
                    action = Action.SELECT;
                  }
                }),
                MachineButton.create(mach3Icon, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    clickedImage = mach3Icon;
                    action = Action.SELECT;
                  }
                })
              )
            )
          )
        )
      )
    );
  }

  private static class MachineButton extends Compose {
    protected MachineButton(BufferedImage image, MouseEvent.Listener mouseListener) {
      super(
        Sized.create(
          width(64),
          height(64),
          child(
            Button.create(
              defaultBackground(null),
              mouseListener(mouseListener),
              child(
                Image.create(
                  image(image)
                )
              )
            )
          )
        )
      );
    }

    public static MachineButton create(
      BufferedImage image,
      MouseEvent.Listener mouseListener
    ) {
      return new MachineButton(image, mouseListener);
    }

    @Override
    public boolean stateEquals(Widget widget) {
      return widget instanceof MachineButton;
    }
  }
}
