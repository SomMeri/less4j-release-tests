package com.github.sommeri.less4j_release_tests.compilators;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.github.sommeri.less4j_release_tests.compilators.rhino.Global;
import com.github.sommeri.less4j_release_tests.compilators.rhino.Main;
import com.github.sommeri.less4j_release_tests.utils.Logger;

public class LessJsCompilator {
  
  private static final String RHINO_LESSJS = "src/test/resources/less.js/less-rhino-1.3.3.js";

  public long compileLessFile(File input, File output) throws Exception {
    return compileLessFile(input.getPath(), output.getPath());
  }
  
  public long compileLessFile(String input, String output) throws Exception {
    long startTime = System.nanoTime();
    Logger.log("compile with Rhino less.js");
    
    Global _global = new Global();
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    _global.setErr(new PrintStream(errContent));
    
    Main.main(_global, (String[]) Arrays.asList(RHINO_LESSJS, input, output).toArray());
    assertEquals("", errContent.toString());
    
    return TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
  }


}
