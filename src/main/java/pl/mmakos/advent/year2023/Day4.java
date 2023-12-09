package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static pl.mmakos.advent.utils.Pair.Stream.pairStream;
import static pl.mmakos.advent.utils.Utils.pow;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day4 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return input()
            .mapToInt(Day4::points)
            .map(Day4::points)
            .sum();
  }

  private static int task2() {
    int sum = 0;
    List<Pair<List<Integer>, List<Integer>>> input = input().unwrap().toList();
    Map<Integer, Integer> counts = new HashMap<>();

    for (int i = 0; i < input.size(); ++i) {
      Pair<List<Integer>, List<Integer>> pair = input.get(i);
      int points = points(pair.first(), pair.second());
      int count = counts.getOrDefault(i, 1);
      for (int j = 1; j <= points; ++j) {
        counts.compute(j + i, (k, v) -> (v == null ? 1 : v) + count);
      }
      sum += count;
    }

    return sum;
  }

  @SuppressWarnings("java:S1854")
  private static Pair.Stream<List<Integer>, List<Integer>> input() {
    Stream<String[]> cards = Utils.lines()
            .map(s -> s.split(": ")[1])
            .map(s -> s.split(" \\| "));
    return pairStream(cards, s -> s[0], s -> s[1])
            .mapFirst(Day4::array)
            .mapSecond(Day4::array);
  }

  private static List<Integer> array(String s) {
    return Arrays.stream(s.split("\\s+"))
            .filter(not(String::isBlank))
            .map(Integer::parseInt)
            .toList();
  }

  private static int points(List<Integer> winning, List<Integer> cards) {
    return (int) cards.stream()
            .filter(winning::contains)
            .count();
  }

  private static int points(int cards) {
    return cards == 0 ? 0 : (int) pow(2, cards - 1);
  }
}
