package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.Comparator;

public class Day1 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return Utils.strings(1, 2022, System.lineSeparator().repeat(2))
            .mapToInt(s -> Arrays.stream(s.split(System.lineSeparator()))
                    .mapToInt(Integer::parseInt)
                    .sum())
            .max()
            .orElseThrow();
  }

  private static int task2() {
    return Utils.strings(1, 2022, System.lineSeparator().repeat(2))
            .map(s -> Arrays.stream(s.split(System.lineSeparator()))
                    .mapToInt(Integer::parseInt)
                    .sum())
            .sorted(Comparator.reverseOrder())
            .mapToInt(i -> i)
            .limit(3)
            .sum();
  }
}