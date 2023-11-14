package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Comparator;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day1 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return Utils.ints(1, 2022, System.lineSeparator().repeat(2), System.lineSeparator())
            .mapToInt(IntStream::sum)
            .max()
            .orElseThrow();
  }

  private static int task2() {
    return Utils.ints(1, 2022, System.lineSeparator().repeat(2), System.lineSeparator())
            .mapToInt(IntStream::sum)
            .boxed()
            .sorted(Comparator.reverseOrder())
            .mapToInt(Utils.toPrimitiveInt())
            .limit(3)
            .sum();
  }
}