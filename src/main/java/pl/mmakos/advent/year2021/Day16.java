package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day16 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    long[] result = parsePackets(input());
    System.err.println("TASK 1: " + result[0]);
    System.err.println("TASK 2: " + result[2]);
  }

  private static long[] parsePackets(boolean[] bits) {
    long version = toInt(bits, 0, 3);
    int type = toInt(bits, 3, 3);
    if (type == 4) {
      int end = 6;
      long value = 0;
      do {
        value <<= 4;
        value |= toInt(bits, 1 + end, 4);
        end += 5;
      } while (bits[end - 5]);
      return new long[]{version, end, value};
    } else {
      long[][] subPackets;
      long end;
      if (bits[6]) {
        int packets = toInt(bits, 7, 11);
        end = 18;
        subPackets = subPackets(Arrays.copyOfRange(bits, 18, bits.length), packets);
      } else {
        int length = toInt(bits, 7, 15);
        end = 22;
        subPackets = subPackets(Arrays.copyOfRange(bits, 22, 22 + length), -1);
      }
      end += subPackets[subPackets.length - 1][0];
      long value = calculate(subPackets, type);
      for (int i = 0; i < subPackets.length - 1; ++i) {
        version += subPackets[i][0];
      }
      return new long[] {version, end, value};
    }
  }

  // Returns array:
  // Packets (array: version, end, value)
  // On the end one-element array with 'end'
  private static long[][] subPackets(boolean[] bits, int packets) {
    long end = 0;
    List<long[]> subPackets = new ArrayList<>();
    while (packets > 0 || (packets < 0 && end < bits.length)) {
      long[] packet = parsePackets(Arrays.copyOfRange(bits, (int) end, bits.length));
      subPackets.add(packet);
      end += packet[1];
      --packets;
    }
    subPackets.add(new long[]{end});
    return subPackets.toArray(long[][]::new);
  }

  private static long calculate(long[][] packets, int type) {
    long[] array = Arrays.stream(packets)
            .limit(packets.length - 1L)
            .mapToLong(i -> i[2])
            .toArray();
    return getCalculator(type).apply(array);
  }

  private static Function<long[], Long> getCalculator(int type) {
    return switch (type) {
      case 0 -> i -> Arrays.stream(i).sum();
      case 1 -> i -> Arrays.stream(i).reduce(1, (i1, i2) -> i1 * i2);
      case 2 -> i -> Arrays.stream(i).min().orElseThrow();
      case 3 -> i -> Arrays.stream(i).max().orElseThrow();
      case 5 -> i -> (long) (i[0] > i[1] ? 1 : 0);
      case 6 -> i -> (long) (i[0] < i[1] ? 1 : 0);
      case 7 -> i -> (long) (i[0] == i[1] ? 1 : 0);
      default -> throw new IllegalStateException();
    };
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
