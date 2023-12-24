package pl.mmakos.advent.year2023;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day24 {
  private static final BigDecimal br = BigDecimal.valueOf(200_000_000_000_000L);
  private static final BigDecimal tr = BigDecimal.valueOf(400_000_000_000_000L);

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    System.err.println("TASK 1: " + task1());
    System.err.println("TASK 2: " + task2());
  }

  private static int task1() {
    Path[] input = input();

    int sum = 0;
    for (int i = 0; i < input.length; ++i) {
      for (int j = i + 1; j < input.length; ++j) {
        try {
          Point p = intersectionPoint(input[i], input[j]);
          if ((((input[i].vx > 0 && p.x.compareTo(bd(input[i].x)) > 0) ||
              (input[i].vx < 0 && p.x.compareTo(bd(input[i].x)) < 0)) &&
              ((input[j].vx > 0 && p.x.compareTo(bd(input[j].x)) > 0) ||
                  (input[j].vx < 0 && p.x.compareTo(bd(input[j].x)) < 0))) &&
              p.x.compareTo(br) >= 0 && p.x.compareTo(tr) <= 0 &&
              p.y.compareTo(br) >= 0 && p.y.compareTo(tr) <= 0) {

            ++sum;
          }
        } catch (Exception e) {
          // Parallel
        }
      }
    }

    return sum;
  }

  private static int task2() {
    return 0;
  }

  private static Point intersectionPoint(Path p1, Path p2) {
    var divisor = bd(p1.vy * p2.vx - p1.vx * p2.vy);

    var a1 = bi(p1.x).multiply(bi(p1.y + p1.vy));
    var a2 = bi(p1.y).multiply(bi(p1.x + p1.vx));
    var a = a1.subtract(a2);

    var b1 = bi(p2.x).multiply(bi(p2.y + p2.vy));
    var b2 = bi(p2.y).multiply(bi(p2.x + p2.vx));
    var b = b1.subtract(b2);

    var x1 = a.multiply(bi(p2.vx)).subtract(b.multiply(bi(p1.vx)));
    var y2 = a.multiply(bi(p2.vy)).subtract(b.multiply(bi(p1.vy)));

    var x = bd(x1).divide(divisor, RoundingMode.FLOOR);
    var y = bd(y2).divide(divisor, RoundingMode.FLOOR);

    return new Point(x, y);
  }

  private static BigInteger bi(long value) {
    return BigInteger.valueOf(value);
  }

  private static BigDecimal bd(BigInteger value) {
    return new BigDecimal(value).setScale(1, RoundingMode.UNNECESSARY);
  }

  private static BigDecimal bd(long value) {
    return BigDecimal.valueOf(value).setScale(1, RoundingMode.UNNECESSARY);
  }

  private static Path[] input() {
    return Utils.lines()
        .map(Path::parse)
        .toArray(Path[]::new);
  }

  private record Point(BigDecimal x, BigDecimal y) {

  }

  private record Path(long x, long y, long z, long vx, long vy, long vz) {
    private static Path parse(String s) {
      String[] split = s.split(" @ ");
      String[] pos = split[0].split(", ");
      String[] vel = split[1].split(", ");

      return new Path(
          Long.parseLong(pos[0].strip()),
          Long.parseLong(pos[1].strip()),
          Long.parseLong(pos[2].strip()),
          Long.parseLong(vel[0].strip()),
          Long.parseLong(vel[1].strip()),
          Long.parseLong(vel[2].strip())
      );
    }
  }
}
