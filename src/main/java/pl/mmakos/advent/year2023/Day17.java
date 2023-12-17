package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;

import java.util.*;

import static java.util.function.Predicate.not;
import static pl.mmakos.advent.utils.Utils.intArrayInput;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day17 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return dijkstra(intArrayInput(), 0, 3);
  }

  private static int task2() {
    return dijkstra(intArrayInput(), 4, 10);
  }

  private static int dijkstra(int[][] map, int min, int max) {
    Queue<Pair<DirPoint, Integer>> queue = new PriorityQueue<>(Comparator.comparingInt(Pair::second));
    Set<DirPoint> visited = new HashSet<>();
    queue.add(new Pair<>(new DirPoint(0, 0, 0, 0), 0));
    Point end = new Point(map[0].length - 1, map.length - 1);

    while (!queue.isEmpty()) {
      Pair<DirPoint, Integer> pair = queue.poll();
      DirPoint point = pair.first();
      int cost = pair.second();
      if (point.point.equals(end)) return cost;

      getNeighbours(map, point, min, max).stream()
              .filter(not(visited::contains))
              .map(p -> new Pair<>(p, cost + map[p.y()][p.x()]))
              .forEach(p -> {
                visited.add(p.first());
                queue.add(p);
              });
    }

    throw new IllegalStateException();
  }

  private static List<DirPoint> getNeighbours(int[][] map, DirPoint point, int min, int max) {
    List<DirPoint> points = new ArrayList<>(4);

    if (point.sameDir < min) {
      DirPoint sameDirNeighbour = getSameDirNeighbour(point);
      if (sameDirNeighbour.point.validBounds(map)) {
        return Collections.singletonList(sameDirNeighbour);
      } else {
        return Collections.emptyList();
      }
    }

    if (point.dir != 0 && (point.dir != 2 || point.sameDir < max) && point.x() > 0) points.add(point.left());
    if (point.dir != 1 && (point.dir != 3 || point.sameDir < max) && point.y() > 0) points.add(point.top());
    if (point.dir != 2 && (point.dir != 0 || point.sameDir < max) && point.x() < map[0].length - 1) points.add(point.right());
    if (point.dir != 3 && (point.dir != 1 || point.sameDir < max) && point.y() < map.length - 1) points.add(point.bottom());

    return points;
  }

  private static DirPoint getSameDirNeighbour(DirPoint point) {
    return switch (point.dir) {
      case 0 -> point.right();
      case 1 -> point.bottom();
      case 2 -> point.left();
      case 3 -> point.top();
      default -> throw new IllegalStateException();
    };
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class DirPoint {
    private final Point point;
    private final int dir;
    private final int sameDir;

    private DirPoint(int x, int y, int dir, int sameDir) {
      this(new Point(x, y), dir, sameDir);
    }

    private int x() {
      return point.x();
    }

    private int y() {
      return point.y();
    }

    private DirPoint left() {
      return new DirPoint(point.left(), 2, dir == 2 ? sameDir + 1 : 1);
    }

    private DirPoint right() {
      return new DirPoint(point.right(), 0, dir == 0 ? sameDir + 1 : 1);
    }

    private DirPoint top() {
      return new DirPoint(point.top(), 3, dir == 3 ? sameDir + 1 : 1);
    }

    private DirPoint bottom() {
      return new DirPoint(point.bottom(), 1, dir == 1 ? sameDir + 1 : 1);
    }
  }
}
