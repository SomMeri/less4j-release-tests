package com.github.sommeri.less4j_release_tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.github.sommeri.less4j_release_tests.rhino.Global;
import com.github.sommeri.less4j_release_tests.rhino.Main;
import com.github.sommeri.less4j_release_tests.rhino.MyLessCss;

public class BootstrapTest extends CommandLineTest {

  private static final String TWITTER_BOOTSTRAP_CLONE_COMMAND = "git clone https://github.com/twitter/bootstrap.git";
  private static final String TWITTER_BOOTSTRAP_CHECKOUT_TAG = "git checkout v2.3.1"; //TODO: either parametrize or checkout last tag
  private static final File workingDirectory = new File("../less4j-release-tests-working-dir/");
  private static final File bootstrapSubDirectory = new File(workingDirectory, "bootstrap/");
  private static final File bootstrapLess = new File(bootstrapSubDirectory, "less/bootstrap.less");
  private static final String RHINO_LESSJS = "src/test/resources/less.js/less-rhino-1.3.3.js";
  private static final String RHINO_INIT_LESSJS = "src/test/resources/less.js/init.js";
  private static final String RHINO_RUN_LESSJS = "src/test/resources/less.js/run.js";

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
    Main.main(new Global(), (String[]) Arrays.asList(RHINO_LESSJS, bootstrapLess.getPath()).toArray());
    System.out.println(outContent.toString());
    System.out.println(errContent.toString());

  }

  private void compileLessjs2() throws Exception {
    log("Init Rhino");
    Context rhinoCx = Context.enter();
    Scriptable scope = rhinoCx.initStandardObjects();

    log("load " + RHINO_LESSJS);
    //    LessCss lessCss = new LessCss();
    MyLessCss lessCss = new MyLessCss();
    String css = lessCss.less("@import 'other.less'; h1 {declaration: @a;} @a:123;");
    System.out.println(outContent.toString());

  }

  //  private void compileLessjs() throws Exception {
  //    log("Init Rhino");
  //    Context rhinoCx = Context.enter();   
  //    Scriptable scope = rhinoCx.initStandardObjects();
  //    
  //    log("load " + RHINO_LESSJS);
  //    rhinoCx.evaluateReader(scope, new FileReader(new File(RHINO_LESSJS)), RHINO_LESSJS, 1, null);
  //    rhinoCx.evaluateReader(scope, new FileReader(new File(RHINO_INIT_LESSJS)), RHINO_INIT_LESSJS, 1, null);
  //    rhinoCx.evaluateReader(scope, new FileReader(new File(RHINO_RUN_LESSJS)), RHINO_RUN_LESSJS, 1, null);
  //  }

  private void checkoutTag() throws ExecuteException, IOException {
    log("# " + TWITTER_BOOTSTRAP_CHECKOUT_TAG);
    executeUtils.runCommand(bootstrapSubDirectory, TWITTER_BOOTSTRAP_CHECKOUT_TAG);
    log("# tag done");
  }

  private static void log(String string) {
    System.out.println(string);
  }

}
