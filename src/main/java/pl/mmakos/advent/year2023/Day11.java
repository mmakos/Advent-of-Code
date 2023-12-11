package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.lang.Math.abs;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day11 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return solve(1);
  }

  private static long task2() {
    return solve(999_999);
  }

  private static long solve(int expansion) {
    Point[] galaxies = input(expansion);

    return LongStream.range(0, galaxies.length)
            .map(i -> LongStream.range(i + 1, galaxies.length)
                    .map(j -> distance(galaxies[(int) i], galaxies[(int) j]))
                    .sum())
            .sum();
  }

  private static int distance(Point p1, Point p2) {
    return abs(p1.x() - p2.x()) + abs(p1.y() - p2.y());
  }

  private static Point[] input(int expansion) {
    String[] array = Utils.lines().toArray(String[]::new);
    Point[] points = IntStream.range(0, array.length)
            .boxed()
            .flatMap(i -> IntStream.range(0, array[i].length())
                    .filter(j -> array[i].charAt(j) == '#')
                    .mapToObj(j -> new Point(j, i)))
            .toArray(Point[]::new);

    int[] emptyRows = IntStream.range(0, array.length)
            .filter(i -> Arrays.stream(points).noneMatch(p -> p.y() == i))
            .toArray();
    int[] emptyCols = IntStream.range(0, array[0].length())
            .filter(i -> Arrays.stream(points).noneMatch(p -> p.x() == i))
            .toArray();

    for (int i = emptyRows.length - 1; i >= 0; --i) {
      for (int j = 0; j < points.length; ++j) {
        Point point = points[j];
        if (point.y() >= emptyRows[i]) {
          points[j] = point.moveY(point.y() + expansion);
        }
      }
    }
    for (int i = emptyCols.length - 1; i >= 0; --i) {
      for (int j = 0; j < points.length; ++j) {
        Point point = points[j];
        if (point.x() >= emptyCols[i]) {
          points[j] = point.moveX(point.x() + expansion);
        }
      }
    }

    return points;
  }
}
