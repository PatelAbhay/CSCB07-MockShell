package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import commands.Mkdir;

import java.lang.reflect.Field;

public class MkdirTest {

  private static MockFileSystem fs;
  private static Mkdir mkdir;

  private String expected;
  private String actual;

  private static ArrayList<String> allContent;

  @Before
  public void setUp() throws Exception {
    fs = MockFileSystem.getMockFileSys("MOCKENV");
    mkdir = new Mkdir();

    allContent = new ArrayList<String>();
  }

  @After
  public void tearDown() throws Exception {
    Field feild = fs.getClass().getDeclaredField("filesys");
    feild.setAccessible(true);
    feild.set(null, null);
  }

  private void collectContent(String path) {
    allContent.clear();
    if (!path.equals("/")) {
      for (int i = 0; i < fs.getCurrent().getList().size(); i++) {
        if (fs.getCurrent().getList().get(i).getName().equals(path)) {
          fs.setCurrent(fs.getCurrent().getList().get(i));
          break;
        }
      }
    }
    for (int i = 0; i < fs.getCurrent().getList().size(); i++)
      allContent.add(fs.getCurrent().getList().get(i).getName());
  }

  @Test
  public void testANoPath() {
    expected = "Error: Invalid Argument : Expected at least 1 argument";
    actual = mkdir.run(fs, "mkdir", false);
    collectContent("/");
    assertTrue(actual.equals(expected));
  }

  @Test
  public void testBInvalidName() {
    expected = "Error: Invalid Directory : ... is not a valid directory name";
    actual = mkdir.run(fs, "mkdir ...", false);
    collectContent("/");
    assertTrue(actual.equals(expected));
  }

  @Test
  public void testCInvalidPath() {
    expected = "Error: Invalid Directory : /hello is not a valid directory";
    actual = mkdir.run(fs, "mkdir /hello/hi", false);
    collectContent("/");
    assertTrue(actual.equals(expected));
  }

  @Test
  public void testDValidAbsolutePath() {
    expected = null;
    actual = mkdir.run(fs, "mkdir /System32", false);
    collectContent("/");
    assertTrue(allContent.contains("System32") && actual == expected);
  }

  @Test
  public void testEValidRelativePath() {
    expected = null;
    actual = mkdir.run(fs, "mkdir System32", false);
    collectContent("/");
    assertTrue(allContent.contains("System32") && actual == expected);
  }

  @Test
  public void testFRepeatedPath() {
    expected = "Invalid Directory: users already exists in /";
    actual = mkdir.run(fs, "mkdir /users", false);
    collectContent("/");
    assertTrue(actual.equals(expected));
  }

  @Test
  public void testGRelativeFromDirPath() {
    expected = null;
    actual = mkdir.run(fs, "mkdir desktop/project", false);
    collectContent("desktop");
    assertTrue(allContent.equals(Arrays.asList("project".split(" "))) && actual == expected);
  }

  @Test
  public void testHMultipleArgsRelativePaths() {
    expected = null;
    actual = mkdir.run(fs, "mkdir Business A6", false);
    collectContent("/");
    assertTrue(allContent.containsAll(Arrays.asList("Business A6".split(" "))) && actual == expected);
  }

  @Test
  public void testIAbsolutePaths() {
    expected = null;
    actual = mkdir.run(fs, "mkdir /A3 /A5", false);
    collectContent("/");
    assertTrue(allContent.containsAll(Arrays.asList("A3 A5".split(" "))) && actual == expected);
  }

  @Test
  public void testJMultipleArgsOneDoesNotExist() {
    expected = "Error: Invalid Directory : /lol is not a valid directory";
    actual = mkdir.run(fs, "mkdir /lol/F anotherFile", false);
    collectContent("/");
    assertTrue(!allContent.contains("anotherFile") && actual.equals(expected));
  }

  /**
   * Test K : User uses redirection for a non redirectionable command
   */
  @Test
  public void testKRedirectionError() {
    // Expected return from Cd
    expected = "Error : Redirection Error : mkdir does not support redirection";
    actual = mkdir.run(fs, "mkdir hello > text", false);
    collectContent("/");
    assertTrue(!allContent.contains("hello") && actual.equals(expected));
  }

}
