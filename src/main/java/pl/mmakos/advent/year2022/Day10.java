package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day10 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    Pair<Integer, String> task12 = task12();
    System.err.println("TASK 1: " + task12.first());
    System.err.println("TASK 2:\n" + task12.second());
  }

  private static Pair<Integer, String> task12() {
    int[] ints = parseInput();

    int strength = 0;
    StringBuilder sb = new StringBuilder();

    int i = 0;
    int ticksToNext = 0;
    int x = 1;
    int toAdd = 0;
    for (int cycle = 1; ; ++cycle) {
      if (ticksToNext <= 0) {
        x += toAdd;
        toAdd = 0;
        int next = ints[i++];
        if (next == 0) {
          ticksToNext = 1;
        } else {
          ticksToNext = 2;
          toAdd = next;
        }
      }

      int px = (cycle - 1) % 40;
      boolean visible = px >= x - 1 && px <= x + 1;
      sb.append(visible ? "#" : ".");
      if (cycle % 40 == 0) {
        sb.append("\n");
      }

      if (cycle % 40 == 20 && cycle <= 220) {
        strength += cycle * x;
      }

      --ticksToNext;
      if (i >= ints.length) {
        break;
      }
    }

    return new Pair<>(strength, sb.toString());
  }

  private static int[] parseInput() {
    return Utils.lines(10, 2022)
            .filter(not(String::isBlank))
            .mapToInt(line -> {
              if (line.startsWith("addx")) {
                return Integer.parseInt(line.substring(5));
              } else {
                return 0;
              }
            })
            .toArray();
  }
}
