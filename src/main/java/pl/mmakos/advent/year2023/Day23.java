package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day23 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return dfs(input(), new Point(1, 0), new Point(139, 140)) - 1;
  }

  private static int task2() {
    return dfs2(input2(), new HashSet<>(), new Point(1, 0), new Point(139, 140), 0);
  }

  private static int dfs(Map<Point, Integer> points, Point start, Point end) {
    if (start.equals(end)) return 1;
    var nexts = neighbours(points, start);
    int max = 0;
    for (var next : nexts) {
      // No need to copy hash map, if there is only one route
      var newPoints = nexts.length > 1 ? new HashMap<>(points) : points;
      newPoints.remove(next);
      newPoints.remove(start);
      int dfs = dfs(newPoints, next, end);
      if (dfs > max) {
        max = dfs + 1;
      }
    }
    return max;
  }

  private static int dfs2(Map<Point, List<PointI>> points, Set<Point> visited, Point start, Point end, int i) {
    if (start.equals(end)) return i;

    visited.add(start);
    int max = 0;
    for (var next : points.get(start)) {
      if (!visited.contains(next)) {
        int dfs = dfs2(points, new HashSet<>(visited), next, end, next.i);
        if (dfs + i > max) {
          max = i + dfs;
        }
      }
    }

    return max;
  }

  private static Point[] neighbours(Map<Point, Integer> points, Point point) {
    return IntStream.range(0, 4)
        .mapToObj(i -> {
          Point neighbour = point.neighbour(i);
          Integer dir = points.get(neighbour);
          return dir != null && (dir == i || dir == -1) ? neighbour : null;
        })
        .filter(Objects::nonNull)
        .toArray(Point[]::new);
  }

  private static Map<Point, Integer> input() {
    char[][] input = Utils.charArrayInput();
    Map<Point, Integer> map = new HashMap<>();
    Set<Point> points = Point.fromCharArray(input, '.');
    points.forEach(p -> map.put(p, -1));
    char[] slopes = {'>', 'v', '<', '^'};

    for (int i = 0; i < 4; ++i) {
      char slope = slopes[i];
      Set<Point> ss = Point.fromCharArray(input, slope);
      int finalI = i;
      ss.forEach(p -> map.put(p, finalI));
    }

    return map;
  }

  @SuppressWarnings({"java:S6204", "SuspiciousMethodCalls"})
  private static Map<Point, List<PointI>> input2() {
    Set<Point> points = input().keySet();

    var map = points.stream()
        .collect(Collectors.toMap(p -> p,
            p -> Arrays.stream(p.neighbours())
                .filter(points::contains)
                .map(n -> new PointI(n.x(), n.y(), 1))
                .collect(Collectors.toList())));

    var copy = new HashMap<>(map);

    copy.forEach((k, v) -> {
      if (v.size() == 2) {
        map.remove(k);
        PointI first = v.get(0);
        PointI second = v.get(1);

        List<PointI> pointIS = map.get(first);
        int i = pointIS.indexOf(k);
        pointIS.set(i, new PointI(second.x(), second.y(), second.i + first.i));

        pointIS = map.get(second);
        i = pointIS.indexOf(k);
        pointIS.set(i, new PointI(first.x(), first.y(), second.i + first.i));
      }
    });

    return map;
  }

  @SuppressWarnings("java:S2160")
  private static class PointI extends Point {
    private final int i;

    public PointI(int x, int y, int i) {
      super(x, y);
      this.i = i;
    }
  }
}
