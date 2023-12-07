package pl.mmakos.advent.utils;

public class Int {
  private int i;

  public int get() {
    return i;
  }

  public void set(int i) {
    this.i = i;
  }

  public void inc() {
    ++i;
  }

  public void inc(int i) {
    this.i += i;
  }

  public void dec() {
    --i;
  }

  public void dec(int i) {
    this.i -= i;
  }

  public void setIfMax(int i) {
    if (i > this.i) {
      this.i = i;
    }
  }
}
