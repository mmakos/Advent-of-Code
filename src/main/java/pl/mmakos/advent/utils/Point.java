package pl.mmakos.advent.utils;

public record Point(int x, int y) {
  public Point left() {
    return new Point(x - 1, y);
  }

  public Point right() {
    return new Point(x + 1, y);
  }

  public Point top() {
    return new Point(x, y - 1);
  }

  public Point bottom() {
    return new Point(x, y + 1);
  }

  public Point moveX(int x) {
    return new Point(x, y);
  }

  public Point moveY(int y) {
    return new Point(x, y);
  }
}
