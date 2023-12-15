package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static pl.mmakos.advent.utils.Utils.strings;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day15 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return strings(",")
            .mapToInt(Day15::hashCode)
            .sum();
  }

  @SuppressWarnings("unchecked")
  private static int task2() {
    List<SI>[] boxes = IntStream.range(0, 256).mapToObj(i -> new ArrayList<>()).toArray(List[]::new);

    strings(",")
            .map(s -> s.split("[=\\-]"))
            .forEach(s -> addOrRemoveLabel(s, boxes));

    return IntStream.range(0, boxes.length)
            .map(i -> IntStream.range(0, boxes[i].size())
                    .map(j -> (i + 1) * (j + 1) * boxes[i].get(j).i)
                    .sum())
            .sum();
  }

  private static void addOrRemoveLabel(String[] s, List<SI>[] boxes) {
    int hash = hashCode(s[0]);
    int existingIdx = boxes[hash].indexOf(new SI(s[0], 0));

    if (s.length == 2) {
      SI si = new SI(s[0], Integer.parseInt(s[1]));
      if (existingIdx >= 0) {
        boxes[hash].set(existingIdx, si);
      } else {
        boxes[hash].add(si);
      }
    } else if (existingIdx >= 0) {
      boxes[hash].remove(existingIdx);
    }
  }

  private static int hashCode(String string) {
    int hash = 0;
    for (int i = 0; i < string.length(); ++i) {
      hash += string.charAt(i);
      hash *= 17;
      hash %= 256;
    }
    return hash;
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class SI {
    private final String s;
    @EqualsAndHashCode.Exclude
    private final int i;
  }
}
