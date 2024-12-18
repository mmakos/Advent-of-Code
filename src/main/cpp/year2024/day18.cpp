#include <iostream>
#include <point.h>
#include <queue>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 18;
constexpr int year = 2024;
constexpr aoc::Point start{0, 0};
constexpr aoc::Point end{70, 70};
constexpr std::array<aoc::Point, 4> dirs = {{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}};

void out(const std::unordered_set<aoc::Point, aoc::PointHash> &set) {
  for (int y = 0; y <= 6; ++y) {
    for (int x = 0; x <= 6; ++x) {
      std::cout << (set.contains({x, y}) ? 'O' : '.');
    }
    std::cout << std::endl;
  }
}

std::unordered_set<aoc::Point, aoc::PointHash> parseInput(const std::vector<std::vector<int>> &input) {
  std::unordered_set<aoc::Point, aoc::PointHash> bytes;
  for (const auto &p: input) {
    bytes.insert({p[0], p[1]});
  }
  return bytes;
}

int bfs(const std::vector<aoc::Point> &allBytes, const int limit) {
  const std::unordered_set<aoc::Point, aoc::PointHash> bytes(allBytes.begin(), allBytes.begin() + limit);
  std::unordered_set<aoc::Point, aoc::PointHash> visited;
  std::queue<std::pair<aoc::Point, int>> queue;
  queue.push({{0, 0}, 0});

  while (!queue.empty()) {
    const auto [point, depth] = queue.front();
    queue.pop();
    if (point == end) return depth;
    for (const auto &dir: dirs) {
      const auto next = point + dir;
      if (next <= end && next >= start && !bytes.contains(next) && !visited.contains(next)) {
        queue.emplace(next, depth + 1);
        visited.insert(next);
      }
    }
  }
  return -1;
}

int solve1(const std::vector<aoc::Point> &bytes) {
  return bfs(bytes, 1024);
}

aoc::Point solve2(const std::vector<aoc::Point> &bytes) {
  auto limit = static_cast<int>(bytes.size() / 2);
  double multiplier = limit / 2.;
  while (true) {
    if (bfs(bytes, limit) == -1) {
      if (multiplier < 0.5) return bytes[limit - 1];
      limit -= static_cast<int>(multiplier + 1);
    }
    else limit += static_cast<int>(multiplier + 1);
    multiplier /= 2;
  }
}

int main() {
  const std::vector<aoc::Point> input = aoc::readInput<aoc::Point>(year, day, [](const auto &s) {
    const auto split = aoc::split(s, std::regex(","));
    return aoc::Point{std::stoi(split[0]), std::stoi(split[1])};
  });

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  const auto [x, y] = solve2(input);
  std::cout << "TASK 2: " << x << ',' << y << std::endl;
}
