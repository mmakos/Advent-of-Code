#include <iostream>
#include <point.h>
#include <queue>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 16;
constexpr int year = 2024;

constexpr std::array<aoc::Point, 4> dirs = {{{0, -1}, {1, 0}, {0, 1}, {-1, 0}}};

class Maze {
  struct Node {
    aoc::Point point;
    int direction;
    int cost;
    std::vector<aoc::Point> path;
  };

  std::unordered_set<aoc::Point, aoc::PointHash> walls;
  aoc::Point end = {0, 0};

public:
  aoc::Point start = {0, 0};

  explicit Maze(const aoc::input &input) {
    for (int y = 0; y < input.size(); ++y) {
      const auto &line = input[y];
      if (line.empty()) break;
      for (int x = 0; x < line.size(); ++x) {
        const char c = line[x];
        if (c == '#') {
          walls.insert({x, y});
        } else if (c == 'S') start = {x, y};
        else if (c == 'E') end = {x, y};
      }
    }
  }

  std::pair<int, int> dijkstra() const {
    std::unordered_map<std::pair<aoc::Point, int>, int, PointHash> visited;
    std::unordered_set<aoc::Point, aoc::PointHash> bestPaths;
    std::priority_queue<Node, std::vector<Node>, CompareNode> queue;
    int minCost = INT32_MAX;
    queue.push({start, 1, 0, {start}});

    while (!queue.empty()) {
      auto [point, dir, cost, path] = queue.top();
      queue.pop();
      if (cost > minCost) break;

      if (point == end) {
        if (cost < minCost) bestPaths.clear();
        minCost = cost;
        bestPaths.insert(path.begin(), path.end());
      }
      visited[{point, dir}] = cost;

      for (int i = dir - 1; i <= dir + 1; ++i) {
        const auto d = (i + 4) % 4;
        const auto p = point + dirs[d];
        if (!walls.contains(p)) {
          const int c = cost + (dir == i ? 1 : 1001);
          auto v = visited.find({p, d});
          if (v == visited.end() || v->second > c) {
            auto pth = path;
            pth.push_back(p);
            queue.emplace(p, d, c, pth);
          }
        }
      }
    }
    return {minCost, bestPaths.size()};
  }

private:
  struct CompareNode {
    bool operator()(const Node &n1, const Node &n2) const {
      return n1.cost > n2.cost;
    }
  };

  struct PointHash {
    std::size_t operator()(const std::pair<aoc::Point, int> &point) const {
      return hash_value(point.first) & static_cast<uint64_t>(point.second) << 62;
    }
  };
};

std::pair<int, int> solve(const aoc::input &input) {
  Maze maze(input);
  const auto [cost, points] = maze.dijkstra();

  return {cost, points};
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
