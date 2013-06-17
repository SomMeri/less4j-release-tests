package com.github.sommeri.less4j_release_tests.normalizers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

public class Normalizator {

  public static void normalize(File compiledCss, File normalizedFile) throws IOException, FileNotFoundException {
    String result = "";
    String mainFile = IOUtils.toString(new FileReader(compiledCss));
    String[] split = mainFile.split("\n");
    split = removeHeader(split);

    for (String line : split) {
      String normalized = normalizeLine(line);
      if (!normalized.isEmpty())
        result += normalized + "\n";
    }

    FileWriter fw = new FileWriter(normalizedFile);
    IOUtils.write(result, fw);
    fw.flush();
    fw.close();
  }

  private static String[] removeHeader(String[] split) {
    if (split.length == 0)
      return split;

    if (!split[0].startsWith("/*!"))
      return split;

    int idx = 1;
    while ((idx < split.length) && split[idx].startsWith(" *")) {
      idx++;
    }

    if ((idx < split.length) && split[idx].startsWith(" */")) {
      idx++;
    }

    return Arrays.copyOfRange(split, idx, split.length);
  }

  private static String normalizeLine(String line) {
    // remove comments
    line = line.replaceAll("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/", "$1 ");
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
    line = line.replaceAll("0,0,0,.075", "0, 0, 0, 0.075");
    line = line.replaceAll(", ", ",");
    line = line.replaceAll(",\\.", ",0\\.");

    //special symbols
    line = line.replaceAll(" = ", "=");

    //ignore last decimal place 51.063829787234035%; 
    line = line.replaceAll("(\\d{1,2}\\.\\d{13})\\d%", "$1%");
    line = line.replaceAll("(\\d{1,2}\\.\\d{14})\\d%", "$1%");

    //whitespaces 
    line = line.replaceAll("@page  \\{", "@page \\{"); 
    line = line.replaceAll("background-position: 0      0;", "background-position: 0 0;"); 
    line = line.replaceAll("display: none !important ;", "display: none !important;");
    
    return line;
  }
}
