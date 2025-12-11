#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 11;
constexpr int year = 2025;

typedef uint32_t Code;

constexpr Code strToCode(const std::string &str) {
  return str[0] - 'a' << 10 | str[1] - 'a' << 5 | str[2] - 'a';
}

constexpr Code you = strToCode("you");
constexpr Code out = strToCode("out");
constexpr Code svr = strToCode("svr");
constexpr Code dac = strToCode("dac");
constexpr Code fft = strToCode("fft");

std::unordered_map<Code, std::vector<Code>> parseInput(const aoc::input &input) {
  std::unordered_map<Code, std::vector<Code>> connections;
  for (const auto &line : input) {
    const auto from = line.substr(0, 3);
    for (const auto &to : aoc::split(line.substr(5), std::regex(" "))) {
      connections[strToCode(from)].push_back(strToCode(to));
    }
  }
  return connections;
}

struct CodeKey {
  Code code;
  bool dac;
  bool fft;
};

uint64_t solveBFS(const std::unordered_map<Code, std::vector<Code>> &connections, const Code &from,
             const std::unordered_set<Code> &visited, const bool part2, std::unordered_map<Code, uint64_t> &cache) {
  const auto cached = cache.find(from);
  if (cache.end() != cached) return cached->second;
  if ((from & 0xffff) == out) {
    return !part2 || (from & 0xffff0000) == 0b11 << 30 ? 1 : 0;
  }
  const auto it = connections.find(from & 0xffff);
  if (it == connections.end()) return 0;

  uint64_t result = 0;
  for (const auto start : it->second) {
    if (!visited.contains(start)) {
      auto newVisited = visited;
      newVisited.insert(start);
      auto startMod = start | from & 0xffff0000;
      if (start == dac) startMod |= 1 << 31;
      else if (start == fft) startMod |= 1 << 30;
      result += solveBFS(connections, startMod, newVisited, part2, cache);
    }
  }
  cache[from] = result;
  return result;
}

int solve1(const std::unordered_map<Code, std::vector<Code>> &connections) {
  std::unordered_map<Code, uint64_t> cache;
  return solveBFS(connections, you, {}, false, cache);
}

uint64_t solve2(const std::unordered_map<Code, std::vector<Code>> &connections) {
  std::unordered_map<Code, uint64_t> cache;
  return solveBFS(connections, svr, {}, true, cache);
}

int main() {
  const std::unordered_map<Code, std::vector<Code>> &connections = parseInput(aoc::readInput<std::string>(year, day));

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(connections) << std::endl;
  std::cout << "TASK 2: " << solve2(connections) << std::endl;
}
