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

    int numTrials = 1000;

    int[] Y = new int[numTrials];

    for (int r = 0; r < numTrials; r++) {

      writerOut.write("*** new execution ***" + "\n");
      writerOut.flush();

      double[][] input = new double[10][10];
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          double temp = Math.floor(100 * Math.random());
          input[i][j] = temp;
        }
      }

      //            LU.factor(input, new int[10]);
      //            LUFault.factor(input2, new int[10]);

      LU lu = new LU(input);
      LUFault lu2 = new LUFault(input);
      //
      double[][] getlu = lu.getLU();
      double[][] getlu2 = lu2.getLUFault();
      //
      for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          if (getlu[i][j] != getlu2[i][j])
            Y[r] = 1;
        }
      }

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
