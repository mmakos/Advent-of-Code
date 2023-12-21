package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day21 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    char[][] chars = Utils.charArrayInput();

    return bfs(Point.fromCharArray(chars, '#'),
            new Point(chars.length / 2, chars.length / 2), 64).size();
  }

  // For part 2 we need to (again) look at the input
  // Start point is exactly a center point, and there is clear path to borders in all 4 direction
  @SuppressWarnings("UnnecessaryLocalVariable")
  private static long task2() {
    char[][] chars = Utils.charArrayInput();
    Set<Point> rocks = Point.fromCharArray(chars, '#');
    rocks = extendRocks(rocks, chars.length, 3);
    Point start = new Point(chars.length / 2, chars.length / 2);

    // We have to compute params of quadratic equation from 3 computed points for x = 0, 1, 2:
    // So we have the following equation system:
    // x0 = c                   a = (f0 - 2f1 + f2) / 2
    // f1 = a + b + c    ===>   b = (-3f0 + 4f1 - f2) / 2
    // f2 = 4a + 2b + c         c = f0
    long f0 = bfs(rocks, start, 65).size();
    long f1 = bfs(rocks, start, 65 + 131).size();
    long f2 = bfs(rocks, start, 65 + 2 * 131).size();

    long a = (f0 - 2 * f1 + f2) / 2;
    long b = (-3 * f0 + 4 * f1 - f2) / 2;
    long c = f0;

    int y = (26501365 - 65) / 131;

    return a * Utils.pow(y, 2) + y * b + c;
  }

  @SuppressWarnings("DataFlowIssue")
  private static Set<Point> bfs(Set<Point> rocks, Point start, int steps) {
    Queue<Pair<Point, Integer>> queue = new ArrayDeque<>();
    queue.add(new Pair<>(start, 0));
    Set<Point> visited = new HashSet<>();

    while (true) {
      Pair<Point, Integer> pair = queue.poll();
      if (pair.second() == steps) break;
      Arrays.stream(pair.first().neighbours())
              .filter(not(rocks::contains))
              .map(p -> new Pair<>(p, pair.second() + 1))
              .filter(p -> !visited.contains(p.first()))
              .forEach(p -> {
                if (pair.second() % 2 != steps % 2) {
                  visited.add(p.first());
                }
                queue.add(p);
              });
    }

    queue.stream()
            .map(Pair::first)
            .forEach(visited::add);
    return visited;
  }

  @SuppressWarnings("SameParameterValue")
  private static Set<Point> extendRocks(Set<Point> rocks, int dimension, int extension) {
    Set<Point> extended = new HashSet<>(rocks);
    for (int i = 0; i <= extension; ++i) {
      for (int j = 0; j <= extension; ++j) {
        if (i == 0 && j == 0) continue;
        for (Point p : rocks) {
          extended.add(p.translate(dimension * i, dimension * j));
          extended.add(p.translate(dimension * -i, dimension * j));
          extended.add(p.translate(dimension * -i, dimension * -j));
          extended.add(p.translate(dimension * i, dimension * -j));
        }
      }
    }

    return extended;
  }
}
