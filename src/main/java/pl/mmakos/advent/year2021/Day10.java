package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day10 {
  private static final Map<Character, Character> BRACKETS = new HashMap<>() {{
    put('[', ']');
    put('{', '}');
    put('(', ')');
    put('<', '>');
  }};
  private static final Map<Character, Integer> POINTS = new HashMap<>() {{
    put(')', 3);
    put(']', 57);
    put('}', 1197);
    put('>', 25137);
  }};
  private static final List<Character> POINTS_2 = List.of(')', ']', '}', '>');

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return Utils.lines(10, 2021)
            .map(Day10::getFirstNotMatchingBracket)
            .filter(c -> c != (char) -1)
            .mapToInt(POINTS::get)
            .sum();
  }

  private static long task2() {
    long[] points = Utils.lines(10, 2021)
            .map(Day10::complete)
            .filter(Objects::nonNull)
            .mapToLong(Day10::points2)
            .sorted()
            .toArray();
    return points[points.length / 2];
  }

  private static char getFirstNotMatchingBracket(String s) {
    Deque<Character> stack = new ArrayDeque<>();

    for (char c : s.toCharArray()) {
      if (BRACKETS.containsKey(c)) {
        stack.addLast(c);
      } else if (stack.isEmpty()) {
        break;
      } else if (BRACKETS.get(stack.removeLast()) != c) {
        return c;
      }
    }
    return (char) -1;
  }

  private static String complete(String s) {
    Deque<Character> stack = new ArrayDeque<>();

    for (char c : s.toCharArray()) {
      if (BRACKETS.containsKey(c)) {
        stack.addLast(c);
      } else if (!stack.isEmpty() && BRACKETS.get(stack.removeLast()) != c) {
        return null;
      }
    }

    StringBuilder sb = new StringBuilder();
    while (!stack.isEmpty()) {
      sb.append(BRACKETS.get(stack.removeLast()));
    }
    return sb.toString();
  }

  private static long points2(String s) {
    long sum = 0;
    for (int i = 0; i < s.length(); ++i) {
      sum *= 5;
      sum += POINTS_2.indexOf(s.charAt(i)) + 1;
    }
    return sum;
  }
}
