package pl.mmakos.advent.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public class Utils {
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

  public static Stream<String> strings(int day, int year, String delimiter) {
    return Arrays.stream(read(day, year).split(delimiter));
  }

  public static int positiveMod(int value, int mod) {
    int ret = value % mod;
    return ret >= 0 ? ret : ret + mod;
  }
}
