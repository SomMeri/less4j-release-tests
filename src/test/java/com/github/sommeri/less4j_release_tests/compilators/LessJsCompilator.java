package com.github.sommeri.less4j_release_tests.compilators;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

import com.github.sommeri.less4j_release_tests.compilators.rhino.Global;
import com.github.sommeri.less4j_release_tests.compilators.rhino.Main;
import com.github.sommeri.less4j_release_tests.utils.Logger;

public class LessJsCompilator {
  
  private static final String RHINO_LESSJS = "src/test/resources/less.js/less-rhino-1.3.3.js";

  public void compileLessFile(File input, File output) throws Exception {
    compileLessFile(input.getPath(), output.getPath());
  }
  
  public void compileLessFile(String input, String output) throws Exception {
    Logger.log("compile with Rhino less.js");
    
    Global _global = new Global();
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    _global.setErr(new PrintStream(errContent));
    
    Main.main(_global, (String[]) Arrays.asList(RHINO_LESSJS, input, output).toArray());
    assertEquals("", errContent.toString());
    
    Logger.logStepEnd();
  }


}
