package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day12 {
  private static final Map<SI, Long> cache = new HashMap<>();

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    return input1()
            .mapToLong(Day12::getPossibilities)
            .sum();
  }

  private static long task2() {
    return input2()
            .mapToLong(Day12::getPossibilities)
            .sum();
  }

  private static long getPossibilities(SI si) {
    Long cached = cache.get(si);
    if (cached != null) return cached;

    long possibilities = 0;
    String s = si.s;
    int[] groups = si.i;

    for (int i = 0; i < s.length() && (i == 0 || s.charAt(i - 1) != '#'); ++i) {
      if (canPlace(s, groups[0], i)) {
        if (groups.length == 1) {
          if (noneLeft(s, i + groups[0] + 1)) possibilities += 1;
        } else if (s.length() > i + groups[0] + 1) {
          possibilities += getPossibilities(new SI(s.substring(i + groups[0] + 1), Arrays.copyOfRange(groups, 1, groups.length)));
        }
      }
    }

    cache.put(si, possibilities);

    return possibilities;
  }

  private static boolean noneLeft(String s, int idx) {
    for (int i = idx; i < s.length(); ++i) {
      if (s.charAt(i) == '#') return false;
    }
    return true;
  }

  private static boolean canPlace(String s, int groupLen, int idx) {
    if (s.length() < groupLen + idx) return false;   // Not enough space
    if (idx > 0 && s.charAt(idx - 1) == '#') return false;   // Cannot place in this index, because left place is taken
    if (idx + groupLen < s.length() && s.charAt(groupLen + idx) == '#')
      return false;   // Cannot place in this index, because right lace is taken

    for (int i = idx; i < idx + groupLen; ++i) {
      if (s.charAt(i) == '.') return false;
    }
    return true;
  }

  private static Stream<SI> input1() {
    return Pair.Stream.pairStream(Utils.lines().map(s -> s.split(" ")), s -> s[0], s -> s[1])
            .mapSecond(s -> Utils.toInts(s, ","))
            .unwrap()
            .map(p -> new SI(p.first(), p.second()));
  }

  private static Stream<SI> input2() {
    return Pair.Stream.pairStream(Utils.lines().map(s -> s.split(" ")), s -> s[0], s -> s[1])
            .mapFirst(s -> repeat5(s, "?"))
            .mapSecond(s -> repeat5(s, ","))
            .mapSecond(s -> Utils.toInts(s, ","))
            .unwrap()
            .map(p -> new SI(p.first(), p.second()));
  }

  private static String repeat5(String s, String join) {
    StringBuilder sb = new StringBuilder();
    sb.append(s);
    for (int i = 0; i < 4; ++i) {
      sb.append(join).append(s);
    }
    return sb.toString();
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class SI {
    private final String s;
    private final int[] i;
  }
}
