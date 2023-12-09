package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day23 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Set<Point> elves = input();
    char[] directions = new char[]{'N', 'S', 'W', 'E'};

    for (int i = 0; i < 10; ++i) {
      Map<Point, Point> dests = new HashMap<>();
      for (Point elf : elves) {
        Point dest = null;
        if (!isAlone(elf, elves)) {
          for (char dir : directions) {
            dest = getDest(elf, dir, elves);
            if (dest != null) break;
          }
        }
        dest = dest == null ? elf : dest;
        dests.put(elf, dest);
      }

      nextDirection(directions);
      elves.clear();
      dests.forEach((k, v) -> {
        if (isDistinctValue(dests, v)) {
          elves.add(v);
        } else {
          elves.add(k);
        }
      });
    }

    Point rectangle = getRectangle(elves);
    return rectangle.x() * rectangle.y() - elves.size();
  }

  private static int task2() {
    Set<Point> elves = input();
    char[] directions = new char[]{'N', 'S', 'W', 'E'};

    for (int i = 0; i < Integer.MAX_VALUE; ++i) {
      Map<Point, Point> dests = new HashMap<>();
      boolean noOneMoved = true;
      for (Point elf : elves) {
        Point dest = null;
        if (!isAlone(elf, elves)) {
          for (char dir : directions) {
            dest = getDest(elf, dir, elves);
            if (dest != null) break;
          }
        }
        if (dest == null) {
          dest = elf;
        } else {
          noOneMoved = false;
        }
        dests.put(elf, dest);
      }
      if (noOneMoved) {
        return i + 1;
      }

      nextDirection(directions);
      elves.clear();
      dests.forEach((k, v) -> {
        if (isDistinctValue(dests, v)) {
          elves.add(v);
        } else {
          elves.add(k);
        }
      });
    }

    throw new IllegalStateException();
  }

  private static Point getRectangle(Set<Point> points) {
    IntSummaryStatistics x = points.stream()
            .mapToInt(Point::x)
            .summaryStatistics();
    IntSummaryStatistics y = points.stream()
            .mapToInt(Point::y)
            .summaryStatistics();
    return new Point(x.getMax() - x.getMin() + 1, y.getMax() - y.getMin() + 1);
  }

  private static <V> boolean isDistinctValue(Map<?, V> map, V value) {
    return map.values().stream()
            .filter(value::equals)
            .count() == 1;
  }

  private static Point getDest(Point elf, char direction, Set<Point> elves) {
    return switch (direction) {
      case 'N' -> !elves.contains(new Point(elf.x(), elf.y() - 1)) &&
              !elves.contains(new Point(elf.x() - 1, elf.y() - 1)) &&
              !elves.contains(new Point(elf.x() + 1, elf.y() - 1)) ?
              new Point(elf.x(), elf.y() - 1) : null;
      case 'S' -> !elves.contains(new Point(elf.x(), elf.y() + 1)) &&
              !elves.contains(new Point(elf.x() - 1, elf.y() + 1)) &&
              !elves.contains(new Point(elf.x() + 1, elf.y() + 1)) ?
              new Point(elf.x(), elf.y() + 1) : null;
      case 'W' -> !elves.contains(new Point(elf.x() - 1, elf.y())) &&
              !elves.contains(new Point(elf.x() - 1, elf.y() - 1)) &&
              !elves.contains(new Point(elf.x() - 1, elf.y() + 1)) ?
              new Point(elf.x() - 1, elf.y()) : null;
      case 'E' -> !elves.contains(new Point(elf.x() + 1, elf.y())) &&
              !elves.contains(new Point(elf.x() + 1, elf.y() - 1)) &&
              !elves.contains(new Point(elf.x() + 1, elf.y() + 1)) ?
              new Point(elf.x() + 1, elf.y()) : null;
      default -> throw new IllegalStateException();
    };
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private static boolean isAlone(Point elf, Set<Point> elves) {
    return !elves.contains(new Point(elf.x(), elf.y() - 1)) &&
            !elves.contains(new Point(elf.x(), elf.y() + 1)) &&
            !elves.contains(new Point(elf.x() - 1, elf.y() - 1)) &&
            !elves.contains(new Point(elf.x() + 1, elf.y() - 1)) &&
            !elves.contains(new Point(elf.x() - 1, elf.y() + 1)) &&
            !elves.contains(new Point(elf.x() + 1, elf.y() + 1)) &&
            !elves.contains(new Point(elf.x() - 1, elf.y())) &&
            !elves.contains(new Point(elf.x() + 1, elf.y()));
  }

  private static Set<Point> input() {
    String[] str = Utils.read().split(Utils.ENDL);
    Set<Point> elves = new HashSet<>();
    for (int i = 0; i < str.length; ++i) {
      for (int j = 0; j < str[i].length(); ++j) {
        if (str[i].charAt(j) == '#') {
          elves.add(new Point(j, i));
        }
      }
    }
    return elves;
  }

  private static void nextDirection(char[] directions) {
    char first = directions[0];
    directions[0] = directions[1];
    directions[1] = directions[2];
    directions[2] = directions[3];
    directions[3] = first;
  }
}
