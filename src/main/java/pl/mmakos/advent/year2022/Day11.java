package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.LongUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day11 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    Monkey[] monkeys = getMonkeys();
    return getMonkeyBusiness(monkeys, 20, i -> i / 3);
  }

  private static long task2() {
    Monkey[] monkeys = getMonkeys();
    int divisor = Arrays.stream(monkeys)
            .mapToInt(Monkey::divisibleTest)
            .reduce(1, (acc, i) -> acc * i);
    return getMonkeyBusiness(monkeys, 10_000, i -> i % divisor);
  }

  private static long getMonkeyBusiness(Monkey[] monkeys, int rounds, LongUnaryOperator decreaseWorry) {
    for (int i = 0; i < rounds; ++i) {
      for (Monkey monkey : monkeys) {
        long item;
        while ((item = monkey.poll()) >= 0) {
          item = decreaseWorry.applyAsLong(monkey.operation.applyAsLong(item));
          monkeys[monkey.test(item) ? monkey.trueMonkey : monkey.falseMonkey].add(item);
        }
      }
    }
    return Arrays.stream(monkeys)
            .map(Monkey::getCounter)
            .sorted(Comparator.reverseOrder())
            .mapToLong(i -> i)
            .limit(2)
            .reduce(1, (acc, i) -> acc * i);
  }

  private static Monkey[] getMonkeys() {
    return Utils.strings(Utils.ENDL_2, Utils.ENDL)
            .map(s -> s.toArray(String[]::new))
            .filter(s -> s.length > 0)
            .map(Monkey::parse)
            .toArray(Monkey[]::new);
  }

  @SuppressWarnings("java:S6218")
  private record Monkey(List<Long> items, LongUnaryOperator operation, int divisibleTest,
                        int trueMonkey, int falseMonkey, int[] counter) {
    private static final Pattern ITEMS_PATTERN = Pattern.compile(" {2}Starting items: (\\d+(, \\d+)*)");
    private static final Pattern OPERATION_PATTERN = Pattern.compile(" {2}Operation: new = old ([*+]) (\\d+|old)");
    private static final Pattern TEST_PATTERN = Pattern.compile(" {2}Test: divisible by (\\d+)");
    private static final Pattern TRUE_PATTERN = Pattern.compile(" {4}If true: throw to monkey (\\d+)");
    private static final Pattern FALSE_PATTERN = Pattern.compile(" {4}If false: throw to monkey (\\d+)");

    @SuppressWarnings({"ResultOfMethodCallIgnored", "java:S3358"})
    private static Monkey parse(String[] s) {
      Matcher matcher = ITEMS_PATTERN.matcher(s[1]);
      matcher.matches();
      @SuppressWarnings("java:S6204")
      List<Long> items = Arrays.stream(matcher.group(1).split(", "))
              .map(Long::parseLong)
              .collect(Collectors.toList());
      matcher = OPERATION_PATTERN.matcher(s[2]);
      matcher.matches();
      int value = matcher.group(2).matches("\\d+") ? Integer.parseInt(matcher.group(2)) : -1;
      LongUnaryOperator operation = "*".equals(matcher.group(1)) ?
              i -> (value != -1 ? value : i) * i :
              i -> (value != -1 ? value : i) + i;
      matcher = TEST_PATTERN.matcher(s[3]);
      matcher.matches();
      int divisibleTest = Integer.parseInt(matcher.group(1));
      matcher = TRUE_PATTERN.matcher(s[4]);
      matcher.matches();
      int trueMonkey = Integer.parseInt(matcher.group(1));
      matcher = FALSE_PATTERN.matcher(s[5]);
      matcher.matches();
      int falseMonkey = Integer.parseInt(matcher.group(1));

      return new Monkey(items, operation, divisibleTest, trueMonkey, falseMonkey, new int[1]);
    }

    private boolean test(long value) {
      return value % divisibleTest == 0;
    }

    private void add(long item) {
      items.add(item);
    }

    private long poll() {
      if (items.isEmpty()) return -1;
      ++counter[0];
      return items.remove(items.size() - 1);
    }

    private int getCounter() {
      return counter[0];
    }
  }
}
