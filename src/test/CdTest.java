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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import commands.Cd;

import java.lang.reflect.Field;

/**
 * Class CdTest runs all the different test cases for Cd
 */
public class CdTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;
	/**
	 * Declare instance of Cd to be tested
	 */
	private static Cd cd;

	/**
	 * Declare two different instance of a String objects called expectedCd and
	 * expectedPath
	 */
	private static String expectedCd, expectedPath;

	/**
	 * Declare two different instance of a String objects called actualCd and
	 * actualPath
	 */
	private static String actualCd, actualPath;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Get the current FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes a Cd Object
		cd = new Cd();
	}

	/**
	 * Destroys the FileSystem after all the testcases have run
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Declares and initializes a Feild variable
		// to the fileSys variable in FileSystem
		Field feild = fs.getClass().getDeclaredField("filesys");
		// Allows the value of this variable in FileSystem to be accessed
		feild.setAccessible(true);
		// Changes the value of the variable in FileSystem to null
		feild.set(null, null);
	}

	/**
	 * Test : User provides no input
	 */
	@Test
	public void testNoArgs() {
		// Declares and initializes an empty array
		String[] emptyArr = {};
		// Expected return from Cd
		expectedCd = "Error : No parameters provided";
		// Expected current working directory
		expectedPath = "/";
		// Actual return from Cd
		actualCd = cd.run(fs, emptyArr, "cd ", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd.equals(expectedCd) && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User provides a relative path
	 */
	@Test
	public void testRelativePath() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/users";
		// Actual return from Cd
		actualCd = cd.run(fs, "users".split(" "), "cd users ", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User provides a relative directory
	 */
	@Test
	public void testRelativeDir() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/users/skeshavaa";
		// Actual return from Cd
		actualCd = cd.run(fs, "users/skeshavaa".split(" "), "cd users/skeshavaa", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User provides a file path instead of a directory
	 */
	@Test
	public void testTraverseFile() {
		// Expected return from Cd
		expectedCd = "Error: Invalid Directory : A2";
		// Expected current working directory
		expectedPath = "/";
		// Actual return from Cd
		actualCd = cd.run(fs, "A2".split(" "), "cd A2", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd.equals(expectedCd) && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User provides an absolute path to a directory
	 */
	@Test
	public void testAbsolutePath() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/documents/journal/week1";
		// Actual return from Cd
		actualCd = cd.run(fs, "/documents/journal/week1".split(" "), "cd /documents/journal/week1", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User uses a special pattern to change directory
	 */
	@Test
	public void testPatternCase1() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/";
		// Sets the current working directory
		fs.setCurrent(fs.documents);
		// Actual return from Cd
		actualCd = cd.run(fs, "..".split(" "), "cd .. ", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User uses a special pattern to change directory
	 */
	@Test
	public void testPatternCase2() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/";
		fs.setCurrent(fs.user1);
		// Actual return from Cd
		actualCd = cd.run(fs, "../..".split(" "), "cd ../.. ", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User uses a special pattern to change directory
	 */
	@Test
	public void testPatternCase3() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/";
		fs.setCurrent(fs.week1);
		// Actual return from Cd
		actualCd = cd.run(fs, "../../../".split(" "), "cd ../../../", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User uses a special pattern to access a subdirectory
	 */
	@Test
	public void testPatternCase4() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/documents";
		// Actual return from Cd
		actualCd = cd.run(fs, "../../documents".split(" "), "cd ../../documents", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User uses a special pattern to stay in current directory
	 */
	@Test
	public void testPatternCase5() {
		// Expected return from Cd
		expectedCd = null;
		// Expected current working directory
		expectedPath = "/documents";
		// Sets the current working directory
		fs.setCurrent(fs.documents);
		// Actual return from Cd
		actualCd = cd.run(fs, ".".split(" "), "cd .", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd == expectedCd && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User provides multiple arguments
	 */
	@Test
	public void testMultipleArgs() {
		// Expected return from Cd
		expectedCd = "Error : Multiple Parameters have been provided : /pics /Sys/LOL";
		// Expected current working directory
		expectedPath = "/";
		// Actual return from Cd
		actualCd = cd.run(fs, "/pics /Sys/LOL".split(" "), "cd /pics /Sys/LOL", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd.equals(expectedCd) && actualPath.equals(expectedPath));
	}

	/**
	 * Test : User uses redirection for a non redirectionable command
	 */
	@Test
	public void testRedirectionError() {
		// Expected return from Cd
		expectedCd = "Error : Redirection Error : cd does not support redirection";
		// Expected current working directory
		expectedPath = "/";
		// Actual return from Cd
		actualCd = cd.run(fs, "/documents > file".split(" "), "cd /documents > file", false);
		// Returns the current working directory
		actualPath = fs.getCurrentPath();
		// Checks if the values are equal or not
		assertTrue(actualCd.equals(expectedCd) && actualPath.equals(expectedPath));
	}
}