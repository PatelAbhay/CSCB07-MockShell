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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commands.Ls;

public class LsTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declare instance of Mkdir to make new directories
	 */
	private static Ls ls;

	/**
	 * Declare two different instance of a String objects called expected and actual
	 */
	private static String expected, actual;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() {
		// Initializes the class to be tested
		ls = new Ls();
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
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
		// Declares an empty array
		String[] input = {};
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, input, "ls", false);
		// Expected return from Ls
		expected = "A2\ndesktop\nusers\ndocuments\ndownloads\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides a relative path
	 */
	@Test
	public void testRelativePath() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "users".split(" "), "ls users", false);
		// Expected return from Ls
		expected = "Path: /users\nskeshavaa\nguest\n\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides an absolute path
	 */
	@Test
	public void testAbsolutePath() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "/documents".split(" "), "ls /documents", false);
		// Expected return from Ls
		expected = "Path: /documents\ntxtone\ntxttwo\njournal\n\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides the special character -R
	 */
	@Test
	public void testRecurisveMode() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "-R".split(" "), "ls -R", false);
		// Expected return from Ls
		expected = "Path: /\nA2\ndesktop\nusers\ndocuments\ndownloads\n\nPath: /desktop\n\nPath: "
				+ "/users\nskeshavaa\nguest\n\nPath: /users/skeshavaa"
				+ "\n\nPath: /users/guest\n\nPath: /documents\ntxtone"
				+ "\ntxttwo\njournal\n\nPath: /documents/journal\nweek1\n\nPath: /documents/journal/week1\n\nPath: /downloads\nhomework\nGames\n\nPath: /downloads/homework"
				+ "\nHW8\n\nPath: /downloads/Games\n\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides multiple valid paths
	 */
	@Test
	public void testMultiplePaths() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "users documents".split(" "), "ls users documents", false);
		// Expected return from Ls
		expected = "Path: /users\nskeshavaa\nguest\n\nPath: /documents\ntxtone\ntxttwo\njournal\n\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides an invalid path
	 */
	@Test
	public void testInvalidPath() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "falsepaths".split(" "), "ls falsepaths", false);
		// Expected return from Ls
		expected = "Error: Invalid Directory : falsepaths is not a valid directory\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides multiple paths, with one invalid path
	 */
	@Test
	public void testMultiplePathsWithInvalidPath() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "users falsepaths".split(" "), "ls users falsepaths", false);
		// Expected return from Ls
		expected = "Error: Invalid Directory : falsepaths is not a valid directory\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User uses redirection to overwrite with new content to file with
	 * result from Ls
	 */
	@Test
	public void testRedirectionOverwrite() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "> A2".split(" "), "ls > A2", false);
		// Expected return from Ls
		expected = "A2\ndesktop\nusers\ndocuments\ndownloads\n";
		// Checks if the values are equal or not
		assertTrue(actual == null && expected.equals(fs.findFile("A2", false).getContent()));
	}

	/**
	 * Test : User uses redirection to append new content to file with result from
	 * Ls
	 */
	@Test
	public void testRedirectionAppend() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "-R >> A2".split(" "), "ls -R >> A2", false);
		// Expected return from Ls
		expected = "Wow what a project\nPath: /\nA2\ndesktop\nusers\ndocuments\ndownloads\n\nPath: /desktop\n\nPath: "
				+ "/users\nskeshavaa\nguest\n\nPath: /users/skeshavaa"
				+ "\n\nPath: /users/guest\n\nPath: /documents\ntxtone"
				+ "\ntxttwo\njournal\n\nPath: /documents/journal\nweek1\n\nPath: /documents/journal/week1\n\nPath: /downloads\nhomework\nGames\n\nPath: /downloads/homework"
				+ "\nHW8\n\nPath: /downloads/Games\n\n";
		// Checks if the values are equal or not
		assertTrue(actual == null && expected.equals(fs.findFile("A2", false).getContent()));
	}

	/**
	 * Test : User provides no file name for redirection
	 */
	@Test
	public void testRedirectionErrorCase1() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, ">".split(" "), "ls >", false);
		// Expected return from Ls
		expected = "Error : No parameters provided for redirection";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides multiple file names for redirection
	 */
	@Test
	public void testRedirectionErrorCase2() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "> LOL polo".split(" "), "ls > LOL polo", false);
		// Expected return from Ls
		expected = "Error : Multiple Parameters have been provided : [LOL, polo] Only one is required for redirection";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides invalid command and uses redirection
	 */
	@Test
	public void testRedirectionErrorCase3() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "users falsepaths > file".split(" "), "ls users falsepaths > file", false);
		// Expected return from Ls
		expected = "Error: Invalid Directory : falsepaths is not a valid directory\n";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides an invalid file name for redirection
	 */
	@Test
	public void testRedirectionErrorCase4() {
		// Actual return from Ls after the operation has been run
		actual = ls.run(fs, "users >> Hello$".split(" "), "ls users >> Hello$", false);
		// Expected return from Ls
		expected = "Error: Invalid File : Hello$ is not a valid file name";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}
}