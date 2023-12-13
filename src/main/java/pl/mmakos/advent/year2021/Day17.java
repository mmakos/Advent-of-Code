package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Rect;
import pl.mmakos.advent.utils.Utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Math.max;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day17 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  // Brute-force, it computes at very short time anyway. If you get invalid solution,
  // then you can increase '200' and '100' values (especially first-one)
  private static int task1() {
    Rect input = input();

    return IntStream.range(0, 200)
            .filter(i -> IntStream.range(0, 100)
                    .mapToObj(j -> simulateLaunch(j, i, input))
                    .anyMatch(Optional::isPresent))
            .map(Day17::getHighestY)
            .max()
            .orElseThrow();
  }

  private static long task2() {
    Rect input = input();

    return IntStream.range(-200, 200)
            .boxed()
            .flatMap(i -> IntStream.range(0, 200)
                    .mapToObj(j -> simulateLaunch(j, i, input)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .count();
  }

  // -1 because Y-axis is reversed
  private static int getHighestY(int startY) {
    return (startY - 1) * startY / 2;
  }

  private static Optional<Point> simulateLaunch(int x, int y, Rect target) {
    Point p = new Point(0, 0);
    while (!p.isAfter(target)) {
      p = p.translate(x--, y++);
      if (target.contains(p)) return Optional.of(p);
      x = max(0, x);
    }
    return Optional.empty();
  }

  private static Rect input() {
    Pattern pattern = Pattern.compile("target area: x=(\\d+)\\.\\.(\\d+), y=-(\\d+)\\.\\.-(\\d+)");
    Matcher m = pattern.matcher(Utils.read());
    //noinspection ResultOfMethodCallIgnored
    m.matches();
    int x1 = Integer.parseInt(m.group(1));
    int x2 = Integer.parseInt(m.group(2));
    int y2 = Integer.parseInt(m.group(3));
    int y1 = Integer.parseInt(m.group(4));

    return new Rect(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
  }
}
