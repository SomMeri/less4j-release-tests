package com.github.sommeri.less4j_release_tests;

import org.junit.BeforeClass;

import com.github.sommeri.less4j_release_tests.utils.TestFileUtils;

public class AbstractLess4jTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    (new TestFileUtils()).ensureDirectory(ReleaseConstants.WORKING_DIRECTORY);
    
    ReleaseConstants.LESS4J_COMPILATOR.acquireLatestLess4j();
  }


}
