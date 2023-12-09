package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day1 {
  private static final List<String> NUMBERS = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Pattern pattern = Pattern.compile("\\d");

    return Utils.lines()
            .map(s -> getFirstAndLastPattern(s, pattern)
                    .collect(Collectors.joining()))
            .mapToInt(Integer::parseInt)
            .sum();
  }

  private static int task2() {
    Pattern pattern = Pattern.compile(String.join("|", NUMBERS) + "|\\d");

    return Utils.lines()
            .map(s -> getFirstAndLastPattern(s, pattern)
                    .map(Day1::parseInt)
                    .map(Object::toString)
                    .collect(Collectors.joining()))
            .mapToInt(Integer::parseInt)
            .sum();
  }

  private static Stream<String> getFirstAndLastPattern(String s, Pattern pattern) {
    Matcher matcher = pattern.matcher(s);
    matcher.find();
    String first = matcher.group();
    String last = first;
    while (matcher.find(matcher.start() + 1)) {
      last = matcher.group();
    }
    return Stream.of(first, last);
  }

  private static int parseInt(String s) {
    if (s.matches("\\d")) {
      return Integer.parseInt(s);
    } else {
      return NUMBERS.indexOf(s);
    }
  }
}
