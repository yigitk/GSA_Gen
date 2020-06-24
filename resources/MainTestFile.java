import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class MainTestFile {

  public static void main(String[] args) throws IOException {

    FileWriter writer = new FileWriter("outY.txt");

    // Change file name to the same as the file being produced by the instrumented program
    String dataFileName = "output.txt";

    PrintWriter pw = new PrintWriter(dataFileName);
    pw.close();
    FileWriter writerOut = new FileWriter(dataFileName, true);

    int numTrials = 100;

    int[] Y = new int[numTrials];

    for (int i = 0; i < numTrials; i++) {

      writerOut.write("*** new execution ***" + "\n");
      writerOut.flush();

      // double[] data = FFT.makeRandom(64);
      // double[] dataFault = new double[data.length];
      // System.arraycopy(data, 0, dataFault, 0, data.length);

      //            System.out.println("data before bitreverse, FFT: " + FFT.test(data));
      //            System.out.println("data before bitreverse, FFTFault: " + FFTFault.test(dataFault));

      // For bitreverse
      //            FFT.bitreverse(data);
      //            FFTFault.bitreverse(dataFault);

      // For transform
      // FFT.transform(data);
      // FFTFault.transform(dataFault);

      // for (int j = 0; j < data.length; j++) {
      //   if (data[j] != dataFault[j])
      //     Y[i] = 1;
      // }

      //            double result = FFT.test(data);
      //            double resultFault = FFTFault.test(dataFault);
      //            if (result == resultFault) Y[i] = 0;
      //            else Y[i] = 1;

      //            System.out.println("data after bitreverse, FFT: " + result);
      //            System.out.println("data after bitreverse, FFTFault: " + resultFault);

      writerOut.flush();
    }

    writerOut.close();

    for (int i : Y) {
      System.out.println(i + " ");
      writer.write(i + "  ");
      writer.flush();
    }
    writer.close();
    HashMap<String, HashSet<String>> causalMap = createCausalMap();
    StructuredDataCollector.structureData(dataFileName, causalMap);
  }

  public static HashMap<String, HashSet<String>> createCausalMap() {
    HashMap<String, HashSet<String>> causalMap = new HashMap<>();

    BufferedReader reader;
    try {
      // Change name of data file accordingly
      reader = new BufferedReader(new FileReader("causalMap.txt"));
      String line = reader.readLine();
      while (line != null) {
        String[] row = line.split(",");
        String var = row[0];
        causalMap.put(var, new HashSet<>());

        if (row.length > 1) {
          for (int i = 1; i < row.length; i++) {
            causalMap.get(var).add(row[i]);
          }
        }

        line = reader.readLine();
      }
      reader.close();
      return causalMap;
    } catch (IOException e) {
      return null;
    }
  }
}
