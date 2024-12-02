package pl.mmakos.advent.year2024;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static pl.mmakos.advent.utils.Utils.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day1 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.out.println("Advent of code 2024, day 1");
    System.out.println("TASK 1: " + task1());
    System.out.println("TASK 2: " + task2());
  }

  private static Pair<List<Integer>, List<Integer>> getInput() {
    List<Integer> first = new ArrayList<>();
    List<Integer> second = new ArrayList<>();
    lines()
        .map(l -> splitToInts(l, "\\s+"))
        .forEach(ints -> {
          first.add(ints[0]);
          second.add(ints[1]);
        });
    return new Pair<>(first, second);
  }

  private static int task1() {
    Pair<List<Integer>, List<Integer>> input = getInput();
    input.first().sort(Integer::compareTo);
    input.second().sort(Integer::compareTo);
    return IntStream.range(0, input.first().size())
        .map(i -> abs(input.first().get(i) - input.second().get(i)))
        .sum();
  }

  private static long task2() {
    Pair<List<Integer>, List<Integer>> input = getInput();
    return input.first().stream()
        .mapToLong(f -> f * input.second().stream()
            .filter(s -> Objects.equals(s, f))
            .count())
        .sum();
  }
}
