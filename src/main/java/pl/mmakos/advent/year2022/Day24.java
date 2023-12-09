package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Point;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

// This is probably not the best solution
// More optimal way would be not to calculate every board after move, because blizzards walk around with no collisions
// So we could only store initial board and then calculate positions of blizzard as (initialPos * iteration) % boardDimension
@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day24 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Tile[][] board = board();
    Point start = new Point(1, 0);
    Point end = new Point(board[0].length - 2, board.length - 2);

    return bfs(board, start, end).first();
  }

  @SuppressWarnings("java:S2234")
  private static int task2() {
    Tile[][] board = board();
    Point start = new Point(1, 0);
    Point end = new Point(board[0].length - 2, board.length - 2);
    Point startEnd = new Point(1, 1);
    Point endStart = new Point(board[0].length - 2, board.length - 1);

    Pair<Integer, Tile[][]> first = bfs(board, start, end);
    Pair<Integer, Tile[][]> second = bfs(first.second(), endStart, startEnd);
    return bfs(second.second(), start, end).first() + first.first() + second.first();
  }

  private static Pair<Integer, Tile[][]> bfs(Tile[][] initBoard, Point start, Point end) {
    Queue<State> queue = new ArrayDeque<>();
    Set<State> visited = new HashSet<>();
    queue.add(new State(initBoard, start, 0));

    while (!queue.isEmpty()) {
      State state = queue.poll();
      visited.add(state);

      Tile[][] newBoard = moveAll(state.board);
      Set<Point> moves = possibleMoves(newBoard, state.position);
      if (moves.contains(end)) {
        return new Pair<>(state.iteration + 2, moveAll(moveAll(state.board)));
      }
      moves.stream()
              .map(p -> new State(newBoard, p, state.iteration + 1))
              .filter(not(visited::contains))
              .filter(not(queue::contains))
              .forEach(queue::add);
    }

    throw new IllegalStateException();
  }

  private static Tile[][] board() {
    return Utils.lines()
            .map(s -> s.chars()
                    .mapToObj(Tile::new)
                    .toArray(Tile[]::new))
            .toArray(Tile[][]::new);
  }

  private record State(Tile[][] board, Point position, int iteration) {
    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder(Utils.toString(board));
      sb.setCharAt(position.y() * (board[0].length + 1) + position.x(), 'E');
      return sb.toString();
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(board) * 31 + position.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof State state &&
              position.equals(state.position) &&
              Arrays.deepEquals(state.board, board);
    }
  }

  private static Set<Point> possibleMoves(Tile[][] board, Point point) {
    Stream<Point> moveCandidates = Stream.of(point.left(), point.right(), point.bottom(), point.top())
            .filter(p -> p.y() > 0)
            .filter(p -> p.y() < board.length - 1);
    return Stream.concat(moveCandidates, Stream.of(point))
            .filter(p -> board[p.y()][p.x()].canMoveInto())
            .collect(Collectors.toSet());
  }

  private static Tile[][] moveAll(Tile[][] board) {
    Tile[][] newBoard = new Tile[board.length][board[0].length];
    for (int i = 0; i < board.length; ++i) {
      for (int j = 0; j < board[0].length; ++j) {
        newBoard[i][j] = new Tile(board[i][j]);
      }
    }

    for (int x = 1; x < board[0].length - 1; ++x) {
      for (int y = 1; y < board.length - 1; ++y) {
        moveRight(board, newBoard, x, y);
        moveBottom(board, newBoard, x, y);
        moveLeft(board, newBoard, x, y);
        moveUp(board, newBoard, x, y);
      }
    }

    return newBoard;
  }

  private static void moveRight(Tile[][] board, Tile[][] newBoard, int x, int y) {
    Tile tile = board[y][x];
    if (!tile.blizzards[0]) return;

    int newX = x + 1;
    if (newX >= board[0].length - 1) {
      newX = 1;
    }
    newBoard[y][newX].blizzards[0] = true;
  }

  private static void moveBottom(Tile[][] board, Tile[][] newBoard, int x, int y) {
    Tile tile = board[y][x];
    if (!tile.blizzards[1]) return;

    int newY = y + 1;
    if (newY >= board.length - 1) {
      newY = 1;
    }
    newBoard[newY][x].blizzards[1] = true;
  }

  private static void moveLeft(Tile[][] board, Tile[][] newBoard, int x, int y) {
    Tile tile = board[y][x];
    if (!tile.blizzards[2]) return;

    int newX = x - 1;
    if (newX < 1) {
      newX = board[0].length - 2;
    }
    newBoard[y][newX].blizzards[2] = true;
  }

  private static void moveUp(Tile[][] board, Tile[][] newBoard, int x, int y) {
    Tile tile = board[y][x];
    if (!tile.blizzards[3]) return;

    int newY = y - 1;
    if (newY < 1) {
      newY = board.length - 2;
    }
    newBoard[newY][x].blizzards[3] = true;
  }

  @EqualsAndHashCode
  private static class Tile implements Utils.Char {
    // Right, Down, Left, Top
    private final boolean[] blizzards = new boolean[4];
    private final char special;

    private Tile(int c) {
      if (c == '#') {
        special = (char) c;
      } else {
        special = 0;
        if (c != '.') {
          int blizzardIdx = switch (c) {
            case '>' -> 0;
            case 'v' -> 1;
            case '<' -> 2;
            case '^' -> 3;
            default -> throw new IllegalStateException();
          };
          blizzards[blizzardIdx] = true;
        }
      }
    }

    private Tile(Tile tile) {
      special = tile.special;
    }

    private boolean canMoveInto() {
      return special == 0 && !hasBlizzards();
    }

    private boolean hasBlizzards() {
      return blizzards[0] || blizzards[1] || blizzards[2] || blizzards[3];
    }

    @Override
    public char asChar() {
      if (special != '\0') {
        return special;
      }
      int[] ints = IntStream.range(0, 4)
              .filter(i -> blizzards[i])
              .toArray();
      if (ints.length == 0) return '.';
      else if (ints.length > 1) return Integer.toString(ints.length).charAt(0);

      if (ints[0] == 0) return '>';
      else if (ints[0] == 1) return 'v';
      else if (ints[0] == 2) return '<';
      else return '^';
    }
  }
}
