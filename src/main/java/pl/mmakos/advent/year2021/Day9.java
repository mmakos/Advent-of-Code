package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day9 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[][] map = input();

    return IntStream.range(0, map.length)
            .map(i -> IntStream.range(0, map[0].length)
                    .filter(j -> getAdjacent(map, i, j).stream()
                            .map(p -> map[p.x()][p.y()])
                            .allMatch(a -> a > map[i][j]))
                    .map(j -> map[i][j] + 1)
                    .sum()
            ).sum();
  }

  private static int task2() {
    int[][] map = input();

    return IntStream.range(0, map.length)
            .flatMap(i -> IntStream.range(0, map[0].length)
                    .filter(j -> getAdjacent(map, i, j).stream()
                            .map(p -> map[p.x()][p.y()])
                            .allMatch(a -> a > map[i][j]))
                    .map(j -> dfs(map, i, j))
            )
            .boxed()
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(1, (acc, i) -> acc * i);
  }

  private static int dfs(int[][] ints, int i, int j) {
    Queue<Point> queue = new ArrayDeque<>();
    Set<Point> visited = new HashSet<>();

    queue.add(new Point(i, j));
    while (!queue.isEmpty()) {
      Point point = queue.poll();
      visited.add(point);
      getAdjacent(ints, point.x(), point.y())
              .stream()
              .filter(p -> ints[p.x()][p.y()] < 9)
              .filter(not(visited::contains))
              .forEach(queue::add);
    }
    return visited.size();
  }

  private static List<Point> getAdjacent(int[][] ints, int i, int j) {
    List<Point> list = new ArrayList<>();
    if (i > 0) {
      list.add(new Point(i - 1, j));
    }
    if (i < ints.length - 1) {
      list.add(new Point(i + 1, j));
    }
    if (j > 0) {
      list.add(new Point(i, j - 1));
    }
    if (j < ints[0].length - 1) {
      list.add(new Point(i, j + 1));
    }

    return list;
  }

  private static int[][] input() {
    return Utils.lines()
            .map(s -> s.chars()
                    .map(c -> c - '0')
                    .toArray())
            .toArray(int[][]::new);
  }
}
