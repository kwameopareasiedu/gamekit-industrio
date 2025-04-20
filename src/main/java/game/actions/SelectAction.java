package game.actions;

import game.components.Machine;

public class SelectAction extends Action {
  public final Machine.Info info;

  public SelectAction(Machine.Info info) {
    this.info = info;
  }
}
