package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day9 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return solve(true);
  }

  private static int task2() {
    return solve(false);
  }

  private static int solve(boolean right) {
    return Utils.lines()
            .map(s -> Utils.splitToInts(s, " "))
            .map(Day9::addRowsTo0)
            .mapToInt(i -> ext(i, right))
            .sum();
  }

  private static List<int[]> addRowsTo0(int[] ints) {
    List<int[]> rows = new ArrayList<>();
    rows.add(ints);
    boolean zeros = false;

    for (int i = 1; !zeros; ++i) {
      zeros = true;
      int[] previous = rows.get(i - 1);
      int[] next = new int[ints.length - i];

      for (int j = 0; j < next.length; ++j) {
        int val = previous[j + 1] - previous[j];
        next[j] = val;
        if (val != 0) zeros = false;
      }
      rows.add(next);
    }

    return rows;
  }

  private static int ext(List<int[]> rows, boolean right) {
    int[] ext = new int[rows.size()];

    for (int i = rows.size() - 2; i >= 0; --i) {
      int[] row = rows.get(i);
      ext[i] = right ?
              ext[i + 1] + row[row.length - 1] :
              row[0] - ext[i + 1];
    }

    return ext[0];
  }

}
