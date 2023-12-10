package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day15 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return dijkstra(input());
  }

  private static int task2() {
    return dijkstra(enlargeMap(input()));
  }

  private static int dijkstra(int[][] map) {
    Queue<Pair<Point, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(Pair::second));
    Set<Point> visited = new HashSet<>();
    queue.add(new Pair<>(new Point(0, 0), 0));
    Point end = new Point(map[0].length - 1, map.length - 1);

    while (!queue.isEmpty()) {
      Pair<Point, Integer> pair = queue.poll();
      Point point = pair.first();
      int cost = pair.second();
      if (point.equals(end)) return cost;

      getNeighbours(map, point).stream()
          .filter(not(visited::contains))
          .map(p -> new Pair<>(p, cost + map[p.y()][p.x()]))
          .forEach(p -> {
            visited.add(p.first());
            queue.add(p);
          });
    }

    throw new IllegalStateException();
  }

  private static List<Point> getNeighbours(int[][] map, Point point) {
    List<Point> points = new ArrayList<>(4);
    if (point.x() > 0) points.add(point.left());
    if (point.y() > 0) points.add(point.top());
    if (point.x() < map[0].length - 1) points.add(point.right());
    if (point.y() < map.length - 1) points.add(point.bottom());

    return points;
  }

  private static int[][] enlargeMap(int[][] map) {
    int[][] enlarged = new int[map.length * 5][map[0].length * 5];

    for (int k = 0; k < 5; ++k) {
      for (int l = 0; l < 5; ++l) {
        for (int i = 0; i < map.length; ++i) {
          for (int j = 0; j < map[0].length; ++j) {
            enlarged[k * map.length + i][l * map[0].length + j] = (map[i][j] + k + l - 1) % 9 + 1;
          }
        }
      }
    }
    return enlarged;
  }

  private static int[][] input() {
    return Utils.lines()
        .map(s -> s.chars()
            .map(i -> i - '0')
            .toArray())
        .toArray(int[][]::new);
  }
}
