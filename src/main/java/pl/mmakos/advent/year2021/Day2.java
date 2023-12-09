package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day2 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[] pos = new int[2];
    Utils.lines()
            .map(Instruction::new)
            .forEach(i -> i.apply(pos));

    return pos[0] * pos[1];
  }

  private static int task2() {
    int[] pos = new int[3];
    Utils.lines()
            .map(Instruction::new)
            .forEach(i -> i.apply(pos));

    return pos[0] * pos[1];
  }

  private static class Instruction {
    int moveForward;
    int moveUp;

    private Instruction(String line) {
      String[] split = line.split(" ");
      int val = Integer.parseInt(split[1]);

      if (split[0].equals("up")) {
        moveUp = val;
      } else if (split[0].equals("down")) {
        moveUp = -val;
      } else {
        moveForward = val;
      }
    }

    private void apply(int[] val) {
      if (val.length == 2) {
        val[0] -= moveUp;
        val[1] += moveForward;
      } else {
        val[0] += moveForward;
        val[1] += moveForward * val[2];
        val[2] -= moveUp;
      }
    }
  }
}
