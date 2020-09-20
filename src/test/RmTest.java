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

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commands.Rm;

/**
 * Class RmTest is used to test Rm
 */
public class RmTest {

	/**
	 * Declare instance of FileSystem so we can access the filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declare instance of Mkdir to make new directories
	 */
	private static Rm rm;

	/**
	 * Declare two different instance of a boolean objects called actual and
	 * expected
	 */
	private boolean actual = false, expected = false;

	/**
	 * Declare instance of a String object called returnRm
	 */
	private String returnRm;

	/**
	 * Sets up the test environment and initializes the instance variables
	 * 
	 * @throws Exception
	 */
	@Before
	public void setup() {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes the class to be tested
		rm = new Rm();
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
	 * Test : User removes a relative directory path
	 */
	@Test
	public void testRelativeDir() {
		// Actual return from Class Rm after the opertaion
		returnRm = rm.run(fs, "users".split(" "), "rm users", false);
		// Checks if the node exists or not
		actual = fs.getCurrent().getList().contains(fs.users);
		// Checks if the values are as expected
		assertTrue(returnRm == null && expected == actual);
	}

	/**
	 * Test B : User removes a relative file path
	 */
	@Test
	public void testRelativeFile() {
		// Actual return from Class Rm after the opertaion
		returnRm = rm.run(fs, "A2".split(" "), "rm A2", false);
		// Checks if the node exists or not
		actual = fs.getCurrent().getList().contains(fs.A2);
		// Checks if the values are as expected
		assertTrue(returnRm.equals("Error: Invalid Directory : A2 is not a directory"));
	}

	/**
	 * Test : User removes a relative subdirectory path
	 */
	@Test
	public void testRelativeSubdirectory() {
		// Actual return from Class Rm after the opertaion
		returnRm = rm.run(fs, "users/skeshavaa".split(" "), "rm users/skeshavaa", false);
		// Changes the current working directory
		fs.setCurrent(fs.users);
		// Checks if the node exists or not
		actual = fs.getCurrent().getList().contains(fs.user1);
		// Checks if the values are as expected
		assertTrue(returnRm == null && expected == actual);
	}

	/**
	 * Test : User removes a subdirectory file path
	 */
	@Test
	public void testSubdirectoryFile() {
		// Actual return from Class Rm after the opertaion
		returnRm = rm.run(fs, "documents/txtone".split(" "), "rm documents/txtone", false);
		// Changes the current working directory
		fs.setCurrent(fs.documents);
		// Checks if the node exists or not
		actual = fs.getCurrent().getList().contains(fs.doc1);
		// Checks if the values are as expected
		assertTrue(returnRm.equals("Error: Invalid Directory : documents/txtone is not a directory")
				&& expected != actual);
	}

	/**
	 * Test : User removes an absolute directory path
	 */
	@Test
	public void testAbsoluteDir() {
		// Actual return from Class Rm after the opertaion
		returnRm = rm.run(fs, "/users/skeshavaa".split(" "), "rm /users/skeshavaa", false);
		// Changes the current working directory
		fs.setCurrent(fs.users);
		// Checks if the node exists or not
		actual = fs.getCurrent().getList().contains(fs.user1);
		// Checks if the values are as expected
		assertTrue(returnRm == null && expected == actual);
	}

	/**
	 * Test : User removes a absolute file path
	 */
	@Test
	public void testAbsoluteFile() {
		// Actual return from Class Rm after the opertaion
		returnRm = rm.run(fs, "/documents/txtone".split(" "), "rm /documents/txtone", false);
		// Changes the current working directory
		fs.setCurrent(fs.documents);
		// Checks if the node exists or not
		actual = fs.getCurrent().getList().contains(fs.doc1);
		// Checks if the values are as expected
		assertTrue(returnRm.equals("Error: Invalid Directory : /documents/txtone is not a directory")
				&& expected != actual);
	}

	/**
	 * Test : User uses redirection for a non redirectionable command
	 */
	@Test
	public void testRedirectionError() {
		// Actual return from Class Rm after the opertaion
		String actual = rm.run(fs, "documents > text".split(" "), "rm documents > text", false);
		// Expected return from Class Rm
		String expected = "Error : Redirection Error : rm does not support redirection";
		// Checks if the values are as expected
		assertTrue(actual.equals(expected) && fs.getCurrent().getList().contains(fs.documents) == true);
	}
}