#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 3;
constexpr int year = 2024;

const std::string mul = R"(mul\((\d{1,3}),(\d{1,3})\))";

long long solve(const aoc::input &input, bool task2=false) {
  std::string inputStr = aoc::join(input, "\n");
  std::regex regex( task2 ? mul + R"(|do\(\)|don't\(\))" : mul);

  auto endIt = std::sregex_iterator();
  long long sum = 0;
  bool enabled = true;
  for (auto i = std::sregex_iterator(inputStr.begin(), inputStr.end(), regex); i != endIt; ++i) {
    const std::smatch &match = *i;
    if (match[0] == "don't()") {
      enabled = false;
    } else if (match[0] == "do()") {
      enabled = true;
    } else if (enabled) {
      sum += stoi(match[1]) * stoi(match[2]);
    }
  }

  return sum;
}

long long solve1(const aoc::input &input) {
  return solve(input);
}

long long solve2(const aoc::input &input) {
  return solve(input, true);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
