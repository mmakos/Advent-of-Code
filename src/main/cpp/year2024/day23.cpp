#include <iostream>
#include <ranges>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 23;
constexpr int year = 2024;

std::string toTriple(const std::string &c1, const std::string &c2, const std::string &c3) {
  std::vector v = {c1, c2, c3};
  std::ranges::sort(v);
  return aoc::join(v, "");
}

std::unordered_set<std::string> intersect(const std::unordered_set<std::string> &first,
                                          const std::unordered_set<std::string> &second) {
  const auto smaller1 = first.size() < second.size();
  const auto smaller = smaller1 ? first : second;
  const auto bigger = smaller1 ? second : first;
  std::unordered_set<std::string> intersection;
  for (const auto &v: smaller) {
    if (bigger.contains(v)) intersection.insert(v);
  }
  return intersection;
}

std::unordered_map<std::string, std::unordered_set<std::string>> parseInput(const aoc::input &input) {
  std::unordered_map<std::string, std::unordered_set<std::string>> computers;
  for (const auto &line: input) {
    const auto first = line.substr(0, 2);
    const auto second = line.substr(3, 2);
    computers[first].insert(second);
    computers[second].insert(first);
  }

  return computers;
}

int solve1(const std::unordered_map<std::string, std::unordered_set<std::string>> &computers) {
  std::unordered_set<std::string> triplets;
  for (const auto &[c1, comps]: computers) {
    if (!c1.starts_with('t')) continue;
    const std::vector connected(comps.begin(), comps.end());
    for (int i = 0; i < connected.size(); ++i) {
      const auto c2 = connected[i];
      for (int j = i + 1; j < connected.size(); ++j) {
        const auto c3 = connected[j];
        if (computers.at(c2).contains(c3)) {
          triplets.insert(toTriple(c1, c2, c3));
        }
      }
    }
  }

  return triplets.size();
}

std::unordered_set<std::string> findLargestNetwork(
  const std::unordered_map<std::string, std::unordered_set<std::string>> &computers,
  std::unordered_set<std::string> &comps, std::unordered_set<std::string> &network) {

  if (comps.empty()) return network;
  std::unordered_set largestNetwork = network;

  auto it = comps.begin();
  while (it != comps.end()) {
    const auto computer = *it;
    network.insert(computer);
    auto neighbours = computers.at(computer);
    std::unordered_set<std::string> intersection = intersect(comps, neighbours);

    auto nextLargestNetwork = findLargestNetwork(computers, intersection, network);
    if (nextLargestNetwork.size() > largestNetwork.size()) largestNetwork = nextLargestNetwork;

    network.erase(computer);
    it = comps.erase(it);
  }

  return largestNetwork;
}

std::string solve2(const std::unordered_map<std::string, std::unordered_set<std::string>> &computers) {
  std::unordered_set<std::string> s;

  auto allComputers = std::unordered_set(std::views::keys(computers).begin(), std::views::keys(computers).end());
  auto largestNetwork = findLargestNetwork(computers, allComputers, s);

  std::vector result(largestNetwork.begin(), largestNetwork.end());
  std::ranges::sort(result);
  return aoc::join(result, ",");
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto computers = parseInput(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(computers) << std::endl;
  std::cout << "TASK 2: " << solve2(computers) << std::endl;
}
