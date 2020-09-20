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

import commands.Pwd;

import java.lang.reflect.Field;

/**
 * Class PwdTest is used to test Pwd Class
 */
public class PwdTest {
	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declares instance of Pwd to be tested
	 */
	private static Pwd pwd;

	/**
	 * Delacres an empty String array
	 */
	private String[] emptyArr = {};

	/**
	 * Declare two different instance of a String objects called expectedPath and
	 * actualPath
	 */
	private static String expectedPath, actualPath;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() throws Exception {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes the class to be tested
		pwd = new Pwd();
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
	 * Test : User runs the first command as pwd
	 */
	@Test
	public void testInitialPath() {
		// Expected return from pwd
		expectedPath = "/";
		// Actual return from pwd
		actualPath = pwd.run(fs, emptyArr, "pwd", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : User runs the command after changing working directory
	 */
	@Test
	public void testChangeDirectory() {
		// Changes the current working directory
		fs.setCurrent(fs.users);
		// Expected return from pwd
		expectedPath = "/users";
		// Actual return from pwd
		actualPath = pwd.run(fs, emptyArr, "pwd", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : Users changes the working directory to a subdirectory
	 */
	@Test
	public void testChangeSubDirectory() {
		// Changes the current working directory
		fs.setCurrent(fs.homework);
		// Expected return from pwd
		expectedPath = "/downloads/homework";
		// Actual return from pwd
		actualPath = pwd.run(fs, emptyArr, "pwd", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : User changes to the root directory
	 */
	@Test
	public void testChangeToRoot() {
		// Changes the current working directory
		fs.setCurrent(fs.root);
		// Expected return from pwd
		expectedPath = "/";
		// Actual return from pwd
		actualPath = pwd.run(fs, emptyArr, "pwd", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : User provides multiple arguments
	 */
	@Test
	public void testMultipleArgs() {
		// Expected return from pwd
		expectedPath = "Error: Invalid Argument : pwd doesn't take any arguments";
		// Actual return from pwd
		actualPath = pwd.run(fs, "/Sys/IO /users".split(" "), "pwd /Sys/IO /users", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : User uses redirction to overwrite a file with current path
	 */
	@Test
	public void testOverwriteToFile() {
		// Changes the current working directory
		fs.setCurrent(fs.games);
		// Expected return from pwd
		expectedPath = null;
		// Actual return from pwd
		actualPath = pwd.run(fs, "> /A2".split(" "), "pwd > /A2", false);
		// Compares the values
		assertTrue(expectedPath == actualPath && "/downloads/Games".equals(fs.findFile("/A2", false).getContent()));
	}

	/**
	 * Test : User uses redirction to append to a file with current path
	 */
	@Test
	public void testAppendToFile() {
		// Changes the current working directory
		fs.setCurrent(fs.games);
		// Expected return from pwd
		expectedPath = null;
		// Actual return from pwd
		actualPath = pwd.run(fs, ">> /A2".split(" "), "pwd >> /A2", false);
		// Compares the values
		assertTrue(expectedPath == actualPath
				&& "Wow what a project\n/downloads/Games".equals(fs.findFile("/A2", false).getContent()));
	}

	/**
	 * Test : User uses redirction but does not provide no file
	 */
	@Test
	public void testRedirectionErrorCase1() {
		// Expected return from pwd
		expectedPath = "Error : No parameters provided";
		// Actual return from pwd
		actualPath = pwd.run(fs, ">>".split(" "), "pwd >>", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : User uses redirction but provides multiple files
	 */
	@Test
	public void testRedirectionErrorCase2() {
		// Expected return from pwd
		expectedPath = "Error : Multiple Parameters have been provided : [lol, plz, work] Only one is required for redirection";
		// Actual return from pwd
		actualPath = pwd.run(fs, ">> lol plz work".split(" "), "pwd >> lol plz work", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}

	/**
	 * Test : User uses redirction but provides an invalid file name
	 */
	@Test
	public void testRedirectionErrorCase3() {
		// Expected return from pwd
		expectedPath = "Error: Invalid File : Hello$ is not a valid file name";
		// Actual return from pwd
		actualPath = pwd.run(fs, ">> Hello$".split(" "), "pwd >> Hello$", false);
		// Compares the values
		assertEquals(expectedPath, actualPath);
	}
}