package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day3 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    String bits = Arrays.stream(Utils.transpose(getInput()))
            .map(ints -> Arrays.stream(ints).filter(i -> i == 0).count() > ints.length / 2 ? 0 : 1)
            .map(Object::toString)
            .collect(Collectors.joining());

    return Integer.parseInt(bits, 2) *
            Integer.parseInt(bits.replace('0', '2').replace('1', '0').replace('2', '1'), 2);
  }

  private static int task2() {
    int[][] input = getInput();

    List<int[]> oxygen = new ArrayList<>(Arrays.asList(input));
    List<int[]> co2 = new ArrayList<>(Arrays.asList(input));
    for (int i = 0; i < input[0].length; ++i) {
      int oxNumber = getZerosCount(oxygen, i) * 2 > oxygen.size() ? 0 : 1;
      int co2Number = getZerosCount(co2, i) * 2 <= co2.size() ? 0 : 1;

      final int j = i;
      int[] lastOx = oxygen.get(oxygen.size() - 1);
      int[] lastCo2 = co2.get(co2.size() - 1);
      oxygen.removeIf(o -> o[j] != oxNumber);
      co2.removeIf(o -> o[j] != co2Number);

      if (oxygen.isEmpty()) oxygen.add(lastOx);
      if (co2.isEmpty()) co2.add(lastCo2);
    }

    return toInt(oxygen.get(0)) * toInt(co2.get(0));
  }

  private static int getZerosCount(List<int[]> list, int idx) {
    return (int) Arrays.stream(Utils.transpose(list.toArray(int[][]::new))[idx])
            .filter(j -> j == 0)
            .count();
  }

  private static int toInt(int[] ints) {
    return Integer.parseInt(Arrays.stream(ints)
            .mapToObj(Objects::toString)
            .collect(Collectors.joining()), 2);
  }

  private static int[][] getInput() {
    return Utils.lines(3, 2021)
            .map(s -> s.chars()
                    .mapToObj(Character::toString)
                    .mapToInt(Integer::parseInt)
                    .toArray())
            .toArray(int[][]::new);
  }
}
