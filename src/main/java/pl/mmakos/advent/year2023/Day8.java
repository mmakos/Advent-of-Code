package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day8 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Pair<char[], Map<String, String[]>> input = input();

    Iterator<Character> instr = Utils.iterator(input.first());
    Map<String, String[]> map = input.second();
    String current = "AAA";
    for (int i = 0; i < Integer.MAX_VALUE; ++i) {
      if ("ZZZ".equals(current)) return i;
      current = instr.next() == 'L' ? map.get(current)[0] : map.get(current)[1];
    }

    throw new IllegalStateException();
  }

  private static long task2() {
    Pair<char[], Map<String, String[]>> input = input();

    Iterator<Character> instr = Utils.iterator(input.first());
    Map<String, String[]> map = input.second();
    List<String> currents = input.second().keySet().stream()
            .filter(s -> s.endsWith("A"))
            .collect(Collectors.toCollection(ArrayList::new));

    List<Integer> firsts = new ArrayList<>();

    for (int i = 1; !currents.isEmpty(); ++i) {
      Character next = instr.next();
      List<Integer> toRemove = new ArrayList<>();
      for (int j = 0; j < currents.size(); ++j) {
        currents.set(j, next == 'L' ? map.get(currents.get(j))[0] : map.get(currents.get(j))[1]);
        if (currents.get(j).endsWith("Z")) {
          firsts.add(i);
          toRemove.add(j);
        }
      }
      Utils.removeIndexes(currents, toRemove);
    }

    return firsts.stream()
            .mapToLong(i -> i)
            .reduce(1, Day8::lcm);
  }

  private static long lcm(long l1, long l2) {
    BigInteger b1 = BigInteger.valueOf(l1);
    BigInteger b2 = BigInteger.valueOf(l2);
    return b1.multiply(b2).divide(b1.gcd(b2)).longValue();
  }

  private static Pair<char[], Map<String, String[]>> input() {
    String[] array = Utils.strings(Utils.ENDL_2)
            .toArray(String[]::new);
    char[] instructions = array[0].toCharArray();
    Map<String, String[]> map = Arrays.stream(array[1].split(Utils.ENDL))
            .map(s -> s.split(" = "))
            .collect(Collectors.toMap(s -> s[0], s -> s[1].substring(1, s[1].length() - 1).split(", ")));

    return new Pair<>(instructions, map);
  }
}
