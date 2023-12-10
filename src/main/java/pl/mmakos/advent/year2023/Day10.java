package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

@SuppressWarnings("java:S3776")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day10 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    int[] res = task12();
    System.err.println("TASK 1: " + res[0]);
    System.err.println("TASK 2: " + res[1]);
  }

  private static int[] task12() {
    int[][] input = input();
    Point start = getStart(input);
    Point startNeighbour = getStartNeighbour(input, start);

    removeNotLoop(input, startNeighbour, start);

    // Inside direction is direction perpendicular to start neighbour and pointed to loop inside
    // It is different for different outputs. For me, it's 2, because I have:
    // S
    // |
    // And loop goes clock-wise (so start neighbour is pointed left)
    int task1 = loop(input, startNeighbour, start, 2);
    fillBiggerSpaces(input);
    int task2 = (int) Arrays.stream(input)
      .flatMapToInt(Arrays::stream)
      .filter(i -> i == 0b10000)
      .count();

    return new int[]{task1, task2};
  }

  private static int loop(int[][] points, Point startNeighbour, Point startPoint,
                          @SuppressWarnings("SameParameterValue") int instideDirection) {
    Point previous = startPoint;
    Point current = startNeighbour;

    int i = 1;
    for (; !current.equals(startPoint); ++i) {
      markInsidePoints(points, current, instideDirection);

      PointAndDir neighbour = getNeighbour(points, current, previous);
      previous = current;
      current = neighbour.point;
      instideDirection += neighbour.turn;
    }

    return i / 2;
  }

  private static void fillBiggerSpaces(int[][] ints) {
    for (int i = 0; i < ints.length; ++i) {
      for (int j = 0; j < ints[0].length; ++j) {
        if (ints[i][j] == 0b10000) {
          if (i < ints.length - 1 && ints[i + 1][j] == 0b0000) ints[i + 1][j] = 0b10000;
          if (j < ints[j].length - 1 && ints[i][j + 1] == 0b0000) ints[i][j + 1] = 0b10000;
        }
      }
    }
  }

  private static void removeNotLoop(int[][] points, Point startNeighbour, Point startPoint) {
    Set<Point> loopPoints = new HashSet<>();
    Point previous = startPoint;
    Point current = startNeighbour;

    loopPoints.add(previous);

    while (!current.equals(startPoint)) {
      loopPoints.add(current);
      PointAndDir neighbour = getNeighbour(points, current, previous);
      previous = current;
      current = neighbour.point;
    }

    IntStream.range(0, points.length)
      .boxed()
      .flatMap(i -> IntStream.range(0, points[0].length)
        .mapToObj(j -> new Point(j, i)))
      .filter(not(loopPoints::contains))
      .forEach(p -> points[p.y()][p.x()] = 0b0000);
  }

  private static Point getStart(int[][] input) {
    return IntStream.range(0, input.length)
      .mapToObj(i -> IntStream.range(0, input[0].length)
        .filter(j -> input[i][j] == 0b1111)
        .mapToObj(j -> new Point(j, i))
        .findFirst())
      .filter(Optional::isPresent)
      .map(Optional::get)
      .findFirst()
      .orElseThrow();
  }

  private static void markInsidePoints(int[][] points, Point currentPoint, int insideDirection) {
    Point[] neighbours = new Point[]{currentPoint.right(), currentPoint.bottom(), currentPoint.left(), currentPoint.top()};
    int value = points[currentPoint.y()][currentPoint.x()];
    insideDirection = Math.floorMod(insideDirection, 4);

    Point[] insidePoints = switch (value) {
      case 0b1100 ->
        insideDirection == 2 || insideDirection == 3 ? new Point[]{currentPoint.left(), currentPoint.left().top(), currentPoint.top()} : new Point[0];
      case 0b0110 ->
        insideDirection == 3 || insideDirection == 0 ? new Point[]{currentPoint.right(), currentPoint.right().top(), currentPoint.top()} : new Point[0];
      case 0b0011 ->
        insideDirection == 0 || insideDirection == 1 ? new Point[]{currentPoint.right(), currentPoint.right().bottom(), currentPoint.bottom()} : new Point[0];
      case 0b1001 ->
        insideDirection == 1 || insideDirection == 2 ? new Point[]{currentPoint.left(), currentPoint.left().bottom(), currentPoint.bottom()} : new Point[0];
      default -> new Point[]{neighbours[insideDirection]};
    };

    for (Point point : insidePoints) {
      markInsidePoint(points, point);
    }
  }

  private static void markInsidePoint(int[][] points, Point point) {
    if (point.x() > 0 && point.y() > 0 && point.x() < points[0].length && point.y() < points.length &&
      points[point.y()][point.x()] == 0b0000) {
      points[point.y()][point.x()] = 0b10000;
    }
  }

  private static PointAndDir getNeighbour(int[][] points, Point point, Point previous) {
    List<PointAndDir> neighbours = getNeighbours(points, point);
    return neighbours.get(0).point.equals(previous) ? neighbours.get(1) : neighbours.get(0);
  }

  private static Point getStartNeighbour(int[][] points, Point start) {
    Point point = start.right();
    int value = points[point.y()][point.x()];
    if ((value & 0b0010) > 0) return point;

    point = start.bottom();
    value = points[point.y()][point.x()];
    if ((value & 0b0001) > 0) return point;

    point = start.left();
    value = points[point.y()][point.x()];
    if ((value & 0b1000) > 0) return point;

    throw new IllegalStateException();
  }

  private static List<PointAndDir> getNeighbours(int[][] points, Point point) {
    List<Pair<Point, Integer>> neighboursAndDirs = new ArrayList<>(2);
    int value = points[point.y()][point.x()];

    if ((value & 0b1000) > 0) neighboursAndDirs.add(new Pair<>(point.right(), 0));
    if ((value & 0b0100) > 0) neighboursAndDirs.add(new Pair<>(point.bottom(), 1));
    if ((value & 0b0010) > 0) neighboursAndDirs.add(new Pair<>(point.left(), 2));
    if ((value & 0b0001) > 0) neighboursAndDirs.add(new Pair<>(point.top(), 3));

    return neighboursAndDirs.stream()
      .map(p -> new PointAndDir(p.first(), getTurn(points[p.first().y()][p.first().x()], p.second())))
      .toList();
  }

  private static int[][] input() {
    return Utils.lines()
      .map(s -> s.chars()
        .map(c -> switch (c) {
          case '-' -> 0b1010;  // can go to: right, down, left, top
          case '|' -> 0b0101;
          case 'F' -> 0b1100;
          case '7' -> 0b0110;
          case 'J' -> 0b0011;
          case 'L' -> 0b1001;
          case 'S' -> 0b1111;
          default -> 0b0000;
        })
        .toArray()
      ).toArray(int[][]::new);
  }

  private static int getTurn(int next, int direction) {
    if (next == 0b1010 || next == 0b0101) {
      return 0;
    }
    if (direction == 0) return (next & 0b0001) > 0 ? -1 : 1;
    if (direction == 2) return (next & 0b0001) > 0 ? 1 : -1;
    if (direction == 1) return (next & 0b0010) > 0 ? 1 : -1;
    if (direction == 3) return (next & 0b0010) > 0 ? -1 : 1;

    throw new IllegalStateException();
  }

  private record PointAndDir(Point point, int turn) {
  }
}
