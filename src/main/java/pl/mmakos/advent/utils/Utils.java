package pl.mmakos.advent.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
}
