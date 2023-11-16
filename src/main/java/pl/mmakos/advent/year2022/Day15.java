package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
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
    int[] bounds = getBounds(input);
    int row = 2_000_000;
    char[] rowChars = new char[bounds[1] - bounds[0] + 1];
    int zeroPos = -bounds[0];

    return (int) find(row, zeroPos, rowChars, input).chars()
            .filter(c -> c == '#')
            .count();
  }

  private static int task2() {
    Pair<Point, Point>[] input = input();
    AtomicInteger result = new AtomicInteger();

    ExecutorService executorService = Executors.newFixedThreadPool(16);
    for (int i = 0; i < 16; ++ i) {
      int finalI = i * 250_000;
      char[] rowChars = new char[4_000_000];
      executorService.execute(() -> {
        for (int row = finalI; row < finalI + 250_000; ++row) {
          String str = find(row, 0, rowChars, input);
          int idx = str.indexOf('\0');
          Arrays.fill(rowChars, '\0');
          if (idx != -1) {
            result.set(4_000_000 * idx + row);
            System.err.println("TASK 2!!!: " + result.get());
            executorService.shutdown();
            return;
          }
          if (row % 10_000 == 0) {
            System.err.println(row);
          }
        }
      });
    }

    return 0;
  }

  private static String find(int row, int zeroPos, char[] rowChars, Pair<Point, Point>[] input) {
    for (Pair<Point, Point> pair : input) {
      Point sensor = pair.first();
      Point beacon = pair.second();

      if (beacon.y() == row && beacon.x() >= -zeroPos) {
        rowChars[beacon.x() + zeroPos] = 'B';
      }
      int noBeaconFieldsInR = distance(sensor, beacon) - abs(sensor.y() - row);
      if (noBeaconFieldsInR >= 0) {
        for (int i = Math.max(-zeroPos, sensor.x() - noBeaconFieldsInR + zeroPos); i < Math.min(rowChars.length + zeroPos, sensor.x() + noBeaconFieldsInR + zeroPos + 1); ++i) {
          if (rowChars[i] != 'B') {
            rowChars[i] = '#';
          }
        }
      }
      if (!new String(rowChars).contains("\0")) break;
    }

    return new String(rowChars);
  }

  @SuppressWarnings("unchecked")
  private static int[] getBounds(Pair<Point, Point>[] input) {
    List<Integer>[] boundLists = new List[]{new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
    Arrays.stream(input)
            .map(p -> getBounds(p.first(), p.second()))
            .forEach(b -> {
              boundLists[0].add(b[0]);
              boundLists[1].add(b[1]);
              boundLists[2].add(b[2]);
              boundLists[3].add(b[3]);
            });
    return new int[]{
            boundLists[0].stream().mapToInt(i -> i).min().orElseThrow(),
            boundLists[1].stream().mapToInt(i -> i).max().orElseThrow(),
            boundLists[2].stream().mapToInt(i -> i).min().orElseThrow(),
            boundLists[3].stream().mapToInt(i -> i).max().orElseThrow(),
    };
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

  private static int[] getBounds(Point sensor, Point beacon) {
    int distance = distance(sensor, beacon);
    return new int[]{
            sensor.x() - distance,  // min x
            sensor.x() + distance,  // max x
            sensor.y() - distance,  // min y
            sensor.y() + distance,  // max y
    };
  }
}
