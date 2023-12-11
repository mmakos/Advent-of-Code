package pl.mmakos.advent.utils;

import java.util.Map;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Pair<T1, T2>(T1 first, T2 second) {
  public <T> Pair<T1, T> mapSecond(Function<T2, T> mapper) {
    return new Pair<>(first, mapper.apply(second));
  }

  public <T> Pair<T, T2> mapFirst(Function<T1, T> mapper) {
    return new Pair<>(mapper.apply(first), second);
  }

  public static class Stream<T1, T2> {
    private java.util.stream.Stream<Pair<T1, T2>> innerStream;

    private Stream(java.util.stream.Stream<Pair<T1, T2>> stream) {
      this.innerStream = stream;
    }

    public static <V1, V2> Stream<V1, V2> pairStream(java.util.stream.Stream<Pair<V1, V2>> stream) {
      return new Stream<>(stream);
    }

    public static <V, V1, V2> Stream<V1, V2> pairStream(java.util.stream.Stream<V> stream, Function<V, V1> firstMapper, Function<V, V2> secondMapper) {
      return pairStream(stream.map(v -> new Pair<>(firstMapper.apply(v), secondMapper.apply(v))));
    }

    public static <V1, V2> Stream<V1, V2> pairStream(Map<V1, V2> map) {
      return new Stream<>(map.entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue())));
    }

    public java.util.stream.Stream<Pair<T1, T2>> unwrap() {
      return innerStream;
    }

    public Stream<T1, T2> filterFirst(Predicate<T1> filter) {
      innerStream = innerStream.filter(p -> filter.test(p.first));
      return this;
    }

    public Stream<T1, T2> filterSecond(Predicate<T2> filter) {
      innerStream = innerStream.filter(p -> filter.test(p.second));
      return this;
    }

    public Stream<T1, T2> filter(BiPredicate<T1, T2> filter) {
      innerStream = innerStream.filter(p -> filter.test(p.first, p.second));
      return this;
    }

    public <T> Stream<T, T2> mapFirst(Function<T1, T> mapper) {
      return new Stream<>(innerStream.map(p -> p.mapFirst(mapper)));
    }

    public <T> Stream<T, T2> flatMapFirst(Function<T1, java.util.stream.Stream<T>> mapper) {
      return new Stream<>(innerStream.flatMap(p -> mapper.apply(p.first()).map(c -> new Pair<>(c, p.second()))));
    }

    public <T> Stream<T1, T> mapSecond(Function<T2, T> mapper) {
      return new Stream<>(innerStream.map(p -> p.mapSecond(mapper)));
    }

    public IntStream mapToInt(BiFunction<T1, T2, Integer> mapper) {
      return innerStream.mapToInt(p -> mapper.apply(p.first, p.second));
    }

    public void forEach(BiConsumer<T1, T2> function) {
      innerStream.forEach(p -> function.accept(p.first, p.second));
    }

    public Map<T1, T2> toMap() {
      return innerStream.collect(Collectors.toMap(Pair::first, Pair::second));
    }
  }
}
