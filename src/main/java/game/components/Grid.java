package game.components;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import game.Constants;

import java.awt.*;

import static dev.gamekit.utils.Math.toInt;

public class Grid extends Prop {
  public final int width;
  public final int height;
  private final Stroke outlineRenderStroke;
  private final Stroke innerRenderStroke;
  private final Machine[][] grid;

  public Grid(int size) {
    super("Grid");

    grid = new Machine[size][size];
    outlineRenderStroke = new BasicStroke(
      2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
      0, new float[]{ 2 * size }, size
    );
    innerRenderStroke = new BasicStroke(
      2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
      0, new float[]{ 2 * size }, size
    );
    width = height = size * Constants.CELL_PX_SIZE;
  }

  @Override
  protected void onRender() {
    super.onRender();

    Renderer.setColor(Color.GRAY);
    Renderer.setStroke(outlineRenderStroke);
    Renderer.drawRect(0, 0, width, height);

    for (int i = 1; i < grid.length; i++) {
      int x = toInt(i * Constants.CELL_PX_SIZE - 0.5 * width);

      Renderer.setColor(Color.GRAY);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineV(x, toInt(-0.5 * height), toInt(0.5 * height));
    }

    for (int i = 1; i < grid.length; i++) {
      int y = toInt(i * Constants.CELL_PX_SIZE - 0.5 * height);

      Renderer.setColor(Color.GRAY);
      Renderer.setStroke(innerRenderStroke);
      Renderer.drawLineH(toInt(-0.5 * width), toInt(0.5 * width), y);
    }
  }
}
