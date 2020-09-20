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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commands.History;

/**
 * Class HistoryTest runs all the different test cases for History
 */
public class HistoryTest {

	/**
	 * Declare instance of MockFileSystem so we can access the preset filesystem
	 */
	private static MockFileSystem fs;

	/**
	 * Declares instance of History to be tested
	 */
	private static History history;

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
	public void setUp() throws Exception {
		// Gets a specific preset FileSystem
		fs = MockFileSystem.getMockFileSys("MOCKENV");
		// Initializes a History object
		history = new History();

		// Setups the History Stack
		fs.getCommandLog().add("mkdir users");
		fs.getCommandLog().add("mkdir pics");
		fs.getCommandLog().add("mkdir Sys");
		fs.getCommandLog().add("echo \"Wow what a project\" > A2");
		fs.getCommandLog().add("cd C/users");
		fs.getCommandLog().add("mkdir desktop");
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
	 * Test : User provides no parameter
	 */
	@Test
	public void testNoArgs() {
		// Declares and initializes an empty array
		String[] testCase1history = {};
		// Add the command to the history stack
		fs.getCommandLog().add("history");
		// Actual return from History after the operation has been run
		actual = history.run(fs, testCase1history, "history", false);
		// Expected return from History
		expected = "1. mkdir users\n" + "2. mkdir pics\n" + "3. mkdir Sys\n" + "4. echo \"Wow what a project\" > A2\n"
				+ "5. cd C/users\n" + "6. mkdir desktop\n" + "7. history";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : Users provides a large integer parameter
	 */
	@Test
	public void testLargeInteger() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 100");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "100".split(" "), "history 100", false);
		// Expected return from History
		expected = "1. mkdir users\n" + "2. mkdir pics\n" + "3. mkdir Sys\n" + "4. echo \"Wow what a project\" > A2\n"
				+ "5. cd C/users\n" + "6. mkdir desktop\n" + "7. history 100";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides a small intger parameter
	 */
	@Test
	public void testSmallInteger() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 5");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "5".split(" "), "history 5", false);
		// Expected return from History
		expected = "3. mkdir Sys\n" + "4. echo \"Wow what a project\" > A2\n" + "5. cd C/users\n" + "6. mkdir desktop\n"
				+ "7. history 5";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides a parameter lower than zero
	 */
	@Test
	public void testNegativeInteger() {
		// Add the command to the history stack
		fs.getCommandLog().add("history -3");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "-3".split(" "), "history -3", false);
		// Expected return from History
		expected = "Error: Invalid Argument : -3 is not either a number or " + "positive or an integer";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides an non-integer parameter
	 */
	@Test
	public void testNotAnIntegerCase1() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1.0");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1.0".split(" "), "history 1.0", false);
		// Expected return from History
		expected = "Error: Invalid Argument : 1.0 is not either a number or " + "positive or an integer";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides a non-numeric parameter
	 */
	@Test
	public void testNotAnIntegerCase2() {
		// Add the command to the history stack
		fs.getCommandLog().add("history hello");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "hello".split(" "), "history hello", false);
		// Expected return from History
		expected = "Error: Invalid Argument : hello is not either a number or " + "positive or an integer";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides multiple parameter
	 */
	@Test
	public void testMultipleArgs() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1 3 5");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1 3 5".split(" "), "history 1 3 5", false);
		// Expected return from History
		expected = "Error : Multiple Parameters have been provided : 1 3 5 , " + "either one or no input";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User overwrites a file with result
	 */
	@Test
	public void testRedirectionOverwrite() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1 > test");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1 > test".split(" "), "history 1 > test", false);
		// Expected return from History
		expected = "7. history 1 > test";
		// Checks if the values are equal or not
		assertEquals(expected, fs.findFile("test", false).getContent());
	}

	/**
	 * Test : User appends the result to a file
	 */
	@Test
	public void testRedirectionAppend() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1 >> file");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1 >> A2".split(" "), "history 1 >> A2", false);
		// Expected return from History
		expected = "Wow what a project\n7. history 1 >> file";
		// Checks if the values are equal or not
		assertEquals(expected, fs.findFile("A2", false).getContent());
	}

	/**
	 * Test : User does not provide a filename for redirection
	 */
	@Test
	public void testRedirectionErrorCase1() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1 >");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1 >".split(" "), "history 1 >", false);
		// Expected return from History
		expected = "Error : No parameters provided for redirection";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User provides multiple file names
	 */
	@Test
	public void testRedirectionErrorCase2() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1 > lol plz work");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1 > lol plz work".split(" "), "history 1 > lol plz work", false);
		// Expected return from History
		expected = "Error : Multiple Parameters have been provided : [lol, plz, work] Only one is required for redirection";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}

	/**
	 * Test : User does not provide a proper file name for redirection
	 */
	@Test
	public void testRedirectionErrorCase3() {
		// Add the command to the history stack
		fs.getCommandLog().add("history 1 > Hello$");
		// Actual return from History after the operation has been run
		actual = history.run(fs, "1 > Hello$".split(" "), "history 1 > Hello$", false);
		// Expected return from History
		expected = "Error: Invalid File : Hello$ is not a valid file name";
		// Checks if the values are equal or not
		assertEquals(expected, actual);
	}
}
