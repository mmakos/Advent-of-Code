#include <chrono>
#include <iostream>
#include <vector>
#include <cmath>

#include <utils.h>

constexpr int day = 7;
constexpr int year = 2024;

uint64_t stripEnd(const uint64_t number, const int end) {
  const int a = static_cast<int>(std::pow(10, static_cast<int>(std::log10(end)) + 1));
  if (number % a == end) return number / a;
  return 0;
}

bool canSolve(const std::vector<uint64_t>::const_reverse_iterator &it,
              const std::vector<uint64_t>::const_reverse_iterator &end,
              const uint64_t result, const bool task2) {
  const int number = static_cast<int>(*it);
  if (it == end) return result == number;
  if (result % number == 0 && canSolve(it + 1, end, result / number, task2)) return true;
  const uint64_t stripped = stripEnd(result, number);
  if (task2 && stripped > 0 && canSolve(it + 1, end, stripped, task2)) return true;
  return canSolve(it + 1, end, result - number, task2);
}

std::pair<uint64_t, uint64_t> solve(const aoc::input &input) {
  uint64_t task1 = 0, task2 = 0;
  for (auto &line: input) {
    std::vector<uint64_t> numbers = aoc::findAll<uint64_t>(line, std::regex(R"(\d+)"), [](auto &str) {
      return static_cast<uint64_t>(std::stoull(str));
    });
    if (canSolve(numbers.rbegin(), numbers.rend() - 2, numbers[0], false)) {
      task1 += numbers[0];
    } else if (canSolve(numbers.rbegin(), numbers.rend() - 2, numbers[0], true)) {
      task2 += numbers[0];
    }
  }

  return std::make_pair(task1, task1 + task2);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  auto [solution1, solution2] = solve(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
