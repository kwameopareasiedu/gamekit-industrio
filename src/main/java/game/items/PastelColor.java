package game.items;

import java.awt.*;

public class PastelColor extends Color {
  public static final PastelColor RED = new PastelColor(0xff8080);
  public static final PastelColor GREEN = new PastelColor(0x80ff80);
  public static final PastelColor BLUE = new PastelColor(0x8080ff);
  public static final PastelColor CYAN = new PastelColor(0x80ffff);
  public static final PastelColor MAGENTA = new PastelColor(0xff80ff);
  public static final PastelColor YELLOW = new PastelColor(0xffff80);
  public static final PastelColor WHITE = new PastelColor(0xffffff);
  public static final PastelColor BLACK = new PastelColor(0x202020);
  public static final PastelColor GRAY = new PastelColor(0x808080);

  private PastelColor(int rgba) {
    super(rgba);
  }
}
