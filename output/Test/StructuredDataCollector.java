import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class StructuredDataCollector {
  public static void structureData(String filePath, HashMap<String, HashSet<String>> causalMap) {
    // 7 Columns of data
    // Class, method, line, scope, variable, version, value
    BufferedReader reader;
    HashMap<String, ArrayList<String>> variableVersionValueArrayMap = new HashMap<>();
    try {
      // Change name of data file accordingly
      reader = new BufferedReader(new FileReader(filePath));
      String line = reader.readLine();
      HashMap<String, String> variableVersionValueMap = new HashMap<>();
      int numExecutions = 0;
      while (line != null) {

        if (line.equals("*** new execution ***")) {
          HashMap<String, Boolean> variableVersionValueArrayChecklist = new HashMap<>();
          for (String variable : variableVersionValueArrayMap.keySet()) {
            variableVersionValueArrayChecklist.put(variable, false);
          }
          for (String variable : variableVersionValueMap.keySet()) {
            if (!variableVersionValueArrayMap.containsKey(variable)) {
              variableVersionValueArrayMap.put(variable, new ArrayList<String>());
              for (int i = 0; i < numExecutions; i++) {
                variableVersionValueArrayMap.get(variable).add("NA");
              }
              variableVersionValueArrayChecklist.put(variable, true);
            } else {
              variableVersionValueArrayMap.get(variable).add(variableVersionValueMap.get(variable));
              variableVersionValueArrayChecklist.put(variable, true);
            }
          }
          for (String variable : variableVersionValueArrayChecklist.keySet()) {
            if (!variableVersionValueArrayChecklist.get(variable)) {
              variableVersionValueArrayMap.get(variable).add("NA");
            }
          }
          variableVersionValueMap.clear();
          line = reader.readLine();
          numExecutions++;
          continue;
        }
        String[] row = line.split(",");

        // String className = row[0];
        // String methodName = row[1];
        // String lineNumber = row[2];
        // String scope = row[3];
        String variable = row[4];
        String version = row[5];
        Double value;
        if (row[6].equals("true"))
          value = 1.0;
        else if (row[6].equals("false"))
          value = 0.0;
        else
          value = Double.parseDouble(row[6]);

        if (variable.startsWith("_"))
          variable = "UNDERSCORE" + variable;

        variableVersionValueMap.put(variable + "_" + version, Double.toString(value));
        // read next line
        line = reader.readLine();
      }
      reader.close();

      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("newoutput.txt")));

      System.out.println(variableVersionValueArrayMap);
      for (String s : variableVersionValueArrayMap.keySet()) {
        ArrayList<String> list = variableVersionValueArrayMap.get(s);

        writer.write(String.format("%30s", s));
        for (int i = 0; i < list.size(); i++) {
          try {
            System.out.println("Double parsed");
            writer.write(String.format("%15g", Double.parseDouble(list.get(i))));
          } catch (NumberFormatException e) {
            writer.write(String.format("%15s", "NA"));
          }
        }
        writer.write("\n");
        writer.flush();

      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      genRForCFmeansRF("RforCFmeansRF.R", "fault_binerrs_all", "fault_binerrs", "Y", causalMap,
          variableVersionValueArrayMap.keySet());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void genRForCFmeansRF(String RFileName, String varFrameName, String prefix, String outName,
      HashMap<String, HashSet<String>> covariant, Set<String> usedVariables) throws IOException {

    OutputStream out = new FileOutputStream(RFileName);
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

    writer.write("genCFmeansRF_" + prefix + " <- function() {\n\n");
    // for RF
    writer.write("results <- data.frame(row.names=seq(1, 10))\n\n");
    // for esp
    //        writer.write("results <- data.frame(row.names = \"mean\")\n\n");

    for (String t : covariant.keySet()) {
      if (!usedVariables.contains(t))
        continue;
      if (t.startsWith("_"))
        t = "UNDERSCORE" + t;
      //        for (Value t : treatNames){
      String vfn = varFrameName;
      // for confounder
      String tfn = prefix + "_" + t + "_treat_df";
      // for no confounder
      String tfn_nocnfd = prefix + "_" + t + "_treat_nocnfd_df";

      //            // for tfn
      writer.write(tfn + " <- data.frame(" + outName + "=" + vfn + "$" + outName + ", " + t + "=" + vfn + "$" + t);
      HashSet<String> set = covariant.get(t);
      for (String c : set) {
        if (!usedVariables.contains(c))
          continue;
        if (c.startsWith("_"))
          c = "UNDERSCORE" + c;
        writer.write(", " + c + "=" + vfn + "$" + c);
      }

      // for tfn_nocnfd
      //            writer.write(tfn_nocnfd + " <- data.frame(" + outName + "=" + vfn + "$" + outName + ", " + t + "=" + vfn + "$" + t);

      writer.write(")\n");

      // to remove NA
      //            writer.write(tfn + " <- " + tfn + "[complete.cases(" + tfn + "),]" + '\n');

      // Only treatement, no confounder (ESP)
      //            writer.write("results[[\"" + t + "\"]] <- CFmeansForESP(" + tfn_nocnfd + ", \"" + outName + "\", \"" + t + "\"");

      // For random forest
      writer
          .write("results[[\"" + t + "\"]] <- CFmeansForDecileBinsRF(" + tfn + ", \"" + outName + "\", \"" + t + "\"");

      // For LM and LASSO
      //            writer.write("results[[\"" + t + "\"]] <- CFmeansForDecileBinsLM(" + tfn + ", \"" + outName + "\", \"" + t + "\"");

      writer.write(")\n\n");
    }
    writer.write("return(results)\n\n");
    writer.write("}\n");
    writer.flush();
    writer.close();
  }
}