package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.floorMod;
import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day16 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return solve(new DirPoint(-1, 0, 0), input());
  }

  private static long task2() {
    char[][] input = input();

    return getTask2StartPoints(input).stream()
            .mapToLong(p -> solve(p, input))
            .max()
            .orElseThrow();
  }

  private static long solve(DirPoint start, char[][] input) {
    Set<DirPoint> points = new HashSet<>();
    Set<DirPoint> visited = new HashSet<>();
    points.add(start);

    while (!points.isEmpty()) {
      Set<DirPoint> newPoints = points.stream()
              .flatMap(p -> nextDirections(p, input))
              .filter(not(visited::contains))
              .collect(Collectors.toSet());
      visited.addAll(points);
      points = newPoints;
    }

    return visited.stream()
            .map(p -> new Point(p.x(), p.y()))
            .distinct()
            .count() - 1; // subtract 1, because there is start point, which is outside input array
  }

  private static Stream<DirPoint> nextDirections(DirPoint point, char[][] input) {
    return Optional.of(point.nextPoint())
            .filter(p -> p.validBounds(input))
            .stream()
            .flatMap(p -> Arrays.stream(getNextDirections(point.dir, input[p.y()][p.x()]))
                    .mapToObj(dir -> new DirPoint(p, dir)));
  }

  private static int[] getNextDirections(int dir, char tile) {
    return switch (tile) {
      case '|' -> dir % 2 == 0 ? i(1, 3) : i(dir);
      case '-' -> dir % 2 == 1 ? i(0, 2) : i(dir);
      case '/' -> dir % 2 == 0 ? i(floorMod(dir - 1, 4)) : i(floorMod(dir + 1, 4));
      case '\\' -> dir % 2 == 1 ? i(floorMod(dir - 1, 4)) : i(floorMod(dir + 1, 4));
      default -> i(dir);
    };
  }

  private static int[] i(int... ints) {
    return ints;
  }

  private static Collection<DirPoint> getTask2StartPoints(char[][] input) {
    List<DirPoint> points = new ArrayList<>();
    for (int i = 0; i < input.length; ++i) {
      points.add(new DirPoint(-1, i, 0));
      points.add(new DirPoint(input[i].length, i, 2));
    }
    for (int i = 0; i < input.length; ++i) {
      points.add(new DirPoint(i, -1, 1));
      points.add(new DirPoint(i, input.length, 3));
    }
    return points;
  }

  private static char[][] input() {
    return Utils.lines()
            .map(String::toCharArray)
            .toArray(char[][]::new);
  }

  @EqualsAndHashCode(callSuper = true)
  private static class DirPoint extends Point {
    private final int dir;

    public DirPoint(Point point, int dir) {
      this(point.x(), point.y(), dir);
    }

    public DirPoint(int x, int y, int dir) {
      super(x, y);
      this.dir = dir;
    }

    private Point nextPoint() {
      return switch (dir) {
        case 0 -> right();
        case 1 -> bottom();
        case 2 -> left();
        case 3 -> top();
        default -> throw new IllegalStateException();
      };
    }
  }
}
