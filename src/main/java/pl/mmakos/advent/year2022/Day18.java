package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.stream.IntStream;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day18 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[][][] input = input();
    return countSurfaceArea(input);
  }

  private static int task2() {
    int[][][] input = input();
    return countSurfaceArea(fill(input));
  }


  private static int countSurfaceArea(int[][][] xyz) {
    int surfaceArea = 0;
    int maxX = xyz.length - 1;
    int maxY = xyz[0].length - 1;
    int maxZ = xyz[0][0].length - 1;
    for (int x = 0; x <= maxX; ++x) {
      for (int y = 0; y <= maxY; ++y) {
        for (int z = 0; z <= maxZ; ++z) {
          if (xyz[x][y][z] == 1) {
            if (x == maxX || xyz[x + 1][y][z] == 0) ++surfaceArea;  // RIGHT
            if (x == 0 || xyz[x - 1][y][z] == 0) ++surfaceArea;     // LEFT
            if (y == maxY || xyz[x][y + 1][z] == 0) ++surfaceArea;  // TOP
            if (y == 0 || xyz[x][y - 1][z] == 0) ++surfaceArea;     // BOTTOM
            if (z == maxZ || xyz[x][y][z + 1] == 0) ++surfaceArea;  // BEHIND
            if (z == 0 || xyz[x][y][z - 1] == 0) ++surfaceArea;     // IN FRONT
          }
        }
      }
    }
    return surfaceArea;
  }

  private static int[][][] fill(int[][][] xyz) {
    // We add one-cube-wide wall on each side to be sure that watter can cool lava from all sides
    int[][][] newXYZ = new int[xyz.length + 2][xyz[0].length + 2][xyz[0][0].length + 2];
    for (int x = 0; x < xyz.length; ++x) {
      for (int y = 0; y < xyz[0].length; ++y) {
        System.arraycopy(xyz[x][y], 0, newXYZ[x + 1][y + 1], 1, xyz[x][y].length);
      }
    }
    xyz = newXYZ;
    // Now we assume that -1 is air, 0 is water and 1 is lava
    // So before fill we have only lava and air
    deepReplace(xyz, 0, -1);
    bfs(xyz);
    return xyz;
  }

  // We fill cubes with bfs - every cube leads to at most 6 cubes
  // If cube is not touching with given side with lava-cube, then there is graph edge
  private static void bfs(int[][][] xyz) {
    Queue<Point> queue = new UniqueQueue<>();
    queue.add(new Point(0, 0, 0));
    int maxX = xyz.length - 1;
    int maxY = xyz[0].length - 1;
    int maxZ = xyz[0][0].length - 1;

    while (!queue.isEmpty()) {
      Point point = queue.poll();
      xyz[point.x][point.y][point.z] = 0;
      if (point.x < maxX && xyz[point.x + 1][point.y][point.z] == -1)
        queue.add(new Point(point.x + 1, point.y, point.z));  // RIGHT
      if (point.x > 0 && xyz[point.x - 1][point.y][point.z] == -1)
        queue.add(new Point(point.x - 1, point.y, point.z));  // LEFT
      if (point.y < maxY && xyz[point.x][point.y + 1][point.z] == -1)
        queue.add(new Point(point.x, point.y + 1, point.z));  // TOP
      if (point.y > 0 && xyz[point.x][point.y - 1][point.z] == -1)
        queue.add(new Point(point.x, point.y - 1, point.z));  // BOTTOM
      if (point.z < maxZ && xyz[point.x][point.y][point.z + 1] == -1)
        queue.add(new Point(point.x, point.y, point.z + 1));  // BEHIND
      if (point.z > 0 && xyz[point.x][point.y][point.z - 1] == -1)
        queue.add(new Point(point.x, point.y, point.z - 1));  // IN FRONT
    }
  }

  private static void deepReplace(int[][][] array, int from, int to) {
    for (int i = 0; i < array.length; ++i) {
      for (int j = 0; j < array[0].length; ++j) {
        for (int k = 0; k < array[0][0].length; ++k) {
          if (array[i][j][k] == from) array[i][j][k] = to;
        }
      }
    }
  }

  // input as [x][y][z], where 1 - is droplet, 0 - no droplet
  private static int[][][] input() {
    int[][] input = Utils.ints(18, 2022, Utils.ENDL, ",")
            .map(IntStream::toArray)
            .toArray(int[][]::new);
    int[] maxes = Arrays.stream(input)
            .reduce(new int[3], (acc, v) -> {
              if (v[0] > acc[0]) acc[0] = v[0];
              if (v[1] > acc[1]) acc[1] = v[1];
              if (v[2] > acc[2]) acc[2] = v[2];
              return acc;
            });
    int[][][] array = new int[maxes[0] + 1][maxes[1] + 1][maxes[2] + 1];

    for (int[] i : input) {
      array[i[0]][i[1]][i[2]] = 1;
    }

    return array;
  }

  private record Point(int x, int y, int z) {
  }

  private static class UniqueQueue<T> extends ArrayDeque<T> {
    @Override
    public boolean add(T t) {
      if (!contains(t)) {
        return super.add(t);
      } else return false;
    }
  }
}
