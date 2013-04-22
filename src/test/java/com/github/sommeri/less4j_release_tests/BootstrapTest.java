package com.github.sommeri.less4j_release_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sommeri.less4j_release_tests.rhino.Global;
import com.github.sommeri.less4j_release_tests.rhino.Main;

//FIXME: ???? test with failing files
public class BootstrapTest extends CommandLineTest {

  private static final boolean SKIP_LESS4J_COMPILATION = true;
  private static final boolean SKIP_BOOTSTRAP_CLONING = true;

  private static final String TWITTER_BOOTSTRAP_CLONE_COMMAND = "git clone https://github.com/twitter/bootstrap.git";
  private static final String TWITTER_BOOTSTRAP_CHECKOUT_TAG = "git checkout v2.3.1"; //TODO: either parametrize or checkout last tag
  private static final File workingDirectory = new File("../less4j-release-tests-working-dir/");
  private static final File bootstrapSubDirectory = new File(workingDirectory, "bootstrap/");
  private static final File bootstrapLess = new File(bootstrapSubDirectory, "less/bootstrap.less");
  private static final File less4jSourceCode = new File("../less4j/");
  private static final String RHINO_LESSJS = "src/test/resources/less.js/less-rhino-1.3.3.js";

  //products 
  private static final File lessjsCss = new File(workingDirectory, "/lessjs-compiled.css");
  private static final File less4jCss = new File(workingDirectory, "/less4j-compiled.css");
  private static final File compiledLess4j = new File(workingDirectory, "/less4j.jar");

  private static ExecuteUtils executeUtils = new ExecuteUtils(workingDirectory);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    compileLatestLess4j();
    //    initializeWorkingDirectory(); //TODO: too slow only pull andchck if possible
    // cloneBootstrap();
  }

  private static void compileLatestLess4j() throws ExecuteException, IOException {
    if (!SKIP_LESS4J_COMPILATION) {
      ExecuteUtils executeUtils = new ExecuteUtils(less4jSourceCode);
      String cleanCommand = "clean";
      String packageCommand = "package -P standalone";

      try {
        executeUtils.runCommand("mvn " + cleanCommand);
        executeUtils.runCommand("mvn " + packageCommand);
      } catch (IOException e) {
        String mavenHome = System.getenv("MAVEN_HOME");
        if (mavenHome == null)
          fail("Unable to run maven.");

        executeUtils.runCommand("\"" + mavenHome + "bin/mvn.bat\" " + cleanCommand);
        executeUtils.runCommand("\"" + mavenHome + "bin/mvn.bat\" " + packageCommand);
      }
    }
    File freshCompiledLess4j = findCompiledLess4j();
    FileUtils.copyFile(freshCompiledLess4j, compiledLess4j);
  }

  private static File findCompiledLess4j() {
    File compiledResultDirectory = new File(less4jSourceCode, "target");
    Collection<File> listFiles = FileUtils.listFiles(compiledResultDirectory, new ShadedFileFilter(), null);
    if (listFiles.isEmpty())
      fail("Could not identify compiled less4j - no candidate found.");

    if (listFiles.size() > 1)
      fail("Could not identify compiled less4j - too many candidates found.");

    return listFiles.iterator().next();
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
    compileByLessJs();

    // compile with less4j
    compileByLess4j();
    // normalize both
    // compare normalized results
    fail("Not yet implemented");
  }

  private void compileByLess4j() throws ExecuteException, IOException {
    executeUtils.runCommand("java -jar less4j.jar " + bootstrapLess.getPath() + " " + less4jCss.getPath());
  }

  private void compileByLessJs() throws Exception {
    log("Init Rhino");
    // original rhino main is taken from here: http://grepcode.com/file/repo1.maven.org/maven2/org.mozilla/rhino/1.7R4/org/mozilla/javascript/tools/shell/Main.java#Main.exec%28java.lang.String[]%29
    Global _global = new Global();
    _global.setErr(new PrintStream(errContent));
    PrintStream out = new PrintStream(lessjsCss);
    _global.setOut(out);
    Main.main(_global, (String[]) Arrays.asList(RHINO_LESSJS, bootstrapLess.getPath()).toArray());
    assertEquals("", errContent.toString());
    out.flush();
    out.close();
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

class ShadedFileFilter implements IOFileFilter {

  public boolean accept(File file) {
    if (file.isDirectory())
      return false;

    String name = file.getName();
    if (!name.startsWith("less4j-"))
      return false;

    if (!name.endsWith("-SNAPSHOT-shaded.jar"))
      return false;

    return true;
  }

  public boolean accept(File dir, String name) {
    return false;
  }

}
