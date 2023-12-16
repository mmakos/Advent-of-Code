package pl.mmakos.advent.utils;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Point {
  private final int x;
  private final int y;

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

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

  public Point translateX(int x) {
    return new Point(this.x + x, y);
  }

  public Point translateY(int y) {
    return new Point(x, this.y + y);
  }

  public Point translate(int x, int y) {
    return new Point(this.x + x, this.y + y);
  }

  public boolean isAfter(Rect rect) {
    return x >= rect.x() + rect.width() || y >= rect.y() + rect.height();
  }

  public boolean validBounds(char[][] array) {
    return validBounds(0, 0, array[0].length, array.length);
  }

  public boolean validBounds(boolean[][] array) {
    return validBounds(0, 0, array[0].length, array.length);
  }

  public boolean validBounds(int[][] array) {
    return validBounds(0, 0, array[0].length, array.length);
  }

  public boolean validBounds(int x, int y, int width, int height) {
    return this.x >= x && this.y >= y && this.x < x + width && this.y < y + height;
  }
}
