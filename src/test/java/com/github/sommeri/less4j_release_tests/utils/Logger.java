package com.github.sommeri.less4j_release_tests.utils;

public class Logger {
  public static void log(String string) {
    System.out.println(string);
  }

  public static void logStepEnd() {
    log("-----------------------");
  }

}
