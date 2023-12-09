package pl.mmakos.advent.year2021;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Bool;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day12 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    return dfs(Caves.parseFrom(Utils.lines()), "start", false);
  }

  private static int task2() {
    return dfs(Caves.parseFrom(Utils.lines()), "start", true);
  }

  private static int dfs(Caves caves, String start, boolean canVisitTwice) {
    List<String> possiblePaths = caves.getPossiblePaths(start);

    int paths = 0;
    for (String path : possiblePaths) {
      if (path.equals("end")) ++paths;
      else {
        Caves copy = caves.copy();
        copy.removeIfSmallVisited(path, canVisitTwice);
        paths += dfs(copy, path, canVisitTwice);
      }
    }
    return paths;
  }

  private record Caves(Map<String, List<String>> map, List<String> smallVisited, Bool smallVisitedTwice) {
    private static Caves parseFrom(Stream<String> strings) {
      Map<String, List<String>> map = new HashMap<>();

      strings.map(s -> s.split("-"))
              .forEach(s -> {
                if (!s[1].equals("start")) {
                  map.computeIfAbsent(s[0], k -> new ArrayList<>()).add(s[1]);
                }
                if (!s[1].equals("end") && !s[0].equals("start")) {
                  map.computeIfAbsent(s[1], k -> new ArrayList<>()).add(s[0]);
                }
              });
      return new Caves(map, new ArrayList<>(), new Bool());
    }

    private void removeIfSmallVisited(String cave, boolean canVisitTwice) {
      if (cave.chars().allMatch(Character::isLowerCase)) {
        if (smallVisited.contains(cave)) {
          smallVisited.forEach(c -> map.values().forEach(l -> l.remove(c)));
          smallVisitedTwice.set(true);
        } else if (!canVisitTwice || smallVisitedTwice.get()) {
          map.values().forEach(l -> l.remove(cave));
        } else {
          smallVisited.add(cave);
        }
      }
    }

    private Caves copy() {
      Map<String, List<String>> map = new HashMap<>();
      this.map.forEach((k, v) -> map.put(k, new ArrayList<>(v)));
      return new Caves(map, new ArrayList<>(smallVisited), smallVisitedTwice.copy());
    }

    private List<String> getPossiblePaths(String cave) {
      return map.get(cave);
    }
  }
}
