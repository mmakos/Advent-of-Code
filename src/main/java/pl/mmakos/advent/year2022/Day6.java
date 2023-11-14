package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day6 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return getMarkerPosition(Utils.read(6, 2022), 4);
  }

  private static int task2() {
    return getMarkerPosition(Utils.read(6, 2022), 14);
  }

  private static int getMarkerPosition(String str, int distinct) {
    int markerPos = 0;
    int[] marker = IntStream.range(0, distinct)
            .toArray();
    marker[marker.length - 1] = str.charAt(0);

    for (int i = 0; i < str.length(); ++i) {
      marker[markerPos++] = str.charAt(i);
      if (isUnique(marker)) {
        return i + 1;
      }
      markerPos %= distinct;
    }
    return -1;
  }

  private static boolean isUnique(int[] chars) {
    return Arrays.stream(chars)
            .distinct()
            .count() == chars.length;
  }
}
