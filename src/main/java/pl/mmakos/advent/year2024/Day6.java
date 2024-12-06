package pl.mmakos.advent.year2024;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static pl.mmakos.advent.utils.Utils.charArrayInput;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day6 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.out.println("Advent of code 2024, day 2");

    int[] solution = solve();
    System.out.println("TASK 1: " + solution[0]);
    System.out.println("TASK 2: " + solution[1]);
  }

  private static int[] solve() {
    char[][] array = charArrayInput();
    Set<Point> points = Point.fromCharArray(array, '#');
    Point start = Point.of(array, '^');

    Map<Point, Integer> solution1 = solve(points, start, array);
    if (solution1 == null) throw new IllegalStateException();

    Set<Point> obstacles = new HashSet<>(solution1.keySet());
    obstacles.remove(start);

    long solution2 = obstacles.stream()
        .filter(p -> {
          points.add(p);
          Map<Point, Integer> sol = solve(points, start, array);
          points.remove(p);
          return sol == null;
        })
        .count();

    return new int[]{solution1.size(), (int) solution2};
  }

  @SuppressWarnings("java:S1168")
  private static Map<Point, Integer> solve(Set<Point> points, Point pos, char[][] array) {
    int dir = 3; // TOP

    Map<Point, Integer> visited = new HashMap<>();
    visited.put(pos, 1 << dir);

    while (true) {
      Point newPos = pos.translateDir(1, dir);
      if (!newPos.validBounds(array)) break;
      if ((visited.getOrDefault(newPos, 0) & (1 << dir)) > 0) return null;
      if (points.contains(newPos)) {
        dir = (dir + 1) % 4;
      } else {
        int d = 1 << dir;
        visited.compute(newPos, (k, v) -> v == null ? d : v | d);
        pos = newPos;
      }
    }

    return visited;
  }
}
