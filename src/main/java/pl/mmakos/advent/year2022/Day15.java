package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day15 {
  private static final Pattern INPUT_PATTERN = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Pair<Point, Point>[] input = input();
    int row = 2_000_000;

    Pair<Ranges, Integer> result = find(row, input);

    return result.first().sum() - result.second();
  }

  private static long task2() {
    Pair<Point, Point>[] input = input();
    int constraint = 4_000_000;
    Pair<Ranges, Integer> ranges = IntStream.rangeClosed(0, constraint)
            .mapToObj(i -> new Pair<>(find(i, input).first(), i))
            .filter(r -> r.first().sum(0, constraint) == constraint)
            .findFirst()
            .orElseThrow();
    int x = ranges.first().ranges.get(0)[1] + 1;

    return x * 4_000_000L + ranges.second();
  }

  private static Pair<Ranges, Integer> find(int row, Pair<Point, Point>[] input) {
    Ranges ranges = new Ranges();
    Set<Integer> beaconsInRow = new HashSet<>();
    for (Pair<Point, Point> pair : input) {
      Point sensor = pair.first();
      Point beacon = pair.second();

      if (beacon.y() == row) {
        beaconsInRow.add(beacon.x());
      }
      int noBeaconFieldsInR = distance(sensor, beacon) - abs(sensor.y() - row);
      if (noBeaconFieldsInR >= 0) {
        ranges.addRange(sensor.x() - noBeaconFieldsInR, sensor.x() + noBeaconFieldsInR);
      }
    }

    return new Pair<>(ranges, beaconsInRow.size());
  }

  @SuppressWarnings("unchecked")
  private static Pair<Point, Point>[] input() {
    return Utils.lines(15, 2022)
            .filter(not(String::isBlank))
            .map(INPUT_PATTERN::matcher)
            .filter(Matcher::matches)
            .map(m -> new Pair<>(new Point(parseInt(m.group(1)), parseInt(m.group(2))),
                    new Point(parseInt(m.group(3)), parseInt(m.group(4)))))
            .toArray(Pair[]::new);
  }

  private static int distance(Point sensor, Point beacon) {
    return abs(sensor.x() - beacon.x()) + abs(sensor.y() - beacon.y());
  }

  private static class Ranges {
    @SuppressWarnings("java:S1700")
    private final List<int[]> ranges = new ArrayList<>();

    private void addRange(int start, int end) {
      if (ranges.isEmpty()) {
        ranges.add(new int[]{start, end});
        return;
      }

      int endIdx = 0;
      int startIdx = 0;
      for (int[] range : ranges) {
        if (start < range[0] && end < range[1]) {
          break;
        }
        if (start >= range[0]) {
          ++startIdx;
        }
        if (end > range[1]) {
          ++endIdx;
        }
      }

      if (endIdx < startIdx) return;

      int[] removePrevious = null;
      int[] removeNext = null;
      if (startIdx > 0 && start <= ranges.get(startIdx - 1)[1]) {
        start = ranges.get(startIdx - 1)[0];
        removePrevious = ranges.get(startIdx - 1);
      }
      if (endIdx < ranges.size() && end >= ranges.get(endIdx)[0]) {
        end = ranges.get(endIdx)[1];
        removeNext = ranges.get(endIdx);
      }
      ranges.subList(startIdx, endIdx).clear();
      if (removePrevious != null) {
        ranges.remove(removePrevious);
        --startIdx;
      }
      if (removeNext != null) {
        ranges.remove(removeNext);
      }
      ranges.add(startIdx, new int[]{start, end});
    }

    private int sum() {
      return ranges.stream()
              .mapToInt(r -> r[1] - r[0] + 1)
              .sum();
    }

    private int sum(int start, int end) {
      return ranges.stream()
              .mapToInt(r -> {
                int s = Math.max(r[0], start);
                int e = Math.min(r[1], end);
                return e - s + 1;
              })
              .sum();
    }
  }
}
