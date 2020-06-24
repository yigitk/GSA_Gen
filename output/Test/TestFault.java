import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Test {
  static HashMap<String, String> __versionMap__ = new HashMap<>();

  public static void record(
      String packageName,
      String clazz,
      String method,
      int line,
      int staticScope,
      String variableName,
      Object value,
      int version) {
    __versionMap__.putIfAbsent(
        variableName + "_" + version,
        clazz
            + ","
            + method
            + ","
            + line
            + ","
            + staticScope
            + ","
            + variableName
            + ","
            + version
            + ","
            + value
            + "\n");
    __versionMap__.put(
        variableName + "_" + version,
        clazz
            + ","
            + method
            + ","
            + line
            + ","
            + staticScope
            + ","
            + variableName
            + ","
            + version
            + ","
            + value
            + "\n");
  }

  public static void writeOutVariables() {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter("output.txt", true));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    try {
      for (String variableVersion : __versionMap__.keySet()) {
        writer.append(__versionMap__.get(variableVersion));
      }
      writer.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public BigFraction add(final BigFraction fraction) {
    int num_version = -1;
    int den_version = -1;
    int fraction_version = 0;
    record("", "Test", "add", 2, 0, "fraction", fraction, fraction_version);
    if (fraction == null) {
      {
        throw new NullArgumentException(LocalizedFormats.FRACTION);
      }
    }
    if (ZERO.equals(fraction)) {
      {
        return this;
      }
    }

    BigInteger num = null;
    num_version = 0;
    record("", "Test", "add", 10, 1, "num", num, num_version);
    BigInteger den = null;
    den_version = 0;
    record("", "Test", "add", 11, 1, "den", den, den_version);

    if (denominator.equals(fraction.denominator)) {
      {
        num = numerator.add(fraction.numerator);
        num_version = 1;
        record("", "Test", "add", 14, 2, "num", num, num_version);
        den = denominator;
        den_version = 1;
        record("", "Test", "add", 15, 2, "den", den, den_version);
      }
    } else {
      {
        num =
            (numerator.multiply(fraction.denominator))
                .add((fraction.numerator).multiply(denominator));
        num_version = 2;
        record("", "Test", "add", 17, 2, "num", num, num_version);
        den = denominator.multiply(fraction.denominator);
        den_version = 2;
        record("", "Test", "add", 18, 2, "den", den, den_version);
      }
    }
    num_version = 3;
    record("", "Test", "add", 19, 1, "num", num, num_version);
    den_version = 3;
    record("", "Test", "add", 19, 1, "den", den, den_version);
    return new BigFraction(num, den);
  }
}
