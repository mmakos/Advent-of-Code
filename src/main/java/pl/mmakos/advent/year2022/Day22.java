package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Arrays;
import java.util.stream.IntStream;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day22 {
  private static boolean cubeWrap;

  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    cubeWrap = false;
    String[] input = Utils.strings(Utils.ENDL_2)
            .toArray(String[]::new);
    char[][] board = board(input[0]);
    Movement[] moves = moves(input[1]);
    Position position = new Position(getStartX(board));

    for (Movement move : moves) {
      move.move(position, board);
    }

    return (position.y + 1) * 1000 + (position.x + 1) * 4 + position.direction;
  }

  private static int task2() {
    cubeWrap = true;
    String[] input = Utils.strings(Utils.ENDL_2)
            .toArray(String[]::new);
    char[][] board = board(input[0]);
    Movement[] moves = moves(input[1]);
    Position position = new Position(getStartX(board));

    for (Movement move : moves) {
      move.move(position, board);
    }

    return (position.y + 1) * 1000 + (position.x + 1) * 4 + position.direction;
  }

  private static int getStartX(char[][] board) {
    for (int i = 0; i < board[0].length; ++i) {
      if (board[0][i] == '.') {
        return i;
      }
    }
    throw new IllegalStateException();
  }

  private static char[][] board(String str) {
    String[] split = str.split(Utils.ENDL);
    int width = Arrays.stream(split)
            .mapToInt(String::length)
            .max()
            .orElseThrow();

    char[][] board = new char[split.length][width];
    for (char[] array : board) {
      Arrays.fill(array, ' ');
    }
    for (int i = 0; i < split.length; ++i) {
      char[] chars = split[i].toCharArray();
      System.arraycopy(chars, 0, board[i], 0, chars.length);
    }

    return board;
  }

  private static Movement[] moves(String str) {
    String[] split = str.split("((?=[RL])|(?<=[RL]))");
    return IntStream.range(0, split.length)
            .mapToObj(i -> {
              if (i % 2 == 0) return new Translate(split[i]);
              else return new Rotate(split[i]);
            })
            .toArray(Movement[]::new);
  }

  private interface Movement {
    void move(Position position, char[][] board);
  }

  private static class Translate implements Movement {
    private final int amount;

    private Translate(String str) {
      amount = Integer.parseInt(str);
    }

    @Override
    public void move(Position position, char[][] board) {
      for (int i = 0; i < amount; ++i) {
        if (position.direction == 0) {
          moveRight(board, position);
        } else if (position.direction == 2) {
          moveLeft(board, position);
        } else if (position.direction == 1) {
          moveDown(board, position);
        } else if (position.direction == 3) {
          moveUp(board, position);
        }
      }
    }

    private void moveRight(char[][] board, Position pos) {
      char[] row = board[pos.y];
      if (pos.x + 1 < row.length) {
        if (row[pos.x + 1] == '.') {
          ++pos.x;
          return;
        }
        if (row[pos.x + 1] == '#') return;
      }
      wrapRight(board, pos);
    }

    private void moveLeft(char[][] board, Position pos) {
      char[] row = board[pos.y];
      if (pos.x > 0) {
        if (row[pos.x - 1] == '.') {
          --pos.x;
          return;
        }
        if (row[pos.x - 1] == '#') return;
      }
      wrapLeft(board, pos);
    }

    private void moveDown(char[][] board, Position pos) {
      if (pos.y + 1 < board.length) {
        if (board[pos.y + 1][pos.x] == '.') {
          ++pos.y;
          return;
        }
        if (board[pos.y + 1][pos.x] == '#') return;
      }
      wrapDown(board, pos);
    }

    private void moveUp(char[][] board, Position pos) {
      if (pos.y > 0) {
        if (board[pos.y - 1][pos.x] == '.') {
          --pos.y;
          return;
        }
        if (board[pos.y - 1][pos.x] == '#') return;
      }
      wrapUp(board, pos);
    }

    private void wrapRight(char[][] board, Position pos) {
      if (cubeWrap) {
        Position wrap = wrappedPosition(pos);
        if (board[wrap.y][wrap.x] == '.') {
          pos.setFrom(wrap);
        }
        return;
      }
      char[] row = board[pos.y];
      for (int i = 0; i < row.length; ++i) {
        if (row[i] != ' ') {
          if (row[i] == '.') {
            pos.x = i;
          }
          return;
        }
      }
      throw new IllegalStateException();
    }

    private void wrapLeft(char[][] board, Position pos) {
      if (cubeWrap) {
        Position wrap = wrappedPosition(pos);
        if (board[wrap.y][wrap.x] == '.') {
          pos.setFrom(wrap);
        }
        return;
      }
      char[] row = board[pos.y];
      for (int i = row.length - 1; i >= 0; --i) {
        if (row[i] != ' ') {
          if (row[i] == '.') {
            pos.x = i;
          }
          return;
        }
      }
      throw new IllegalStateException();
    }

    private void wrapDown(char[][] board, Position pos) {
      if (cubeWrap) {
        Position wrap = wrappedPosition(pos);
        if (board[wrap.y][wrap.x] == '.') {
          pos.setFrom(wrap);
        }
        return;
      }
      for (int i = 0; i < board.length; ++i) {
        if (board[i][pos.x] != ' ') {
          if (board[i][pos.x] == '.') {
            pos.y = i;
          }
          return;
        }
      }
      throw new IllegalStateException();
    }

    private void wrapUp(char[][] board, Position pos) {
      if (cubeWrap) {
        Position wrap = wrappedPosition(pos);
        if (board[wrap.y][wrap.x] == '.') {
          pos.setFrom(wrap);
        }
        return;
      }
      for (int i = board.length - 1; i >= 0; --i) {
        if (board[i][pos.x] != ' ') {
          if (board[i][pos.x] == '.') {
            pos.y = i;
          }
          return;
        }
      }
      throw new IllegalStateException();
    }

    // My shape is (0 is first edge condition):
    //   0 - 1
    //   _____ \
    //   |_|_|  2 (itd.)
    //  _|_|
    // |_|_|
    // |_|
    @SuppressWarnings("java:S3776")
    private Position wrappedPosition(Position position) {
      if (position.direction == 0) {
        if (position.y < 50) {  // edge 2
          return new Position(position.x - 50, 149 - position.y, 2);
        } else if (position.y < 100) {  // edge 4
          return new Position(position.y + 50, 49, 3);
        } else if (position.y < 150) {  // edge 5
          return new Position(position.x + 50, 149 - position.y, 2);
        } else {  // edge 7
          return new Position(position.y - 100, 149, 3);
        }
      } else if (position.direction == 1) {
        if (position.x < 50) { // edge 8
          return new Position(position.x + 100, 0, 1);
        } else if (position.x < 100) { // edge 6
          return new Position(49, position.x + 100, 2);
        } else {  // edge 3
          return new Position(99, position.x - 50, 2);
        }
      } else if (position.direction == 2) {
        if (position.y < 50) { // edge 13
          return new Position(position.x - 50, 149 - position.y, 0);
        } else if (position.y < 100) { // edge 12
          return new Position(position.y - 50, 100, 1);
        } else if (position.y < 150) { // edge 10
          return new Position(position.x + 50, 149 - position.y, 0);
        } else { // edge 9
          return new Position(position.y - 100, 0, 1);
        }
      } else {
        if (position.x < 50) { // edge 11
          return new Position(50, position.x + 50, 0);
        } else if (position.x < 100) { // edge 0
          return new Position(0, position.x + 100, 0);
        } else {  // edge 1
          return new Position(position.x - 100, 199, 3);
        }
      }
    }
  }

  private static class Rotate implements Movement {
    private final int direction;

    private Rotate(String str) {
      direction = switch (str) {
        case "L" -> -1;
        case "R" -> 1;
        default -> throw new IllegalStateException();
      };
    }

    @Override
    public void move(Position position, char[][] board) {
      position.direction = Math.floorMod(direction + position.direction, 4);
    }
  }

  @AllArgsConstructor
  private static class Position {
    int x;
    int y;
    // 0 - right, clockwise
    int direction;

    private Position(int x) {
      this.x = x;
    }

    private void setFrom(Position other) {
      x = other.x;
      y = other.y;
      direction = other.direction;
    }
  }
}
