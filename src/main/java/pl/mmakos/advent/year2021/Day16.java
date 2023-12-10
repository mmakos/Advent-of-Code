package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day16 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return parsePackets(input());
  }

  private static int task2() {
    return 0;
  }

  private static int parsePackets(boolean[] bits) {
    int version = toInt(bits, 0, 3);
    int type = toInt(bits, 3, 3);
    if (type == 4) {
      int end = 0;
      while (bits[6 + end]) {
        end += 5;
      }
      end += 5;
      end += (4 - end % 4) % 4;
      return version + bits.length > end ? parsePackets(Arrays.copyOfRange(bits, end, bits.length)) : 0;
    } else {
      if (bits[6]) {
        throw new UnsupportedOperationException();
      } else {
        int length = toInt(bits, 7, 15);
        return version + parsePackets(Arrays.copyOfRange(bits, 22, 22 + length));
//            + bits.length > 22 + length ? parsePackets(Arrays.copyOfRange(bits, 22 + length, bits.length)) : 0;
      }
    }
  }

  private static boolean[] input() {
    String string = Utils.read();
    List<Boolean> list = IntStream.range(0, string.length())
        .mapToObj(i -> string.substring(i, i + 1))
        .map(c -> Integer.parseInt(c, 16))
        .flatMap(i -> Stream.of((i & 0b1000) > 0, (i & 0b100) > 0, (i & 0b10) > 0, (i & 0b1) > 0))
        .toList();
    return Utils.toPrimitiveBools(list);
  }

  private static int toInt(boolean[] bits, int start, int length) {
    int result = 0;
    for (int i = start; i < start + length; ++i) {
      result <<= 1;
      if (bits[i]) result |= 1;
    }
    return result;
  }
}
