package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day13 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return input()
            .map(Day13::findMirror)
            .mapToInt(Day13::mirrorSummary)
            .sum();
  }

  private static int task2() {
    return input()
            .map(Day13::fixAndFindMirror)
            .mapToInt(Day13::mirrorSummary)
            .sum();
  }

  private static int mirrorSummary(Point point) {
    return point.x() + 100 * point.y();
  }

  private static Point fixAndFindMirror(boolean[][] array) {
    for (int i = 0; i < array.length; ++i) {
      for (int j = 0; j < array[0].length; ++j) {
        Point normalMirror = findMirror(array);
        array[i][j] = !array[i][j];
        Point fixedMirror = findMirror(array, normalMirror);
        array[i][j] = !array[i][j];
        if (fixedMirror != null && !Objects.equals(normalMirror, fixedMirror)) return fixedMirror;
      }
    }
    throw new IllegalStateException();
  }

  private static Point findMirror(boolean[][] array, Point not) {
    for (int i = 1; i < array.length; ++i) {
      if (isHorizontalMirror(array, i)) {
        Point p = new Point(0, i);
        if (!p.equals(not)) return p;
      }
    }
    for (int i = 1; i < array[0].length; ++i) {
      if (isVerticalMirror(array, i)) {
        Point p = new Point(i, 0);
        if (!p.equals(not)) return p;
      }
    }

    return null;
  }

  private static Point findMirror(boolean[][] array) {
    return findMirror(array, null);
  }

  private static boolean isHorizontalMirror(boolean[][] array, int row) {
    for (int i = row - 1, j = row; i >= 0 && j < array.length; --i, ++j) {
      for (int col = 0; col < array[0].length; ++col) {
        if (array[i][col] != array[j][col]) return false;
      }
    }
    return true;
  }

  private static boolean isVerticalMirror(boolean[][] array, int col) {
    for (int i = col - 1, j = col; i >= 0 && j < array[0].length; --i, ++j) {
      for (boolean[] row : array) {
        if (row[i] != row[j]) return false;
      }
    }
    return true;
  }

  private static Stream<boolean[][]> input() {
    return Utils.strings(Utils.ENDL_2, Utils.ENDL)
            .map(s -> s.map(l -> l.chars()
                            .mapToObj(c -> c == '#')
                            .toList())
                    .map(Utils::toPrimitiveBools)
                    .toArray(boolean[][]::new));
  }
}
