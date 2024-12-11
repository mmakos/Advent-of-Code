#include <cmath>
#include <iostream>
#include <unordered_map>
#include <vector>

#include <utils.h>

constexpr int day = 11;
constexpr int year = 2024;

struct PairHash {
  std::size_t operator()(const std::pair<uint64_t, int> &p) const {
    return static_cast<uint64_t>(p.second) << 56 ^ p.first;
  }
};

std::pair<uint64_t, uint64_t> splitIfPossible(const uint64_t stone) {
  if (const int digits = static_cast<int>(std::log10(stone)) + 1; digits % 2 == 0) {
    const auto pow = static_cast<int>(std::pow(10, digits / 2));
    uint64_t first = stone / pow;
    return std::make_pair(first, stone - first * pow);
  }
  return std::make_pair(stone, UINT64_MAX);
}

class Solver {
  const int maxDepth;
  std::unordered_map<std::pair<uint64_t, int>, uint64_t, PairHash> cache;

public:
  explicit Solver(const int max_depth)
    : maxDepth(max_depth) {
  }

  uint64_t solve(const uint64_t stone, const int depth = 0) { // NOLINT(*-no-recursion)
    if (depth == maxDepth) return 1;
    const auto pair = std::make_pair(stone, depth);
    if (const auto it = cache.find(pair); it != cache.end()) return it->second;

    const uint64_t result = calcStones(stone, depth + 1);
    cache[pair] = result;
    return result;
  }

private:
  uint64_t calcStones(const uint64_t stone, const int depth) { // NOLINT(*-no-recursion)
    if (stone == 0) return solve(1, depth);
    auto [first, second] = splitIfPossible(stone);

    if (second == UINT64_MAX) return solve(first * 2024, depth);
    return solve(first, depth) + solve(second, depth);
  }
};

uint64_t solve(const std::vector<int> &input, const int depth) {
  uint64_t stones = 0;
  Solver solver(depth);
  for (const auto stone: input) {
    stones += solver.solve(stone);
  }
  return stones;
}

int main() {
  const std::vector<std::vector<int> > input = aoc::readNestedInts(year, day, std::regex("\\s+"));

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve(input[0], 25) << std::endl;
  std::cout << "TASK 2: " << solve(input[0], 75) << std::endl;
}
