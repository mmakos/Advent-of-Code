package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("StatementWithEmptyBody")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day14 {
  public static final int CYCLES = 1_000_000_000;

  // Earlier I've implemented it with point sets in hope, that second task will be much lager than size.
  // Unfortunately it was much larger in iterations, not size.
  // In first solution, task 1 completes after ~60ms and task 2 after ~1700ms!
  // In this solution it takes only: task 1 - ~25ms, task 2 - ~45ms!
  // So it's much better (caching sets is much more time-consuming).
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    char[][] input = input();

    for (int i = 1; i < input.length; ++i) {
      moveUp(i, input);
    }
    return count(input);
  }

  // Whole trick to task 2 is to find repeating pattern
  @SuppressWarnings("java:S127")
  private static long task2() {
    char[][] input = input();

    Map<CacheKey, Integer> cache = new HashMap<>();

    for (int i = 0; i < CYCLES; ++i) {
      moveCycle(input);
      Integer cached = cache.get(new CacheKey(input));
      if (cached != null) {
        i = CYCLES - ((CYCLES - i) % (cache.size() - cached));
      } else {
        cache.put(new CacheKey(Utils.deepCopy(input)), i);
      }
    }

    return count(input);
  }

  private static void moveCycle(char[][] array) {
    for (int i = 1; i < array.length; ++i) {
      moveUp(i, array);
    }
    for (int i = 1; i < array[0].length; ++i) {
      moveLeft(i, array);
    }
    for (int i = array.length - 1; i >= 0; --i) {
      moveDown(i, array);
    }
    for (int i = array[0].length - 1; i >= 0; --i) {
      moveRight(i, array);
    }
  }

  private static void moveUp(int row, char[][] array) {
    for (int i = 0; i < array[0].length; ++i) {
      for (int j = row; j > 0 && move(i, j, 0, array); --j);
    }
  }

  private static void moveDown(int row, char[][] array) {
    for (int i = 0; i < array[0].length; ++i) {
      for (int j = row; j < array.length - 1 && move(i, j, 2, array); ++j);
    }
  }

  private static void moveLeft(int col, char[][] array) {
    for (int i = 0; i < array.length; ++i) {
      for (int j = col; j > 0 && move(j, i, 1, array); --j);
    }
  }

  private static void moveRight(int col, char[][] array) {
    for (int i = 0; i < array.length; ++i) {
      for (int j = col; j < array[0].length - 1 && move(j, i, 3, array); ++j);
    }
  }

  private static boolean move(int x, int y, int dir, char[][] array) {
    if (array[y][x] != 'O') return false;

    if (dir == 0) { // UP
      if (array[y - 1][x] != '.') return false;
      array[y - 1][x] = 'O';
    } else if (dir == 1) { // LEFT
      if (array[y][x - 1] != '.') return false;
      array[y][x - 1] = 'O';
    } else if (dir == 2) { // DOWN
      if (array[y + 1][x] != '.') return false;
      array[y + 1][x] = 'O';
    } else if (dir == 3) { // RIGHT
      if (array[y][x + 1] != '.') return false;
      array[y][x + 1] = 'O';
    }

    array[y][x] = '.';
    return true;
  }
  // Whole trick to task 1 is to find repeating pattern

  private static long count(char[][] array) {
    return IntStream.range(0, array.length)
            .mapToLong(i -> IntStream.range(0, array[0].length)
                    .filter(j -> array[i][j] == 'O')
                    .count() * (array.length - i))
            .sum();
  }

  private static char[][] input() {
    return Utils.lines()
            .map(String::toCharArray)
            .toArray(char[][]::new);
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class CacheKey {
    private final char[][] array;
  }
}
