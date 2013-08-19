package com.github.sommeri.less4j_release_tests;

import java.io.File;

import com.github.sommeri.less4j_release_tests.compilators.Less4jCompilator;

public class ReleaseConstants {
  
  public static final File ORIGINAL_WORKING_DIRECTORY = new File(System.getProperty("user.dir"));
  
  public static final File WORKING_DIRECTORY = new File(ORIGINAL_WORKING_DIRECTORY, "../less4j-release-tests-working-dir/");

  public static final File DEPENDENCIES_SUBDIR = new File(ReleaseConstants.WORKING_DIRECTORY, "dependencies");

  private static final boolean SKIP_LESS4J_COMPILATION = false;

  public static final String OPTIONAL_GROUPS = "com.beust";

  public static Less4jCompilator LESS4J_COMPILATOR = new Less4jCompilator(ReleaseConstants.WORKING_DIRECTORY, SKIP_LESS4J_COMPILATION);

}
