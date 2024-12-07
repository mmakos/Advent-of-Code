#include <iostream>
#include <vector>

#include <utils.h>

constexpr int day = 5;
constexpr int year = 2024;

class Rules {
private:
  std::map<int, std::vector<int>> rulesMap; // number y has to be after numbers vector<x>

public:
  explicit Rules(const aoc::input &input) {
    std::map<int, std::vector<int>> map;
    for (const auto &line: input) {
      auto split = aoc::split(line, std::regex("\\|"));
      if (split.size() != 2) break;
      int x = stoi(split[0]);
      int y = stoi(split[1]);
      map[y].push_back(x);
    }

    this->rulesMap = map;
  }

  bool isNumberInValidOrder(const std::vector<int> &pages, int pageIdx) {
    int page = pages[pageIdx];
    auto rulesIt = rulesMap.find(page);
    if (rulesIt == rulesMap.end()) return true;
    auto rules = rulesIt->second;

    for (int i = pageIdx + 1; i < pages.size(); ++i) {
      if (aoc::contains(rules, pages[i])) {
        return false;
      }
    }
    return true;
  }

  std::vector<int> correctOrder(const std::vector<int> &pages) {
    std::vector<int> result = pages;
    for (auto page: pages) {
      auto rulesIt = rulesMap.find(page);
      if (rulesIt == rulesMap.end()) continue;
      auto rules = rulesIt->second;
      int i = aoc::indexOf(result, page);

      int moveToIdx = i;
      for (auto rule: rules) {
        auto j = aoc::indexOf(result, rule);
        moveToIdx = std::max(moveToIdx, j);
      }
      if (moveToIdx > i) {
        std::rotate(result.begin() + i, result.begin() + i + 1, result.begin() + moveToIdx + 1);
      }
    }
    return result;
  }
};

std::pair<Rules, std::vector<std::vector<int>>> parseInput(const aoc::input &input) {
  Rules rules(input);

  std::vector<std::vector<int>> pages;
  auto it = input.begin();
  for (; !it->empty(); ++it);
  ++it;
  for (; it != input.end(); ++it) {
    auto split = aoc::split(*it, std::regex(","));
    std::vector<int> ints;
    for (auto &page: split) {
      ints.push_back(stoi(page));
    }
    pages.push_back(ints);
  }
  return std::make_pair(rules, pages);
}

std::pair<int, int> solve12(const aoc::input &input) {
  auto [rules, pages] = parseInput(input);
  int sum1 = 0, sum2 = 0;
  for (auto &pgs: pages) {
    int i = 0;
    for (; i < pgs.size(); ++i) {
      if (!rules.isNumberInValidOrder(pgs, i)) {
        break;
      }
    }
    if (i == pgs.size()) {
      sum1 += pgs[pgs.size() / 2];
    } else {
      auto corrected = rules.correctOrder(pgs);
      sum2 += corrected[corrected.size() / 2];
    }
  }
  return std::make_pair(sum1, sum2);
}


int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  auto [solution1, solution2] = solve12(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solution1 << std::endl;
  std::cout << "TASK 2: " << solution2 << std::endl;
}
