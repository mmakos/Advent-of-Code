package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import java.util.stream.IntStream;

public class Day8 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    int[][] heights = getHeights();
    return IntStream.range(0, heights.length)
            .mapToLong(i -> IntStream.range(0, heights[i].length)
                    .filter(j -> isVisible(i, j, heights))
                    .count())
            .sum();
  }

  private static int task2() {
    int[][] heights = getHeights();
    return IntStream.range(0, heights.length)
            .map(i -> IntStream.range(0, heights[i].length)
                    .map(j -> getScore(i, j, heights))
                    .max()
                    .orElseThrow())
            .max()
            .orElseThrow();
  }

  private static int[][] getHeights() {
    return Utils.lines()
            .map(s -> Utils.split(s, 1)
                    .mapToInt(Integer::parseInt)
                    .toArray())
            .toArray(int[][]::new);
  }

  private static boolean isVisible(int i, int j, int[][] heights) {
    int value = heights[i][j];
    int[] row = heights[i];

    return getLeftVisible(j, row, value) == j ||
            getRightVisible(j, row, value) == row.length - j - 1 ||
            getTopVisible(i, j, heights, value) == i ||
            getBottomVisible(i, j, heights, value) == heights.length - i - 1;
  }

  private static int getScore(int i, int j, int[][] heights) {
    int value = heights[i][j];
    int[] row = heights[i];

    return Math.min(getLeftVisible(j, row, value) + 1, j) *
            Math.min(getRightVisible(j, row, value) + 1, row.length - j - 1) *
            Math.min(getTopVisible(i, j, heights, value) + 1, i) *
            Math.min(getBottomVisible(i, j, heights, value) + 1, heights.length - i - 1);
  }

  private static int getBottomVisible(int i, int j, int[][] heights, int value) {
    int visible = 0;
    for (int k = i + 1; k < heights.length; ++k) {
      if (heights[k][j] < value) {
        ++visible;
      } else {
        break;
      }
    }
    return visible;
  }

  private static int getTopVisible(int i, int j, int[][] heights, int value) {
    int visible = 0;
    for (int k = i - 1; k >= 0; --k) {
      if (heights[k][j] < value) {
        ++visible;
      } else {
        break;
      }
    }
    return visible;
  }

  private static int getRightVisible(int j, int[] row, int value) {
    int visible = 0;
    for (int k = j + 1; k < row.length; ++k) {
      if (row[k] < value) {
        ++visible;
      } else {
        break;
      }
    }
    return visible;
  }

  private static int getLeftVisible(int j, int[] row, int value) {
    int visible = 0;
    for (int k = j - 1; k >= 0; --k) {
      if (row[k] < value) {
        ++visible;
      } else {
        break;
      }
    }
    return visible;
  }
}
