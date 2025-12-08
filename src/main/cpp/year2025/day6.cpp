#include <iostream>
#include <ranges>
#include <vector>

#include <utils.h>

constexpr int day = 6;
constexpr int year = 2025;

std::vector<std::pair<std::vector<std::string>, std::string>> parseInput(const aoc::input &input) {
  const auto &operatorsLine = input[input.size() - 1];
  const std::regex r("[*+] *");
  std::sregex_iterator it(operatorsLine.begin(), operatorsLine.end(), r);
  const std::sregex_iterator end;

  std::vector<std::pair<std::vector<std::string>, std::string>> result;
  for (; it != end; ++it) {
    result.emplace_back(std::vector<std::string>(), it->str());
  }

  for (int i = 0; i < input.size() - 1; ++i) {
    uint64_t pos = 0;
    for (int j = 0; j < result.size(); ++j) {
      const auto len = result[j].second.size();
      const auto n = j == result.size() - 1 ? 100 : len - 1;
      result[j].first.push_back(input[i].substr(pos, n));
      pos += len;
    }
  }

  return result;
}

uint64_t countProblem(const std::vector<std::string> &values, const std::string &oper) {
  const bool mul = oper.starts_with("*");
  uint64_t result = mul ? 1 : 0;
  for (const auto &value : values) {
    if (mul) result *= stoull(value);
    else result += stoull(value);
  }
  return result;
}

uint64_t countProblem2(const std::vector<std::string> &values, const std::string &oper) {
  const bool mul = oper.starts_with("*");
  uint64_t result = mul ? 1 : 0;
  for (int i = static_cast<int>(values[0].length()) - 1; i >= 0; --i) {
    std::string problem;
    for (const auto &value : values) {
      problem += value[i];
    }
    if (mul) result *= stoull(problem);
    else result += stoull(problem);
  }

  return result;
}

uint64_t solve(const aoc::input &input, std::function<uint64_t(const std::vector<std::string> &, const std::string &)> counter) {
  const auto &problems = parseInput(input);
  uint64_t result = 0;
  std::cout << problems[problems.size() - 1].first[0] << std::endl;
  for (const auto &[values, oper] : problems) {
    result += counter(values, oper);
  }
  return result;
}

uint64_t solve1(const aoc::input &input) {
  return solve(input, countProblem);
}

uint64_t solve2(const aoc::input &input) {
  return solve(input, countProblem2);
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
