package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BiPredicate;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day12 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    String[] lines = Utils.lines(12, 2022).toArray(String[]::new);
    return bfs(getStart('S', lines), lines, 'E', (value, newValue) ->
            ((newValue <= value + 1) && newValue >= 'a') ||
                    (value == 'S' && (newValue == 'a' || newValue == 'b')) ||
                    (newValue == 'E' && (value == 'z' || value == 'y')));
  }

  private static int task2() {
    String[] lines = Utils.lines(12, 2022).toArray(String[]::new);
    return bfs(getStart('E', lines), lines, 'a', (value, newValue) -> newValue >= value - 1);
  }

  private static Point getStart(char c, String[] lines) {
    Point start = null;
    for (int i = 0; i < lines.length; ++i) {
      int s = lines[i].indexOf(c);
      if (s != -1) {
        start = new Point(s, i, 0);
      }
    }
    return start;
  }


  private static int bfs(Point start, String[] lines, char end, BiPredicate<Character, Character> valid) {
    boolean[][] visited = new boolean[lines.length][lines[0].length()];
    Deque<Point> queue = new LinkedList<>();
    visited[start.y][start.x] = true;
    queue.add(start);
    while (!queue.isEmpty()) {
      Point p = queue.poll();
      char value = lines[p.y].charAt(p.x);
      if (value == end) return p.i;

      Point top = new Point(p.x, p.y - 1, p.i + 1);
      Point bottom = new Point(p.x, p.y + 1, p.i + 1);
      Point right = new Point(p.x + 1, p.y, p.i + 1);
      Point left = new Point(p.x - 1, p.y, p.i + 1);
      if (valid(top, lines, value, valid) && !visited[top.y][top.x]) {
        queue.add(top);
        visited[top.y][top.x] = true;
      }
      if (valid(bottom, lines, value, valid) && !visited[bottom.y][bottom.x]) {
        queue.add(bottom);
        visited[bottom.y][bottom.x] = true;
      }
      if (valid(right, lines, value, valid) && !visited[right.y][right.x]) {
        queue.add(right);
        visited[right.y][right.x] = true;
      }
      if (valid(left, lines, value, valid) && !visited[left.y][left.x]) {
        queue.add(left);
        visited[left.y][left.x] = true;
      }
    }
    return -1;
  }

  private static boolean valid(Point p, String[] lines, char value, BiPredicate<Character, Character> valid) {
    if (p.x < 0 || p.y < 0 || p.x >= lines[0].length() || p.y >= lines.length) return false;

    char newValue = lines[p.y].charAt(p.x);
    return valid.test(value, newValue);
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class Point {
    private final int x;
    private final int y;
    @EqualsAndHashCode.Exclude
    private final int i;
  }
}
