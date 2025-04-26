package game.items;

import dev.gamekit.core.IO;
import dev.gamekit.core.Prop;

import java.awt.image.BufferedImage;

public abstract class ItemSource<T extends Item> extends Prop {
  private static final BufferedImage ICON = IO.getResourceImage("item.png");

  public final int row;
  public final int col;

  public ItemSource(String name, int row, int col) {
    super(name);
    this.row = row;
    this.col = col;
  }

  public abstract BufferedImage getIcon();
}
