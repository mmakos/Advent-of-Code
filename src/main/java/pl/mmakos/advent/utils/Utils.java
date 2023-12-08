package pl.mmakos.advent.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.regex.Matcher;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.function.Predicate.not;

@SuppressWarnings({"java:S112", "unused"})
@NoArgsConstructor(access = AccessLevel.NONE)
public class Utils {
  public static final String ENDL = System.lineSeparator();
  public static final String ENDL_2 = ENDL.repeat(2);

  public static String read(int day, int year) {
    try {
      return Files.readString(Path.of("input/year" + year + "/day" + day + ".txt"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Stream<String> lines(int day, int year) {
    try {
      return Files.lines(Path.of("input/year" + year + "/day" + day + ".txt"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Stream<Stream<String>> lines(int day, int year, int batchSize) {
    return split(lines(day, year), batchSize);
  }

  public static <T> Stream<Stream<T>> split(Stream<T> stream, int batchSize) {
    List<T> lines = stream.toList();
    return IntStream.range(0, lines.size())
            .filter(i -> i % batchSize == 0)
            .mapToObj(i -> lines.subList(i, i + batchSize))
            .map(Collection::stream);
  }

  public static Stream<String> strings(int day, int year, String delimiter) {
    return Arrays.stream(read(day, year).split(delimiter));
  }

  public static Stream<Stream<String>> strings(int day, int year, String delimiter, String innerDelimiter) {
    return strings(day, year, delimiter)
            .map(s -> Arrays.stream(s.split(innerDelimiter)));
  }

  public static IntStream ints(int day, int year, String delimiter) {
    return Arrays.stream(read(day, year).split(delimiter))
            .mapToInt(Integer::parseInt);
  }

  public static Stream<IntStream> ints(int day, int year, String delimiter, String innerDelimiter) {
    return strings(day, year, delimiter)
            .map(s -> Arrays.stream(s.split(innerDelimiter))
                    .filter(not(String::isBlank))
                    .mapToInt(Integer::parseInt));
  }

  public static ToIntFunction<Integer> toPrimitiveInt() {
    return i -> i;
  }

  public static int[] splitToInts(String string, String delimiter) {
    return Arrays.stream(string.split(delimiter))
            .mapToInt(Integer::parseInt)
            .toArray();
  }

  @SafeVarargs
  public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T>... p2) {
    return Arrays.stream(p2)
            .reduce(p1, Predicate::or);
  }

  public static Stream<String> split(String s, int batchSize) {
    return IntStream.range(0, s.length())
            .filter(i -> i % batchSize == 0)
            .mapToObj(i -> s.substring(i, Math.min(i + batchSize, s.length())));
  }

  public static int bitsAt(int b, int pos, int length) {
    return ((1 << length) - 1) & (b >> pos);
  }

  public static long pow(int a, int b) {
    long x = 1;
    for (int i = 0; i < b; ++i) {
      x *= a;
    }
    return x;
  }

  public static String toString(char[][] chars) {
    StringBuilder sb = new StringBuilder();
    for (char[] row : chars) {
      sb.append(row).append("\n");
    }
    return sb.toString();
  }

  public static String toString(Char[][] chars) {
    StringBuilder sb = new StringBuilder();
    for (Char[] row : chars) {
      for (Char c : row) {
        sb.append(c.asChar());
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  public static int[] toInts(String str, String delimiter) {
    return Arrays.stream(str.split(delimiter))
            .mapToInt(Integer::parseInt)
            .toArray();
  }

  public static int[] toInts(String str) {
    return toInts(str, "\\s+");
  }

  public static long[] toLongs(String str, String delimiter) {
    return Arrays.stream(str.split(delimiter))
            .mapToLong(Long::parseLong)
            .toArray();
  }

  public static long[] toLongs(String str) {
    return toLongs(str, "\\s+");
  }

  public static int[][] transpose(int[][] matrix) {
    int[][] transposed = new int[matrix[0].length][matrix.length];
    for (int i = 0; i < matrix.length; ++i) {
      for (int j = 0; j < transposed.length; ++j) {
        transposed[j][i] = matrix[i][j];
      }
    }
    return transposed;
  }

  public static int[] toPrimitiveInts(List<Integer> ints) {
    return ints.stream()
            .mapToInt(toPrimitiveInt())
            .toArray();
  }

  public static Iterator<Character> iterator(char[] chars) {
    return IntStream.iterate(0, i -> ++i % chars.length)
            .mapToObj(i -> chars[i])
            .iterator();
  }

  public static void removeIndexes(List<?> list, List<Integer> indexes) {
    indexes.stream()
            .sorted(Comparator.reverseOrder())
            .mapToInt(toPrimitiveInt())
            .forEach(list::remove);
  }

  public static boolean contains(char c, char[] array) {
    for (char ch : array) {
      if (c == ch) return true;
    }
    return false;
  }

  public interface Char {
    char asChar();
  }

  // returns start and matched value
  public static Stream<Matcher> stream(Matcher matcher) {
    return stream(matcher::find, () -> matcher);
  }

  public static <T> Stream<T> stream(BooleanSupplier hasNext, Supplier<T> next) {
    Iterator<T> it = new Iterator<>() {
      @Override
      public boolean hasNext() {
        return hasNext.getAsBoolean();
      }

      @Override
      public T next() {
        return next.get();
      }
    };
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, 0), false);
  }
}
