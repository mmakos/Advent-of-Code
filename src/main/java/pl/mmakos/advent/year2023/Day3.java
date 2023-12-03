package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static pl.mmakos.advent.utils.Pair.Stream.pairStream;
import static pl.mmakos.advent.utils.Utils.stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day3 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Pattern pattern = Pattern.compile("\\d+");
    String[] input = input();

    return IntStream.range(0, input.length)
            .flatMap(i -> stream(pattern.matcher(input[i]))
                    .filter(m -> getAdjacentSymbolPosition(m.start(), i, m.group().length(), input, Day3::isSymbol) != null)
                    .map(Matcher::group)
                    .mapToInt(Integer::parseInt))
            .sum();
  }

  private static int task2() {
    Pattern pattern = Pattern.compile("\\d+");
    String[] input = input();

    return IntStream.range(0, input.length)
            .mapToObj(i -> pairStream(stream(pattern.matcher(input[i])), m -> getAdjacentSymbolPosition(m.start(), i, m.group().length(), input, c -> c == '*'), Function.identity())
                    .filterFirst(Objects::nonNull)
                    .mapSecond(Matcher::group)
                    .mapSecond(Integer::parseInt)
            )
            .flatMap(Pair.Stream::unwrap)
            .collect(Collectors.groupingBy(Pair::first))
            .values()
            .stream()
            .filter(list -> list.size() == 2)
            .mapToInt(l -> l.get(0).second() * l.get(1).second())
            .sum();
  }

  private static Point getAdjacentSymbolPosition(int x, int y, int length, String[] lines, IntPredicate isSymbol) {
    for (int i = x - 1; i <= x + length; ++i) {
      if (i < 0 || i >= lines[0].length()) continue;

      // UPPER LINE
      if (y > 0 && isSymbol.test(lines[y - 1].charAt(i))) {
        return new Point(i, y - 1);
      }
      // LOWER LINE
      if (y < lines.length - 1 && isSymbol.test(lines[y + 1].charAt(i))) {
        return new Point(i, y + 1);
      }
    }

    // LEFT
    if (x > 0 && isSymbol.test(lines[y].charAt(x - 1))) {
      return new Point(x - 1, y);
    }
    // RIGHT
    if (x + length < lines[y].length() - 1 && isSymbol.test(lines[y].charAt(x + length))) {
      return new Point(x + length, y);
    }
    return null;
  }

  private static boolean isSymbol(int c) {
    return !Character.isDigit(c) && c != '.';
  }

  private static String[] input() {
    return Utils.lines(3, 2023)
            .toArray(String[]::new);
  }
}
