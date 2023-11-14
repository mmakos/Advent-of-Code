package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;

public class Day4 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return Utils.lines(4, 2022)
            .map(s -> Arrays.stream(s.split(","))
                    .map(str -> Utils.splitToInts(str, "-"))
                    .toArray(int[][]::new))
            .filter(i -> contains(i[0], i[1]) || contains(i[1], i[0]))
            .count();
  }

  private static long task2() {
    return Utils.lines(4, 2022)
            .map(s -> Arrays.stream(s.split(","))
                    .map(str -> Utils.splitToInts(str, "-"))
                    .toArray(int[][]::new))
            .filter(i -> overlaps(i[0], i[1]) || overlaps(i[1], i[0]))
            .count();
  }

  private static boolean contains(int[] first, int[] second) {
    return first[0] >= second[0] && first[1] <= second[1];
  }

  private static boolean overlaps(int[] first, int[] second) {
    return first[1] <= second[1] && first[1] >= second[0];
  }
}
