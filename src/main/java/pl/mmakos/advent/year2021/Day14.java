package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pl.mmakos.advent.utils.Pair.Stream.pairStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day14 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return solve(10);
  }

  private static long task2() {
    return solve(40);
  }

  private static long solve(int steps) {
    Pair<String, Map<String, String>> input = input();

    Map<String, Long> pairs = new HashMap<>();
    for (int i = 1; i < input.first().length(); ++i) {
      pairs.compute(input.first().substring(i - 1, i + 1), (k, v) -> v == null ? 1 : v + 1);
    }

    Map<String, String> map = input.second();

    for (int i = 0; i < steps; ++i) {
      pairs = step(pairs, map);
    }

    return result(pairs, new char[]{input.first().charAt(0), input.first().charAt(input.first().length() - 1)});
  }

  private static Map<String, Long> step(Map<String, Long> pairs, Map<String, String> map) {
    Map<String, Long> newPairs = new HashMap<>();
    pairStream(pairs)
            .mapFirst(p -> getNewPairs(p, map))
            .flatMapFirst(Collection::stream)
            .forEach((p, c) -> newPairs.compute(p, (k, v) -> v == null ? c : v + c));
    return newPairs;
  }

  private static List<String> getNewPairs(String pair, Map<String, String> map) {
    return Optional.ofNullable(map.get(pair))
            .map(v -> List.of(pair.charAt(0) + v, v + pair.charAt(1)))
            .orElseGet(() -> Collections.singletonList(pair));
  }

  private static long result(Map<String, Long> pairs, char[] firstAndLast) {
    Map<Character, Long> counts = new HashMap<>();
    pairs.forEach((p, c) -> {
      counts.compute(p.charAt(0), (k, v) -> v == null ? c : v + c);
      counts.compute(p.charAt(1), (k, v) -> v == null ? c : v + c);
    });
    counts.computeIfPresent(firstAndLast[0], (k, v) -> v + 1);
    counts.computeIfPresent(firstAndLast[1], (k, v) -> v + 1);

    LongSummaryStatistics summary = counts.values().stream()
            .mapToLong(Long::longValue)
            .summaryStatistics();
    return summary.getMax() / 2 - summary.getMin() / 2;
  }

  private static Pair<String, Map<String, String>> input() {
    List<Stream<String>> strings = Utils.strings(Utils.ENDL_2, Utils.ENDL)
            .toList();

    String string = strings.get(0).findFirst().orElseThrow();
    Map<String, String> map = strings.get(1)
            .map(s -> s.split(" -> "))
            .collect(Collectors.toMap(s -> s[0], s -> s[1]));

    return new Pair<>(string, map);
  }
}
