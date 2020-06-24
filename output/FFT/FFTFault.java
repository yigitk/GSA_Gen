/**
 * Computes FFT's of complex, double precision data where n is an integer power of 2. This appears
 * to be slower than the Radix2 method, but the code is smaller and simpler, and it requires no
 * extra storage.
 *
 * <p>
 *
 * @author Bruce R. Miller bruce.miller@nist.gov,
 * @author Derived from GSL (Gnu Scientific Library),
 * @author GSL's FFT Code by Brian Gough bjg@vvv.lanl.gov
 */

/* See {@link ComplexDoubleFFT ComplexDoubleFFT} for details of data layout.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FFT {
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

  public static final double num_flops(int N) {
    int Nd_version = -1;
    int logN_version = -1;
    int N_version = 0;
    record("", "FFT", "num_flops", 18, 0, "N", N, N_version);
    double Nd = (double) N;
    Nd_version = 0;
    record("", "FFT", "num_flops", 19, 1, "Nd", Nd, Nd_version);
    double logN = (double) log2(N);
    logN_version = 0;
    record("", "FFT", "num_flops", 20, 1, "logN", logN, logN_version);

    return (5.0 * Nd - 2) * logN + 2 * (Nd + 1);
  }

  /** Compute Fast Fourier Transform of (complex) data, in place. */
  public static void transform(double data[]) {
    transform_internal(data, -1);
  }

  /** Compute Inverse Fast Fourier Transform of (complex) data, in place. */
  public static void inverse(double data[]) {
    int nd_version = -1;
    int i_version = -1;
    int n_version = -1;
    int norm_version = -1;
    transform_internal(data, +1);
    // Normalize
    int nd = data.length;
    nd_version = 0;
    record("", "FFT", "inverse", 38, 1, "nd", nd, nd_version);
    int n = nd / 2;
    n_version = 0;
    record("", "FFT", "inverse", 39, 1, "n", n, n_version);
    double norm = 1 / ((double) n);
    norm_version = 0;
    record("", "FFT", "inverse", 40, 1, "norm", norm, norm_version);
    {
      int i = 0;
      i_version = 0;
      record("", "FFT", "inverse", 41, 1, "i", i, i_version);
      while (true) {
        i_version = 1;
        record("", "FFT", "inverse", 41, 1, "i", i, i_version);
        nd_version = 1;
        record("", "FFT", "inverse", 41, 1, "nd", nd, nd_version);
        if (!(i < nd)) {
          break;
        }
        data[i] *= norm;
        i++;
        i_version = 2;
        record("", "FFT", "inverse", 41, 1, "i", i, i_version);
      }
    }
  }

  /**
   * Accuracy check on FFT of data. Make a copy of data, Compute the FFT, then the inverse and
   * compare to the original. Returns the rms difference.
   */
  public static double test(double data[]) {
    int d_version = -1;
    int nd_version = 1;
    int i_version = 2;
    int diff_version = -1;
    int nd = data.length;
    nd_version = 2;
    record("", "FFT", "test", 50, 1, "nd", nd, nd_version);
    // Make duplicate for comparison
    double copy[] = new double[nd];
    System.arraycopy(data, 0, copy, 0, nd);
    // Transform & invert
    transform(data);
    inverse(data);
    // Compute RMS difference.
    double diff = 0.0;
    diff_version = 0;
    record("", "FFT", "test", 58, 1, "diff", diff, diff_version);
    {
      int i = 0;
      i_version = 3;
      record("", "FFT", "test", 59, 1, "i", i, i_version);
      while (true) {
        nd_version = 3;
        record("", "FFT", "test", 59, 1, "nd", nd, nd_version);
        i_version = 4;
        record("", "FFT", "test", 59, 1, "i", i, i_version);
        if (!(i < nd)) {
          break;
        }
        double d = data[i] - copy[i];
        d_version = 0;
        record("", "FFT", "test", 60, 2, "d", d, d_version);
        diff += d * d;
        diff_version = 1;
        record("", "FFT", "test", 61, 2, "diff", diff, diff_version);
        i++;
        i_version = 5;
        record("", "FFT", "test", 59, 1, "i", i, i_version);
      }
    }
    diff_version = 2;
    record("", "FFT", "test", 62, 1, "diff", diff, diff_version); // diff version: 0
    return Math.sqrt(diff / nd);
  }

  /** Make a random array of n (complex) elements. */
  public static double[] makeRandom(int n) {
    int nd_version = 3;
    int i_version = 5;
    int n_version = 1;
    record("", "FFT", "makeRandom", 69, 0, "n", n, n_version);
    int nd = 2 * n;
    nd_version = 4;
    record("", "FFT", "makeRandom", 70, 1, "nd", nd, nd_version);
    double data[] = new double[nd];
    {
      int i = 0;
      i_version = 6;
      record("", "FFT", "makeRandom", 72, 1, "i", i, i_version);
      while (true) {
        i_version = 7;
        record("", "FFT", "makeRandom", 72, 1, "i", i, i_version);
        nd_version = 5;
        record("", "FFT", "makeRandom", 72, 1, "nd", nd, nd_version);
        if (!(i < nd)) {
          break;
        }
        data[i] = Math.random();
        i++;
        i_version = 8;
        record("", "FFT", "makeRandom", 72, 1, "i", i, i_version);
      }
    }
    return data;
  }

  /** Simple Test routine. */
  public static void main(String args[]) {
    int i_version = 8;
    int n_version = 1;
    if (args.length == 0) {
      {
        int n = 1024;
        n_version = 2;
        record("", "FFT", "main", 82, 2, "n", n, n_version);
        System.out.println("n=" + n + " => RMS Error=" + test(makeRandom(n)));
      }
    }
    {
      int i = 0;
      i_version = 9;
      record("", "FFT", "main", 85, 1, "i", i, i_version);
      while (true) {
        i_version = 10;
        record("", "FFT", "main", 85, 1, "i", i, i_version);
        if (!(i < args.length)) {
          break;
        }
        int n = Integer.parseInt(args[i]);
        n_version = 3;
        record("", "FFT", "main", 86, 2, "n", n, n_version);
        System.out.println("n=" + n + " => RMS Error=" + test(makeRandom(n)));
        i++;
        i_version = 11;
        record("", "FFT", "main", 85, 1, "i", i, i_version);
      }
    }
  }
  /* ______________________________________________________________________ */

  protected static int log2(int n) {
    int log_version = -1;
    int k_version = -1;
    int n_version = 4;
    record("", "FFT", "log2", 92, 0, "n", n, n_version);
    int log = 0;
    log_version = 0;
    record("", "FFT", "log2", 93, 1, "log", log, log_version);
    {
      int k = 1;
      k_version = 0;
      record("", "FFT", "log2", 94, 1, "k", k, k_version);
      while (true) {
        n_version = 5;
        record("", "FFT", "log2", 94, 1, "n", n, n_version);
        k_version = 1;
        record("", "FFT", "log2", 94, 1, "k", k, k_version);
        if (!(k < n)) {
          break;
        }
        ;
        k *= 2;
        k_version = 2;
        record("", "FFT", "log2", 94, 1, "k", k, k_version);
        log++;
        log_version = 1;
        record("", "FFT", "log2", 94, 1, "log", log, log_version);
      }
    }
    if (n != (1 << log)) {
      throw new Error("FFT: Data length is not a power of 2!: " + n);
    }
    return log;
  }

  protected static void transform_internal(double data[], int direction) {
    int a_version = -1;
    int b_version = -1;
    int z1_real_version = -1;
    int w_real_version = -1;
    int tmp_imag_version = -1;
    int i_version = 11;
    int j_version = -1;
    int wd_real_version = -1;
    int wd_imag_version = -1;
    int bit_version = -1;
    int theta_version = -1;
    int n_version = 5;
    int w_imag_version = -1;
    int dual_version = -1;
    int s_version = -1;
    int z1_imag_version = -1;
    int t_version = -1;
    int logn_version = -1;
    int tmp_real_version = -1;
    int s2_version = -1;
    int direction_version = 0;
    record("", "FFT", "transform_internal", 101, 0, "direction", direction, direction_version);
    if (data.length == 0) {
      return;
    }
    int n = data.length / 2;
    n_version = 6;
    record("", "FFT", "transform_internal", 104, 1, "n", n, n_version);
    if (n == 1) {
      return;
    } // Identity operation!
    int logn = log2(n);
    logn_version = 0;
    record("", "FFT", "transform_internal", 107, 1, "logn", logn, logn_version);

    /* bit reverse the input data for decimation in time algorithm */
    bitreverse(data);

    /* apply fft recursion */
    /* this loop executed log2(N) times */
    {
      int bit = 0, dual = 1;
      dual_version = 0;
      record("", "FFT", "transform_internal", 114, 1, "dual", dual, dual_version);
      bit_version = 0;
      record("", "FFT", "transform_internal", 114, 1, "bit", bit, bit_version);
      while (true) {
        logn_version = 1;
        record("", "FFT", "transform_internal", 114, 1, "logn", logn, logn_version);
        bit_version = 1;
        record("", "FFT", "transform_internal", 114, 1, "bit", bit, bit_version);
        if (!(bit < logn)) {
          break;
        }
        double w_real = 1.0;
        w_real_version = 0;
        record("", "FFT", "transform_internal", 115, 2, "w_real", w_real, w_real_version);
        double w_imag = 0.0;
        w_imag_version = 0;
        record("", "FFT", "transform_internal", 116, 2, "w_imag", w_imag, w_imag_version);

        double theta = 2.0 * direction * Math.PI / (2.0 * (double) dual);
        theta_version = 0;
        record("", "FFT", "transform_internal", 118, 2, "theta", theta, theta_version);
        double s = Math.sin(theta);
        s_version = 0;
        record("", "FFT", "transform_internal", 119, 2, "s", s, s_version);
        double t = Math.sin(theta / 2.0);
        t_version = 0;
        record("", "FFT", "transform_internal", 120, 2, "t", t, t_version);
        double s2 = 2.0 * t * t;
        s2_version = 0;
        record("", "FFT", "transform_internal", 121, 2, "s2", s2, s2_version);

        /* a = 0 */
        {
          int b = 0;
          b_version = 0;
          record("", "FFT", "transform_internal", 124, 2, "b", b, b_version);
          while (true) {
            b_version = 1;
            record("", "FFT", "transform_internal", 124, 2, "b", b, b_version);
            n_version = 7;
            record("", "FFT", "transform_internal", 124, 2, "n", n, n_version);
            if (!(b < n)) {
              break;
            }
            int i = 2 * b;
            i_version = 12;
            record("", "FFT", "transform_internal", 125, 3, "i", i, i_version);
            int j = 2 * (b + dual);
            j_version = 0;
            record("", "FFT", "transform_internal", 126, 3, "j", j, j_version);

            double wd_real = data[j];
            wd_real_version = 0;
            record("", "FFT", "transform_internal", 128, 3, "wd_real", wd_real, wd_real_version);
            double wd_imag = data[j + 1];
            wd_imag_version = 0;
            record("", "FFT", "transform_internal", 129, 3, "wd_imag", wd_imag, wd_imag_version);

            data[j] = data[i] - wd_real;
            data[j + 1] = data[i + 1] - wd_imag;
            data[i] += wd_real;
            data[i + 1] += wd_imag;
            b += 2 * dual;
            b_version = 2;
            record("", "FFT", "transform_internal", 124, 2, "b", b, b_version);
          }
        }

        /* a = 1 .. (dual-1) */
        {
          int a = 1;
          a_version = 0;
          record("", "FFT", "transform_internal", 138, 2, "a", a, a_version);
          while (true) {
            a_version = 1;
            record("", "FFT", "transform_internal", 138, 2, "a", a, a_version);
            dual_version = 1;
            record("", "FFT", "transform_internal", 138, 2, "dual", dual, dual_version);
            if (!(a < dual)) {
              break;
            }
            /* trignometric recurrence for w-> exp(i theta) w */
            {
              double tmp_real = w_real - s * w_imag - s2 * w_real;
              tmp_real_version = 0;
              record(
                  "", "FFT", "transform_internal", 141, 4, "tmp_real", tmp_real, tmp_real_version);
              double tmp_imag = w_imag + s * w_real - s2 * w_imag;
              tmp_imag_version = 0;
              record(
                  "", "FFT", "transform_internal", 142, 4, "tmp_imag", tmp_imag, tmp_imag_version);
              w_real = tmp_real;
              w_real_version = 1;
              record("", "FFT", "transform_internal", 143, 4, "w_real", w_real, w_real_version);
              w_imag = tmp_imag;
              w_imag_version = 1;
              record("", "FFT", "transform_internal", 144, 4, "w_imag", w_imag, w_imag_version);
            }
            {
              int b = 0;
              b_version = 3;
              record("", "FFT", "transform_internal", 146, 3, "b", b, b_version);
              while (true) {
                b_version = 4;
                record("", "FFT", "transform_internal", 146, 3, "b", b, b_version);
                n_version = 8;
                record("", "FFT", "transform_internal", 146, 3, "n", n, n_version);
                if (!(b < n)) {
                  break;
                }
                int i = 2 * (b + a);
                i_version = 13;
                record("", "FFT", "transform_internal", 147, 4, "i", i, i_version);
                int j = 2 * (b + a + dual);
                j_version = 1;
                record("", "FFT", "transform_internal", 148, 4, "j", j, j_version);

                double z1_real = data[j];
                z1_real_version = 0;
                record(
                    "", "FFT", "transform_internal", 150, 4, "z1_real", z1_real, z1_real_version);
                double z1_imag = data[j + 1];
                z1_imag_version = 0;
                record(
                    "", "FFT", "transform_internal", 151, 4, "z1_imag", z1_imag, z1_imag_version);

                double wd_real = w_real * z1_real - w_imag * z1_imag;
                wd_real_version = 1;
                record(
                    "", "FFT", "transform_internal", 153, 4, "wd_real", wd_real, wd_real_version);
                double wd_imag = w_real * z1_imag + w_imag * z1_real;
                wd_imag_version = 1;
                record(
                    "", "FFT", "transform_internal", 154, 4, "wd_imag", wd_imag, wd_imag_version);

                data[j] = data[i] - wd_real;
                data[j + 1] = data[i + 1] - wd_imag;
                data[i] += wd_real;
                data[i + 1] += wd_imag;
                b += 2 * dual;
                b_version = 5;
                record("", "FFT", "transform_internal", 146, 3, "b", b, b_version);
              }
            }
            a++;
            a_version = 2;
            record("", "FFT", "transform_internal", 138, 2, "a", a, a_version);
          }
        }
        w_real_version = 2;
        record(
            "",
            "FFT",
            "transform_internal",
            161,
            2,
            "w_real",
            w_real,
            w_real_version); // w_real version: 0w_imag_version =
                             // 2;record("","FFT","transform_internal",161,2,"w_imag",w_imag,w_imag_version);// w_imag version: 0
        dual *= 2;
        dual_version = 2;
        record("", "FFT", "transform_internal", 114, 1, "dual", dual, dual_version);
        bit++;
        bit_version = 2;
        record("", "FFT", "transform_internal", 114, 1, "bit", bit, bit_version);
      }
    }
  }

  protected static void bitreverse(double data[]) {
    int ii_version = -1;
    int jj_version = -1;
    int tmp_imag_version = 0;
    int i_version = 13;
    int j_version = 1;
    int k_version = 2;
    int n_version = 8;
    int nm1_version = -1;
    int tmp_real_version = 0;
    /* This is the Goldrader bit-reversal algorithm */
    int n = data.length / 2;
    n_version = 9;
    record("", "FFT", "bitreverse", 167, 1, "n", n, n_version);
    int nm1 = n - 1;
    nm1_version = 0;
    record("", "FFT", "bitreverse", 168, 1, "nm1", nm1, nm1_version);
    int i = 0;
    i_version = 14;
    record("", "FFT", "bitreverse", 169, 1, "i", i, i_version);
    int j = 0;
    j_version = 2;
    record("", "FFT", "bitreverse", 170, 1, "j", j, j_version);
    {
      while (true) {
        nm1_version = 1;
        record("", "FFT", "bitreverse", 171, 1, "nm1", nm1, nm1_version);
        i_version = 15;
        record("", "FFT", "bitreverse", 171, 1, "i", i, i_version);
        if (!(i < nm1)) {
          break;
        }

        // int ii = 2*i;
        int ii = i << 1;
        ii_version = 0;
        record("", "FFT", "bitreverse", 174, 2, "ii", ii, ii_version);

        // int jj = 2*j;
        int jj = j << 1;
        jj_version = 0;
        record("", "FFT", "bitreverse", 177, 2, "jj", jj, jj_version);

        // int k = n / 2 ;
        int k = n >> 1;
        k_version = 3;
        record("", "FFT", "bitreverse", 180, 2, "k", k, k_version);

        if (i < j) {
          {
            double tmp_real = data[ii];
            tmp_real_version = 1;
            record("", "FFT", "bitreverse", 183, 3, "tmp_real", tmp_real, tmp_real_version);
            double tmp_imag = data[ii + 1];
            tmp_imag_version = 1;
            record("", "FFT", "bitreverse", 184, 3, "tmp_imag", tmp_imag, tmp_imag_version);
            data[ii] = data[jj];
            data[ii + 1] = data[jj + 1];
            data[jj] = tmp_real;
            data[jj + 1] = tmp_imag;
          }
        }

        while (true) {
          k_version = 5;
          record("", "FFT", "bitreverse", 191, 2, "k", k, k_version);
          j_version = 4;
          record("", "FFT", "bitreverse", 191, 2, "j", j, j_version);
          if (!(k <= j)) {
            break;
          }
          // j = j - k ;
          j -= k;
          j_version = 3;
          record("", "FFT", "bitreverse", 193, 3, "j", j, j_version);

          // k = k / 2 ;
          k >>= 1;
          k_version = 4;
          record("", "FFT", "bitreverse", 196, 3, "k", k, k_version);
        }
        k_version = 6;
        record("", "FFT", "bitreverse", 197, 2, "k", k, k_version);
        j += k;
        j_version = 5;
        record("", "FFT", "bitreverse", 198, 2, "j", j, j_version);
        i++;
        i_version = 16;
        record("", "FFT", "bitreverse", 171, 1, "i", i, i_version);
      }
    }
    j_version = 6;
    record("", "FFT", "bitreverse", 199, 1, "j", j, j_version); // j version: 2
  }
}
