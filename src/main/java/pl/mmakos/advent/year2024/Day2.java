package pl.mmakos.advent.year2024;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.IntStream;

import static java.lang.Integer.compare;
import static java.lang.Math.abs;
import static pl.mmakos.advent.utils.Utils.ENDL;
import static pl.mmakos.advent.utils.Utils.ints;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day2 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.out.println("Advent of code 2024, day 2");
    System.out.println("TASK 1: " + task1());
    System.out.println("TASK 2: " + task2());
  }

  private static boolean isValidPair(int i1, int i2, int direction) {
    return abs(i2 - i1) > 0 && abs(i2 - i1) <= 3 && compare(i1, i2) == direction;
  }

  private static boolean isValidReport(int[] report) {
    int direction = compare(report[0], report[1]);
    if (direction == 0) return false;
    for (int i1 = 0, i2 = 1; i2 < report.length; ++i1, ++i2) {
      if (!isValidPair(report[i1], report[i2], direction)) return false;
    }
    return true;
  }

  private static boolean isValidReport2(int[] report) {
    for (int i = 0; i < report.length; ++i) {
      int[] report2 = new int[report.length - 1];
      for (int j = 0, k = 0; k < report2.length; k += (k == i) ? 2 : 1, ++j) report2[j] = report[j];
      if (isValidReport(report2)) return true;
    }
    return false;
  }

  private static long task1() {
    return ints(ENDL, " ")
        .map(IntStream::toArray)
        .filter(Day2::isValidReport)
        .count();
  }

  private static long task2() {
    return ints(ENDL, " ")
        .map(IntStream::toArray)
        .filter(Day2::isValidReport2)
        .count();
  }
}
