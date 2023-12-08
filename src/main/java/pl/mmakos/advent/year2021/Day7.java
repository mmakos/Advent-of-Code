package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day7 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[] input = Utils.ints(7, 2021, ",").toArray();
    int max = Arrays.stream(input).max().orElseThrow();

    return IntStream.range(0, max)
            .map(pos -> fuel(input, pos))
            .min()
            .orElseThrow();
  }

  private static int task2() {
    int[] input = Utils.ints(7, 2021, ",").toArray();
    int max = Arrays.stream(input).max().orElseThrow();

    return IntStream.range(0, max)
            .map(pos -> fuel2(input, pos))
            .min()
            .orElseThrow();
  }

  private static int fuel(int[] positions, int pos) {
    return Arrays.stream(positions)
            .map(p -> abs(pos - p))
            .sum();
  }

  private static int fuel2(int[] positions, int pos) {
    return Arrays.stream(positions)
            .map(p -> sumFrom1(abs(pos - p)))
            .sum();
  }

  private static int sumFrom1(int val) {
    return (int) ((val + 1) * (val / 2.));
  }
}
