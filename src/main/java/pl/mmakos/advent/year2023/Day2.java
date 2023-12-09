package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day2 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return input()
            .filter(g -> g.possible(12, 13, 14))
            .mapToInt(Game::number)
            .sum();
  }

  private static int task2() {
    return input()
            .map(Game::leastCubesRGB)
            .mapToInt(i -> i[0] * i[1] * i[2])
            .sum();
  }

  private static Stream<Game> input() {
    return Utils.lines()
            .map(Day2::game);
  }

  private static Game game(String line) {
    String[] split = line.split(": ");
    int gameNumber = Integer.parseInt(split[0].split(" ")[1]);
    Set<Map<String, Integer>> bags = Arrays.stream(split[1].split("; "))
            .map(Day2::bag)
            .collect(Collectors.toSet());
    return new Game(gameNumber, bags);
  }

  private static Map<String, Integer> bag(String s) {
    return Arrays.stream(s.split(", "))
            .map(spl -> spl.split(" "))
            .collect(toMap(v -> v[1], v -> Integer.parseInt(v[0])));
  }

  private record Game(int number, Set<Map<String, Integer>> game) {
    private boolean possible(int red, int green, int blue) {
      return game.stream()
              .allMatch(m -> m.getOrDefault("red", 0) <= red &&
                      m.getOrDefault("green", 0) <= green &&
                      m.getOrDefault("blue", 0) <= blue);
    }

    private int[] leastCubesRGB() {
      return new int[] {
              game.stream().mapToInt(m -> m.getOrDefault("red", 0)).max().orElseThrow(),
              game.stream().mapToInt(m -> m.getOrDefault("green", 0)).max().orElseThrow(),
              game.stream().mapToInt(m -> m.getOrDefault("blue", 0)).max().orElseThrow()
      };
    }
  }
}
