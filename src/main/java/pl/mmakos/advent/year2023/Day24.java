package pl.mmakos.advent.year2023;

import com.microsoft.z3.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.mmakos.advent.utils.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@SuppressWarnings("java:S106")
@NoArgsConstructor(access = AccessLevel.NONE)
public final class Day24 {
  private static final BigDecimal br = BigDecimal.valueOf(200_000_000_000_000L);
  private static final BigDecimal tr = BigDecimal.valueOf(400_000_000_000_000L);

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

  // For part 2 you need to get Z3 library. If you are using my build.gradle, then it should automatically download it.
  @SuppressWarnings("resource")
  private static long task2() {
    Path[] input = input();

    // We have 6 + n variables, and each path (hailstone, n) generates 3 equations
    // So we need to consider 3 (non-collinear hailstones), because it gives us 9 equations and 9 variables
    // Equations for n point:
    // x + t0 * vx = nx + t0 * nvx
    // y + t1 * vy = ny + t1 * nvy
    // z + t2 * vz = nz + t2 * nvz
    Context ctx = new Context();
    Solver solver = ctx.mkSolver();

    IntExpr x = ctx.mkIntConst("x");
    IntExpr y = ctx.mkIntConst("y");
    IntExpr z = ctx.mkIntConst("z");
    IntExpr vx = ctx.mkIntConst("vx");
    IntExpr vy = ctx.mkIntConst("vy");
    IntExpr vz = ctx.mkIntConst("vz");

    for (int i = 0; i < 3; ++i) {
      Path path = input[i];
      IntExpr t = ctx.mkIntConst("t" + i);

      System.err.printf("x + t%d * vx = %d + t%d * %d,%n", i, path.x, i, path.vx);
      BoolExpr eqx = ctx.mkEq(ctx.mkAdd(x, ctx.mkMul(t, vx)),
          ctx.mkAdd(ctx.mkInt(path.x), ctx.mkMul(t, ctx.mkInt(path.vx))));

      System.err.printf("y + t%d * vy = %d + t%d * %d,%n", i, path.y, i, path.vy);
      BoolExpr eqy = ctx.mkEq(ctx.mkAdd(y, ctx.mkMul(t, vy)),
          ctx.mkAdd(ctx.mkInt(path.y), ctx.mkMul(t, ctx.mkInt(path.vy))));

      System.err.printf("z + t%d * vz = %d + t%d * %d,%n", i, path.z, i, path.vz);
      BoolExpr eqz = ctx.mkEq(ctx.mkAdd(z, ctx.mkMul(t, vz)),
          ctx.mkAdd(ctx.mkInt(path.z), ctx.mkMul(t, ctx.mkInt(path.vz))));

      solver.add(eqx);
      solver.add(eqy);
      solver.add(eqz);
    }

    solver.check();
    Model model = solver.getModel();
    long sx = ((IntNum) model.eval(x, false)).getInt64();
    long sy = ((IntNum) model.eval(y, false)).getInt64();
    long sz = ((IntNum) model.eval(z, false)).getInt64();

    return sx + sy + sz;
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
