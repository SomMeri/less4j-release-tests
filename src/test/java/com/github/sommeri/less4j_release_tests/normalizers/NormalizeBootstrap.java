package com.github.sommeri.less4j_release_tests.normalizers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class NormalizeBootstrap {
  
  private static final String BOOTSTRAP_MODIFIED_FILENAME = "bootstrap-normalized.css";
  private static final String BOOTSTRAP_COMPILED_FILENAME = "bootstrap-compiled.css";
  private static final String RESPONSIVE_MODIFIED_FILENAME = "responsive-normalized.css";
  private static final String RESPONSIVE_COMPILED_FILENAME = "responsive-compiled.css";
  private static final String LESS4J_PATH = "c:\\data\\meri\\less4java\\bootstrap-2.3.1\\less4j\\";
  private static final String LESSJS_PATH = "c:\\data\\meri\\less4java\\bootstrap-2.3.1\\less.js\\";
  private static final String LESS4J_RUNNER = "run.bat";
  private static final String LESSJS_RUNNER = "runjs.bat";

  public static void main(String[] args) throws FileNotFoundException, IOException {
    run(LESS4J_PATH, LESS4J_RUNNER);
    run(LESSJS_PATH, LESSJS_RUNNER);
    process(LESS4J_PATH, BOOTSTRAP_COMPILED_FILENAME, BOOTSTRAP_MODIFIED_FILENAME);
    process(LESSJS_PATH, BOOTSTRAP_COMPILED_FILENAME, BOOTSTRAP_MODIFIED_FILENAME);
    process(LESS4J_PATH, RESPONSIVE_COMPILED_FILENAME, RESPONSIVE_MODIFIED_FILENAME);
    process(LESSJS_PATH, RESPONSIVE_COMPILED_FILENAME, RESPONSIVE_MODIFIED_FILENAME);
    System.out.println("!!!!! done !!!!!");
  }

  private static void run(String directory, String filename) {
    
  }

  private static void process(String directory, String compiledFilename, String modifiedFilename) throws IOException, FileNotFoundException {
    String result = "";
    String mainFile = IOUtils.toString(new FileReader(new File(directory+compiledFilename)));
    String[] split = mainFile.split("\n");
    for (String line : split) {
        String normalized = normalizeLine(line);
        if (!normalized.isEmpty())
          result+=normalized+"\n";
    }

    System.out.println(result);
    FileWriter fw = new FileWriter(new File(directory+modifiedFilename));
    IOUtils.write(result, fw);
    fw.flush();
    fw.close();
  }

  private static String normalizeLine(String line) {
    // remove comments
    line = line.replaceAll("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 " );
    // empty lines
    line = line.trim();
    if (line.isEmpty())
      return "";
    
    // colors
    line = line.replaceAll("#000;", "#000000;");
    line = line.replaceAll("#333;", "#333333;");
    line = line.replaceAll("#222;", "#222222;");
    line = line.replaceAll("#222 ", "#222222 ");
    line = line.replaceAll("#444,", "#444444,");
    line = line.replaceAll("#555;", "#555555;");
    line = line.replaceAll("#777;", "#777777;");
    line = line.replaceAll("#999;", "#999999;");
    line = line.replaceAll("#fff;", "#ffffff;");
    line = line.replaceAll("#fff,", "#ffffff,");
    line = line.replaceAll("#eee;", "#eeeeee;");
    line = line.replaceAll("#eee ", "#eeeeee ");
    line = line.replaceAll("#ddd;", "#dddddd;");
    line = line.replaceAll("#ddd ", "#dddddd ");
    line = line.replaceAll("#ccc;", "#cccccc;");
    line = line.replaceAll("#bbb;", "#bbbbbb;");
    line = line.replaceAll("#08c;", "#0088cc;");
    line = line.replaceAll("#08c,", "#0088cc,");
    line = line.replaceAll("#08c\\)", "#0088cc\\)");
    line = line.replaceAll("#fff\\)", "#ffffff\\)");
    line = line.replaceAll("#222\\)", "#222222\\)");
    line = line.replaceAll("#444\\)", "#444444\\)");

    //numbers
    line = line.replaceAll(" .15s ", " 0.15s ");
    line = line.replaceAll(" .35s ", " 0.35s ");
    line = line.replaceAll(" .1s ", " 0.1s ");
    line = line.replaceAll(" .2s ", " 0.2s ");
    line = line.replaceAll(" .6s ", " 0.6s ");
    line = line.replaceAll("\\(.75,", "\\(0.75,");
    line = line.replaceAll("\\(.5,", "\\(0.5,");
    line = line.replaceAll("\\(.25,", "\\(0.25,");
    
    //ignore last decimal place 51.063829787234035%; 
    line = line.replaceAll("(\\d{1,2}\\.\\d{13})\\d%", "$1%" );
    line = line.replaceAll("(\\d{1,2}\\.\\d{14})\\d%", "$1%" );

    return line;
  }

}
