package com.github.sommeri.less4j_release_tests.compilators;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import com.github.sommeri.less4j_release_tests.ReleaseConstants;
import com.github.sommeri.less4j_release_tests.utils.ExecuteUtils;
import com.github.sommeri.less4j_release_tests.utils.Logger;

public class Less4jCompilator {

  private static final String RUN_LESS4J_JAR = "java -jar less4j.jar ";
  private static final String MVN_CLEAN = "clean";
  private static final String MVN_PACKAGE = "package -DskipTests=true -P standalone";
  private static final String MVN_COPY_ALL_DEPENDENCIES = "dependency:copy-dependencies -DexcludeGroupIds="+ReleaseConstants.OPTIONAL_GROUPS+" -DoutputDirectory=" + ReleaseConstants.DEPENDENCIES_SUBDIR.getAbsolutePath();
  
  private static final File LESS4J_SOURCE_CODE_LOCATION = new File("../less4j/");

  private final File commandLineLess4j;
  private final File apiLess4j;
  private final ExecuteUtils executeUtils;
  
  private boolean less4jIsCompiled = false;

  public Less4jCompilator(File workingDirectory) {
    this(workingDirectory, false);
  }

  public Less4jCompilator(File workingDirectory, boolean assumeIsCompiled) {
    commandLineLess4j = new File(workingDirectory, "/less4j.jar");
    apiLess4j = new File(workingDirectory, "/api-less4j.jar");
    executeUtils = new ExecuteUtils(workingDirectory);
    less4jIsCompiled = assumeIsCompiled;
  }
  
  public void acquireLatestLess4j() throws ExecuteException, IOException {
    if (!less4jIsCompiled) {
      ExecuteUtils executeUtils = new ExecuteUtils(LESS4J_SOURCE_CODE_LOCATION);

      try {
        executeUtils.runCommand("mvn " + MVN_CLEAN);
        executeUtils.runCommand("mvn " + MVN_PACKAGE);
      } catch (IOException e) {
        String mavenHome = System.getenv("MAVEN_HOME");
        if (mavenHome == null)
          fail("Unable to run maven.");

        executeUtils.runCommand("\"" + mavenHome + "bin/mvn.bat\" " + MVN_CLEAN);
        executeUtils.runCommand("\"" + mavenHome + "bin/mvn.bat\" " + MVN_PACKAGE);
        executeUtils.runCommand("\"" + mavenHome + "bin/mvn.bat\" " + MVN_COPY_ALL_DEPENDENCIES);
      }
      
      less4jIsCompiled = true;
    }
    
    File freshCommandLineLess4j = findCompiledCommandLineLess4j();
    FileUtils.copyFile(freshCommandLineLess4j, commandLineLess4j);
    
    File freshApiLess4j = findCompiledApiLess4j();
    FileUtils.copyFile(freshApiLess4j, apiLess4j);
  }

  private File findCompiledCommandLineLess4j() {
    return findCompiledLess4j(new ShadedFileFilter(),"command line");
  }

  private File findCompiledApiLess4j() {
    return findCompiledLess4j(new ApiFileFilter(),"api");
  }

  private File findCompiledLess4j(IOFileFilter fileFilter, String name) {
    File compiledResultDirectory = new File(LESS4J_SOURCE_CODE_LOCATION, "target");
    Collection<File> listFiles = FileUtils.listFiles(compiledResultDirectory, fileFilter, null);
    if (listFiles.isEmpty())
      fail("Could not identify compiled "+name+" less4j - no candidate found.");

    if (listFiles.size() > 1)
      fail("Could not identify compiled "+name+" less4j - too many candidates found.");

    return listFiles.iterator().next();
  }

  public long compileLessFile(File input, File output) throws ExecuteException, IOException {
    return compileLessFile(input.getPath(), output.getPath());
  }
  
  public long compileLessFile(String input, String output) throws ExecuteException, IOException {
    long startTime = System.nanoTime();
    Logger.log("compile by less4j");
    String command = RUN_LESS4J_JAR + input + " " + output;
    Logger.log("# " + command);
    executeUtils.runCommand(command);
    Logger.logStepEnd();
    return TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
  }

  public File getApiLess4j() {
    return apiLess4j;
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

class ApiFileFilter implements IOFileFilter {

  public boolean accept(File file) {
    if (file.isDirectory())
      return false;

    String name = file.getName();
    if (!name.startsWith("original-less4j-"))
      return false;

    if (!name.endsWith(".jar"))
      return false;

    return true;
  }

  public boolean accept(File dir, String name) {
    return false;
  }

}
