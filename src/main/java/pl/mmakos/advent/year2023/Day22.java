package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;
import static java.util.function.Predicate.not;
import static pl.mmakos.advent.utils.Utils.or;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day22 {
  @SuppressWarnings("java:S106")
  // It's slow (first task is running about 10s and second 40s), so there is probably something to optimize it
  public static void main(String[] args) {
    List<Brick> input = input();
    fallDown(input);

    System.err.println("TASK 1: " + task1(input));
    System.err.println("TASK 2: " + task2(input));
  }

  private static long task1(List<Brick> input) {
    Set<Brick> copy = new HashSet<>(input);
    return input.stream()
            .filter(b -> {
              copy.remove(b);
              boolean anyFall = copy.stream().anyMatch(b1 -> b1.canFallDown(copy));
              copy.add(b);
              return !anyFall;
            })
            .count();
  }

  private static long task2(List<Brick> input) {
    long sum = 0;
    for (Brick brick : input) {
      List<Brick> copy = input.stream()
              .map(Brick::copy)
              .collect(Collectors.toList());
      copy.remove(brick);
      List<Brick> notFallen = new ArrayList<>(copy);

      boolean anyFall = true;
      while (anyFall) {
        anyFall = false;
        for (int i = notFallen.size() - 1; i >= 0; --i) {
          Brick b = notFallen.get(i);
          if (b.canFallDown(copy)) {
            b.fallDown();
            notFallen.remove(i);
            anyFall = true;
          }
        }
      }

      sum += copy.size() - notFallen.size();
      copy.add(brick);
    }

    return sum;
  }

  private static void fallDown(Collection<Brick> bricks) {
    boolean anyFall = true;
    while (anyFall) {
      anyFall = false;
      for (Brick b : bricks) {
        if (b.canFallDown(bricks)) {
          b.fallDown();
          anyFall = true;
        }
      }
    }
  }

  private static List<Brick> input() {
    return Utils.lines()
            .map(Brick::parse)
            .toList();
  }

  @EqualsAndHashCode
  @AllArgsConstructor
  private static class Point3D {
    private final int x;
    private final int y;
    private int z;

    private Point3D(String s) {
      String[] split = s.split(",");
      x = parseInt(split[0]);
      y = parseInt(split[1]);
      z = parseInt(split[2]);
    }

    private Point3D copy() {
      return new Point3D(x, y, z);
    }
  }

  private record Brick(Point3D begin, Point3D end) {
    private static Brick parse(String s) {
      String[] split = s.split("~");
      Point3D begin = new Point3D(split[0]);
      Point3D end = new Point3D(split[1]);
      if (begin.x > end.x || begin.y > end.y || begin.z > end.z) {  // begin is always lower (x or y)
        Point3D tmp = begin;
        begin = end;
        end = tmp;
      }
      return new Brick(begin, end);
    }

    private boolean canFallDown(Collection<Brick> bricks) {
      if (begin.z <= 1) return false;  // Cannot fall down lower than 1
      return bricks.stream()
              .allMatch(or(this::isBrickNotOneLevelBelow, not(this::isXYIntersecting)));
    }

    private boolean isBrickNotOneLevelBelow(Brick brick) {
      return brick.begin.z >= begin.z || brick.end.z >= begin.z || // Both z need to be lower than this brick
              (brick.begin.z < begin.z - 1 && brick.end.z < begin.z - 1); // one of begin/end z has to be one level below this
    }

    private boolean isXYIntersecting(Brick brick) {
      return min(end.x, brick.end.x) >= max(begin.x, brick.begin.x) &&
              min(end.y, brick.end.y) >= max(begin.y, brick.begin.y);
    }

    private void fallDown() {
      --end.z;
      --begin.z;
    }

    private Brick copy() {
      return new Brick(begin.copy(), end.copy());
    }
  }
}
