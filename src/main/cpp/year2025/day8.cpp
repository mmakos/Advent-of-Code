#include <complex>
#include <iostream>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 8;
constexpr int year = 2025;
constexpr int limit = 10;

namespace aoc {
  typedef std::tuple<int, int, int> Point3D;
  typedef std::tuple<Point3D, Point3D, uint64_t> PointPair;

  struct Point3DHash {
    std::size_t operator()(const Point3D &p) const noexcept {
      const size_t h1 = std::hash<int>{}(std::get<0>(p));
      const size_t h2 = std::hash<int>{}(std::get<1>(p));
      const size_t h3 = std::hash<int>{}(std::get<2>(p));
      return h1 ^ h2 << 1 ^ h3 << 2;
    }
  };

  PointPair makePointPair(const Point3D &first, const Point3D &second) {
    return {
      first, second,
      std::pow(std::get<0>(second) - std::get<0>(first), 2) +
      std::pow(std::get<1>(second) - std::get<1>(first), 2) +
      std::pow(std::get<2>(second) - std::get<2>(first), 2)
    };
  }
}

typedef std::unordered_set<aoc::Point3D, aoc::Point3DHash> Circuit;

std::vector<aoc::Point3D> parseInput(const aoc::input &input) {
  std::vector<aoc::Point3D> result;
  for (const auto &line : input) {
    const auto &split = aoc::split(line, std::regex(","));
    result.emplace_back(std::stoi(split[0]), std::stoi(split[1]), std::stoi(split[2]));
  }
  return result;
}

std::pair<uint64_t, uint64_t> solve(const aoc::input &input) {
  uint64_t firstResult;

  const auto &boxes = parseInput(input);
  std::vector<aoc::PointPair> pairs;
  std::vector<Circuit> circuits;

  for (int i = 0; i < boxes.size(); ++i) {
    for (int j = i + 1; j < boxes.size(); ++j) {
      pairs.push_back(aoc::makePointPair(boxes[i], boxes[j]));
    }
  }
  std::ranges::sort(pairs, [](const aoc::PointPair &first, const aoc::PointPair &second) {
    return std::get<2>(first) < std::get<2>(second);
  });

  int i = -1;
  while (circuits.size() != 1 || circuits[0].size() < boxes.size()) {
    const auto &pair = pairs[++i];
    std::vector<int> modifiedCircuits;
    for (int j = 0; j < circuits.size(); ++j) {
      auto &circuit = circuits[j];
      if (circuit.contains(std::get<0>(pair)) || circuit.contains(std::get<1>(pair))) {
        circuit.insert(std::get<0>(pair));
        circuit.insert(std::get<1>(pair));
        modifiedCircuits.push_back(j);
      }
    }
    if (modifiedCircuits.empty()) {
      circuits.push_back({std::get<0>(pair), std::get<1>(pair)});
    }
    else {
      for (int j = static_cast<int>(modifiedCircuits.size()) - 1; j > 0; --j) {
        auto &circuit = circuits[modifiedCircuits[j]];
        circuits[modifiedCircuits[0]].merge(circuit);
        circuits.erase(circuits.begin() + modifiedCircuits[j]);
      }
    }

    if (i == limit - 1) {
      std::ranges::sort(circuits, [](const Circuit &first, const Circuit &second) { return first.size() > second.size(); });
      firstResult = circuits[0].size() * circuits[1].size() * circuits[2].size();
    }
  }
  const auto &[point1, point2, distance] = pairs[i];

  return {firstResult, static_cast<uint64_t>(std::get<0>(point1)) * std::get<0>(point2)};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto [result1, result2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << result1 << std::endl;
  std::cout << "TASK 2: " << result2 << std::endl;
}
