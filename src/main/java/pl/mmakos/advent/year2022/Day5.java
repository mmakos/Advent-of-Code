package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {
  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("move (\\d+) from (\\d) to (\\d)");

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static String task1() {
    return taskN((l1, l2) -> {
      for (int j = l2.size() - 1; j >= 0; --j) {
        l1.add(l2.get(j));
      }
    });
  }

  private static String task2() {
    return taskN(List::addAll);
  }

  private static String taskN(BiConsumer<List<String>, List<String>> adder) {
    Pair<List<String>[], Stream<int[]>> pair = loadCrates();
    List<String>[] stacks = pair.first();
    Stream<int[]> stream = pair.second();
    stream.forEach(i -> {
      List<String> from = stacks[i[1] - 1];
      int count = i[0];
      List<String> els = from.subList(from.size() - count, from.size());
      adder.accept(stacks[i[2] - 1], els);
      els.clear();
    });
    return Arrays.stream(stacks)
            .map(s -> s.isEmpty() ? "" : s.get(s.size() - 1))
            .collect(Collectors.joining());
  }

  private static Pair<List<String>[], Stream<int[]>> loadCrates() {
    String[] array = Utils.strings(5, 2022, Utils.ENDL_2).toArray(String[]::new);
    @SuppressWarnings("unchecked")
    List<String>[] stacks = IntStream.range(0, 9)
            .mapToObj(i -> new ArrayList<>())
            .toArray(List[]::new);
    Arrays.stream(array[0].split(Utils.ENDL))
            .takeWhile(s -> !s.contains("1"))
            .forEach(s -> putLine(stacks, s));

    Stream<int[]> stream = Arrays.stream(array[1].split(Utils.ENDL))
            .map(Day5::mapInstruction);

    return new Pair<>(stacks, stream);
  }

  private static int[] mapInstruction(String string) {
    Matcher matcher = INSTRUCTION_PATTERN.matcher(string);
    //noinspection ResultOfMethodCallIgnored
    matcher.matches();
    return new int[]{
            Integer.parseInt(matcher.group(1)),
            Integer.parseInt(matcher.group(2)),
            Integer.parseInt(matcher.group(3))
    };
  }

  private static void putLine(List<String>[] stacks, String s) {
    String[] array = Utils.split(s, 4)
            .map(str -> str.substring(1, 2))
            .toArray(String[]::new);
    for (int i = 0; i < 9; ++i) {
      if (array.length > i && !array[i].isBlank()) {
        stacks[i].add(0, array[i]);
      }
    }
  }
}
