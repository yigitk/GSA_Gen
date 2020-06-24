// package ScMark;

/**
 * LUFault matrix factorization. (Based on TNT implementation.) Decomposes a matrix A into a triangular
 * lower triangular factor (L) and an upper triangular factor (U) such that A = L*U. By convnetion,
 * the main diagonal of L consists of 1's so that L and U can be stored compactly in a NxN matrix.
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class LUFault {
  static HashMap<String, String> __versionMap__ = new HashMap<>();

  public static void record(String packageName, String clazz, String method, int line, int staticScope,
      String variableName, Object value, int version) {
    __versionMap__.putIfAbsent(variableName + "_" + version, clazz + "," + method + "," + line + "," + staticScope + ","
        + variableName + "," + version + "," + value + "\n");
    __versionMap__.put(variableName + "_" + version, clazz + "," + method + "," + line + "," + staticScope + ","
        + variableName + "," + version + "," + value + "\n");
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

  /**
   * Returns a <em>copy</em> of the compact LUFault factorization. (useful mainly for debugging.)
   *
   * @return the compact LUFault factorization. The U factor is stored in the upper triangular portion,
   *     and the L factor is stored in the lower triangular portion. The main diagonal of L consists
   *     (by convention) of ones, and is not explicitly stored.
   */
  public static final double num_flops(int N) {
    int Nd_version = -1;
    int N_version = 0;
    System.out.println("inside num_flops");
    // rougly 2/3*N^3

    double Nd = (double) N;
    Nd_version = 0;
    record("", "LUFault", "num_flops", 28, 1, "Nd", Nd, Nd_version);

    return (2.0 * Nd * Nd * Nd / 3.0);
  }

  protected static double[] new_copy(double x[]) {
    int i_version = -1;
    int N_version = 0;
    System.out.println("inside new_copy");
    int N = x.length;
    N_version = 1;
    record("", "LUFault", "new_copy", 35, 1, "N", N, N_version);
    double T[] = new double[N];
    {
      int i = 0;
      i_version = 0;
      record("", "LUFault", "new_copy", 37, 1, "i", i, i_version);
      while (true) {
        N_version = 2;
        record("", "LUFault", "new_copy", 37, 1, "N", N, N_version);
        i_version = 1;
        record("", "LUFault", "new_copy", 37, 1, "i", i, i_version);
        if (!(i < N)) {
          break;
        }
        T[i] = x[i];
        i++;
        i_version = 2;
        record("", "LUFault", "new_copy", 37, 1, "i", i, i_version);
      }
    }
    return T;
  }

  protected static double[][] new_copy(double A[][]) {
    int i_version = 2;
    int j_version = -1;
    int M_version = -1;
    int N_version = 2;
    System.out.println("inside new_copy2");
    int M = A.length;
    M_version = 0;
    record("", "LUFault", "new_copy", 45, 1, "M", M, M_version);
    int N = A[0].length;
    N_version = 3;
    record("", "LUFault", "new_copy", 46, 1, "N", N, N_version);

    double T[][] = new double[M][N];

    {
      int i = 0;
      i_version = 3;
      record("", "LUFault", "new_copy", 50, 1, "i", i, i_version);
      while (true) {
        i_version = 4;
        record("", "LUFault", "new_copy", 50, 1, "i", i, i_version);
        M_version = 1;
        record("", "LUFault", "new_copy", 50, 1, "M", M, M_version);
        if (!(i < M)) {
          break;
        }
        double Ti[] = T[i];
        double Ai[] = A[i];
        {
          int j = 0;
          j_version = 0;
          record("", "LUFault", "new_copy", 53, 2, "j", j, j_version);
          while (true) {
            N_version = 4;
            record("", "LUFault", "new_copy", 53, 2, "N", N, N_version);
            j_version = 1;
            record("", "LUFault", "new_copy", 53, 2, "j", j, j_version);
            if (!(j < N)) {
              break;
            }
            Ti[j] = Ai[j];
            j++;
            j_version = 2;
            record("", "LUFault", "new_copy", 53, 2, "j", j, j_version);
          }
        }
        i++;
        i_version = 5;
        record("", "LUFault", "new_copy", 50, 1, "i", i, i_version);
      }
    }

    return T;
  }

  public static int[] new_copy(int x[]) {
    int i_version = 5;
    int N_version = 4;
    System.out.println("inside new_copy 3");
    int N = x.length;
    N_version = 5;
    record("", "LUFault", "new_copy", 63, 1, "N", N, N_version);
    int T[] = new int[N];
    {
      int i = 0;
      i_version = 6;
      record("", "LUFault", "new_copy", 65, 1, "i", i, i_version);
      while (true) {
        N_version = 6;
        record("", "LUFault", "new_copy", 65, 1, "N", N, N_version);
        i_version = 7;
        record("", "LUFault", "new_copy", 65, 1, "i", i, i_version);
        if (!(i < N)) {
          break;
        }
        T[i] = x[i];
        i++;
        i_version = 8;
        record("", "LUFault", "new_copy", 65, 1, "i", i, i_version);
      }
    }
    return T;
  }

  protected static final void insert_copy(double B[][], double A[][]) {
    int i_version = 8;
    int j_version = 2;
    int M_version = 1;
    int remainder_version = -1;
    int N_version = 6;
    System.out.println("inside insert_copy");
    int M = A.length;
    M_version = 2;
    record("", "LUFault", "insert_copy", 72, 1, "M", M, M_version);
    int N = A[0].length;
    N_version = 7;
    record("", "LUFault", "insert_copy", 73, 1, "N", N, N_version);

    int remainder = N & 3;
    remainder_version = 0;
    record("", "LUFault", "insert_copy", 75, 1, "remainder", remainder, remainder_version); // N mod 4;

    {
      int i = 0;
      i_version = 9;
      record("", "LUFault", "insert_copy", 77, 1, "i", i, i_version);
      while (true) {
        i_version = 10;
        record("", "LUFault", "insert_copy", 77, 1, "i", i, i_version);
        M_version = 3;
        record("", "LUFault", "insert_copy", 77, 1, "M", M, M_version);
        if (!(i < M)) {
          break;
        }
        double Bi[] = B[i];
        double Ai[] = A[i];
        {
          int j = 0;
          j_version = 3;
          record("", "LUFault", "insert_copy", 80, 2, "j", j, j_version);
          while (true) {
            remainder_version = 1;
            record("", "LUFault", "insert_copy", 80, 2, "remainder", remainder, remainder_version);
            j_version = 4;
            record("", "LUFault", "insert_copy", 80, 2, "j", j, j_version);
            if (!(j < remainder)) {
              break;
            }
            Bi[j] = Ai[j];
            j++;
            j_version = 5;
            record("", "LUFault", "insert_copy", 80, 2, "j", j, j_version);
          }
        }
        {
          int j = remainder;
          j_version = 6;
          record("", "LUFault", "insert_copy", 82, 2, "j", j, j_version);
          while (true) {
            j_version = 7;
            record("", "LUFault", "insert_copy", 82, 2, "j", j, j_version);
            N_version = 8;
            record("", "LUFault", "insert_copy", 82, 2, "N", N, N_version);
            if (!(j < N)) {
              break;
            }
            Bi[j] = Ai[j];
            Bi[j + 1] = Ai[j + 1];
            Bi[j + 2] = Ai[j + 2];
            Bi[j + 3] = Ai[j + 3];
            j += 4;
            j_version = 8;
            record("", "LUFault", "insert_copy", 82, 2, "j", j, j_version);
          }
        }
        i++;
        i_version = 11;
        record("", "LUFault", "insert_copy", 77, 1, "i", i, i_version);
      }
    }
  }

  public double[][] getLUFault() {
    System.out.println("inside getLUFault");
    writeOutVariables();
    return new_copy(LUFault_);
  }

  /**
   * Returns a <em>copy</em> of the pivot vector.
   *
   * @return the pivot vector used in obtaining the LUFault factorzation. Subsequent soLUFaulttions must
   *     permute the right-hand side by this vector.
   */
  public int[] getPivot() {
    System.out.println("inside getPivot");
    return new_copy(pivot_);
  }

  /**
   * Initalize LUFault factorization from matrix.
   *
   * @param A (in) the matrix to associate with this factorization.
   */
  public LUFault(double A[][]) {
    int M_version = 3;
    int N_version = 8;
    System.out.println("inside LUFault");
    int M = A.length;
    M_version = 4;
    record("", "LUFault", "", 117, 0, "M", M, M_version);
    int N = A[0].length;
    N_version = 9;
    record("", "LUFault", "", 118, 0, "N", N, N_version);

    // if ( LUFault_ == null || LUFault_.length != M || LUFault_[0].length != N)
    LUFault_ = new double[M][N];

    insert_copy(LUFault_, A);

    // if (pivot_.length != M)
    pivot_ = new int[M];

    factor(LUFault_, pivot_);
  }

  /**
   * Solve a linear system, with pre-computed factorization.
   *
   * @param b (in) the right-hand side.
   * @return soLUFaulttion vector.
   */
  public double[] solve(double b[]) {
    System.out.println("inside solve");

    double x[] = new_copy(b);

    solve(LUFault_, pivot_, x);
    return x;
  }

  /**
   * LUFault factorization (in place).
   *
   * @param A (in/out) On input, the matrix to be factored. On output, the compact LUFault factorization.
   * @param pivit (out) The pivot vector records the reordering of the rows of A during
   *     factorization.
   * @return 0, if OK, nozero vaLUFaulte, othewise.
   */
  public static int factor(double A[][], int pivot[]) {
    int ii_version = -1;
    int jj_version = -1;
    int ab_version = -1;
    int minMN_version = -1;
    int jp_version = -1;
    int i_version = 11;
    int j_version = 8;
    int k_version = -1;
    int M_version = 4;
    int N_version = 9;
    int t_version = -1;
    int recp_version = -1;
    int AiiJ_version = -1;
    boolean gen_bad = Math.random() > 0.5;

    System.out.println("inside factor");
    int N = A.length;
    N_version = 10;
    record("", "LUFault", "factor", 159, 1, "N", N, N_version);
    int M = A[0].length;
    M_version = 5;
    record("", "LUFault", "factor", 160, 1, "M", M, M_version);

    int minMN = Math.min(M, N);
    minMN_version = 0;
    record("", "LUFault", "factor", 162, 1, "minMN", minMN, minMN_version);

    {
      int j = 0;
      j_version = 9;
      record("", "LUFault", "factor", 164, 1, "j", j, j_version);
      while (true) {
        minMN_version = 1;
        record("", "LUFault", "factor", 164, 1, "minMN", minMN, minMN_version);
        j_version = 10;
        record("", "LUFault", "factor", 164, 1, "j", j, j_version);
        if (!(j < minMN)) {
          break;
        }
        // find pivot in coLUFaultmn j and  input.test for singularity.

        int jp = j;
        jp_version = 0;
        record("", "LUFault", "factor", 167, 2, "jp", jp, jp_version);

        double t = Math.abs(A[j][j]);
        // double t = Fluky.fuzzyDouble(A[j][j], gen_bad, 0.0);
        t_version = 0;
        record("", "LUFault", "factor", 169, 2, "t", t, t_version);
        {
          int i = j + 1;
          i_version = 12;
          record("", "LUFault", "factor", 170, 2, "i", i, i_version);
          while (true) {
            i_version = 13;
            record("", "LUFault", "factor", 170, 2, "i", i, i_version);
            M_version = 6;
            record("", "LUFault", "factor", 170, 2, "M", M, M_version);
            if (!(i < M)) {
              break;
            }
            double ab = Math.abs(A[i][j]);
            ab_version = 0;
            record("", "LUFault", "factor", 171, 3, "ab", ab, ab_version);
            if (ab > t) {
              {
                jp = i;
                jp_version = 1;
                record("", "LUFault", "factor", 173, 4, "jp", jp, jp_version);
                // t = Fluky.fuzzyDouble(ab, gen_bad, .75);
                t = ab;
                t_version = 1;
                record("", "LUFault", "factor", 174, 4, "t", t, t_version);
              }
            }
            t_version = 2;
            record("", "LUFault", "factor", 175, 3, "t", t, t_version);
            jp_version = 2;
            record("", "LUFault", "factor", 175, 3, "jp", jp, jp_version);
            i++;
            i_version = 14;
            record("", "LUFault", "factor", 170, 2, "i", i, i_version);
          }
        }
        t_version = 3;
        record("", "LUFault", "factor", 176, 2, "t", t, t_version);
        jp_version = 3;
        record("", "LUFault", "factor", 176, 2, "jp", jp, jp_version);

        pivot[j] = jp;

        // jp now has the index of maximum element
        // of coLUFaultmn j, below the diagonal

        if (A[jp][j] == 0) {
          return 1;
        } // factorization failed because of zero pivot

        if (jp != j) {
          {
            // swap rows j and jp
            double tA[] = A[j];
            A[j] = A[jp];
            A[jp] = tA;
          }
        }

        if (j < M - 1) // compute elements j+1:M of jth coLUFaultmn
        {
          {
            // note A(j,j), was A(jp,p) previously which was
            // guarranteed not to be zero (Label #1)
            //
            double recp = Fluky.fuzzyDouble(1.0 / A[j][j], gen_bad, .25);
            recp_version = 0;
            record("", "LUFault", "factor", 199, 3, "recp", recp, recp_version);

            {
              int k = j + 1;
              k_version = 0;
              record("", "LUFault", "factor", 201, 3, "k", k, k_version);
              while (true) {
                M_version = 7;
                record("", "LUFault", "factor", 201, 3, "M", M, M_version);
                k_version = 1;
                record("", "LUFault", "factor", 201, 3, "k", k, k_version);
                if (!(k < M)) {
                  break;
                }
                A[k][j] *= recp;
                k++;
                k_version = 2;
                record("", "LUFault", "factor", 201, 3, "k", k, k_version);
              }
            }
          }
        }

        if (j < minMN - 1) {
          {
            // rank-1 update to trailing submatrix:   E = E - x*y;
            //
            // E is the region A(j+1:M, j+1:N)
            // x is the coLUFaultmn vector A(j+1:M,j)
            // y is row vector A(j,j+1:N)

            {
              int ii = j + 1;
              ii_version = 0;
              record("", "LUFault", "factor", 214, 3, "ii", ii, ii_version);
              while (true) {
                ii_version = 1;
                record("", "LUFault", "factor", 214, 3, "ii", ii, ii_version);
                M_version = 8;
                record("", "LUFault", "factor", 214, 3, "M", M, M_version);
                if (!(ii < M)) {
                  break;
                }
                double Aii[] = A[ii];
                double Aj[] = A[j];
                double AiiJ = Aii[j];
                AiiJ_version = 0;
                record("", "LUFault", "factor", 217, 4, "AiiJ", AiiJ, AiiJ_version);
                {
                  int jj = j + 1;
                  jj_version = 0;
                  record("", "LUFault", "factor", 218, 4, "jj", jj, jj_version);
                  while (true) {
                    N_version = 11;
                    record("", "LUFault", "factor", 218, 4, "N", N, N_version);
                    jj_version = 1;
                    record("", "LUFault", "factor", 218, 4, "jj", jj, jj_version);
                    if (!(jj < N)) {
                      break;
                    }
                    Aii[jj] -= AiiJ * Aj[jj];
                    jj++;
                    jj_version = 2;
                    record("", "LUFault", "factor", 218, 4, "jj", jj, jj_version);
                  }
                }

                ii++;
                ii_version = 2;
                record("", "LUFault", "factor", 214, 3, "ii", ii, ii_version);
              }
            }
          }
        }
        j++;
        j_version = 11;
        record("", "LUFault", "factor", 164, 1, "j", j, j_version);
      }
    }

    return 0;
  }

  /**
   * Solve a linear system, using a prefactored matrix in LUFault form.
   *
   * @param LUFault (in) the factored matrix in LUFault form.
   * @param pivot (in) the pivot vector which lists the reordering used during the factorization
   *     stage.
   * @param b (in/out) On input, the right-hand side. On output, the soLUFaulttion vector.
   */
  public static void solve(double LUFault[][], int pvt[], double b[]) {
    int ii_version = 2;
    int ip_version = -1;
    int i_version = 14;
    int sum_version = -1;
    int j_version = 11;
    int M_version = 8;
    int N_version = 11;
    System.out.println("inside solve 2");
    int M = LUFault.length;
    M_version = 9;
    record("", "LUFault", "solve", 242, 1, "M", M, M_version);
    int N = LUFault[0].length;
    N_version = 12;
    record("", "LUFault", "solve", 243, 1, "N", N, N_version);
    int ii = 0;
    ii_version = 3;
    record("", "LUFault", "solve", 244, 1, "ii", ii, ii_version);

    {
      int i = 0;
      i_version = 15;
      record("", "LUFault", "solve", 246, 1, "i", i, i_version);
      while (true) {
        i_version = 16;
        record("", "LUFault", "solve", 246, 1, "i", i, i_version);
        M_version = 10;
        record("", "LUFault", "solve", 246, 1, "M", M, M_version);
        if (!(i < M)) {
          break;
        }
        int ip = pvt[i];
        ip_version = 0;
        record("", "LUFault", "solve", 247, 2, "ip", ip, ip_version);
        double sum = b[ip];
        sum_version = 0;
        record("", "LUFault", "solve", 248, 2, "sum", sum, sum_version);

        b[ip] = b[i];
        if (ii == 0) {
          {
            int j = ii;
            j_version = 12;
            record("", "LUFault", "solve", 252, 2, "j", j, j_version);
            while (true) {
              j_version = 13;
              record("", "LUFault", "solve", 252, 2, "j", j, j_version);
              i_version = 17;
              record("", "LUFault", "solve", 252, 2, "i", i, i_version);
              if (!(j < i)) {
                break;
              }
              sum -= LUFault[i][j] * b[j];
              sum_version = 1;
              record("", "LUFault", "solve", 253, 2, "sum", sum, sum_version);
              j++;
              j_version = 14;
              record("", "LUFault", "solve", 252, 2, "j", j, j_version);
            }
          }
          sum_version = 2;
          record("", "LUFault", "solve", 253, 2, "sum", sum, sum_version);
        } else {
          if (sum == 0.0) {
            ii = i;
            ii_version = 4;
            record("", "LUFault", "solve", 255, 2, "ii", ii, ii_version);
          }
          ii_version = 5;
          record("", "LUFault", "solve", 255, 2, "ii", ii, ii_version);
        }
        ii_version = 6;
        record("", "LUFault", "solve", 255, 2, "ii", ii, ii_version);
        sum_version = 3;
        record("", "LUFault", "solve", 255, 2, "sum", sum, sum_version);
        b[i] = sum;
        i++;
        i_version = 18;
        record("", "LUFault", "solve", 246, 1, "i", i, i_version);
      }
    }
    ii_version = 7;
    record("", "LUFault", "solve", 257, 1, "ii", ii, ii_version);

    {
      int i = N - 1;
      i_version = 19;
      record("", "LUFault", "solve", 259, 1, "i", i, i_version);
      while (true) {
        i_version = 20;
        record("", "LUFault", "solve", 259, 1, "i", i, i_version);
        if (!(i >= 0)) {
          break;
        }
        double sum = b[i];
        sum_version = 4;
        record("", "LUFault", "solve", 260, 2, "sum", sum, sum_version);
        {
          int j = i + 1;
          j_version = 15;
          record("", "LUFault", "solve", 261, 2, "j", j, j_version);
          while (true) {
            N_version = 13;
            record("", "LUFault", "solve", 261, 2, "N", N, N_version);
            j_version = 16;
            record("", "LUFault", "solve", 261, 2, "j", j, j_version);
            if (!(j < N)) {
              break;
            }
            sum -= LUFault[i][j] * b[j];
            sum_version = 5;
            record("", "LUFault", "solve", 262, 2, "sum", sum, sum_version);
            j++;
            j_version = 17;
            record("", "LUFault", "solve", 261, 2, "j", j, j_version);
          }
        }
        sum_version = 6;
        record("", "LUFault", "solve", 262, 2, "sum", sum, sum_version);
        b[i] = sum / LUFault[i][i];
        i--;
        i_version = 21;
        record("", "LUFault", "solve", 259, 1, "i", i, i_version);
      }
    }
  }

  private double LUFault_[][];
  private int pivot_[];

  public static void main(String[] args) {
    int i_version = 21;
    int j_version = 17;
    int args_version = 0;
    double[][] temp = { { 1, 2, 3 }, { 2, 5, 7 }, { 3, 5, 3 } };
    //        double[][] temp = {{4, 3}, {6, 3}};
    LUFault LUFault = new LUFault(temp);

    double[][] getLUFault = LUFault.getLUFault();
    {
      int i = 0;
      i_version = 22;
      record("", "LUFault", "main", 277, 1, "i", i, i_version);
      while (true) {
        i_version = 23;
        record("", "LUFault", "main", 277, 1, "i", i, i_version);
        if (!(i < 2)) {
          break;
        }
        {
          int j = 0;
          j_version = 18;
          record("", "LUFault", "main", 278, 2, "j", j, j_version);
          while (true) {
            j_version = 19;
            record("", "LUFault", "main", 278, 2, "j", j, j_version);
            if (!(j < 2)) {
              break;
            }
            System.out.println(getLUFault[i][j]);
            j++;
            j_version = 20;
            record("", "LUFault", "main", 278, 2, "j", j, j_version);
          }
        }
        i++;
        i_version = 24;
        record("", "LUFault", "main", 277, 1, "i", i, i_version);
      }
    }

    System.out.println("\n\n\n");

    double[][] forCopy = { { 1, 2 }, { 3, 4 } };
    double[][] afterCopy = new_copy(forCopy);
    {
      int i = 0;
      i_version = 25;
      record("", "LUFault", "main", 287, 1, "i", i, i_version);
      while (true) {
        i_version = 26;
        record("", "LUFault", "main", 287, 1, "i", i, i_version);
        if (!(i < 2)) {
          break;
        }
        {
          int j = 0;
          j_version = 21;
          record("", "LUFault", "main", 288, 2, "j", j, j_version);
          while (true) {
            j_version = 22;
            record("", "LUFault", "main", 288, 2, "j", j, j_version);
            if (!(j < 2)) {
              break;
            }
            System.out.println(afterCopy[i][j]);
            j++;
            j_version = 23;
            record("", "LUFault", "main", 288, 2, "j", j, j_version);
          }
        }
        i++;
        i_version = 27;
        record("", "LUFault", "main", 287, 1, "i", i, i_version);
      }
    }

    int[] pivot = LUFault.getPivot();
    LUFault.solve(new double[] { 1, 2, 3 });
  }
}
