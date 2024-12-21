#include <iostream>
#include <point.h>
#include <unordered_map>
#include <vector>

#include <utils.h>

constexpr int day = 21;
constexpr int year = 2024;

const std::unordered_map<char, aoc::Point> numericKeypad = {
  {'7', {0, 3}}, {'8', {1, 3}}, {'9', {2, 3}},
  {'4', {0, 2}}, {'5', {1, 2}}, {'6', {2, 2}},
  {'1', {0, 1}}, {'2', {1, 1}}, {'3', {2, 1}},
  {'0', {1, 0}}, {'A', {2, 0}}
};

const std::unordered_map<char, aoc::Point> directionalKeypad = {
  {'^', {1, 1}}, {'A', {2, 1}},
  {'<', {0, 0}}, {'v', {1, 0}}, {'>', {2, 0}},
};

struct StringIntHash {
  std::size_t operator()(const std::pair<std::string, int> &stringInt) const {
    constexpr std::hash<std::string> strHasher;
    constexpr std::hash<int> intHasher;
    return strHasher(stringInt.first) ^ intHasher(stringInt.second) << 1;
  }
};

std::unordered_map<std::pair<std::string, int>, uint64_t, StringIntHash> cache;

std::string typeNumeric(const std::string &code) {
  std::stringstream ss;
  aoc::Point position = numericKeypad.at('A');
  for (const char c: code) {
    const aoc::Point target = numericKeypad.at(c);
    const auto [dx, dy] = target - position;
    // We prefer x movement first if it's left movement (and we don't enter empty space)
    // or it is right movement, but we enter empty space if we go down at first
    if ((dx < 0 && !(target.x == 0 && position.y == 0)) || (dy < 0 && position.x == 0 && target.y == 0)) {
      ss << std::string(std::abs(dx), dx > 0 ? '>' : '<') << std::string(std::abs(dy), dy > 0 ? '^' : 'v') << 'A';
    } else {
      ss << std::string(std::abs(dy), dy > 0 ? '^' : 'v') << std::string(std::abs(dx), dx > 0 ? '>' : '<') << 'A';
    }
    position = target;
  }
  return ss.str();
}

std::string typeSingleDirectional(const aoc::Point &target, const aoc::Point &position) {
  const auto [dx, dy] = target - position;
  // We prefer x movement first if it's left movement (and we don't enter empty space)
  // or it is right movement, but we enter empty space if we go down at first
  if ((dx < 0 && !(target.x == 0 && position.y == 1)) || (dy > 0 && position.x == 0 && target.y == 1)) {
    return std::string(std::abs(dx), dx > 0 ? '>' : '<') + std::string(std::abs(dy), dy > 0 ? '^' : 'v') + 'A';
  }
  return std::string(std::abs(dy), dy > 0 ? '^' : 'v') + std::string(std::abs(dx), dx > 0 ? '>' : '<') + 'A';
}

uint64_t typeDirectional(const std::string &code, const int depth) {
  if (depth == 0) return code.size();
  const auto cached = cache.find({code, depth});
  if (cached != cache.end()) return cached->second;
  uint64_t sum = 0;
  aoc::Point position = directionalKeypad.at('A');
  for (const char c: code) {
    const aoc::Point target = directionalKeypad.at(c);
    sum += typeDirectional(typeSingleDirectional(target, position), depth - 1);
    position = target;
  }
  cache[{code, depth}] = sum;
  return sum;
}

uint64_t solve1(const aoc::input &input) {
  uint64_t sum = 0;
  for (const auto &line: input) {
    const int value = std::stoi(line.substr(0, line.size() - 1));
    sum += typeDirectional(typeNumeric(line), 2) * value;
  }
  return sum;
}

uint64_t solve2(const aoc::input &input) {
  uint64_t sum = 0;
  for (const auto &line: input) {
    const int value = std::stoi(line.substr(0, line.size() - 1));
    sum += typeDirectional(typeNumeric(line), 25) * value;
  }
  return sum;
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
