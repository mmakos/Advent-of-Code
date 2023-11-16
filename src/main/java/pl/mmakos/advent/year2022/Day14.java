package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day14 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    char[][] cave = getCave();
    int dropped = dropSand(cave);
    for (char[] row : cave) {
      System.err.println(new String(row));
    }
    return dropped;
  }

  private static int task2() {
    char[][] cave = getCave();
    char[][] newCave = new char[cave.length + 2][];
    System.arraycopy(cave, 0, newCave, 0, cave.length);
    newCave[cave.length] = new char[cave[0].length];
    newCave[cave.length + 1] = new char[cave[0].length];
    Arrays.fill(newCave[cave.length], '.');
    Arrays.fill(newCave[cave.length + 1], '#');
    int dropped = dropSand(newCave);
    for (char[] row : newCave) {
      System.err.println(new String(row));
    }
    return dropped;
  }

  private static int dropSand(char[][] cave) {
    int startX = new String(cave[0]).indexOf('+');

    int drops = 0;
    while (true) {
      int x = startX, y = 0;
      while (true) {
        if (cave[y + 1][x] == '.') {
          ++y;
        } else if (cave[y + 1][x - 1] == '.') {
          --x;
          ++y;
        } else if (cave[y + 1][x + 1] == '.') {
          ++x;
          ++y;
        } else {
          cave[y][x] = 'o';
          if (y == 0) {
            return drops + 1;
          }
          break;
        }
        if (y >= cave.length - 1) {
          return drops;
        }
      }

      ++drops;
    }
  }

  private static char[][] getCave() {
    Point[][] paths = paths();
    int maxY = Arrays.stream(paths)
            .flatMap(Arrays::stream)
            .mapToInt(Point::y)
            .max()
            .orElseThrow();
    // safe bias
    int minX = 500 - maxY - 2;
    int maxX = 500 + maxY + 2;
    int startX = 500 - minX;

    char[][] cave = new char[maxY + 1][maxX - minX + 1];
    for (char[] chars : cave) {
      Arrays.fill(chars, '.');
    }

    for (Point[] path : paths) {
      Point start = path[0];
      cave[start.y()][start.x() - minX] = '#';
      for (int i = 1; i < path.length; ++i) {
        Point end = path[i];
        if (start.x() < end.x()) {
          for (int x = start.x() - minX + 1; x <= end.x() - minX; ++x) {
            cave[start.y()][x] = '#';
          }
        } else if (start.x() > end.x()) {
          for (int x = start.x() - minX - 1; x >= end.x() - minX; --x) {
            cave[start.y()][x] = '#';
          }
        } else if (start.y() < end.y()) {
          for (int y = start.y() + 1; y <= end.y(); ++y) {
            cave[y][start.x() - minX] = '#';
          }
        } else if (start.y() > end.y()) {
          for (int y = start.y() - 1; y >= end.y(); --y) {
            cave[y][start.x() - minX] = '#';
          }
        }

        start = end;
      }
    }

    cave[0][startX] = '+';

    return cave;
  }

  private static Point[][] paths() {
    return Utils.lines(14, 2022)
            .filter(not(String::isBlank))
            .map(s -> Arrays.stream(s.split(" -> "))
                    .map(str -> str.split(","))
                    .map(str -> new Point(Integer.parseInt(str[0]), Integer.parseInt(str[1])))
                    .toArray(Point[]::new))
            .toArray(Point[][]::new);
  }
}
