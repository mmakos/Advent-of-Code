package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day6 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    int[][] input = Utils.lines(6, 2023)
          .map(s -> s.split(":\\s+")[1])
          .map(Utils::toInts)
          .toArray(int[][]::new);
    input = Utils.transpose(input);

    return Arrays.stream(input)
            .mapToLong(i -> getWins(i[0], i[1]))
            .reduce(1, (acc, v) -> acc * v);
  }

  private static long task2() {
    long[] input = Utils.lines(6, 2023)
            .map(s -> s.split(":\\s+")[1])
            .map(s -> s.replaceAll("\\s+", ""))
            .mapToLong(Long::parseLong)
            .toArray();

    return getWins(input[0], input[1]);
  }

  private static long getWins(long time, long distance) {
    long wins = 0;
    for (long i = 0; i < time; ++i) {
      long d = getDistance(i, time);
      if (d > distance) {
        ++wins;
      } else if (wins > 0) {
        break;
      }
    }
    return wins;
  }

  private static long getDistance(long buttonTime, long time) {
    return (time - buttonTime) * buttonTime;
  }
}
