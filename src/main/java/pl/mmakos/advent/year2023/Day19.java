package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unchecked"})
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day19 {
  private static final String xmas = "xmas";

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Pair<Map<String, Workflow>, Part[]> input = input();

    return Arrays.stream(input.second())
            .filter(p -> isPartAccepted(p, "in", input.first()))
            .mapToInt(Part::sum)
            .sum();
  }

  private static long task2() {
    Map<String, Workflow> map = input().first();

    List<int[][]> accepted = new ArrayList<>();
    Queue<Pair<String, int[][]>> queue = new ArrayDeque<>();

    int[][] startRanges = new int[][]{new int[]{1, 4000}, new int[]{1, 4000}, new int[]{1, 4000}, new int[]{1, 4000}};
    queue.add(new Pair<>("in", startRanges));

    while (!queue.isEmpty()) {
      Pair<String, int[][]> pair = queue.poll();
      List<Pair<String, int[][]>> newRanges = map.get(pair.first()).proceed(pair.second());
      for (Pair<String, int[][]> range : newRanges) {
        if (range.first().equals("A")) accepted.add(range.second());
        else if (!range.first().equals("R")) queue.add(range);
      }
    }

    return accepted.stream()
            .mapToLong(i -> Arrays.stream(i)
                    .mapToLong(j -> j[1] - j[0] + 1)
                    .reduce(1, (a, v) -> a * v))
            .sum();
  }

  private static boolean isPartAccepted(Part part, String input, Map<String, Workflow> map) {
    Workflow workflow = map.get(input);
    String result = workflow.proceed(part);
    if (result.equals("A")) return true;
    if (result.equals("R")) return false;

    return isPartAccepted(part, result, map);
  }

  private static Pair<Map<String, Workflow>, Part[]> input() {
    Stream<String>[] streams = Utils.strings(Utils.ENDL_2, Utils.ENDL)
            .toArray(Stream[]::new);

    Map<String, Workflow> map = streams[0].map(Workflow::parse)
            .collect(Collectors.toMap(Pair::first, Pair::second));
    Part[] parts = streams[1].map(Part::parse)
            .toArray(Part[]::new);
    return new Pair<>(map, parts);
  }

  // condition: 0 - no condition, -1 - <, +1 - >
  private record Rule(char c, int condition, int value, String result) {
    private static final Pattern pattern = Pattern.compile("([xmas])([<>])(\\d+)");

    private static Rule parse(String s) {
      String[] split = s.split(":");
      if (split.length == 1) return new Rule((char) 0, 0, 0, split[0]);
      Matcher m = pattern.matcher(split[0]);
      m.matches();
      return new Rule(m.group(1).charAt(0), m.group(2).equals("<") ? -1 : 1, Integer.parseInt(m.group(3)), split[1]);
    }

    private Optional<String> proceed(Part part) {
      if (condition == 0) return Optional.of(result);
      int partValue = part.get(c);
      if ((condition == -1 && partValue < value) || (condition == 1 && partValue > value)) {
        return Optional.of(result);
      }
      return Optional.empty();
    }

    private List<Pair<String, int[][]>> proceed(int[][] ranges) {
      if (condition == 0) return Collections.singletonList(new Pair<>(result, ranges));
      int cIdx = xmas.indexOf(c);
      int[] range = ranges[cIdx];

      if (value <= range[0] || value >= range[1]) {  // value outside range
        if (condition == -1) return Collections.singletonList(new Pair<>(range[0] < value ? result : null, ranges));
        else Collections.singletonList(new Pair<>(range[0] > value ? result : null, ranges));
      }

      // value in range
      int split = condition == -1 ? value - 1 : value;
      int[][] firstRanges = Utils.deepCopy(ranges);
      int[][] secondRanges = Utils.deepCopy(ranges);
      firstRanges[cIdx][0] = range[0];
      firstRanges[cIdx][1] = split;
      secondRanges[cIdx][0] = split + 1;
      secondRanges[cIdx][1] = range[1];

      return List.of(
              new Pair<>(condition == -1 ? result : null, firstRanges),
              new Pair<>(condition == -1 ? null : result, secondRanges));
    }
  }

  private record Workflow(Rule[] rules) {
    private static final Pattern pattern = Pattern.compile("(\\w+)\\{(.+)}");

    private static Pair<String, Workflow> parse(String s) {
      Matcher m = pattern.matcher(s);
      m.matches();
      String name = m.group(1);
      Rule[] rules = Arrays.stream(m.group(2).split(","))
              .map(Rule::parse)
              .toArray(Rule[]::new);
      return new Pair<>(name, new Workflow(rules));
    }

    private String proceed(Part part) {
      return Arrays.stream(rules)
              .map(r -> r.proceed(part))
              .filter(Optional::isPresent)
              .map(Optional::get)
              .findFirst()
              .orElseThrow();
    }

    private List<Pair<String, int[][]>> proceed(int[][] range) {
      List<Pair<String, int[][]>> ranges = new ArrayList<>();
      for (Rule rule : rules) {
        List<Pair<String, int[][]>> res = rule.proceed(range);
        range = null;
        Pair<String, int[][]> pair = res.get(0);
        if (pair.first() == null) range = pair.second();  // first to proceed
        else ranges.add(pair);

        if (res.size() > 1) {
          pair = res.get(1);
          if (pair.first() == null) range = pair.second();  // second to proceed
          else ranges.add(pair);
        }
      }
      return ranges;
    }
  }

  private record Part(int[] values) {
    private static final Pattern pattern = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");

    private static Part parse(String s) {
      int[] values = new int[4];
      Matcher m = pattern.matcher(s);
      m.matches();
      values[0] = Integer.parseInt(m.group(1));
      values[1] = Integer.parseInt(m.group(2));
      values[2] = Integer.parseInt(m.group(3));
      values[3] = Integer.parseInt(m.group(4));

      return new Part(values);
    }

    private int get(char c) {
      return values[xmas.indexOf(c)];
    }

    private int sum() {
      return Arrays.stream(values).sum();
    }
  }
}
