package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day13 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2:\n" + task2());
  }

  private static int task1() {
    return solve(true).size();
  }

  private static String task2() {
    return toString(solve(false));
  }

  private static Set<Point> solve(boolean singleFold) {
    Pair<Set<Point>, int[][]> input = input();
    Set<Point> points = input.first();
    int[][] folds = input.second();
    if (singleFold) {
      folds = new int[][]{folds[0]};
    }

    for (int[] fold : folds) {
      Set<Point> foldedPoints;

      foldedPoints = points.stream()
              .filter(p -> fold[0] == 0 ? p.x() > fold[1] : p.y() > fold[1])
              .collect(Collectors.toSet());
      points.removeAll(foldedPoints);
      foldedPoints.stream()
              .map(p -> fold[0] == 0 ? p.moveX(2 * fold[1] - p.x()) : p.moveY(2 * fold[1] - p.y()))
              .forEach(points::add);
    }

    return points;
  }

  private static String toString(Set<Point> points) {
    char[][] chars = new char[points.stream().mapToInt(Point::y).max().orElseThrow() + 1][points.stream().mapToInt(Point::x).max().orElseThrow() + 1];
    for (char[] c : chars) {
      Arrays.fill(c, ' ');
    }
    points.forEach(p -> chars[p.y()][p.x()] = '#');
    return Utils.toString(chars);
  }

  // int[] is dir (x->0/y->1) and value
  private static Pair<Set<Point>, int[][]> input() {
    List<Stream<String>> list = Utils.strings(Utils.ENDL_2, Utils.ENDL).toList();

    Set<Point> points = list.get(0)
            .map(s -> Arrays.stream(s.split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray())
            .map(i -> new Point(i[0], i[1]))
            .collect(Collectors.toSet());

    int[][] folds = list.get(1)
            .map(s -> s.substring("fold along ".length()))
            .map(s -> s.split("="))
            .map(s -> new int[]{s[0].equals("x") ? 0 : 1, Integer.parseInt(s[1])})
            .toArray(int[][]::new);

    return new Pair<>(points, folds);
  }
}
