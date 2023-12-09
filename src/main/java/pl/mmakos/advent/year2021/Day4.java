package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Int;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Ref;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day4 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Pair<int[], Cell[][][]> input = input();
    int[] vals = input.first();
    Cell[][][] boards = input.second();

    Pair<Cell[][], Integer> win = winningBoard(boards, vals);

    return boardScore(win.first(), win.second());
  }

  private static int task2() {
    Pair<int[], Cell[][][]> input = input();
    int[] vals = input.first();
    Cell[][][] boards = input.second();

    Pair<Cell[][], Integer> win = lastWinningBoard(boards, vals);

    return boardScore(win.first(), win.second());
  }

  private static Pair<Cell[][], Integer> winningBoard(Cell[][][] boards, int[] vals) {
    for (int v : vals) {
      Optional<Cell[][]> winning = Arrays.stream(boards)
              .filter(board -> winsAfter(board, v))
              .findFirst();
      if (winning.isPresent()) return new Pair<>(winning.get(), v);
    }
    throw new IllegalStateException();
  }

  private static Pair<Cell[][], Integer> lastWinningBoard(Cell[][][] boards, int[] vals) {
    List<Cell[][]> boardList = new ArrayList<>(Arrays.asList(boards));
    Ref<Cell[][]> winLast = new Ref<>();
    Int lastVal = new Int();
    for (int v : vals) {
      List<Cell[][]> toRemove = new ArrayList<>();
      boardList.stream()
              .filter(b -> winsAfter(b, v))
              .forEach(b -> {
                winLast.set(b);
                toRemove.add(b);
                lastVal.set(v);
              });
      boardList.removeAll(toRemove);
    }
    return new Pair<>(winLast.get(), lastVal.get());
  }

  private static boolean allMatched(Cell[] cells) {
    return Arrays.stream(cells)
            .allMatch(Cell::isMatched);
  }

  private static boolean allMatched(Cell[][] cells, int col) {
    return allMatched(Arrays.stream(cells)
            .map(row -> row[col])
            .toArray(Cell[]::new));
  }

  private static boolean wins(Cell[][] cells) {
    return Arrays.stream(cells).anyMatch(Day4::allMatched)
            || IntStream.range(0, cells[0].length).anyMatch(i -> allMatched(cells, i));
  }

  private static boolean winsAfter(Cell[][] cells, int value) {
    for (Cell[] row : cells) {
      for (Cell cell : row) {
        cell.match(value);
      }
    }
    return wins(cells);
  }

  private static int boardScore(Cell[][] board, int val) {
    return Arrays.stream(board)
            .flatMap(Arrays::stream)
            .filter(not(Cell::isMatched))
            .mapToInt(Cell::getValue)
            .sum() * val;
  }

  private static Pair<int[], Cell[][][]> input() {
    String[][] strs = Utils.strings(Utils.ENDL_2, Utils.ENDL)
            .map(s -> s.toArray(String[]::new))
            .toArray(String[][]::new);

    int[] vals = Arrays.stream(strs[0][0].split(","))
            .mapToInt(Integer::parseInt)
            .toArray();

    Cell[][][] boards = Arrays.stream(strs)
            .skip(1)
            .map(Day4::parseBoard)
            .toArray(Cell[][][]::new);

    return new Pair<>(vals, boards);
  }

  private static Cell[][] parseBoard(String[] strs) {
    return Arrays.stream(strs)
            .map(s -> Arrays.stream(s.split("\\s+"))
                    .filter(not(String::isBlank))
                    .map(Integer::parseInt)
                    .map(Cell::new)
                    .toArray(Cell[]::new))
            .toArray(Cell[][]::new);
  }

  @Getter
  @RequiredArgsConstructor
  private static class Cell {
    private final int value;
    private boolean matched;

    private void match(int v) {
      if (v == value) {
        matched = true;
      }
    }
  }
}
