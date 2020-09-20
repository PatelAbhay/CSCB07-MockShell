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

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import commands.Push;

public class PushTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declares instance of Push to be tested
	 */
	private static Push push;

	/**
	 * Declare two different instance of a String objects called expected and actual
	 */
	private String actual, expected;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes the class to be tested
		push = new Push();
	}

	/**
	 * Destroys the MockFileSystem after each testcases have run
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Declares and initializes a Feild variable
		// to the fileSys variable in MockFileSystem
		Field feild = fs.getClass().getDeclaredField("filesys");
		// Allows the value of this variable in MockFileSystem to be accessed
		feild.setAccessible(true);
		// Changes the value of the variable in MockFileSystem to null
		feild.set(null, null);
	}

	/**
	 * Test : User provides an relative directory path
	 */
	@Test
	public void testRelativePath() {
		// Expected return from Push
		expected = null;
		// Actual return from Push
		actual = push.run(fs, "users/skeshavaa".split(" "), "pushd users/skeshavaa", false);
		// Check the return from Push and the current working directory
		assertTrue(actual == expected && fs.getCurrentPath().equals("/users/skeshavaa"));
	}

	/**
	 * Test : User provides an absolute directory path
	 */
	@Test
	public void testAbsolutePath() {
		// Expected return from Push
		expected = null;
		// Actual return from Push
		actual = push.run(fs, "/documents/journal".split(" "), "pushd /documents/journal", false);
		// Check the return from Push and the current working directory
		assertTrue(actual == expected && fs.getCurrentPath().equals("/documents/journal"));
	}

	/**
	 * Test : User provides an invalid directory path
	 */
	@Test
	public void testInvalidPath() {
		// Expected return from Push
		expected = "Error: Invalid Directory : wrongpath is not a valid directory";
		// Actual return from Push
		actual = push.run(fs, "wrongpath".split(" "), "pushd wrongpath", false);
		// Check the return from Push and the current working directory
		assertTrue(actual.equals(expected) && fs.getCurrentPath().equals("/"));
	}

	/**
	 * Test : User provides no arguments
	 */
	@Test
	public void testNoArguments() {
		// Declares an empty String array
		String[] path = {};
		// Expected return from Push
		expected = "Error : No parameters provided";
		// Actual return from Push
		actual = push.run(fs, path, "pushd ", false);
		// Check the return from Push and the current working directory
		assertTrue(actual.equals(expected) && fs.getCurrentPath().equals("/"));
	}

	/**
	 * Test : User provides multiple paths
	 */
	@Test
	public void testMultipleArguments() {
		// Expected return from Push
		expected = "Error : Multiple Parameters have been provided : "
				+ "hi hello bye Only 1 valid directory path is required";
		// Actual return from Push
		actual = push.run(fs, "hi hello bye".split(" "), "pushd hi hello bye", false);
		// Check the return from Push and the current working directory
		assertTrue(actual.equals(expected) && fs.getCurrentPath().equals("/"));
	}

	/**
	 * Test : User tries to use redirection on a non redirectionable command
	 */
	@Test
	public void testRedirectionError() {
		// Expected return from Push
		expected = "Error : Redirection Error : pushd does not support redirection";
		// Actual return from Push
		actual = push.run(fs, "/users > test".split(" "), "pushd /users > test", false);
		// Check the return from Push and the current working directory
		assertTrue(actual.equals(expected) && fs.getCurrentPath().equals("/"));
	}

}