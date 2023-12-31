package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import static java.lang.Math.floorMod;

public class Day2 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return Utils.lines()
            .map(s -> s.split(" "))
            .map(s -> new int[]{first(s[0]), second(s[1])})
            .mapToInt(s -> score(s[0], s[1]))
            .sum();
  }

  private static int task2() {
    return Utils.lines()
            .map(s -> s.split(" "))
            .map(s -> new int[]{first(s[0]), second(s[1])})
            .map(s -> new int[]{s[0], second(s[0], s[1])})
            .mapToInt(s -> score(s[0], s[1]))
            .sum();
  }

  private static int second(int first, int second) {
    return switch (second) {
      case 0 -> floorMod(first - 1, 3);
      case 1 -> first;
      case 2 -> (first + 1) % 3;
      default -> -1;
    };
  }

  private static int score(int first, int second) {
    if (first == second) {
      return 3 + second + 1;
    } else if (floorMod(second - first, 3) == 1) {
      return 6 + second + 1;
    } else {
      return second + 1;
    }
  }

  private static int first(String s) {
    return s.charAt(0) - 'A';
  }

  private static int second(String s) {
    return s.charAt(0) - 'X';
  }
}