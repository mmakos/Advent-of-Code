package pl.mmakos.advent.utils;

public record Rect(int x, int y, int width, int height) {
  public boolean contains(Point point) {
    return point.x() >= x && point.y() >= y && point.x() < x + width && point.y() < y + height;
  }
}
