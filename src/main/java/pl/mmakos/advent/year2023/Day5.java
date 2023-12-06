package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Pair;
import pl.mmakos.advent.utils.Utils;

import java.util.*;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day5 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static long task1() {
    Pair<long[], Mapper[][]> input = input();
    long[] seeds = input.first();
    Mapper[][] mappers = input.second();

    return Arrays.stream(seeds)
            .map(s -> map(s, mappers))
            .min()
            .orElseThrow();
  }

  private static long task2() {
    Pair<long[], Mapper[][]> input = input();
    Mapper[][] mappers = input.second();

    List<long[]> seeds = IntStream.iterate(0, i -> i < input.first().length, i -> i + 2)
            .mapToObj(i -> new long[]{input.first()[i], input.first()[i + 1]})
            .toList();

    // Most outer loop to iterate through next levels (seed-to-soil, soil-to-fertilizer, etc.)
    for (Mapper[] map : mappers) {
      // We store ranges, which we already mapped with previous mappers
      List<long[]> mapped = new ArrayList<>();

      // We iterate through mappers from level to map all possible ranges
      for (Mapper m : map) {
        // We store ranges that was not mapped by current mapper
        List<long[]> notMapped = new ArrayList<>();

        // We iterate through seeds (which was not mapped yet in current level) and try to map them
        for (long[] seed : seeds) {
          long[][] mapping = m.map(seed);
          if (mapping[0][1] > 0) notMapped.add(mapping[0]);
          if (mapping[1][1] > 0) mapped.add(mapping[1]);
          if (mapping[2][1] > 0) notMapped.add(mapping[2]);
        }
        // Now all mapped ranges by current mapper are in `mapped` collection and `seeds` will contain only not already mapped ranges
        seeds = notMapped;
      }
      // There are no mappers, so we take not mapped ranges as mapped, but without any mapping
      mapped.addAll(seeds);
      // Now our seeds to next level are mapped values from current level
      seeds = mapped;
    }

    return seeds.stream()
            .mapToLong(l -> l[0])
            .min()
            .orElseThrow();
  }

  private static Pair<long[], Mapper[][]> input() {
    String[] array = Utils.strings(5, 2023, Utils.ENDL_2).toArray(String[]::new);

    long[] seeds = Utils.toLongs(array[0].split(": ")[1]);

    Mapper[][] mappers = Arrays.stream(array)
            .skip(1)
            .map(s -> Arrays.stream(s.split(Utils.ENDL))
                    .skip(1)
                    .map(Utils::toLongs)
                    .map(longs -> new Mapper(longs[1], longs[0], longs[2]))
                    .toArray(Mapper[]::new))
            .toArray(Mapper[][]::new);

    return new Pair<>(seeds, mappers);
  }

  private static long map(long seed, Mapper[][] mappers) {
    long dest = seed;
    for (Mapper[] m : mappers) {
      dest = map(dest, m);
    }
    return dest;
  }

  private static long map(long value, Mapper[] mappers) {
    return Arrays.stream(mappers)
            .map(m -> m.map(value))
            .filter(OptionalLong::isPresent)
            .mapToLong(OptionalLong::getAsLong)
            .findFirst()
            .orElse(value);
  }

  private record Mapper(long source, long destination, long length) {
    private OptionalLong map(long value) {
      if (value >= source && value < source + length) {
        return OptionalLong.of(destination - source + value);
      } else {
        return OptionalLong.empty();
      }
    }

    // Probably there is much easier way to do it - we just want to get intersection and subdivision of mapper and range
    // Returns an array of 3 ranges: before mapper range (not mapped), mapped range and after mapper range (not mapped)
    // Second value of range (length) can be 0 (it means range is invalid)
    private long[][] map(long[] range) {
      long[] before = new long[2];
      long[] toMap = new long[2];
      long[] after = new long[2];

      // WHOLE RANGE IS BEFORE MAPPER
      if (range[0] < source && range[0] + range[1] < source) {
        before = range;
      }
      // WHOLE RANGE IS AFTER MAPPER
      else if (range[0] >= source + length) {
        after = range;
      } else if (range[0] < source) {
        before[0] = range[0];
        before[1] = source - range[0];
        toMap[0] = source;
        // RANGE INTERSECTS (STARTS BEFORE MAPPER AND ENDS WITHIN)
        if (range[0] + range[1] < source + length) {
          toMap[1] = range[0] + range[1] - source;
        }
        // RANGE CONTAINS MAPPER
        else {
          toMap[1] = length;
          after[0] = source + length;
          after[1] = range[0] + range[1] - (source + length);

        }
      } else {
        // RANGE INTERSECTS (STARTS WITHIN MAPPER AND ENDS AFTER)
        toMap[0] = range[0];
        if (range[0] + range[1] > source + length) {
          toMap[1] = source + length - range[0];
          after[0] = source + length;
          after[1] = range[0] + range[1] - (source + length);
        }
        // MAPPER CONTAINS RANGE
        else {
          toMap[1] = range[1];
        }
      }

      if (toMap[1] > 0) {
        toMap[0] = map(toMap[0]).orElseThrow();
      }

      return new long[][]{before, toMap, after};
    }
  }
}

