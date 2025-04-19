package game;

import dev.gamekit.core.*;
import dev.gamekit.ui.Spacing;
import dev.gamekit.ui.enums.Alignment;
import dev.gamekit.ui.events.MouseEvent;
import dev.gamekit.ui.widgets.Button;
import dev.gamekit.ui.widgets.Image;
import dev.gamekit.ui.widgets.*;
import dev.gamekit.utils.Position;
import game.components.Grid;
import game.components.Machine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
import static dev.gamekit.utils.Math.toInt;

public class Playground extends Scene {
  private final BufferedImage mach1Icon = IO.getResourceImage("icons/mach1.png");
  private final BufferedImage mach2Icon = IO.getResourceImage("icons/mach2.png");
  private final BufferedImage mach3Icon = IO.getResourceImage("icons/mach3.png");
  private final Grid grid;
  private final List<Machine> machines;

  private ClickMode mode = ClickMode.NONE;
  private BufferedImage clickedImage = null;

  public Playground() {
    super("Playground");

    grid = new Grid(10);
    machines = new ArrayList<>();
  }

  @Override
  protected void onStart() {
    super.onStart();
    add(grid);
  }

  @Override
  protected void onUpdate() {
    super.onUpdate();

    if (Input.isButtonClicked(Input.BUTTON_RMB)) {
      mode = ClickMode.NONE;
      clickedImage = null;
    } else if (Input.isButtonClicked(Input.BUTTON_LMB)) {
      if (mode == ClickMode.SELECT)
        mode = ClickMode.PLACE;
    }

    //    for (int i = 0; i < grid.length; i++) {
    //      Machine[] cells = grid[i];
    //
    //      for (int j = 0; j < cells.length; j++) {
    //        int x = toInt((i + 0.5) * Constants.CELL_PX_SIZE - 0.5 * width);
    //        int y = toInt((j + 0.5) * Constants.CELL_PX_SIZE - 0.5 * height);
    //
    //        Renderer.setColor(Color.GRAY);
    //        Renderer.setStroke(renderStroke);
    //        Renderer.drawRect(
    //          x, -y, Constants.CELL_PX_SIZE,
    //          Constants.CELL_PX_SIZE
    //        );
    //      }
    //    }

    switch (mode) {
      case NONE -> clickedImage = null;
      case PLACE -> {
        Position pos = Input.getMousePosition();
        Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
        int row = toInt(Math.floor((0.5 * grid.width + worldPos.x) / Constants.CELL_PX_SIZE));
        int col = toInt(Math.floor((0.5 * grid.height + worldPos.y) / Constants.CELL_PX_SIZE));
        System.out.printf("R:%d, C:%d\n", row, col);
        mode = ClickMode.NONE;
      }
    }
  }

  @Override
  protected void onRender() {
    Renderer.setBackground(Color.WHITE);
    Renderer.clear();

    if (mode == ClickMode.SELECT) {
      Position pos = Input.getMousePosition();
      Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
      Renderer.drawImage(clickedImage, worldPos.x, worldPos.y, Constants.CELL_PX_SIZE, Constants.CELL_PX_SIZE);
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
                    mode = ClickMode.SELECT;
                  }
                }),
                MachineButton.create(mach2Icon, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    clickedImage = mach2Icon;
                    mode = ClickMode.SELECT;
                  }
                }),
                MachineButton.create(mach3Icon, (e) -> {
                  if (e.type == MouseEvent.Type.CLICK) {
                    clickedImage = mach3Icon;
                    mode = ClickMode.SELECT;
                  }
                })
              )
            )
          )
        )
      )
    );
  }

  private enum ClickMode {
    NONE, SELECT, PLACE
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
