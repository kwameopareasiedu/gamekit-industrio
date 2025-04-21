package game.machines;

import dev.gamekit.core.Prop;
import dev.gamekit.core.Renderer;
import dev.gamekit.utils.Position;
import game.Constants;
import game.Utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Machine extends Prop {
  public final int gridIndex;
  public final Direction direction;
  public final Port topPort;
  public final Port rightPort;
  public final Port bottomPort;
  public final Port leftPort;
  protected final List<Port> inputs;
  protected final List<Port> outputs;

  public Machine(
    String name,
    int gridIndex,
    Direction direction,
    Port topPort,
    Port rightPort,
    Port bottomPort,
    Port leftPort
  ) {
    super(name);
    this.gridIndex = gridIndex;
    this.direction = direction;

    this.topPort = switch (direction) {
      case UP -> topPort;
      case RIGHT -> leftPort;
      case DOWN -> bottomPort;
      case LEFT -> rightPort;
    };

    this.rightPort = switch (direction) {
      case UP -> rightPort;
      case RIGHT -> topPort;
      case DOWN -> leftPort;
      case LEFT -> bottomPort;
    };

    this.bottomPort = switch (direction) {
      case UP -> bottomPort;
      case RIGHT -> rightPort;
      case DOWN -> topPort;
      case LEFT -> leftPort;
    };

    this.leftPort = switch (direction) {
      case UP -> leftPort;
      case RIGHT -> bottomPort;
      case DOWN -> rightPort;
      case LEFT -> topPort;
    };

    inputs = new ArrayList<>();
    outputs = new ArrayList<>();

    List<Port> tempList = new ArrayList<>();
    tempList.add(this.topPort);
    tempList.add(this.rightPort);
    tempList.add(this.bottomPort);
    tempList.add(this.leftPort);

    for (Port p : tempList) {
      if (p == null) continue;

      switch (p.type) {
        case IN -> inputs.add(p);
        case OUT -> outputs.add(p);
      }
    }
  }

  public abstract void process();

  public final void output() {
    for (Port p : outputs) {
      if (p.linked != null && p.linked.cargo == null) {
        p.linked.cargo = p.cargo;
        p.cargo = null;
      }
    }
  }

  public abstract BufferedImage getImage();

  @Override
  protected void render() {
    BufferedImage icon = getImage();
    Position pos = Utils.indexToWorldPosition(gridIndex);

    Renderer.withRotation(
      pos.x, pos.y, direction.getAngle(),
      () -> Renderer.drawImage(
        icon, pos.x, pos.y,
        Constants.CELL_PIXEL_SIZE,
        Constants.CELL_PIXEL_SIZE
      )
    );
  }

  public record Info(String name, BufferedImage icon) { }
}
