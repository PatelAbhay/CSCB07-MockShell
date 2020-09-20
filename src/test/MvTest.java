// **********************************************************
// Assignment2:
// Student1:
// Author: Shawn Santhoshgeorge
// Github: @ShawnGeorge03
//
// Student2:
// Author: Keshavaa Shaiskandan
// Github: @skeshavaa
//
// Student3:
// Author: Tirth Patel
// Github:@ProgramTP
//
// Student4:
// Author: Abhay Patel
// Github: @PatelAbhay
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import commands.Mv;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class MvTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declare instance of Mkdir to make new directories
	 */
	private static Mv mv;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() {
		// Initializes the class to be tested
		mv = new Mv();
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
	}

	/**
	 * Test : Move a directory into another directory, both using relative paths
	 */
	@Test
	public void testMoveRelativePath() {
		mv.run(fs, "users documents".split(" "), "mv users documents", false);
		boolean expected = false;
		boolean actual = false;
		for (int i = 0; i < fs.getCurrent().getList().size(); i++) {
			if (fs.getCurrent().getList().get(i).getName().equals("users")) {
				actual = true;
				assertEquals(expected, actual);
			} else if (fs.getCurrent().getList().get(i).getName().equals("documents")) {
				for (int j = 0; j < fs.getCurrent().getList().get(i).getList().size(); j++) {
					if (fs.getCurrent().getList().get(i).getList().get(j).getName().equals("users")) {
						actual = false;
						assertEquals(expected, actual);
					}
				}
			}
		}
	}

	/**
	 * Test : Move a directory into another directory based on absolute paths
	 */
	@Test
	public void testMoveAbsolutePath() {
		mv.run(fs, "/users /documents".split(" "), "mv users documents", false);
		boolean expected = false;
		boolean actual = false;
		for (int i = 0; i < fs.getCurrent().getList().size(); i++) {
			if (fs.getCurrent().getList().get(i).getName().equals("users")) {
				actual = true;
				assertEquals(expected, actual);
			} else if (fs.getCurrent().getList().get(i).getName().equals("downloads")) {
				for (int j = 0; j < fs.getCurrent().getList().get(i).getList().size(); j++) {
					if (fs.getCurrent().getList().get(i).getList().get(j).getName().equals("users")) {
						actual = false;
						assertEquals(expected, actual);
					}
				}
			}
		}
	}

	/**
	 * Test : Move a directory into an invalid path, requiring mv to create a
	 * directory
	 */
	@Test
	public void testMoveDirToInvalidPath() {
		String actual = mv.run(fs, "/users clearlyfake".split(" "), "mv /users clearlyfake", false);
		String expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test : Try moving an invalid path
	 */
	@Test
	public void testMoveInvalidDirtoPath() {
		String actual = mv.run(fs, "fake /users".split(" "), "mv fake /users", false);
		String expected = "Error: Directory Not Found : fake does not exist in the path you specified!";
		assertEquals(expected, actual);
	}

	/**
	 * Test : Move an invalid path with a file within it to a valid path
	 */
	@Test
	public void testMoveInvalidPathDirToPath() {
		String actual = mv.run(fs, "fake/user documents".split(" "), "mv fake/user documents", false);
		String expected = "Error: Invalid Directory : fake does not exist!";
		assertEquals(expected, actual);
	}

	/**
	 * Test : Move the root
	 */
	@Test
	public void testMoveRoot() {
		String actual = mv.run(fs, "/ documents".split(" "), "mv / documents", false);
		String expected = "Error: Invalid Directory : Cannot move the root directory";
		assertEquals(expected, actual);
	}

	/**
	 * Test : Try redirecting output to a file
	 */
	@Test
	public void testRedirectionError() {
		String actual = mv.run(fs, "/ documents > text".split(" "), "mv / documents > text", false);
		String expected = "Error : Redirection Error : mv does not support redirection";
		assertEquals(expected, actual);
	}

}