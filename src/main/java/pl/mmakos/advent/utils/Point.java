package pl.mmakos.advent.utils;


import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unused")
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

  public Point neighbour(int dir) {
    return switch (dir) {
      case 0 -> right();
      case 1 -> bottom();
      case 2 -> left();
      case 3 -> top();
      default -> throw new IllegalArgumentException();
    };
  }

  public Point[] neighbours() {
    return new Point[]{right(), left(), bottom(), top()};
  }

  public Stream<Point> neighbours(Rect bounds) {
    return Arrays.stream(neighbours())
            .filter(p -> p.validBounds(bounds));
  }

  public Point translateDir(int amount, int direction) {
    return switch (direction) {
      case 0 -> translateX(amount);
      case 1 -> translateY(amount);
      case 2 -> translateX(-amount);
      case 3 -> translateY(-amount);
      default -> throw new IllegalArgumentException();
    };
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

  public boolean validBounds(Rect rect) {
    return validBounds(rect.x(), rect.y(), rect.width(), rect.height());
  }

  public static Set<Point> fromCharArray(char[][] array, char pointChar) {
    return IntStream.range(0, array.length)
            .boxed()
            .flatMap(i -> IntStream.range(0, array[i].length)
                    .filter(j -> array[i][j] == pointChar)
                    .mapToObj(j -> new Point(j, i)))
            .collect(Collectors.toSet());
  }

  public static Point of(char[][] array, char distinctChar) {
    return IntStream.range(0, array.length)
            .boxed()
            .flatMap(i -> IntStream.range(0, array[i].length)
                    .filter(j -> array[i][j] == distinctChar)
                    .mapToObj(j -> new Point(j, i))
            ).findFirst()
            .orElseThrow();
  }
}
