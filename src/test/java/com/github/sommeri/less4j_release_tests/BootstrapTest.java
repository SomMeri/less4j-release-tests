package com.github.sommeri.less4j_release_tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sommeri.less4j_release_tests.rhino.Global;
import com.github.sommeri.less4j_release_tests.rhino.Main;

public class BootstrapTest extends CommandLineTest {

  private static final String TWITTER_BOOTSTRAP_CLONE_COMMAND = "git clone https://github.com/twitter/bootstrap.git";
  private static final String TWITTER_BOOTSTRAP_CHECKOUT_TAG = "git checkout v2.3.1"; //TODO: either parametrize or checkout last tag
  private static final File workingDirectory = new File("../less4j-release-tests-working-dir/");
  private static final File bootstrapSubDirectory = new File(workingDirectory, "bootstrap/");
  private static final File bootstrapLess = new File(bootstrapSubDirectory, "less/bootstrap.less");
  private static final String RHINO_LESSJS = "src/test/resources/less.js/less-rhino-1.3.3.js";

  private static ExecuteUtils executeUtils = new ExecuteUtils(workingDirectory);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    //    initializeWorkingDirectory(); //TODO: too slow only pull andchck if possible
    //    cloneBootstrap();
    //checkLess4jVersion();
  }

  private static void cloneBootstrap() throws Exception {
    log("# " + TWITTER_BOOTSTRAP_CLONE_COMMAND);
    executeUtils.runCommand(TWITTER_BOOTSTRAP_CLONE_COMMAND);
    log("# clone done");
  }

  private static void initializeWorkingDirectory() throws IOException {
    if (workingDirectory.exists()) {
      FileUtils.cleanDirectory(workingDirectory);
    } else {
      workingDirectory.mkdir();
    }
  }

  @Test
  public void test() throws Exception {
    // checkout tag
    checkoutTag();

    // compile with less.js
    compileLessjs();

    // compile with less4j
    // normalize both
    // compare normalized results
    fail("Not yet implemented");
  }

  private void compileLessjs() throws Exception {
    log("Init Rhino");
    // original main is taken from here: http://grepcode.com/file/repo1.maven.org/maven2/org.mozilla/rhino/1.7R4/org/mozilla/javascript/tools/shell/Main.java#Main.exec%28java.lang.String[]%29
    Global _global = new Global();
    _global.setErr(new PrintStream(errContent));
    _global.setOut(new PrintStream(outContent));
    Main.main(_global, (String[]) Arrays.asList(RHINO_LESSJS, bootstrapLess.getPath()).toArray());
    System.out.println(outContent.toString());
    System.out.println(errContent.toString());

  }

  private void checkoutTag() throws ExecuteException, IOException {
    log("# " + TWITTER_BOOTSTRAP_CHECKOUT_TAG);
    executeUtils.runCommand(bootstrapSubDirectory, TWITTER_BOOTSTRAP_CHECKOUT_TAG);
    log("# tag done");
  }

  private static void log(String string) {
    System.out.println(string);
  }

}
