package com.github.sommeri.less4j_release_tests;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

public class ExecuteUtils {
  
  private final File workingDirectory;

  public ExecuteUtils(File workingDirectory) {
    super();
    this.workingDirectory = workingDirectory;
  }

  private DefaultExecutor createCommandExecutor() {
    return createCommandExecutor(workingDirectory);
  }

  private DefaultExecutor createCommandExecutor(File directory) {
    DefaultExecutor executor = new DefaultExecutor();
    executor.setWorkingDirectory(directory);
    return executor;
  }

  public void runCommand(String command) throws ExecuteException, IOException {
    DefaultExecutor executor = createCommandExecutor();

    CommandLine cmdLine = CommandLine.parse(command);
    executor.execute(cmdLine);    
  }

  public void runCommand(File subdirectory, String command) throws ExecuteException, IOException {
    DefaultExecutor executor = createCommandExecutor(new File(workingDirectory.getPath() + "\\" + subdirectory));
    
    CommandLine cmdLine = CommandLine.parse(command);
    //executor.setExitValue(1);
    executor.execute(cmdLine);    
  }

  public void runCommandInDir(File directory, String command) throws ExecuteException, IOException {
    DefaultExecutor executor = createCommandExecutor(directory);
    
    CommandLine cmdLine = CommandLine.parse(command);
    executor.execute(cmdLine);    
  }

}
