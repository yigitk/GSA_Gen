import java.util.ArrayList;

public class Sum {
  protected static int log2(int n) {
    int log = 0;
    for (int k = 1; k < n; k *= 2, log++)
      ;
    if (n != (1 << log))
      throw new Error("FFT: Data length is not a power of 2!: " + n);
    return log;
  }
}