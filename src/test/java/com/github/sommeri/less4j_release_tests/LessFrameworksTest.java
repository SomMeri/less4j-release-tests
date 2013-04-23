package com.github.sommeri.less4j_release_tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.exec.ExecuteException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sommeri.less4j_release_tests.compilators.Less4jCompilator;
import com.github.sommeri.less4j_release_tests.compilators.LessJsCompilator;
import com.github.sommeri.less4j_release_tests.normalizers.Normalizator;
import com.github.sommeri.less4j_release_tests.twitterbootstrap.TwitterBootstrap;
import com.github.sommeri.less4j_release_tests.twitterbootstrap.TwitterBootstrap.Tag;
import com.github.sommeri.less4j_release_tests.utils.TestFileUtils;

//FIXME: ???? test with failing maven compilation
public class LessFrameworksTest {

  private static final boolean SKIP_LESS4J_COMPILATION = false;

  private static final File workingDirectory = new File("../less4j-release-tests-working-dir/");
  
  private static TwitterBootstrap twitterBootstrap = new TwitterBootstrap(workingDirectory);
  private static Less4jCompilator less4jCompilator = new Less4jCompilator(workingDirectory);
  private static LessJsCompilator lessJsCompilator = new LessJsCompilator();

  private TestFileUtils fileUtils = new TestFileUtils();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    (new TestFileUtils()).ensureDirectory(workingDirectory);
    
    twitterBootstrap.acquireClone();
    less4jCompilator.acquireLatestLess4j(SKIP_LESS4J_COMPILATION);
  }

  @Test
  public void testTwitterBootstrapResponsive_2_3_1() throws Exception {
    twitterBootstrap.checkoutTag(Tag.v2_3_1);
    runTest("testTwitterBootstrapResponsive_2_3_1", twitterBootstrap.getBootstrapResponsiveLess());
  }

  private void runTest(String testName, File inputLess) throws IOException, Exception, FileNotFoundException, ExecuteException {
    File testInDirectory = new File(workingDirectory, testName);
    fileUtils.ensureCleanExistingDirectory(testInDirectory);

    File lessjsCss = new File(testInDirectory, "/lessjs-compiled.css");
    File normalizedLessjsCss = new File(testInDirectory, "/lessjs-normalized.css");
    File less4jCss = new File(testInDirectory, "/less4j-compiled.css");
    File normalizedLess4jCss = new File(testInDirectory, "/less4j-normalized.css");

    // compile with less.js
    lessJsCompilator.compileLessFile(inputLess, lessjsCss);
    Normalizator.normalize(lessjsCss, normalizedLessjsCss);

    // compile with less4j
    less4jCompilator.compileLessFile(inputLess, less4jCss);
    Normalizator.normalize(less4jCss, normalizedLess4jCss);

    // compare normalized results
    fileUtils.assertSameFileContent(normalizedLessjsCss.getPath(), normalizedLess4jCss.getPath());
  }

  @Test
  public void testTwitterBootstrap_2_3_1() throws Exception {
    twitterBootstrap.checkoutTag(Tag.v2_3_1);
    runTest("testTwitterBootstrap_2_3_1", twitterBootstrap.getBootstrapLess());
  }

}
