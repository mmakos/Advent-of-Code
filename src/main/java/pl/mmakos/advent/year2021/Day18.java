package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day18 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    Pair[] array = Utils.lines()
            .map(Pair::new)
            .toArray(Pair[]::new);

    Pair p = array[0];
    for (int i = 1; i < array.length; ++i) {
      p = sum(p, array[i]);
    }

    return p.magnitude();
  }

  private static long task2() {
    Pair[] array = Utils.lines()
            .map(Pair::new)
            .toArray(Pair[]::new);

    return LongStream.range(0, array.length)
            .flatMap(i -> IntStream.range(0, array.length)
                    .filter(j -> j != i)
                    .mapToObj(j -> sum(array[(int) i].deepCopy(), array[j].deepCopy()))
                    .mapToLong(Pair::magnitude))
            .max()
            .orElseThrow();
  }

  private static Pair sum(Pair p1, Pair p2) {
    p1 = join(p1, p2);

    do {
      while (true) {
        Pair pair = p1.getFirstLeftPair(4);
        if (pair == null) break;
        pair.explode();
      }
    } while (p1.splitFirst());
    return p1;
  }

  private static Pair join(Pair p1, Pair p2) {
    Pair pair = new Pair(null, p1, p2);
    p1.parent = pair;
    p2.parent = pair;
    return pair;
  }

  @AllArgsConstructor
  private static class Pair {
    private Pair parent;
    private Object left;
    private Object right;

    public Pair(Object left, Object right) {
      this.left = left;
      this.right = right;

      if (left instanceof Pair p) p.parent = this;
      if (right instanceof Pair p) p.parent = this;
    }

    private Pair(String s) {
      this(s, null);
    }

    @SuppressWarnings("java:S127")
    private Pair(String s, Pair parent) {
      this.parent = parent;

      int leftEndIdx = 2; // [0, - inex of coma
      if (s.startsWith("[[")) {
        for (int leftBrackets = 1; leftBrackets > 0; ++leftEndIdx) {
          char c = s.charAt(leftEndIdx);
          if (c == '[') ++leftBrackets;
          else if (c == ']') --leftBrackets;
        }
        left = new Pair(s.substring(1, leftEndIdx), this);
      } else {
        left = s.charAt(1) - '0';
      }

      if (s.charAt(leftEndIdx + 1) == '[') {
        right = new Pair(s.substring(leftEndIdx + 1, s.length() - 1), this);
      } else {
        right = s.charAt(leftEndIdx + 1) - '0';
      }
    }

    private long magnitude() {
      long l = left instanceof Pair p ? p.magnitude() : ((Number) left).longValue();
      long r = right instanceof Pair p ? p.magnitude() : ((Number) right).longValue();
      return 3 * l + 2 * r;
    }

    private Pair getFirstLeftPair(int nest) {
      if (nest == 0) return this;
      if (this.left instanceof Pair pair) {
        Pair p = pair.getFirstLeftPair(nest - 1);
        if (p != null) return p;
      }
      if (this.right instanceof Pair pair) {
        return pair.getFirstLeftPair(nest - 1);
      }
      return null;
    }

    private boolean splitFirst() {
      if (this.left instanceof Pair pair) {
        if (pair.splitFirst()) return true;
      } else if ((int) this.left >= 10) {
        this.splitLeft();
        return true;
      }

      if (this.right instanceof Pair pair) {
        //noinspection RedundantIfStatement
        if (pair.splitFirst()) return true;
      } else if ((int) this.right >= 10) {
        this.splitRight();
        return true;
      }

      return false;
    }

    private boolean isLeft() {
      return parent.left == this;
    }

    private void splitLeft() {
      int l = (int) this.left;
      this.left = new Pair(this, l / 2, l / 2 + l % 2);
    }

    private void splitRight() {
      int r = (int) this.right;
      this.right = new Pair(this, r / 2, r / 2 + r % 2);
    }

    private void explode() {
      if (isLeft()) {
        addToLeftWhenLeft((int) left);
        addToRightWhenLeft((int) right);
        parent.left = 0;
      } else {
        addToLeftWhenRight((int) left);
        addToRightWhenRight((int) right);
        parent.right = 0;
      }
    }

    private void addToRightWhenLeft(int value) {
      if (parent.right instanceof Pair p) {
        addToLeftMostInnerChild(p, value);
      } else {
        parent.right = (int) parent.right + value;
      }
    }

    private void addToLeftWhenRight(int value) {
      parent.left = (int) parent.left + value;
    }

    private void addToLeftWhenLeft(int value) {
      Pair pair = this;
      while (pair.parent != null && pair.isLeft()) {
        pair = pair.parent;
      }
      pair = pair.parent;
      if (pair != null) {
        if (pair.left instanceof Pair l) {
          addToRightMostInnerChild(l, value);
        } else {
          pair.left = (int) pair.left + value;
        }
      }
    }

    private void addToRightWhenRight(int value) {
      Pair pair = this;
      while (pair.parent != null && !pair.isLeft()) {
        pair = pair.parent;
      }
      pair = pair.parent;
      if (pair != null) {
        if (pair.right instanceof Pair r) {
          addToLeftMostInnerChild(r, value);
        } else {
          pair.right = (int) pair.right + value;
        }
      }
    }

    private void addToLeftMostInnerChild(Pair pair, int value) {
      if (pair.left instanceof Pair l) {
        addToLeftMostInnerChild(l, value);
      } else {
        pair.left = (int) pair.left + value;
      }
    }

    private void addToRightMostInnerChild(Pair pair, int value) {
      if (pair.right instanceof Pair r) {
        addToRightMostInnerChild(r, value);
      } else {
        pair.right = (int) pair.right + value;
      }
    }

    private Pair deepCopy() {
      return new Pair(
              left instanceof Pair p ? p.deepCopy() : left,
              right instanceof Pair p ? p.deepCopy() : right);
    }

    @Override
    public String toString() {
      return "[" + left + "," + right + "]";
    }
  }
}
