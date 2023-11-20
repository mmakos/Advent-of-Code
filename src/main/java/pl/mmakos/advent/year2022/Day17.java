package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.max;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day17 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Iterator<Character> input = input();
    Iterator<Stone> stones = Stone.iterator();
    Chamber chamber = new Chamber();

    for (int i = 0; i < 2022; ++i) {
      chamber.addStone(stones.next());
      while (true) {
        chamber.moveHorizontally(input.next());
        if (chamber.canMoveDown()) {
          chamber.moveDown();
        } else {
          break;
        }
      }
    }

    System.err.println(chamber);

    return chamber.height;
  }

  private static int task2() {
    return 0;
  }

  private static Iterator<Character> input() {
    char[] chars = Utils.read(17, 2022)
            .toCharArray();
    return IntStream.iterate(0, i -> ++i % chars.length)
            .mapToObj(i -> chars[i])
            .iterator();
  }

  @RequiredArgsConstructor
  private static class Stone {
    private static final Stone hLine = new Stone(new byte[]{0b1111}, 4);
    private static final Stone plus = new Stone(new byte[]{0b010, 0b111, 0b010}, 3);
    private static final Stone lStone = new Stone(new byte[]{0b111, 0b001, 0b001}, 3);
    private static final Stone vLine = new Stone(new byte[]{1, 1, 1, 1}, 1);
    private static final Stone square = new Stone(new byte[]{0b11, 0b11}, 2);

    // bottom to top
    private final byte[] bytes;
    private final int width;

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
  }

  private static class Chamber {
    private final List<Byte> rows = new ArrayList<>();
    private int height;
    private Stone currentStone;
    private int currentStoneX;
    private int currentStoneY;

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
    }

    private void applyStone() {
      for (int i = 0; i < currentStone.height(); ++i) {
        byte row = rows.get(currentStoneY + i);
        byte stone = (byte) (currentStone.bytes[i] << 7 - currentStone.width - currentStoneX);
        rows.set(currentStoneY + i, (byte) (row | stone));
      }
      height = max(height, currentStoneY + currentStone.height());
    }

    private boolean canMoveDown() {
      int mask = mask(currentStoneX, currentStoneY - 1);
      int stoneMask = currentStone.mask();
      if ((mask & stoneMask) > 0) {
        applyStone();
        return false;
      } else {
        return true;
      }
    }

    private void moveDown() {
      --currentStoneY;
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
}
