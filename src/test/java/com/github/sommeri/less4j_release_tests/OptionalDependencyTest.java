package com.github.sommeri.less4j_release_tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import com.github.sommeri.less4j_release_tests.compilators.Less4jCompilator;

public class OptionalDependencyTest {
  private Less4jCompilator less4jCompilator = ReleaseConstants.LESS4J_COMPILATOR;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    AbstractLess4jTest.setUpBeforeClass();
  }

  @Test
  public void testOptional() throws Exception {
    Object obj = createLess4jInstance();

    Method method = obj.getClass().getMethod("compile", new Class[] { String.class });
    Object result = method.invoke(obj, "* { color: blue; } pre > .class { color: red; } ");
    assertNotNull("Nothing returned.", result);
  }

  private Object createLess4jInstance() {
    JarClassLoader jcl = loadLess4jApiWithDependencies();

    JclObjectFactory factory = JclObjectFactory.getInstance();
    Object obj = factory.create(jcl, "com.github.sommeri.less4j.core.DefaultLessCompiler");
    return obj;
  }

  private JarClassLoader loadLess4jApiWithDependencies() {
    JarClassLoader jcl = new JarClassLoader();
    jcl.add(less4jCompilator.getApiLess4j().getAbsolutePath());
    
    File dependencies = ReleaseConstants.DEPENDENCIES_SUBDIR;
    File[] listFiles = dependencies.listFiles((FileFilter)new SuffixFileFilter(".jar"));
    for (File file : listFiles) {
      jcl.add(file.getAbsolutePath());
    }
    
    return jcl;
  }
}
