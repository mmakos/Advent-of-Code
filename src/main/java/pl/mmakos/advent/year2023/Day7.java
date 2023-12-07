package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day7 {
  private static final List<Character> cards1 = Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
  private static final List<Character> cards2 = Arrays.asList('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return solve(false);
  }

  private static int task2() {
    return solve(true);
  }

  private static int solve(boolean task2) {
    Card[] cards = Utils.lines(7, 2023)
            .map(l -> new Card(l, task2))
            .sorted(strengthComparator().thenComparing(valuesComparator(task2 ? cards2 : cards1)))
            .toArray(Card[]::new);

    return IntStream.range(0, cards.length)
            .map(i -> cards[i].bid * (i + 1))
            .sum();
  }

  public static Comparator<Card> strengthComparator() {
    return (o1, o2) -> {
      if (isFive(o1.counts)) return isFive(o2.counts) ? 0 : 1;
      if (isFive(o2.counts)) return -1;

      if (isFour(o1.counts)) return isFour(o2.counts) ? 0 : 1;
      if (isFour(o2.counts)) return -1;

      if (isFull(o1.counts)) return isFull(o2.counts) ? 0 : 1;
      if (isFull(o2.counts)) return -1;

      if (isThree(o1.counts)) return isThree(o2.counts) ? 0 : 1;
      if (isThree(o2.counts)) return -1;

      if (isDoublePair(o1.counts)) return isDoublePair(o2.counts) ? 0 : 1;
      if (isDoublePair(o2.counts)) return -1;

      if (isPair(o1.counts)) return isPair(o2.counts) ? 0 : 1;
      if (isPair(o2.counts)) return -1;

      return 0;
    };
  }

  public static Comparator<Card> valuesComparator(List<Character> cards) {
    return (o1, o2) -> {
      for (int i = 0; i < o1.cards.length; ++i) {
        if (o1.cards[i] != o2.cards[i]) {
          return -Integer.compare(cards.indexOf(o1.cards[i]), cards.indexOf(o2.cards[i]));
        }
      }
      return 0;
    };
  }

  private static boolean isFive(int[] c) {
    return c.length == 1;
  }

  private static boolean isFour(int[] c) {
    return c.length == 2 && c[1] == 4;
  }

  private static boolean isFull(int[] c) {
    return c.length == 2 && c[0] == 2 && c[1] == 3;
  }

  private static boolean isThree(int[] c) {
    return c.length == 3 && c[2] == 3;
  }

  private static boolean isDoublePair(int[] c) {
    return c.length == 3 && c[1] == 2 && c[2] == 2;
  }

  private static boolean isPair(int[] c) {
    return c.length == 4;
  }

  public static class Card {
    private final char[] cards;
    private final int bid;
    private final int[] counts;

    private Card(String line, boolean task2) {
      String[] split = line.split(" ");

      bid = Integer.parseInt(split[1]);
      cards = split[0].toCharArray();

      if (split[0].equals("JJJJJ")) {
        counts = new int[] {5};
      } else {
        Map<Character, Long> countsMap = split[0].chars()
                .mapToObj(i -> (char) i)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        int j = 0;
        if (task2) {
          Long jokers = countsMap.remove('J');
          if (jokers != null) j = jokers.intValue();
        }

        counts = countsMap.values()
                .stream()
                .mapToInt(Long::intValue)
                .sorted()
                .toArray();

        counts[counts.length - 1] += j;
      }
    }
  }
}
