package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static pl.mmakos.advent.utils.Utils.lines;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day18 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return solve(lines().map(Move::parse).toArray(Move[]::new));
  }

  private static long task2() {
    return solve(lines().map(Move::parse2).toArray(Move[]::new));
  }

  private static long solve(Move[] input) {
    Pair<List<Point>, Long> path = getPath(input);
    long area = shoelaceFormula(path.first());
    // Area is not exactly number of points, so we need to add interior points and path length (path points)
    return picksTheorem(area, path.second()) + path.second();
  }

  @SuppressWarnings("java:S1121")
  private static Pair<List<Point>, Long> getPath(Move[] moves) {
    List<Point> path = new ArrayList<>();
    path.add(new Point(0, 0));

    long pathLength = 0;
    for (Move move : moves) {
      path.add(path.getLast().translateDir(move.amount, move.direction));
      pathLength += move.amount;
    }

    return new Pair<>(path, pathLength);
  }

  private static long shoelaceFormula(List<Point> points) {
    long area = 0;
    for (int i = 0; i < points.size(); ++i) {
      Point curr = points.get(i);
      Point next = points.get((i + 1) % points.size());
      area += (long) (curr.y() + next.y()) * (curr.x() - next.x());
    }
    return abs(area) / 2;
  }

  private static long picksTheorem(long area, long pathLength) {
    return area - pathLength / 2 + 1L;
  }

  private record Move(int direction, int amount) {
    private static final List<String> directions = List.of("R", "D", "L", "U");
    private static final Pattern pattern = Pattern.compile("([RDLU]) (\\d+) \\(#([0-9a-f]{5})([0-3])\\)");

    private static Move parse(String s) {
      Matcher matcher = pattern.matcher(s);
      //noinspection ResultOfMethodCallIgnored
      matcher.matches();
      return new Move(getDir(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    private static Move parse2(String s) {
      Matcher matcher = pattern.matcher(s);
      //noinspection ResultOfMethodCallIgnored
      matcher.matches();
      return new Move(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(3), 16));
    }

    private static int getDir(String s) {
      return directions.indexOf(s);
    }
  }
}
