package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day14 {
  public static final int CYCLES = 1_000_000_000;

  // I've implemented in with sets of points, and it works in milliseconds,
  // but array implementation would probably be more efficient
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Input input = input();
    Set<Point> rounded = input.rounded();
    Set<Point> squared = input.squared();
    int height = input.height;

    for (int i = 1; i < height; ++i) {
      moveUp(i, rounded, squared);
    }
    return count(rounded, height);
  }

  // Whole trick to task 1 is to find repeating pattern
  @SuppressWarnings("java:S127")
  private static int task2() {
    Input input = input();
    Set<Point> rounded = input.rounded();
    Set<Point> squared = input.squared();
    int width = input.width;
    int height = input.height;

    List<Pair<Set<Point>, Set<Point>>> cache = new ArrayList<>();

    for (int i = 0; i < CYCLES; ++i) {
      moveCycle(rounded, squared, width, height);
      Pair<Set<Point>, Set<Point>> pair = new Pair<>(new HashSet<>(rounded), new HashSet<>(squared));
      int pIdx = cache.indexOf(pair);
      if (pIdx != -1) {
        // i is what's left after 'n' repetitions of length 'cache.size() - pIdx'
        i = CYCLES - ((CYCLES - i) % (cache.size() - pIdx));
      } else {
        cache.add(pair);
      }
    }

    return count(rounded, height);
  }

  private static int count(Set<Point> rounded, int height) {
    return rounded.stream()
            .mapToInt(Point::y)
            .map(y -> height - y)
            .sum();
  }

  private static void moveCycle(Set<Point> rounded, Set<Point> squared, int width, int height) {
    for (int i = 1; i < height; ++i) {
      moveUp(i, rounded, squared);
    }
    for (int i = 1; i < width; ++i) {
      moveLeft(i, rounded, squared);
    }
    for (int i = height - 1; i >= 0; --i) {
      moveDown(i, height, rounded, squared);
    }
    for (int i = width - 1; i >= 0; --i) {
      moveRight(i, width, rounded, squared);
    }
  }

  private static void moveUp(int row, Set<Point> rounded, Set<Point> squared) {
    new ArrayList<>(rounded).stream()
            .filter(p -> p.y() == row)
            .forEach(p -> moveRounded(p, rounded, squared, Point::top, pt -> pt.y() >= 0));
  }

  private static void moveDown(int row, int height, Set<Point> rounded, Set<Point> squared) {
    new ArrayList<>(rounded).stream()
            .filter(p -> p.y() == row)
            .forEach(p -> moveRounded(p, rounded, squared, Point::bottom, pt -> pt.y() < height));
  }

  private static void moveLeft(int col, Set<Point> rounded, Set<Point> squared) {
    new ArrayList<>(rounded).stream()
            .filter(p -> p.x() == col)
            .forEach(p -> moveRounded(p, rounded, squared, Point::left, pt -> pt.x() >= 0));
  }

  private static void moveRight(int col, int width, Set<Point> rounded, Set<Point> squared) {
    new ArrayList<>(rounded).stream()
            .filter(p -> p.x() == col)
            .forEach(p -> moveRounded(p, rounded, squared, Point::right, pt -> pt.x() < width));
  }

  private static void moveRounded(Point rock, Set<Point> rounded, Set<Point> squared,
          UnaryOperator<Point> move, Predicate<Point> canMove) {
    Point up = move.apply(rock);
    Point highest = rock;
    while (canMove.test(up) && !rounded.contains(up) && !squared.contains(up)) {
      highest = up;
      up = move.apply(up);
    }
    if (rock != highest) {
      rounded.remove(rock);
      rounded.add(highest);
    }
  }

  private static Input input() {
    int[][] input = Utils.lines()
            .map(String::chars)
            .map(IntStream::toArray)
            .toArray(int[][]::new);

    Set<Point> rounded = IntStream.range(0, input.length)
            .boxed()
            .flatMap(i -> IntStream.range(0, input[0].length)
                    .filter(j -> input[i][j] == 'O')
                    .mapToObj(j -> new Point(j, i)))
            .collect(Collectors.toSet());

    Set<Point> squared = IntStream.range(0, input.length)
            .boxed()
            .flatMap(i -> IntStream.range(0, input[0].length)
                    .filter(j -> input[i][j] == '#')
                    .mapToObj(j -> new Point(j, i)))
            .collect(Collectors.toSet());

    return new Input(rounded, squared, input[0].length, input.length);
  }

  private record Input(Set<Point> rounded, Set<Point> squared, int width, int height) {
  }

  public static String toString(int width, int height, Set<Point> rounded, Set<Point> squared) {
    char[][] c = new char[height][width];

    for (char[] ch : c) {
      Arrays.fill(ch, '.');
    }

    rounded.forEach(p -> c[p.y()][p.x()] = 'O');
    squared.forEach(p -> c[p.y()][p.x()] = '#');
    return Utils.toString(c);
  }
}
