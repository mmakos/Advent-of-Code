package pl.mmakos.advent.year2022;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static java.util.function.Predicate.not;

public class Day7 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return getFileSystem().stream()
            .mapToInt(Dir::getSize)
            .filter(i -> i <= 100_000)
            .sum();
  }

  private static int task2() {
    List<Dir> fileSystem = getFileSystem();
    Dir root = fileSystem.get(0);
    int diff = 30_000_000 - (70_000_000 - root.size);
    return fileSystem.stream()
            .mapToInt(Dir::getSize)
            .filter(i -> i >= diff)
            .min()
            .orElseThrow();
  }

  private static List<Dir> getFileSystem() {
    List<Dir> dirs = new ArrayList<>();
    dirs.add(new Dir(null));
    Dir[] currentDir = new Dir[]{dirs.get(0)};

    Utils.lines(7, 2022)
            .filter(not(String::isBlank))
            .forEach(line -> {
              if ("$ cd /".equals(line)) {
                currentDir[0] = dirs.get(0);
              } else if (line.startsWith("$ cd ..")) {
                currentDir[0] = currentDir[0].parent;
              } else if (line.startsWith("$ cd ")) {
                currentDir[0] = new Dir(currentDir[0]);
                dirs.add(currentDir[0]);
              }else if (!"$ ls".equals(line) && !line.startsWith("dir")){
                currentDir[0].add(Integer.parseInt(line.split(" ")[0]));
              }
            });

    return dirs;
  }

  @RequiredArgsConstructor
  private static class Dir {
    private final Dir parent;
    @Getter
    private int size = 0;

    private void add(int fileSize) {
      size += fileSize;
      if (parent != null) {
        parent.add(fileSize);
      }
    }
  }
}
