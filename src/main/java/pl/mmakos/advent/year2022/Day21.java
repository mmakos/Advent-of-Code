package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day21 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    Map<String, Monkey> monkeys = Utils.lines(21, 2022)
            .map(s -> s.split(": "))
            .collect(Collectors.toMap(s -> s[0], s -> Monkey.parse(s[1])));

    return monkeys.get("root").apply(monkeys);
  }

  private static long task2() {
    Map<String, Monkey> monkeys = Utils.lines(21, 2022)
            .map(s -> s.split(": "))
            .collect(Collectors.toMap(s -> s[0], s -> Monkey.parse(s[1])));

    RootMonkey rootMonkey = (RootMonkey) monkeys.computeIfPresent("root", (k, v) -> new RootMonkey((OperationMonkey) v));
    assert rootMonkey != null;

    Map<String, Monkey> replacements = new HashMap<>();
    String monkey = "humn";
    while (!monkey.equals("root")) {
      String finalMonkey = monkey;
      Pair<String, OperationMonkey> monkeyParent = monkeys.entrySet().stream()
              .filter(e -> e.getValue() instanceof OperationMonkey)
              .filter(e -> ((OperationMonkey) e.getValue()).monkey1.equals(finalMonkey) ||
                      ((OperationMonkey) e.getValue()).monkey2.equals(finalMonkey))
              .map(e -> new Pair<>(e.getKey(), (OperationMonkey) e.getValue()))
              .findFirst()
              .orElseThrow();
      replacements.put(monkey, monkeyParent.second().transform(monkey, monkeyParent.first()));
      monkey = monkeyParent.first();
    }
    monkeys.putAll(replacements);
    monkeys.put("root", new NumberMonkey(0));

    return monkeys.get("humn").apply(monkeys);
  }

  private interface Monkey {
    Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    Pattern OPERATION_PATTERN = Pattern.compile("([a-z]{4}) ([+\\-*/]) ([a-z]{4})");
    Operator add = new Operator(Long::sum, (i1, i2) -> i1 - i2, (i1, i2) -> i1 - i2);
    Operator sub = new Operator((i1, i2) -> i1 - i2, Long::sum, (i1, i2) -> i2 - i1);
    Operator mul = new Operator((i1, i2) -> i1 * i2, (i1, i2) -> i1 / i2, (i1, i2) -> i1 / i2);
    Operator div = new Operator((i1, i2) -> i1 / i2, (i1, i2) -> i1 * i2, (i1, i2) -> i2 / i1);

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static Monkey parse(String s) {
      if (NUMBER_PATTERN.matcher(s).matches()) {
        return new NumberMonkey(Long.parseLong(s));
      } else {
        Matcher matcher = OPERATION_PATTERN.matcher(s);
        matcher.matches();
        Operator operator = switch (matcher.group(2)) {
          case "+" -> add;
          case "-" -> sub;
          case "*" -> mul;
          case "/" -> div;
          default -> throw new IllegalStateException();
        };
        return new OperationMonkey(matcher.group(1), matcher.group(3), operator);
      }
    }

    long apply(Map<String, Monkey> monkeys);
  }

  @RequiredArgsConstructor
  private static class OperationMonkey implements Monkey {
    private final String monkey1;
    private final String monkey2;
    private final Operator operator;

    @Override
    public long apply(Map<String, Monkey> monkeys) {
      long int1 = monkeys.get(monkey1).apply(monkeys);
      long int2 = monkeys.get(monkey2).apply(monkeys);
      return operator.applyAsLong(int1, int2);
    }

    public OperationMonkey transform(String toLeftSide, String toRightSide) {
      if (toLeftSide.equals(monkey1)) {
        return new OperationMonkey(toRightSide, monkey2, operator.leftTransform);
      } else if (toLeftSide.equals(monkey2)) {
        return new OperationMonkey(toRightSide, monkey1, operator.rightTransform);
      }
      throw new IllegalStateException();
    }
  }

  private record NumberMonkey(long number) implements Monkey {
    @Override
    public long apply(Map<String, Monkey> monkeys) {
      return number;
    }
  }

  private static class RootMonkey extends OperationMonkey {
    private RootMonkey(OperationMonkey monkey) {
      super(monkey.monkey1, monkey.monkey2, new Operator(
              Long::sum,
              (i1, i2) -> i2,
              (i1, i2) -> i1
      ));
    }
  }

  @SuppressWarnings("java:S1700")
  private static class Operator implements LongBinaryOperator {
    private final LongBinaryOperator operator;
    private Operator leftTransform;
    private Operator rightTransform;

    private Operator(LongBinaryOperator operator, LongBinaryOperator leftTransform, LongBinaryOperator rightTransform) {
      this.operator = operator;
      this.leftTransform = new Operator(leftTransform);
      this.rightTransform = new Operator(rightTransform);
    }

    private Operator(LongBinaryOperator operator) {
      this.operator = operator;
    }

    @Override
    public long applyAsLong(long left, long right) {
      return operator.applyAsLong(left, right);
    }
  }
}
