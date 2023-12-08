package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day6 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return solve(80);
  }

  private static long task2() {
    return solve(256);
  }

  private static long solve(int days) {
    Map<Integer, Long> fish = input();
    for (int i = 0; i < days; ++i) {
      day(fish);
    }
    return fish.values().stream()
            .mapToLong(l -> l)
            .sum();
  }

  private static void day(Map<Integer, Long> fish) {
    long zeros = fish.getOrDefault(0, 0L);
    for (int i = 0; i < 8; ++i) {
      fish.put(i, fish.getOrDefault(i + 1, 0L));
    }
    fish.put(8, zeros);
    fish.compute(6, (k, v) -> v == null ? zeros : v + zeros);
  }

  private static Map<Integer, Long> input() {
    return Utils.ints(6, 2021, ",")
            .boxed()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }
}
