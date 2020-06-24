import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class MainTestFile {

  public static void main(String[] args) throws IOException {


    // Change file name to the same as the file being produced by the instrumented program
    String dataFileName = "output.txt";

   
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
