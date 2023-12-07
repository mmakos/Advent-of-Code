package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;

import static java.lang.Math.max;
import static java.lang.Math.min;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day5 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return solve(false);
  }

  private static int task2() {
    return solve(true);
  }

  private static int solve(boolean diagonal) {
    int[][] array = new int[1000][1000];
    input().forEach((p1, p2) -> addToArray(array, p1, p2, diagonal));
    return countOnes(array);
  }

  private static int countOnes(int[][] array) {
    return (int) Arrays.stream(array)
            .flatMapToInt(Arrays::stream)
            .filter(i -> i > 1)
            .count();
  }

  private static void addToArray(int[][] array, Point start, Point end, boolean diagonal) {
    if (start.y() == end.y()) {
      for (int x = min(start.x(), end.x()); x <= max(start.x(), end.x()); ++x) {
        ++array[start.y()][x];
      }
    } else if (start.x() == end.x()) {
      for (int y = min(start.y(), end.y()); y <= max(start.y(), end.y()); ++y) {
        ++array[y][start.x()];
      }
    } else if (diagonal) {
      Point minX = start.x() < end.x() ? start : end;
      Point minY = start.y() < end.y() ? start : end;
      int incY = minX == minY ? 1 : -1;
      for (int x = minX.x(), y = minX.y(); x <= max(start.x(), end.x()); ++x, y += incY) {
        ++array[y][x];
      }
    }
  }

  private static Pair.Stream<Point, Point> input() {
    return Pair.Stream.pairStream(Utils.lines(5, 2021).map(s -> s.split(" -> ")), s -> s[0], s -> s[1])
            .mapFirst(Day5::point)
            .mapSecond(Day5::point);
  }

  private static Point point(String s) {
    String[] split = s.split(",");
    return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
  }
}
