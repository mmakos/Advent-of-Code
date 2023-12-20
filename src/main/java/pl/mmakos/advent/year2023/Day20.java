package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day20 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Map<String, Module> input = input();

    int[] pulses = new int[2];
    for (int i = 0; i < 1000; ++i) {
      int[] pulse = pulse(input, null).first();
      pulses[0] += pulse[0];
      pulses[1] += pulse[1];
    }

    return pulses[0] * pulses[1];
  }

  // For part 2, we need to look into input. We will see, that rx depends on one conjunction,
  // which depends on 4 modules. If we find cycles in which these modules are repeating to be false,
  // we can take lcm of those cycles lengths, and it will be our result
  private static long task2() {
    Map<String, Module> input = input();
    List<String> rxConjunctionParents = getParents("rx", input)
            .findFirst()
            .stream()
            .flatMap(p -> getParents(p, input))
            .toList();

    Map<String, Long> results = new HashMap<>();
    for (long i = 1; ; ++i) {
      List<String> ended = pulse(input, rxConjunctionParents).second();
      for (String e : ended) {
        results.putIfAbsent(e, i);
      }
      if (results.size() == rxConjunctionParents.size()) {
        return Utils.lcm(results.values().stream().mapToLong(l -> l).toArray());
      }
    }
  }

  private static Pair<int[], List<String>> pulse(Map<String, Module> modules, List<String> part2Ends) {
    record Pulse(String parent, String module, boolean pulse) {
    }

    Queue<Pulse> queue = new ArrayDeque<>();
    queue.add(new Pulse("", "broadcaster", false));

    List<String> ended = new ArrayList<>();
    int[] pulses = new int[2];
    while (!queue.isEmpty()) {
      Pulse p = queue.poll();
      if (part2Ends != null && part2Ends.contains(p.module) && !p.pulse) {
        ended.add(p.module);
      }
      ++pulses[p.pulse ? 1 : 0];
      Module module = modules.get(p.module);
      if (module == null) continue;
      Boolean pulse = module.pulse(p.parent, p.pulse);
      if (pulse != null) {
        Arrays.stream(module.outputs)
                .forEach(m -> queue.add(new Pulse(p.module, m, pulse)));
      }
    }

    return new Pair<>(pulses, ended);
  }

  private static Stream<String> getParents(String module, Map<String, Module> modules) {
    return modules.entrySet().stream()
            .filter(e -> ArrayUtils.contains(e.getValue().outputs, module))
            .map(Map.Entry::getKey);

  }

  private static Map<String, Module> input() {
    Map<String, Module> modules = Utils.lines()
            .map(s -> s.split(" -> "))
            .map(s -> {
              String name = StringUtils.stripStart(s[0], "%&");
              String[] outputs = s[1].split(", ");
              if (s[0].startsWith("%")) {
                return new FlipFlop(name, outputs);
              } else if (s[0].startsWith("&")) {
                return new Conjunction(name, outputs);
              } else {
                return new Broadcast(name, outputs);
              }
            }).collect(Collectors.toMap(Module::getName, identity()));

    modules.values().stream()
            .filter(Conjunction.class::isInstance)
            .map(Conjunction.class::cast)
            .forEach(c -> getParents(c.getName(), modules)
                    .forEach(c::addOutput));
    return modules;
  }

  @Getter
  @RequiredArgsConstructor
  private abstract static class Module {
    private final String name;
    private final String[] outputs;

    protected abstract Boolean pulse(String input, boolean pulse);
  }

  private static class Broadcast extends Module {
    public Broadcast(String name, String[] outputs) {
      super(name, outputs);
    }

    @Override
    protected Boolean pulse(String input, boolean pulse) {
      return pulse;
    }
  }

  @SuppressWarnings({"java:S1121", "java:S2447"})
  private static class FlipFlop extends Module {
    private boolean state = false;

    private FlipFlop(String name, String[] outputs) {
      super(name, outputs);
    }

    @Override
    protected Boolean pulse(String input, boolean pulse) {
      if (pulse) return null;
      return state = !state;
    }
  }

  @SuppressWarnings({"java:S1121", "java:S2447"})
  private static class Conjunction extends Module {
    private final Map<String, Boolean> inputs = new HashMap<>();

    public Conjunction(String name, String[] outputs) {
      super(name, outputs);
    }

    @Override
    protected Boolean pulse(String input, boolean pulse) {
      inputs.put(input, pulse);
      return !inputs.values().stream().allMatch(b -> b);
    }

    private void addOutput(String output) {
      inputs.put(output, false);
    }
  }
}
