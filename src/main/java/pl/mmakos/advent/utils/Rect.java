package pl.mmakos.advent.utils;

import java.util.Collection;
import java.util.IntSummaryStatistics;

@SuppressWarnings("unused")
public record Rect(int x, int y, int width, int height) {
  public int maxX() {
    return x + width - 1;
  }

  public int maxY() {
    return y + height - 1;
  }

  public boolean contains(Point point) {
    return point.x() >= x && point.y() >= y && point.x() < x + width && point.y() < y + height;
  }

  public int surface() {
    return width * height;
  }

  public static Rect bounds(Collection<Point> points) {
    IntSummaryStatistics x = points.stream()
            .mapToInt(Point::x)
            .summaryStatistics();
    IntSummaryStatistics y = points.stream()
            .mapToInt(Point::y)
            .summaryStatistics();

    return new Rect(x.getMin(), y.getMin(), x.getMax() - x.getMin() + 1, y.getMax() - y.getMin() + 1);
  }
}
