package pl.mmakos.advent.year2022;

import pl.mmakos.advent.utils.Utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day3 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return Utils.lines(3, 2022)
            .map(s -> new String[]{s.substring(0, s.length() / 2), s.substring(s.length() / 2)})
            .mapToInt(s -> getSameLetters(s[0], s[1])[0])
            .map(Day3::normalize)
            .sum();
  }

  private static int task2() {
    return Utils.lines(3, 2022, 3)
            .map(Stream::toList)
            .mapToInt(lines -> getSameLetter(lines.get(0), lines.get(1), lines.get(2)))
            .map(Day3::normalize)
            .sum();
  }

  private static int[] getSameLetters(String s1, String s2) {
    return IntStream.range(0, s1.length())
            .mapToObj(i -> s1.substring(i, i + 1))
            .filter(s2::contains)
            .mapToInt(s -> s.charAt(0))
            .toArray();
  }

  private static int normalize(int i) {
    if (Character.isLowerCase(i)) {
      return i - 'a' + 1;
    } else {
      return i - 'A' + 27;
    }
  }

  private static int getSameLetter(String s1, String s2, String s3) {
    int[] array = getSameLetters(s1, s2);
    return getSameLetters(new String(array, 0, array.length), s3)[0];
  }
}
