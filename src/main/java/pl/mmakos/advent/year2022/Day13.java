package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day13 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[] ints = Utils.strings(13, 2022, Utils.ENDL_2, Utils.ENDL)
            .map(s -> s.toArray(String[]::new))
            .filter(s -> s.length == 2)
            .map(Day13::compare)
            .mapToInt(b -> Boolean.TRUE.equals(b) ? 1 : 0)
            .toArray();

    return IntStream.rangeClosed(1, ints.length)
            .filter(i -> ints[i - 1] == 1)
            .sum();
  }

  private static int task2() {
    return 0;
  }

  private static boolean compare(String[] s) {
    Input left = Input.getInput(s[0]);
    Input right = Input.getInput(s[1]);

    System.err.println(left);
    System.err.println(right);

    return true;
  }

  private interface Input {
    static Input getInput(String s) {
      if (!s.contains("[")) {
        return new InputList(Arrays.stream(s.split(","))
                .filter(not(String::isBlank))
                .map(Integer::parseInt)
                .map(IntInput::new)
                .map(Input.class::cast)
                .toList());
      }

      int nest = 0;
      int idx = 0;
      List<Input> inputs = new ArrayList<>();
      while (idx < s.length()) {
        int intStart = idx;
        if (s.charAt(idx) == '[') {
          ++nest;
          while (nest > 0) {
            ++idx;
            if (s.charAt(idx) == '[') {
              ++nest;
            } else if (s.charAt(idx) == ']') {
              --nest;
            }
          }
          inputs.add(Input.getInput(s.substring(intStart + 1, idx)));
          ++idx;
        } else {
          while (idx < s.length() && Character.isDigit(s.charAt(idx))) {
            ++idx;
          }
          int i = Integer.parseInt(s.substring(intStart, idx));
          inputs.add(new IntInput(i));
        }
        if (idx < s.length()) { // przecinek
          ++idx;
        }
      }
      return new InputList(inputs);
    }
  }

  private record IntInput(int i) implements Input {
  }

  private record InputList(List<Input> inputs) implements Input {
  }
}
