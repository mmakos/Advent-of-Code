#include <iostream>
#include <point.h>
#include <unordered_map>
#include <unordered_set>

#include <utils.h>

constexpr int day = 4;
constexpr int year = 2015;

const std::unordered_map<char, aoc::Point<>> dirs = {{'^', {0, 1}}, {'>', {1, 0}}, {'v', {0, -1}}, {'<', {-1, 0}}};

std::pair<int, int> solve(const std::string_view &input) {

}

int main() {
  const std::string input = aoc::readInput<std::string>(year, day)[0];
  const auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
