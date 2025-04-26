package game.items;

import dev.gamekit.core.Prop;

import java.awt.image.BufferedImage;

public abstract class Item extends Prop {
  public final int row;
  public final int col;

  public Item(String name, int row, int col) {
    super(name);
    this.row = row;
    this.col = col;
  }

  public abstract BufferedImage getIcon();
}
