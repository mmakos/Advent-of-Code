package pl.mmakos.advent.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Bool {
  private boolean bool;

  public boolean get() {
    return bool;
  }

  public void set(boolean bool) {
    this.bool = bool;
  }

  public Bool copy() {
    return new Bool(bool);
  }
}
