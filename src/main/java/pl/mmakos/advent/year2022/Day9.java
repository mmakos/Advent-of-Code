package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day9 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return proceed(2);
  }

  private static int task2() {
    return proceed(10);
  }

  private static int proceed(int amount) {
    Set<Position> visited = new HashSet<>();
    Position[] positions = IntStream.range(0, amount)
            .mapToObj(i -> new Position(0, 0))
            .toArray(Position[]::new);
    visited.add(positions[positions.length - 1]);

    List<String> moves = Utils.lines(9, 2022)
            .map(l -> l.split(" "))
            .flatMap(l -> IntStream.range(0, Integer.parseInt(l[1]))
                    .mapToObj(i -> l[0]))
            .toList();
    for (String mv : moves) {
      if ("U".equals(mv)) {
        positions[0] = new Position(positions[0].x, positions[0].y + 1);
      } else if ("D".equals(mv)) {
        positions[0] = new Position(positions[0].x, positions[0].y - 1);
      } else if ("R".equals(mv)) {
        positions[0] = new Position(positions[0].x + 1, positions[0].y);
      } else if ("L".equals(mv)) {
        positions[0] = new Position(positions[0].x - 1, positions[0].y);
      }

      for (int i = 1; i < positions.length; ++i) {
        positions[i] = positions[i].getNext(positions[i - 1]);
      }

      visited.add(positions[positions.length - 1]);
    }
    return visited.size();
  }

  private record Position(int x, int y) {
    Position getNext(Position parent) {
      if (Math.abs(parent.x - x) <= 1 && Math.abs(parent.y - y) <= 1) {
        return this;
      }
      int nX = x;
      int nY = y;
      if (x > parent.x) {
        --nX;
      } else if (x < parent.x) {
        ++nX;
      }
      if (y > parent.y) {
        --nY;
      } else if (y < parent.y) {
        ++nY;
      }
      return new Position(nX, nY);
    }
  }
}
