#include <iostream>
#include <ranges>
#include <unordered_map>
#include <vector>

#include <utils.h>

constexpr int day = 24;
constexpr int year = 2024;

struct Connection {
  const std::string w1;
  const std::string w2;
  const std::string gate;
};

using Connections = std::unordered_map<std::string, Connection, aoc::StringHash, std::equal_to<>>;
using Wires = std::unordered_map<std::string, bool, aoc::StringHash, std::equal_to<>>;

std::pair<Wires, Connections> parseInput(const aoc::input &input) {
  Wires wires;
  Connections connections;
  auto it = input.begin();
  for (; !it->empty(); ++it) {
    const auto &str = *it;
    wires[str.substr(0, 3)] = '0' - str[5];
  }
  ++it;
  for (; it != input.end(); ++it) {
    const auto &str = *it;
    const auto split = aoc::split(str, std::regex(" "));
    connections.try_emplace(split[4], split[0], split[2], split[1]);
  }

  return {wires, connections};
}

bool evaluate(const std::string &wire, Wires &wires, const Connections &connections);

bool evaluateGate(const std::string_view &gate, const bool w1, const bool w2) {
  if (gate == "OR") return w1 || w2;
  if (gate == "XOR") return w1 != w2;
  if (gate == "AND") return w1 && w2;
  throw std::exception();
}

bool evaluateWire(const std::string &wire, Wires &wires, const Connections &connections) {
  const auto w = wires.find(wire);
  if (w == wires.end()) {
    return evaluate(wire, wires, connections);
  }
  return w->second;
}

bool evaluate(const std::string &wire, Wires &wires, const Connections &connections) {
  const auto &[w1, w2, gate] = connections.at(wire);
  const auto wire1 = evaluateWire(w1, wires, connections);
  const auto wire2 = evaluateWire(w2, wires, connections);
  const bool value = evaluateGate(gate, wire1, wire2);
  wires.try_emplace(wire, value);
  return value;
}

uint64_t solve1(Wires wires, const Connections &connections) {
  uint64_t result = 0;
  for (int i = 45; i >= 0; --i) {
    result = result << 1 | evaluate(std::format("z{:02}", i), wires, connections);
  }

  return result;
}

bool isEndWire(const std::string_view &wire) {
  return wire[0] >= 'x' && wire[0] <= 'z';
}

std::string solve2(const Connections &connections) {
  std::vector<std::string> result;
  for (const auto &[out, connection]: connections) {
    if (connection.gate == "XOR") {
      if (!isEndWire(out) && !isEndWire(connection.w1) && !isEndWire(connection.w2)) {
        result.push_back(out);
      } else {
        for (const auto &[w1, w2, gate]: std::views::values(connections)) {
          if (gate == "OR" && (out == w1 || out == w2)) {
            result.push_back(out);
          }
        }
      }
    } else if (out[0] == 'z' && out != "z45") {
      result.push_back(out);
    } else if (connection.gate == "AND" && connection.w1 != "x00" && connection.w2 != "x00") {
      for (const auto &[w1, w2, gate]: std::views::values(connections)) {
        if (gate != "OR" && (out == w1 || out == w2)) {
          result.push_back(out);
        }
      }
    }
  }

  std::ranges::sort(result);
  result.erase(std::ranges::unique(result).begin(), result.end());

  return aoc::join(result, ",");
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);
  const auto [wires, connections] = parseInput(input);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(wires, connections) << std::endl;
  std::cout << "TASK 2:" << solve2(connections) << std::endl;
}
