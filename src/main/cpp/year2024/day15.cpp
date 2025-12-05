#include <iostream>
#include <point.h>
#include <unordered_set>
#include <vector>

#include <utils.h>

constexpr int day = 15;
constexpr int year = 2024;

constexpr aoc::Point<> dirs[] = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
constexpr aoc::Point<> movX = {1, 0};

class Warehouse {
  std::unordered_set<aoc::Point<>, aoc::PointHash<>> walls;
  std::unordered_set<aoc::Point<>, aoc::PointHash<>> boxes;
  aoc::Point<> robot = {0, 0};
  std::vector<int> movements;
  const bool wide;

public:
  explicit Warehouse(const aoc::input &input, const bool wide = false): wide(wide) {
    int y = 0;
    for (const auto &line: input) {
      if (line.empty()) break;
      for (int i = 0, x = 0; i < line.size(); ++i, x += wide ? 2 : 1) {
        const char c = line[i];
        if (c == '#') {
          walls.insert({x, y});
          if (wide) walls.insert({x + 1, y});
        } else if (c == 'O') boxes.insert({x, y});
        else if (c == '@') robot = {x, y};
      }
      ++y;
    }
    for (auto it = input.begin() + y + 1; it != input.end(); ++it) {
      for (const char c: *it) {
        if (c == '^') movements.push_back(0);
        if (c == '>') movements.push_back(1);
        if (c == 'v') movements.push_back(2);
        if (c == '<') movements.push_back(3);
      }
    }
  }

  int solve() {
    for (int i = 0; i < movements.size(); ++i) {
      wide ? moveWideRobot(i) : moveRobot(i);
    }
    int sum = 0;
    for (const auto &[x, y]: boxes) {
      sum += x + 100 * y;
    }
    return sum;
  }

private:
  void moveRobot(const int i) {
    const auto &dir = dirs[movements[i]];
    const auto newPos = robot + dir;
    if (!walls.contains(newPos) && (!boxes.contains(newPos) || moveBox(newPos, dir))) {
      robot = newPos;
    }
  }

  bool moveBox(const aoc::Point<> &box, const aoc::Point<> &dir) {
    const auto newPos = box + dir;
    if (!walls.contains(newPos) && (!boxes.contains(newPos) || moveBox(newPos, dir))) {
      boxes.erase(box);
      boxes.insert(newPos);
      return true;
    }
    return false;
  }

  // ================= TASK 2 =================

  void moveWideRobot(const int i) {
    const auto dir = movements[i];
    const auto newPos = robot + dirs[dir];
    const auto newPosL = newPos - movX;
    if (walls.contains(newPos)) return;
    bool canMove = false;
    if (dir % 2 == 0) {
      canMove = (!boxes.contains(newPos) || moveBoxesVertical({newPos}, dirs[dir])) &&
                (!boxes.contains(newPosL) || moveBoxesVertical({newPosL}, dirs[dir]));
    } else if (dir == 1) {
      canMove = !boxes.contains(newPos) || moveBoxRight(newPos);
    } else {
      canMove = !boxes.contains(newPosL) || moveBoxLeft(newPosL);
    }
    if (canMove) {
      robot = newPos;
    }
  }

  void moveWideBox(const aoc::Point<> &box, const int dir) {
    if (dir % 2 == 0) moveBoxesVertical({box}, dirs[dir]);
    if (dir == 1) moveBoxRight(box);
    moveBoxLeft(box);
  }

  bool moveBoxRight(const aoc::Point<> &box) {
    const auto newPos = box + movX;
    const auto newPosR = newPos + movX;
    if (!walls.contains(newPosR) && (!boxes.contains(newPosR) || moveBoxRight(newPosR))) {
      boxes.erase(box);
      boxes.insert(newPos);
      return true;
    }
    return false;
  }

  bool moveBoxLeft(const aoc::Point<> &box) {
    const auto newPos = box - movX;
    const auto newPosL = newPos - movX;
    if (!walls.contains(newPos) && (!boxes.contains(newPosL) || moveBoxLeft(newPosL))) {
      boxes.erase(box);
      boxes.insert(newPos);
      return true;
    }
    return false;
  }

  bool moveBoxesVertical(const std::unordered_set<aoc::Point<>, aoc::PointHash<>> &boxes, const aoc::Point<> &dir) {
    std::unordered_set<aoc::Point<>, aoc::PointHash<>> nextBoxes;
    for (const auto &box: boxes) {
      const auto newPos = box + dir;
      const auto newPosR = newPos + movX;
      if (walls.contains(newPos) || walls.contains(newPosR)) return false;
      if (this->boxes.contains(newPos)) nextBoxes.insert(newPos);
      else {
        const auto newPosL = newPos - movX;
        if (this->boxes.contains(newPosR)) nextBoxes.insert(newPosR);
        if (this->boxes.contains(newPosL)) nextBoxes.insert(newPosL);
      }
    }
    if (nextBoxes.empty() || moveBoxesVertical(nextBoxes, dir)) {
      for (const auto &box: boxes) {
        this->boxes.erase(box);
        this->boxes.insert(box + dir);
      }
      return true;
    }
    return false;
  }
};

int solve1(const aoc::input &input) {
  Warehouse warehouse(input);
  return warehouse.solve();
}

int solve2(const aoc::input &input) {
  Warehouse warehouse(input, true);
  return warehouse.solve();
}

int main() {
  const aoc::input input = aoc::readInput<std::string>(year, day);

  std::cout << "Advent of code " << year << ", day " << day << std::endl;
  std::cout << "TASK 1: " << solve1(input) << std::endl;
  std::cout << "TASK 2: " << solve2(input) << std::endl;
}
