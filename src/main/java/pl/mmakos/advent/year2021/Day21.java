package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import static java.lang.Math.max;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day21 {
  private static final int[][] possibilities = new int[][]{
          new int[]{3, 1},
          new int[]{4, 3},
          new int[]{5, 6},
          new int[]{6, 7},
          new int[]{7, 6},
          new int[]{8, 3},
          new int[]{9, 1},
  };

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return play(input());
  }

  private static long task2() {
    int[] input = input();
    long[] result = play(input[0], 0, input[1], 0);
    return max(result[0], result[1]);
  }

  private static int play(int[] players) {
    int dice = 0;
    int[] scores = new int[2];
    while (true) {
      for (int i = 0; i < 2; ++i) {
        int move = 0;
        for (int j = 0; j < 3; ++j) {
          move += dice++ % 100 + 1;
        }
        players[i] = (players[i] + move - 1) % 10 + 1;
        scores[i] += players[i];
        if (scores[i] >= 1000) {
          return dice * scores[(i + 1) % 2];
        }
      }
    }
  }

  private static long[] play(int player1, int score1, int player2, int score2) {
    if (score2 >= 21) return new long[]{0, 1};

    long[] wins = new long[2];
    for (int[] possibility : possibilities) {
      // We call this function in turns - once for player 1 moving, and once for player 2
      int newPosition = (player1 + possibility[0] - 1) % 10 + 1;
      long[] play = play(player2, score2, newPosition, score1 + newPosition);
      wins[0] += play[1] * possibility[1];
      wins[1] += play[0] * possibility[1];
    }
    return wins;
  }

  private static int[] input() {
    return Utils.lines()
            .map(s -> s.split(" "))
            .map(s -> s[s.length - 1])
            .mapToInt(Integer::parseInt)
            .toArray();
  }
}
