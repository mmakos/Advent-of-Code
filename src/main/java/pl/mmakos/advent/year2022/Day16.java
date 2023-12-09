package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day16 {
  private static final Pattern INPUT_PATTERN = Pattern.compile("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ([A-Z]{2}(, [A-Z]{2})*)");

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Valve aaValve = input();
    return dfs(aaValve, 30, new HashSet<>(), aaValve.valves.size() + 1);
  }

  private static int task2() {
    Valve aaValve = input();

    Valve[] valves = aaValve.valves.keySet().toArray(new Valve[0]);
    int max = 0;
    // All possible combinations of 1, 2, 3 ... valves.length() elements
    // So elephant goes to DD, and I go to BB, CC, EE, JJ, HH etc.
    // Indices of elements taken by elephant are stored as ones in given mask
    // Iterating over mask will give all possible combinations
    // We have to iterate only over half of masks, because other half would be just swap of me and elephant
    for (int mask = 0; mask < (int) Math.pow(2, valves.length) / 2; ++mask) {
      Set<Valve> elephantValves = new HashSet<>();
      Set<Valve> myValves = new HashSet<>();
      for (int i = 0; i < valves.length; ++i) {
        (((mask >> i) & 0b1) == 1 ? elephantValves : myValves).add(valves[i]);
      }
      int candidate = dfs(aaValve, 26, elephantValves, valves.length + 1) + dfs(aaValve, 26, myValves, valves.length + 1);
      if (candidate > max) {
        max = candidate;
      }
    }

    return max;
  }

  private static Valve input() {
    Map<String, Pair<Valve, String[]>> input = Utils.lines()
            .filter(not(String::isBlank))
            .map(INPUT_PATTERN::matcher)
            .filter(Matcher::matches)
            .collect(Collectors.toMap(m -> m.group(1), m -> new Pair<>(new Valve(m.group(1), Integer.parseInt(m.group(2))), m.group(3).split(", "))));

    input.forEach((key, value) -> {
      for (String v : value.second()) {
        value.first().valves.put(input.get(v).first(), 1);
      }
    });

    Map<Valve, Valve> valves = input.values().stream()
            .map(Pair::first)
            .filter(v -> "AA".equals(v.name) || v.flow > 0)
            .map(v -> new Valve(v.name, v.flow, bfs(v)))
            .collect(Collectors.toMap(v -> v, v -> v));

    // Replace all neighbors to objects from above map (so each "XY" node is the same object
    for (Valve valve : valves.keySet()) {
      Map<Valve, Integer> map = valve.valves.entrySet()
              .stream()
              .map(e -> new Pair<>(valves.get(e.getKey()), e.getValue()))
              .collect(Collectors.toMap(Pair::first, Pair::second));
      valve.valves.clear();
      valve.valves.putAll(map);
    }

    return valves.get(new Valve("AA", 0, null));
  }

  // DFS to find all possible combinations of nodes within 30 minutes
  // In this algorithm graph is treated like a tree with root in AA
  // The end of walk is when we reach 30 minutes or visited all vertices
  // Return weights sum in maximum variant
  private static int dfs(Valve start, int timeLeft, Set<Valve> opened, int totalValves) {
    opened = new HashSet<>(opened);
    opened.add(start);
    int maxResult = 0;
    if (opened.size() == totalValves) {
      return maxResult;
    }
    for (Map.Entry<Valve, Integer> entry : start.valves.entrySet()) {
      if (!opened.contains(entry.getKey()) && entry.getValue() + 1 <= timeLeft) {
        int resultCandidate = dfs(entry.getKey(), timeLeft - entry.getValue() - 1, opened, totalValves) + entry.getKey().flow * (timeLeft - entry.getValue() - 1);
        if (resultCandidate > maxResult) {
          maxResult = resultCandidate;
        }
      }
    }
    return maxResult;
  }

  // BFS to find all routes to given valve
  private static Map<Valve, Integer> bfs(Valve start) {
    Map<Valve, Integer> result = new HashMap<>();

    Set<Valve> visited = new HashSet<>();
    Queue<Pair<Valve, Integer>> toVisit = new ArrayDeque<>();
    toVisit.add(new Pair<>(start, 0));

    while (!toVisit.isEmpty()) {
      Pair<Valve, Integer> pair = toVisit.poll();
      for (Valve v : pair.first().valves.keySet()) {
        if (!visited.contains(v) && v != start) {
          visited.add(v);
          if (v.flow > 0) {
            result.put(v, pair.second() + 1);
          }
          toVisit.add(new Pair<>(v, pair.second() + 1));
        }
      }
    }

    return result;
  }


  @SuppressWarnings("DuplicatedCode")
  private static Pair<Map.Entry<Valve, Integer>, Map.Entry<Valve, Integer>> getTwoValidValves(Iterator<Map.Entry<Valve, Integer>> valves, Set<Valve> opened, int timeLeft) {
    Map.Entry<Valve, Integer> e1 = getNextValidValve(valves, opened, timeLeft);
    if (e1 == null) return null;
    return new Pair<>(e1, getNextValidValve(valves, opened, timeLeft));
  }

  private static Map.Entry<Valve, Integer> getNextValidValve(Iterator<Map.Entry<Valve, Integer>> valves, Set<Valve> opened, int timeLeft) {
    while (valves.hasNext()) {
      Map.Entry<Valve, Integer> entry = valves.next();
      if (!opened.contains(entry.getKey()) && entry.getValue() + 1 <= timeLeft) {
        return entry;
      }
    }
    return null;
  }

  @EqualsAndHashCode(onlyExplicitlyIncluded = true)
  @RequiredArgsConstructor
  private static class Valve {
    @EqualsAndHashCode.Include
    private final String name;
    private final int flow;
    private final Map<Valve, Integer> valves;

    public Valve(String name, int flow) {
      this(name, flow, new HashMap<>());
    }

    @Override
    public String toString() {
      return name + ": " + valves.entrySet().stream().map(e -> e.getKey().name + "(" + e.getValue() + ")").collect(Collectors.joining(", "));
    }
  }
}
