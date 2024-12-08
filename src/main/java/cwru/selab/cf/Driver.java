package cwru.selab.cf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.google.googlejavaformat.java.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apache.commons.io.FilenameUtils;

public class Driver {
  public static void main(String[] args) throws IOException {
    // create a CharStream that reads from a file at path in the first arg
    CharStream input = CharStreams.fromFileName(args[0]);
    File file = new File(args[0]);
    String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
    new File("output/" + fileNameWithOutExt).mkdir();

   /* Files.copy(Paths.get(args[0]), Paths.get("output/" + fileNameWithOutExt + "/" + file.getName()),StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Paths.get("resources/Fluky.java"), Paths.get("output/" + fileNameWithOutExt + "/Fluky.java"),
        StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Paths.get("resources/MainTestFile.java"),
        Paths.get("output/" + fileNameWithOutExt + "/MainTestFile.java"), StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Paths.get("resources/StructuredDataCollector.java"),
        Paths.get("output/" + fileNameWithOutExt + "/StructuredDataCollector.java"),
        StandardCopyOption.REPLACE_EXISTING);
    Files.copy(Paths.get("resources/RFCIcode.R"), Paths.get("output/" + fileNameWithOutExt + "/RFCIcode.R"),
        StandardCopyOption.REPLACE_EXISTING);
*/
    // create a lexer that feeds off of the input CharStream
    Java8Lexer lexer = new Java8Lexer(input);

    // create a buffer of tokens pulled from the lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // create a parser that feeds off the tokens buffer
    Java8Parser parser = new Java8Parser(tokens);

    TokenStreamRewriter rewriter = new TokenStreamRewriter(tokens);

    ParseTree tree = parser.compilationUnit(); // begin parsing at init rule

    ParseTreeWalker walker = new ParseTreeWalker(); // Standard walker

    Converter converter = new Converter(parser, rewriter);

    walker.walk(converter, tree);
    String formattedSource = "";
    BufferedWriter writer = Files.newBufferedWriter("output/" + fileNameWithOutExt + "/" + fileNameWithOutExt + ".java".toPath());
    BufferedWriter causalMapWriter = Files.newBufferedWriter("output/" + fileNameWithOutExt + "/" + "CausalMap.txt".toPath());

    try {
      formattedSource = new Formatter().formatSource(rewriter.getText());
    } catch (Exception e) {
      
      System.out.println(e.getMessage());
      writer.write(rewriter.getText());
      writer.close();
     for (String key : converter.causalMap.keySet()) {
      causalMapWriter.write(key);
      for (String confounder : converter.causalMap.get(key)) {
        causalMapWriter.write("," + confounder);
      }
      causalMapWriter.write("\n");
    }
    causalMapWriter.close();
      return;
    }
    writer.write(formattedSource);
    writer.close();

    // System.out.println(formattedSource);
    System.out.println(converter.causalMap);
    for (String key : converter.causalMap.keySet()) {
      causalMapWriter.write(key);
      for (String confounder : converter.causalMap.get(key)) {
        causalMapWriter.write("," + confounder);
      }
      causalMapWriter.write("\n");
    }
    causalMapWriter.close();
   
  }
}
