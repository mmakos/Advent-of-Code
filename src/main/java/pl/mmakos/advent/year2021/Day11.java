package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day11 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[][] oct = input();
    return IntStream.range(0, 100)
            .map(i -> step(oct))
            .sum();
  }

  private static int task2() {
    int[][] oct = input();
    int size = oct.length * oct[0].length;
    return IntStream.iterate(1, i -> i + 1)
            .filter(i -> step(oct) == size)
            .findFirst()
            .orElseThrow();
  }

  private static int step(int[][] oct) {
    for (int i = 0; i < oct.length; ++i) {
      for (int j = 0; j < oct[0].length; ++j) {
        ++oct[i][j];
      }
    }

    boolean anyFlashed;
    do {
      anyFlashed = false;
      for (int i = 0; i < oct.length; ++i) {
        for (int j = 0; j < oct[0].length; ++j) {
          if (oct[i][j] > 9) {
            oct[i][j] = -1;
            flash(oct, i, j);
            anyFlashed = true;
          }
        }
      }
    } while (anyFlashed);

    int flashes = 0;
    for (int i = 0; i < oct.length; ++i) {
      for (int j = 0; j < oct[0].length; ++j) {
        if (oct[i][j] == -1) {
          oct[i][j] = 0;
          ++flashes;
        }
      }
    }
    return flashes;
  }

  private static void flash(int[][] oct, int i, int j) {
    flashIJ(oct, i - 1, j - 1);
    flashIJ(oct, i - 1, j);
    flashIJ(oct, i - 1, j + 1);
    flashIJ(oct, i, j - 1);
    flashIJ(oct, i, j + 1);
    flashIJ(oct, i + 1, j - 1);
    flashIJ(oct, i + 1, j);
    flashIJ(oct, i + 1, j + 1);
  }

  private static void flashIJ(int[][] oct, int i, int j) {
    if (i >= 0 && i < oct.length && j >= 0 && j < oct[0].length && oct[i][j] >= 0) {
      ++oct[i][j];
    }
  }

  private static int[][] input() {
    return Utils.lines(11, 2021)
            .map(s -> s.chars()
                    .map(i -> i - '0')
                    .toArray())
            .toArray(int[][]::new);
  }
}
