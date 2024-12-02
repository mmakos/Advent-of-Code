package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.interfaces.MinimumSTCutAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import pl.mmakos.advent.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day25 {
  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private static int task1() {
    var input = input();

    MinimumSTCutAlgorithm<String, DefaultEdge> min = new EdmondsKarpMFImpl<>(input);
    var vertexes = new ArrayList<>(input.vertexSet());
    // Find 2 vertices which will be in different sub-graphs after cut (min cut will be 3)
    for (int i = 1; min.calculateMinCut(vertexes.get(0), vertexes.get(i)) != 3; ++i);
    return min.getSourcePartition().size() * min.getSinkPartition().size();
  }

  private static UndirectedGraph<String, DefaultEdge> input() {
    UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    Utils.lines()
        .map(s -> s.split(": "))
        .forEach(s -> {
          graph.addVertex(s[0]);
          Arrays.stream(s[1].split(" ")).forEach(str -> {
            graph.addVertex(str);
            graph.addEdge(s[0], str);
          });
        });
    return graph;
  }
}
