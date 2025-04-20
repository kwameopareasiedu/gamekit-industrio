package game.components;

import dev.gamekit.core.Camera;
import dev.gamekit.core.Input;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;
import game.enums.Action;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dev.gamekit.utils.Math.toInt;

public class Factory extends Prop {
  public final int size;
  public final int pixelSize;
  private final Stroke outlineRenderStroke;
  private final Stroke innerRenderStroke;
  private final List<Machine> machines;

  public Factory() {
    super("Factory");
    size = Constants.GRID_SIZE;
    pixelSize = size * Constants.CELL_PIXEL_SIZE;
    outlineRenderStroke = new BasicStroke(
      2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
      0, new float[]{ 10 }, 5
    );
    innerRenderStroke = new BasicStroke(
      2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
      0, new float[]{ 10 }, 5
    );
    machines = new ArrayList<>();
  }

  @Override
  protected void render() {
    Renderer.setColor(Color.GRAY);
    Renderer.setStroke(outlineRenderStroke);
    Renderer.drawRect(0, 0, pixelSize, pixelSize);

    for (int i = 1; i < size; i++) {
      int x = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(Color.GRAY);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineV(x, toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize));
    }

    for (int i = 1; i < size; i++) {
      int y = toInt(i * Constants.CELL_PIXEL_SIZE - 0.5 * pixelSize);

      Renderer.setColor(Color.GRAY);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineH(toInt(-0.5 * pixelSize), toInt(0.5 * pixelSize), y);
    }
  }

  public void setAction(Action action) {
    switch (action) {
      case PLACE -> {
        Position pos = Input.getMousePosition();
        Position worldPos = Camera.screenToWorldPosition(pos.x, pos.y);
        Position indexPos = Utils.positionToIndex(worldPos);
        System.out.printf("R:%d, C:%d\n", indexPos.y, indexPos.x);

        Machine producer = new Producer("Producer", indexPos.y, indexPos.x);
        machines.add(producer);
        addChild(producer);
      }
    }
  }
}
