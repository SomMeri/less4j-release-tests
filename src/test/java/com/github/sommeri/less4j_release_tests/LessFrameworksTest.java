package com.github.sommeri.less4j_release_tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.ExecuteException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sommeri.less4j_release_tests.compilators.Less4jCompilator;
import com.github.sommeri.less4j_release_tests.compilators.LessJsCompilator;
import com.github.sommeri.less4j_release_tests.normalizers.Normalizator;
import com.github.sommeri.less4j_release_tests.twitterbootstrap.TwitterBootstrap;
import com.github.sommeri.less4j_release_tests.twitterbootstrap.TwitterBootstrap.Tag;
import com.github.sommeri.less4j_release_tests.utils.Logger;
import com.github.sommeri.less4j_release_tests.utils.TestFileUtils;

public class LessFrameworksTest {

  private static final boolean SKIP_LESS4J_COMPILATION = true;

  private static final File workingDirectory = new File("../less4j-release-tests-working-dir/");
  
  private static TwitterBootstrap twitterBootstrap = new TwitterBootstrap(workingDirectory);
  private static Less4jCompilator less4jCompilator = new Less4jCompilator(workingDirectory);
  private static LessJsCompilator lessJsCompilator = new LessJsCompilator();
  private static TimeReporter timeReporter = new TimeReporter();

  private TestFileUtils fileUtils = new TestFileUtils();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    (new TestFileUtils()).ensureDirectory(workingDirectory);
    
    twitterBootstrap.acquireClone();
    less4jCompilator.acquireLatestLess4j(SKIP_LESS4J_COMPILATION);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
    timeReporter.printReport();
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
    long lessJsTime = lessJsCompilator.compileLessFile(inputLess, lessjsCss);
    Normalizator.normalize(lessjsCss, normalizedLessjsCss);

    // compile with less4j
    long less4jTime = less4jCompilator.compileLessFile(inputLess, less4jCss);
    Normalizator.normalize(less4jCss, normalizedLess4jCss);

    timeReporter.addTestRunData(testName, lessJsTime, less4jTime);

    // compare normalized results
    fileUtils.assertSameFileContent(normalizedLessjsCss.getPath(), normalizedLess4jCss.getPath());
  }

  @Test
  public void testTwitterBootstrap_2_3_1() throws Exception {
    twitterBootstrap.checkoutTag(Tag.v2_3_1);
    runTest("testTwitterBootstrap_2_3_1", twitterBootstrap.getBootstrapLess());
  }

}

class TimeReporter {
  
  private List<TestRunData> data = new ArrayList<TimeReporter.TestRunData>();
  
  public void addTestRunData(String testName, long lessJsTime, long less4jTime) {
    data.add(new TestRunData(testName, lessJsTime, less4jTime));
  }

  public void printReport() {
    for (TestRunData run : data) {
      reportTimes(run.getTestName(), run.getLessJsTime(), run.getLess4jTime());
    }
  }

  private void reportTimes(String testName, long lessJsTime, long less4jTime) {
    String timeReport = testName + ": "; 
    timeReport += lessJsTime < less4jTime ? "less.js" : "less4j";
    timeReport += " was faster";
    timeReport += "\n  less.js: " + Logger.formatTimeDifference(lessJsTime);
    timeReport += "\n  less4j: " + Logger.formatTimeDifference(less4jTime);
    
    Logger.log(timeReport);
  }

  private class TestRunData {
    
    private final String testName;
    private final long lessJsTime;
    private final long less4jTime;
    
    public TestRunData(String testName, long lessJsTime, long less4jTime) {
      super();
      this.testName = testName;
      this.lessJsTime = lessJsTime;
      this.less4jTime = less4jTime;
    }

    public String getTestName() {
      return testName;
    }

    public long getLessJsTime() {
      return lessJsTime;
    }

    public long getLess4jTime() {
      return less4jTime;
    }
    
    
  }

}