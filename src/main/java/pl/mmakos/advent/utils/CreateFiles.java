package pl.mmakos.advent.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class CreateFiles {
  private static final int YEAR = 2024;

  private static final String TEMPLATE =
      """
      package pl.mmakos.advent.year%d;
                
      import lombok.AccessLevel;
      import lombok.NoArgsConstructor;
                
      @NoArgsConstructor(access = AccessLevel.NONE)
      public final class Day%d {
        @SuppressWarnings("java:S106")
        public static void main(String[] args) {
          System.out.println("Advent of code %d, day %d");
          System.out.println("TASK 1: " + task1());
          System.out.println("TASK 2: " + task2());
        }
                
        private static int task1() {
          return 0;
        }
                
        private static int task2() {
          return 0;
        }
      }
      """;

  @SuppressWarnings({"ResultOfMethodCallIgnored", "java:S899", "java:S112"})
  public static void main(String[] args) {
    IntStream.rangeClosed(1, 25)
        .forEach(i -> {
          try {
            new File("input/year" + YEAR + "/day" + i + ".txt").createNewFile();
            Files.writeString(Path.of("src/main/java/pl/mmakos/advent/year" + YEAR + "/Day" + i + ".java"),
                String.format(TEMPLATE, YEAR, i, YEAR, i), StandardOpenOption.CREATE);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }
}
