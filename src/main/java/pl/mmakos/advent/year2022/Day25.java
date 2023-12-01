package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day25 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static String task1() {
    return new SnafuNumber(Utils.lines(25, 2022)
            .map(SnafuNumber::new)
            .mapToLong(SnafuNumber::getNumber)
            .sum())
            .toSnafuFormat();
  }

  private static long task2() {
    return new SnafuNumber("1-0---0").getNumber();
  }

  @Getter
  @RequiredArgsConstructor
  private static class SnafuNumber {
    private final long number;

    private SnafuNumber(String s) {
      long x = 0;
      for (int i = s.length() - 1; i >= 0; --i) {
        int pow = s.length() - i - 1;
        char c = s.charAt(i);
        int value = parseVal(c);
        x += Utils.pow(5, pow) * value;
      }
      this.number = x;
    }

    private String toSnafuFormat() {
      StringBuilder builder = new StringBuilder();
      long x = number;
      while (x > 0) {
        int mod = (int) (x % 5);
        builder.insert(0, getChar(mod));
        x /= 5;
        if (mod > 2) {
          ++x;
        }
      }
      return builder.toString();
    }

    private static char getChar(int val) {
      return switch (val) {
        case 0 -> '0';
        case 1 -> '1';
        case 2 -> '2';
        case 3 -> '=';
        case 4 -> '-';
        default -> throw new IllegalStateException();
      };
    }

    private static int parseVal(char c) {
      return switch (c) {
        case '-' -> -1;
        case '=' -> -2;
        case '0' -> 0;
        case '1' -> 1;
        case '2' -> 2;
        default -> throw new IllegalStateException();
      };
    }
  }
}
