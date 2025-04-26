package game.resources;

import dev.gamekit.core.IO;
import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;

import java.awt.image.BufferedImage;

public final class Deposit extends Prop {
  private static final BufferedImage ROCK_DEPOSIT_IMAGE = IO.getResourceImage("rock-deposit.png");

  public final Resource.Type type;
  public final int row;
  public final int col;
  public final int index;

  public Deposit(Resource.Type type, int row, int col) {
    super(String.format("%s deposit", type));
    this.type = type;
    this.row = row;
    this.col = col;
    this.index = Utils.rowColToIndex(row, col);
  }

  public Resource extract(int row, int col) {
    return switch (type) {
      case ROCK -> new Resource(Resource.Type.ROCK, row, col);
    };
  }

  public BufferedImage getImage() {
    return switch (type) {
      case ROCK -> ROCK_DEPOSIT_IMAGE;
    };
  }

  @Override
  protected void render() {
    BufferedImage image = getImage();
    Position pos = Utils.indexToWorldPosition(index);

    Renderer.drawImage(
      image, pos.x, pos.y,
      Constants.CELL_PIXEL_SIZE,
      Constants.CELL_PIXEL_SIZE
    );
  }
}
