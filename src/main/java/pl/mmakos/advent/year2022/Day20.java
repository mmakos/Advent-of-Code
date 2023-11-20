package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day20 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    LongRef[] origin = Utils.ints(20, 2022, Utils.ENDL)
            .mapToObj(LongRef::new)
            .toArray(LongRef[]::new);

    CircularList sorted = new CircularList(origin);
    sort(sorted, origin);
    CListNode node0 = sorted.getNode0();

    return sorted.get(1000, node0).i +
            sorted.get(2000, node0).i +
            sorted.get(3000, node0).i;
  }

  private static long task2() {
    LongRef[] origin = Utils.ints(20, 2022, Utils.ENDL)
            .mapToLong(i -> i * 811_589_153L)
            .mapToObj(LongRef::new)
            .toArray(LongRef[]::new);

    CircularList sorted = new CircularList(origin);
    for (int i = 0; i < 10; i++) {
      sort(sorted, origin);
    }
    CListNode node0 = sorted.getNode0();

    return sorted.get(1000, node0).i +
            sorted.get(2000, node0).i +
            sorted.get(3000, node0).i;
  }

  private static void sort(CircularList sorted, LongRef[] origin) {
    for (LongRef anLongRef : origin) {
      if (anLongRef.i == 0) continue;
      sorted.move(anLongRef);
    }
  }

  private record LongRef(long i) {
  }

  private static class CircularList {
    private CListNode head;
    private final int size;

    private CircularList(LongRef[] array) {
      size = array.length;
      head = new CListNode(array[0]);
      CListNode last = head;
      for (int i = 1; i < size; ++i) {
        last.next = new CListNode(array[i]);
        last.next.prev = last;
        last = last.next;
      }
      last.next = head;
      head.prev = last;
    }

    private void move(LongRef val) {
      int move = (int) (val.i % (size - 1));
      CListNode valNode = get(val);
      CListNode node = valNode;
      node.prev.next = node.next;
      node.next.prev = node.prev;
      if (node == head) {
        head = node.next;
      }
      if (move < 0) {
        for (int i = 0; i >= move; --i) {
          node = node.prev;
        }
      } else {
        for (int i = 0; i < move; ++i) {
          node = node.next;
        }
      }
      valNode.prev = node;
      valNode.next = node.next;
      node.next.prev = valNode;
      node.next = valNode;
    }

    private CListNode get(LongRef i) {
      CListNode node = head;
      while (node.value != i) {
        node = node.next;
      }
      return node;
    }

    private CListNode getNode0() {
      CListNode node = head;
      while (node.value.i != 0) {
        node = node.next;
      }
      return node;
    }

    private LongRef get(int index, CListNode node) {
      for (int i = 0; i < index; ++i) {
        node = node.next;
      }
      return node.value;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      CListNode node = head;
      for (int i = 0; i < size; ++i) {
        sb.append(node.value.i);
        sb.append(", ");
        node = node.next;
      }
      return sb.toString();
    }
  }

  @RequiredArgsConstructor
  private static class CListNode {
    private final LongRef value;
    private CListNode next;
    private CListNode prev;
  }
}
