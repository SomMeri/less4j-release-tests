package com.github.sommeri.less4j_release_tests.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TestFileUtils {

  public TestFileUtils() {
  }

  public void removeFile(String filename) {
    File file = new File(filename);
    if (file.exists())
      file.delete();
  }

  public void assertFileContent(String lessFile, String expectedContent) {
    String realContent = readFile(lessFile);
    assertEquals(expectedContent, realContent);
  }

  public void assertSameFileContent(String expectedFile, String actualFile) {
    String actualContent = readFile(actualFile);
    String expectedContent = readFile(expectedFile);
    assertEquals(expectedContent, actualContent);
  }

  public void assertFileNotExists(String filename) {
    File file = new File(filename);
    assertFalse("File " + filename + " should not exists.", file.exists());
  }

  public String readFile(String filename) {
    File inputFile = new File(filename);
    if (!inputFile.exists())
      fail("File " + filename + " was not created.");

    try {
      FileReader input = new FileReader(inputFile);
      String expected = IOUtils.toString(input).replace("\r\n", "\n");
      input.close();
      return expected;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String concatenateFiles(String... files) {
    String result = "";
    for (String file : files) {
      result += readFile(file);
    }
    return result;
  }

  public void removeFiles(String... files) {
    for (String file : files) {
      removeFile(file);
    }
  }

  public void ensureCleanExistingDirectory(File directory) throws IOException {
    if (directory.exists()) {
      FileUtils.cleanDirectory(directory);
    } else {
      directory.mkdir();
    }
}

  public void ensureDirectory(File directory) {
    if (!directory.exists()) {
      directory.mkdir();
    }
  }

}
