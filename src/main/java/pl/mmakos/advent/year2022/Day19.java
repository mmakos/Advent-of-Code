package pl.mmakos.advent.year2022;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import pl.mmakos.advent.utils.Int;
import pl.mmakos.advent.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("java:S106")
/**
 * It is probably possible to try more optimizations
 * Currently, for my input, it calculates task 1 in just a moment,
 * but second task took around 4 minutes to compute
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day19 {
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Blueprint[] blueprints = Utils.strings(19, 2022, Utils.ENDL)
            .map(Blueprint::new)
            .toArray(Blueprint[]::new);

    int result = 0;
    for (int i = 0; i < blueprints.length; ++i) {
      int dfs = dfs(new State(blueprints[i], 24));
      System.err.println("BLPT: " + (i + 1) + " \tRES: " + dfs);
      result += dfs * (i + 1);
    }
    return result;
  }

  private static int task2() {
    Blueprint[] blueprints = Utils.strings(19, 2022, Utils.ENDL)
            .limit(3)
            .map(Blueprint::new)
            .toArray(Blueprint[]::new);

    int result = 1;
    for (int i = 0; i < blueprints.length; ++i) {
      int dfs = dfs(new State(blueprints[i], 32));
      System.err.println("BLPT: " + (i + 1) + " \tRES: " + dfs);
      result *= dfs;
    }
    return result;
  }

  private static int dfs(State state) {
    --state.timeLeft;
    if (state.timeLeft < 0) {
      return state.max.get();
    }
    if (getMaxPossibleGeodes(state) <= state.max.get()) {
      return state.max.get();
    }

    // There is no point in buying other robots than geodes, because we won't be able to buy geode robot on time anyway
    if (state.timeLeft > 0) {
      if (state.canBuyOre()) {
        dfs(state.copy()
                .saveBuyPossibilitiesState()
                .collect()
                .buyOre());
      }
      if (state.canBuyClay()) {
        dfs(state.copy()
                .saveBuyPossibilitiesState()
                .collect()
                .buyClay());
      }
      if (state.canBuyObsidian()) {
        dfs(state.copy()
                .saveBuyPossibilitiesState()
                .collect()
                .buyObsidian());
      }
    }
    if (state.canBuyGeode()) {
      dfs(state.copy()
              .saveBuyPossibilitiesState()
              .collect()
              .buyGeode());
    }
    dfs(state.copy().saveBuyPossibilitiesState().collect());
    return state.max.get();
  }

  private static int getMaxPossibleGeodes(State state) {
    int geodes = state.geodes;
    int geodeRobots = state.geodeRobots;
    int time = state.timeLeft;
    geodes += state.geodes * geodeRobots;
    if (state.canBuyGeode()) {
      ++geodeRobots;
    }
    for (; time >= 0; --time) {
      geodes += geodeRobots;
      ++geodeRobots;
    }
    return geodes;
  }

  @RequiredArgsConstructor
  private static class State {
    private final Blueprint blueprint;

    private int ores;
    private int clays;
    private int obsidians;
    private int geodes;

    private int oreRobots = 1;
    private int clayRobots;
    private int obsidianRobots;
    private int geodeRobots;

    private boolean didntBoughtOreRobotWhenCould;
    private boolean didntBoughtClayRobotWhenCould;
    private boolean didntBoughtObsidianRobotWhenCould;
    private boolean didntBoughtGeodeRobotWhenCould;

    private final Int max;
    private int timeLeft;

    private State(Blueprint blueprint, int time) {
      this(blueprint, new Int());
      timeLeft = time;
    }

    private State copy() {
      State state = new State(blueprint, max);
      state.ores = ores;
      state.clays = clays;
      state.obsidians = obsidians;
      state.geodes = geodes;
      state.oreRobots = oreRobots;
      state.clayRobots = clayRobots;
      state.obsidianRobots = obsidianRobots;
      state.geodeRobots = geodeRobots;
      state.timeLeft = timeLeft;

      return state;
    }

    private State collect() {
      ores += oreRobots;
      clays += clayRobots;
      obsidians += obsidianRobots;
      geodes += geodeRobots;

      max.setIfMax(geodes);

      return this;
    }

    private boolean canBuyOre() {
      return blueprint.oreCost <= ores && !didntBoughtOreRobotWhenCould;
    }

    private boolean canBuyClay() {
      return blueprint.clayCost <= ores && !didntBoughtClayRobotWhenCould;
    }

    private boolean canBuyObsidian() {
      return blueprint.obsidianOreCost <= ores && blueprint.obsidianClayCost <= clays && !didntBoughtObsidianRobotWhenCould;
    }

    private boolean canBuyGeode() {
      return blueprint.geodeOreCost <= ores && blueprint.geodeObsidianCost <= obsidians && !didntBoughtGeodeRobotWhenCould;
    }

    private State buyOre() {
      anythingBought();
      ores -= blueprint.oreCost;
      ++oreRobots;
      return this;
    }

    private State buyClay() {
      anythingBought();
      ores -= blueprint.clayCost;
      ++clayRobots;
      return this;
    }

    private State buyObsidian() {
      anythingBought();
      ores -= blueprint.obsidianOreCost;
      clays -= blueprint.obsidianClayCost;
      ++obsidianRobots;
      return this;
    }

    private State buyGeode() {
      anythingBought();
      ores -= blueprint.geodeOreCost;
      obsidians -= blueprint.geodeObsidianCost;
      ++geodeRobots;
      return this;
    }

    private State saveBuyPossibilitiesState() {
      if (canBuyOre()) didntBoughtOreRobotWhenCould = true;
      if (canBuyClay()) didntBoughtClayRobotWhenCould = true;
      if (canBuyObsidian()) didntBoughtObsidianRobotWhenCould = true;
      if (canBuyGeode()) didntBoughtGeodeRobotWhenCould = true;

      return this;
    }

    private void anythingBought() {
      didntBoughtOreRobotWhenCould = false;
      didntBoughtClayRobotWhenCould = false;
      didntBoughtObsidianRobotWhenCould = false;
      didntBoughtGeodeRobotWhenCould = false;
    }
  }

  private static class Blueprint {
    private static final Pattern PATTERN = Pattern.compile("Blueprint \\d+:\\s+" +
            "Each ore robot costs (\\d+) ore.\\s+" +
            "Each clay robot costs (\\d+) ore.\\s+" +
            "Each obsidian robot costs (\\d+) ore and (\\d+) clay.\\s+" +
            "Each geode robot costs (\\d+) ore and (\\d+) obsidian.");

    private final int oreCost;
    private final int clayCost;
    private final int obsidianOreCost;
    private final int obsidianClayCost;
    private final int geodeOreCost;
    private final int geodeObsidianCost;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Blueprint(String str) {
      Matcher matcher = PATTERN.matcher(str);
      matcher.matches();
      oreCost = Integer.parseInt(matcher.group(1));
      clayCost = Integer.parseInt(matcher.group(2));
      obsidianOreCost = Integer.parseInt(matcher.group(3));
      obsidianClayCost = Integer.parseInt(matcher.group(4));
      geodeOreCost = Integer.parseInt(matcher.group(5));
      geodeObsidianCost = Integer.parseInt(matcher.group(6));
    }
  }
}
