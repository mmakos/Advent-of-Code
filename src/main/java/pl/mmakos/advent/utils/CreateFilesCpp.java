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
public final class CreateFilesCpp {
  private static final int YEAR = 2025;

  public static void main(String[] args) throws IOException {
    String template = Files.readString(Path.of("src/main/cpp/utils/template.cpp"))
        .replace("constexpr int day = 1;", "constexpr int day = %d;")
        .replace("constexpr int year = 2024;", "constexpr int year = %d;");
    IntStream.rangeClosed(1, 25)
        .forEach(i -> {
          try {
            new File("input/year" + YEAR + "/day" + i + ".txt").createNewFile();
            Files.writeString(Path.of("src/main/cpp/year" + YEAR + "/day" + i + ".cpp"),
                String.format(template, i, YEAR), StandardOpenOption.CREATE);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }
}
