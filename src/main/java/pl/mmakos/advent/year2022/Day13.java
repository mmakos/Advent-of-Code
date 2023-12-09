package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day13 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    int[] ints = Utils.strings(Utils.ENDL_2, Utils.ENDL)
            .map(s -> s.toArray(String[]::new))
            .filter(s -> s.length == 2)
            .mapToInt(Day13::compare)
            .toArray();

    return IntStream.rangeClosed(1, ints.length)
            .filter(i -> ints[i - 1] <= 0)
            .sum();
  }

  private static int task2() {
    Input[] addInputs = new Input[]{Input.getInput("[[2]]"), Input.getInput("[[6]]")};
    Stream<Input> parsed = Utils.lines()
            .filter(not(String::isBlank))
            .map(Input::getInput);
    List<Input> list = Stream.concat(parsed, Arrays.stream(addInputs))
            .sorted(Input::compare)
            .toList();
    return (list.indexOf(addInputs[0]) + 1) * (list.indexOf(addInputs[1]) + 1);
  }

  private static int compare(String[] s) {
    Input left = Input.getInput(s[0]);
    Input right = Input.getInput(s[1]);

    return left.compare(right);
  }

  private interface Input {
    int compare(Input input);

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
    @Override
    public int compare(Input input) {
      return input instanceof InputList inputList ? compare(inputList) : compare((IntInput) input);
    }

    private int compare(IntInput input) {
      return Integer.compare(this.i, input.i);
    }

    private int compare(InputList input) {
      return -input.compare(this);
    }
  }

  private record InputList(List<Input> inputs) implements Input {
    @Override
    public int compare(Input input) {
      return input instanceof InputList inputList ? compare(inputList) : compare((IntInput) input);
    }

    private int compare(InputList input) {
      for (int i = 0; i < inputs.size() && i < input.inputs.size(); ++i) {
        int compared = inputs.get(i).compare(input.inputs.get(i));
        if (compared != 0) return compared;
      }
      return Integer.compare(inputs.size(), input.inputs.size());
    }

    private int compare(IntInput input) {
      return compare(new InputList(Collections.singletonList(input)));
    }
  }
}
