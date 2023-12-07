package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day1 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    int[] ints = Utils.ints(1, 2021, Utils.ENDL).toArray();

    return IntStream.range(1, ints.length)
            .filter(i -> ints[i] > ints[i - 1])
            .count();
  }

  private static long task2() {
    int[] ints = Utils.ints(1, 2021, Utils.ENDL).toArray();

    return IntStream.range(3, ints.length)
            .filter(i -> ints[i] + ints[i - 1] + ints[i - 2] > ints[i - 1] + ints[i - 2] + ints[i - 3])
            .count();
  }
}
