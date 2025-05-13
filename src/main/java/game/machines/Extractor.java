package game.machines;

import dev.gamekit.core.IO;
import game.factory.Factory;
import game.resources.Shape;
import game.resources.Source;

import java.awt.image.BufferedImage;

public final class Extractor extends Machine {
  private static final BufferedImage IMAGE = IO.getResourceImage("machines/extractor.png");
  public static final Info INFO = new Info("Extractor", IMAGE);
  public static int TICKS_FOR_EXTRACTION = 7;

  private final Source source;
  private int tickCounter = TICKS_FOR_EXTRACTION;
//
//  public static Extractor create(int index, Direction direction, Source source) {
//    if (direction == null || source == null)
//      return null;
//    return new Extractor(index, direction, source);
//  }

  public Extractor(int row, int col, Factory factory, Direction direction, Source source) {
    super("Extractor", row, col, factory, direction, Port.Type.OUT, null, null, null);
    this.source = source;
  }

  @Override
  public void tick() {
    tickCounter -= 1;

    if (tickCounter <= 0) {
      Port outputPort = outputs.get(0);

      if (!outputPort.hasItem()) {
        Shape shape = source.extract(position);
        outputPort.item = shape;
        factory.addItem(shape);
      }

      tickCounter = TICKS_FOR_EXTRACTION;
    }
  }

  @Override
  public BufferedImage getImage() { return IMAGE; }
}
