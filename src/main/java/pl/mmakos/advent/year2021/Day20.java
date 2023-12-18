package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Rect;
import pl.mmakos.advent.utils.Utils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day20 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return solve(2);
  }

  private static int task2() {
    return solve(50);
  }

  private static int solve(int times) {
    String algorithm = algorithm();
    Set<Point> image = image();
    for (int i = 0; i < times; ++i) {
      image = enhance(algorithm, image, i % 2 == 1);
    }
    return image.size();
  }

  private static Set<Point> enhance(String algorithm, Set<Point> image, boolean fillBoundary) {
    Rect bounds = Rect.bounds(image);
    if (fillBoundary) {
      fillBoundary(image);
      fillBoundary(image);
    }

    return IntStream.rangeClosed(bounds.x() - 1, bounds.maxX() + 1)
            .boxed()
            .flatMap(i -> IntStream.rangeClosed(bounds.y() - 1, bounds.maxY() + 1)
                    .mapToObj(j -> new Point(i, j)))
            .filter(p -> algorithm. charAt(mask(p, image)) == '#')
            .collect(Collectors.toSet());
  }

  private static void fillBoundary(Set<Point> points) {
    Rect bounds = Rect.bounds(points);
    for (int x = bounds.x() - 1; x <= bounds.maxX() + 1; ++x) {
      points.add(new Point(x, bounds.y() - 1));
      points.add(new Point(x, bounds.maxY() + 1));
    }
    for (int y = bounds.y(); y <= bounds.maxY(); ++y) {
      points.add(new Point(bounds.x() - 1, y));
      points.add(new Point(bounds.maxX() + 1, y));
    }
  }

  private static int mask(Point point, Set<Point> points) {
    Point[] p = new Point[]{
            point.top().left(), point.top(), point.top().right(),
            point.left(), point, point.right(),
            point.bottom().left(), point.bottom(), point.bottom().right()};
    int result = 0;
    for (Point value : p) {
      result <<= 1;
      if (points.contains(value)) {
        result |= 1;
      }
    }
    return result;
  }

  private static String algorithm() {
    return Utils.lines().findFirst().orElseThrow();
  }

  private static Set<Point> image() {
    char[][] image = Utils.lines()
            .skip(2)
            .map(String::toCharArray)
            .toArray(char[][]::new);


    return IntStream.range(0, image.length)
            .boxed()
            .flatMap(i -> IntStream.range(0, image[i].length)
                    .filter(j -> image[i][j] == '#')
                    .mapToObj(j -> new Point(j, i)))
            .collect(Collectors.toSet());
  }
}
