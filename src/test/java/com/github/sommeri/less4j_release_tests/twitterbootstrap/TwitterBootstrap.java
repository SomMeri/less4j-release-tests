package com.github.sommeri.less4j_release_tests.twitterbootstrap;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.ExecuteException;

import com.github.sommeri.less4j_release_tests.utils.ExecuteUtils;
import com.github.sommeri.less4j_release_tests.utils.Logger;
import com.github.sommeri.less4j_release_tests.utils.TestFileUtils;

public class TwitterBootstrap {

  private static final String CLONE = "git clone https://github.com/twitter/bootstrap.git";
  private static final String CHECKOUT_TAG = "git checkout #TAG#";
  private static final String CHECKOUT_MASTER = "git checkout master";
  private static final String PULL = "git pull";

  private final File workingDirectory;
  private final File bootstrapClone;
  private final File bootstrapLess;
  private final File bootstrapResponsiveLess;

  private final ExecuteUtils executeUtils;
  private TestFileUtils fileUtils = new TestFileUtils();

  public TwitterBootstrap(File workingDirectory) {
    this.workingDirectory = workingDirectory;
    bootstrapClone = new File(workingDirectory, "bootstrap/");
    executeUtils = new ExecuteUtils(bootstrapClone);
    bootstrapLess = new File(bootstrapClone, "less/bootstrap.less");
    bootstrapResponsiveLess = new File(bootstrapClone, "less/responsive.less");
  }

  public void acquireClone() throws Exception {
    Logger.log("Acquiring Twitter Bootstrap.");
    if (bootstrapOK()) {
      checkoutHeadAndPull();
    } else {
      cloneRepository(); 
    }
    Logger.logStepEnd();
  }

  private boolean bootstrapOK() {
    File gitRepo = new File(bootstrapClone, ".git/");
    return gitRepo.exists();
  }

  private void checkoutHeadAndPull() throws ExecuteException, IOException {
    Logger.log("# " + CHECKOUT_MASTER);
    executeUtils.runCommand(CHECKOUT_MASTER);
    Logger.log("# " + PULL);
    executeUtils.runCommand(PULL);
  }

  private void cloneRepository() throws Exception {
    Logger.log("cleaning " + bootstrapClone.getPath());
    fileUtils.ensureCleanExistingDirectory(bootstrapClone);
    Logger.log("# " + CLONE);
    (new ExecuteUtils(workingDirectory)).runCommand(CLONE);
  }

  public void checkoutTag(Tag tag) throws ExecuteException, IOException {
    Logger.log("Checkout twitter bootstrap tag");
    String command = CHECKOUT_TAG.replaceAll("#TAG#", tag.getTagName());
    Logger.log("# " + command);
    executeUtils.runCommand(command);
    Logger.logStepEnd();
  }

  public File getBootstrapResponsiveLess() {
    return bootstrapResponsiveLess;
  }

  public File getBootstrapLess() {
    return bootstrapLess;
  }

  public enum Tag {
    v2_3_1;
    
    public String getTagName() {
      return name().replaceAll("_", ".");
    }
  }
    
}
