package com.github.sommeri.less4j_release_tests.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
  
  private static DateFormat formatter = new SimpleDateFormat("mm:ss:SSS");
  
  public static void log(String string) {
    System.out.println(string);
  }

  public static void logStepEnd() {
    log("-----------------------");
  }
  
  public static String formatTimeDifference(long timeInMiliseconds) {
    Date date = new Date(timeInMiliseconds);
    return formatter.format(date);
  }

}
