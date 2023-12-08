package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Math.max;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day17 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    char[] input = input();
    Iterator<Character> inputIt = Utils.iterator(input());
    Iterator<Stone> stones = Stone.iterator();
    Chamber chamber = new Chamber(input.length);

    for (int i = 0; i < 2022; ++i) {
      chamber.addStone(stones.next());
      do {
        chamber.moveHorizontally(inputIt.next());
      } while (chamber.moveDownIfPossible() == null);
    }

    System.err.println(chamber);

    return chamber.height;
  }

  @SuppressWarnings("java:S127")
  private static long task2() {
    char[] input = input();
    Iterator<Character> inputIt = Utils.iterator(input());
    Iterator<Stone> stones = Stone.iterator();
    Chamber chamber = new Chamber(input.length);
    chamber.repetitionsEnabled = true;

    long repetitionHeights = 0;
    int[] repetition = new int[0];
    for (long i = 0; i < 1_000_000_000_000L; ++i) {
      if (repetition.length == 2) { // repetition occured
        long height = repetition[0];
        long length = repetition[1];

        long repetitionsCount = (1_000_000_000_000L - i) / length;
        repetitionHeights = height * repetitionsCount;
        i += repetitionsCount * length;

        chamber.repetitionsEnabled = false;
      }

      chamber.addStone(stones.next());
      do {
        chamber.moveHorizontally(inputIt.next());
      } while ((repetition = chamber.moveDownIfPossible()) == null);
    }

    return chamber.height + repetitionHeights;
  }

  private static char[] input() {
    return Utils.read(17, 2022)
            .toCharArray();
  }

  @RequiredArgsConstructor
  private static class Stone {
    private static final Stone hLine = new Stone(new byte[]{0b1111}, 4, 1);
    private static final Stone plus = new Stone(new byte[]{0b010, 0b111, 0b010}, 3, 2);
    private static final Stone lStone = new Stone(new byte[]{0b111, 0b001, 0b001}, 3, 3);
    private static final Stone vLine = new Stone(new byte[]{1, 1, 1, 1}, 1, 4);
    private static final Stone square = new Stone(new byte[]{0b11, 0b11}, 2, 5);

    // bottom to top
    private final byte[] bytes;
    private final int width;
    private final int hashCode;

    private static Iterator<Stone> iterator() {
      Stone[] stones = new Stone[]{hLine, plus, lStone, vLine, square};
      return IntStream.iterate(0, i -> ++i % stones.length)
              .mapToObj(i -> stones[i])
              .iterator();
    }

    private int height() {
      return bytes.length;
    }

    // mask from left to right, from top to bottom
    // for _| it will be 001001111
    private int mask() {
      int result = 0;
      for (int i = 0; i < bytes.length; ++i) {
        result |= bytes[i] << (i * width);
      }
      return result;
    }

    @Override
    @SuppressWarnings("java:S1206")
    public int hashCode() {
      return hashCode;
    }
  }

  @RequiredArgsConstructor
  private static class Chamber {
    private final List<Byte> rows = new ArrayList<>();
    private final int maxMovements;
    private int height;
    private Stone currentStone;
    private int currentStoneX;
    private int currentStoneY;

    // For repetitions
    // Every time the stone is dropped, last couple of rows are remembered as RepetitionKey with some info
    // If this RepetitionKey occured again, then the difference between heights of stored repetition and current
    // can be multiplied without actual dropping stones
    private final Map<RepetitionKey, RepetitionValue> repetitionCandidates = new HashMap<>();
    private boolean repetitionsEnabled;
    private int movementIdx;
    private int stones;

    private void addStone(Stone stone) {
      currentStone = stone;
      currentStoneX = 2;
      currentStoneY = height + 3;
      int newRows = (currentStoneY + stone.height()) - rows.size();
      for (int i = 0; i < newRows; ++i) {
        rows.add((byte) 0);
      }
    }

    private void moveHorizontally(char direction) {
      switch (direction) {
        case '<' -> moveLeft();
        case '>' -> moveRight();
        default -> throw new IllegalStateException();
      }
      ++movementIdx;
      movementIdx %= maxMovements;
    }

    private int[] applyStone() {
      for (int i = 0; i < currentStone.height(); ++i) {
        byte row = rows.get(currentStoneY + i);
        byte stone = (byte) (currentStone.bytes[i] << 7 - currentStone.width - currentStoneX);
        rows.set(currentStoneY + i, (byte) (row | stone));
      }
      height = max(height, currentStoneY + currentStone.height());
      ++stones;

      if (repetitionsEnabled) {
        return proceedRepetition();
      } else return new int[0];
    }

    // If this fragment is repeated, returns array of (stonesDiff, heightDiff)
    // stonesDiff - how many rocks are repeated
    // heightDiff - how many height is gained during repetition
    private int[] proceedRepetition() {
      // We don't count repetitions, if min length is not exceeded
      if (height < RepetitionKey.DEFAULT_LENGTH) return new int[0];

      byte[] lastNRows = bytes((height - RepetitionKey.DEFAULT_LENGTH), height);
      RepetitionKey repetitionKey = new RepetitionKey(lastNRows, currentStone.hashCode, movementIdx);

      RepetitionValue existing = repetitionCandidates.get(repetitionKey);
      if (existing != null) {
        int heightDiff = this.height - existing.height;
        int stonesDiff = this.stones - existing.stones;

        return new int[]{heightDiff, stonesDiff};
      } else {
        RepetitionValue repetitionValue = new RepetitionValue(height, stones);
        repetitionCandidates.put(repetitionKey, repetitionValue);
      }
      return new int[0];
    }

    // if move possible then moves down and returns null
    // if not then applies stone and returns repetition (if possible) or empty array
    @SuppressWarnings("java:S1168")
    private int[] moveDownIfPossible() {
      int mask = mask(currentStoneX, currentStoneY - 1);
      int stoneMask = currentStone.mask();
      if ((mask & stoneMask) > 0) {
        return applyStone();
      } else {
        --currentStoneY;
        return null;
      }
    }

    private void moveLeft() {
      int mask = mask(currentStoneX - 1, currentStoneY);
      int stoneMask = currentStone.mask();
      if ((mask & stoneMask) == 0) {
        --currentStoneX;
      }
    }

    private void moveRight() {
      int mask = mask(currentStoneX + 1, currentStoneY);
      int stoneMask = currentStone.mask();
      if ((mask & stoneMask) == 0) {
        ++currentStoneX;
      }
    }

    // mask from given bottom left corner with dimension as current stone
    private int mask(int x, int y) {
      int result = 0;
      for (int i = 0; i < currentStone.height(); ++i) {
        int row = y >= 0 ? rows.get(i + y) : 0b1111111;
        row <<= 1;
        row |= 0b100000001;
        byte b = (byte) Utils.bitsAt(row, 7 - currentStone.width - x + 1, currentStone.width);
        result |= b << (i * currentStone.width);
      }
      return result;
    }

    private byte[] bytes(int start, int end) {
      List<Byte> sublist = rows.subList(start, end);
      byte[] bytes = new byte[sublist.size()];
      for (int i = 0; i < bytes.length; ++i) {
        bytes[i] = sublist.get(i);
      }
      return bytes;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = rows.size() - 1; i >= 0; --i) {
        String b = String.format("%7s", Integer.toBinaryString(rows.get(i)))
                .replace(" ", "0")
                .replace("0", ".")
                .replace("1", "#");
        sb.append(b).append('\n');
      }
      return sb.toString();
    }
  }

  // Each time the stone is dropped, last <DEFAULT_LENGTH> rows are remembered in hash map
  // Where key is RepetitionKey - identifies the same pattern throughout <DEFAULT_LENGTH> last rows
  private static class RepetitionKey {
    private static final int DEFAULT_LENGTH = 100;

    private final byte[] bytes;
    private final int stone;
    private final int inputIdx;
    private final int hashCode;

    private RepetitionKey(byte[] bytes, int stone, int inputIdx) {
      this.bytes = bytes;
      this.stone = stone;
      this.inputIdx = inputIdx;
      hashCode = calcHash();
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof RepetitionKey key &&
              key.hashCode == hashCode &&
              key.inputIdx == inputIdx &&
              key.stone == stone &&
              Arrays.equals(bytes, key.bytes);
    }

    private int calcHash() {
      int hash = Arrays.hashCode(bytes);
      hash *= 31;
      hash += stone;
      hash *= 31;
      hash += inputIdx;
      return hash;
    }
  }

  private record RepetitionValue(int height, int stones) {
  }
}
