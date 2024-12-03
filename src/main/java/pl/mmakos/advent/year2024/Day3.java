package pl.mmakos.advent.year2024;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Bool;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static pl.mmakos.advent.utils.Utils.read;
import static pl.mmakos.advent.utils.Utils.stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day3 {
  private static final String MUL_REGEX = "mul\\((\\d{1,3}),(\\d{1,3})\\)";

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.out.println("Advent of code 2024, day 2");
    System.out.println("TASK 1: " + task1());
    System.out.println("TASK 2: " + task2());
  }

  private static int solve(String input, boolean task2) {
    String regex = MUL_REGEX;
    if (task2) regex += "|do\\(\\)|don't\\(\\)";
    Pattern pattern = Pattern.compile(regex);
    Bool enabled = new Bool(true);
    return stream(pattern.matcher(input))
        .filter(m -> {
          if (m.group().startsWith("don")) {
            enabled.set(false);
          } else if (m.group().startsWith("do")) enabled.set(true);
          return enabled.get() && !m.group().startsWith("do");
        })
        .mapToInt(m -> parseInt(m.group(1)) * parseInt(m.group(2)))
        .sum();
  }

  private static long task1() {
    return solve(read(), false);
  }

  private static long task2() {
    return solve(read(), true);
  }
}
