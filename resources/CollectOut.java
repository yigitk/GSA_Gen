package AntlrExperiments;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CollectOut{

	public static HashMap<String, String> __versionMap__ = new HashMap<String, String>();

	  public static void record(
	      String packageName,
	      String clazz,
	      String method,
	      int line,
	      int staticScope,
	      String variableName,
	      Object value,
	      int version) {
	      //int version = Integer.parseInt(versionStr.trim());
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

}

