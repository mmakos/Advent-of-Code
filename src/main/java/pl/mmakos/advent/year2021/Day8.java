package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day8 {
  //   0
  // 1   2
  //   3
  // 4   5
  //   6
  private static final List<List<Integer>> NUMBERS = List.of(
          UnorderedList.list(0, 1, 2, 4, 5, 6),
          UnorderedList.list(2, 5),
          UnorderedList.list(0, 2, 3, 4, 6),
          UnorderedList.list(0, 2, 3, 5, 6),
          UnorderedList.list(1, 2, 3, 5),
          UnorderedList.list(0, 1, 3, 5, 6),
          UnorderedList.list(0, 1, 3, 4, 5, 6),
          UnorderedList.list(0, 2, 5),
          UnorderedList.list(0, 1, 2, 3, 4, 5, 6),
          UnorderedList.list(0, 1, 2, 3, 5, 6)
  );

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    List<Integer> seek = List.of(2, 4, 3, 7);
    return input()
            .map(s -> stream(s[1])
                    .map(String::length)
                    .toList())
            .mapToLong(s -> howManyContains(s, seek))
            .sum();
  }

  private static int task2() {
    return input()
            .mapToInt(s -> toNumber(s[0], s[1]))
            .sum();
  }

  private static int toNumber(String[] wires, String[] encoded) {
    Map<Character, Integer> mapping = getMapping(wires);

    String strNumber = stream(encoded)
            .map(s -> toNumber(mapping, s))
            .map(String::valueOf)
            .collect(Collectors.joining());
    return Integer.parseInt(strNumber);
  }

  private static int toNumber(Map<Character, Integer> mapping, String s) {
    List<Integer> ints = s.chars()
            .mapToObj(c -> mapping.get((char) c))
            .collect(Collectors.toCollection(UnorderedList::new));
    return NUMBERS.indexOf(ints);
  }

  private static Map<Character, Integer> getMapping(String[] wires) {
    Map<Character, Integer> mapping = new HashMap<>();

    char[] chars25 = stream(wires)
            .filter(s -> s.length() == 2)
            .findFirst()
            .map(String::toCharArray)
            .orElseThrow();
    char[] chars025 = stream(wires)
            .filter(s -> s.length() == 3)
            .findFirst()
            .map(String::toCharArray)
            .orElseThrow();
    char char0 = minus(chars025, chars25)[0];
    mapping.put(char0, 0);

    char[] chars1235 = stream(wires).filter(s -> s.length() == 4).findFirst().map(String::toCharArray).orElseThrow();
    char[] chars01235 = sum(chars1235, char0);
    System.arraycopy(chars1235, 0, chars01235, 0, 4);
    chars01235[4] = char0;
    char[] chars012356 = stream(wires)
            .filter(s -> s.length() == 6)
            .map(String::toCharArray)
            .filter(s -> containsAll(s, chars01235))
            .findFirst()
            .orElseThrow();
    char char6 = minus(chars012356, chars01235)[0];
    mapping.put(char6, 6);

    char[] chars012456 = stream(wires)
            .filter(s -> s.length() == 6)
            .map(String::toCharArray)
            .filter(c -> !containsAll(c, chars012356))  // Not 9
            .filter(c -> containsAll(c, chars25)) // Not 6
            .findFirst()
            .orElseThrow();

    char[] chars046 = minus(chars012456, chars1235);
    char char4 = minus(chars046, char0, char6)[0];
    mapping.put(char4, 4);

    char[] chars02456 = sum(chars046, chars025);
    char char1 = minus(chars012456, chars02456)[0];
    mapping.put(char1, 1);

    char char3 = minus(chars1235, chars012456)[0];
    mapping.put(char3, 3);

    char[] chars013456 = stream(wires)
            .filter(s -> s.length() == 6)
            .map(String::toCharArray)
            .filter(c -> !containsAll(c, chars012356)) // Not 9
            .filter(c -> !containsAll(c, chars012456)) // Not 0
            .findFirst()
            .orElseThrow();
    char[] chars0123456 = stream(wires)
            .filter(s -> s.length() == 7)
            .map(String::toCharArray)
            .findFirst()
            .orElseThrow();

    char char2 = minus(chars0123456, chars013456)[0];
    mapping.put(char2, 2);
    mapping.put(minus(chars1235, char1, char2, char3)[0], 5);

    return mapping;
  }

  private static <T> long howManyContains(Collection<T> collection, Collection<T> col) {
    return col.stream()
            .mapToLong(c -> collection.stream().filter(el -> el.equals(c)).count())
            .sum();
  }

  private static Stream<String[][]> input() {
    return Utils.lines(8, 2021)
            .map(line -> stream(line.split(" \\| "))
                    .map(s -> s.split(" "))
                    .toArray(String[][]::new));
  }

  private static char[] minus(char[] bigger, char... smaller) {
    List<Character> characters = new ArrayList<>();
    for (char c : bigger) {
      if (!Utils.contains(c, smaller)) characters.add(c);
    }
    return toPrimitive(characters);
  }

  private static char[] sum(char[] c1, char... c2) {
    Set<Character> chars = new HashSet<>();
    for (char c : c1) {
      chars.add(c);
    }
    for (char c : c2) {
      chars.add(c);
    }
    return toPrimitive(new ArrayList<>(chars));
  }

  private static boolean containsAll(char[] bigger, char[] smaller) {
    for (char c : smaller) {
      if (!Utils.contains(c, bigger)) return false;
    }
    return true;
  }

  private static char[] toPrimitive(List<Character> characters) {
    char[] chars = new char[characters.size()];
    for (int i = 0; i < chars.length; ++i) {
      chars[i] = characters.get(i);
    }
    return chars;
  }

  private static class UnorderedList extends ArrayList<Integer> {
    private static UnorderedList list(int... ints) {
      UnorderedList list = new UnorderedList();
      for (int anInt : ints) {
        list.add(anInt);
      }
      return list;
    }

    @SuppressWarnings({"SuspiciousMethodCalls", "java:S1206"})
    @Override
    public boolean equals(Object o) {
      if (o instanceof Collection<?> collection) {
        return collection.containsAll(this) && this.containsAll(collection);
      }
      return super.equals(o);
    }
  }
}
